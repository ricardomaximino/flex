package es.brasatech.flex.user;

import es.brasatech.flex.shared.InternalUserEvent;
import es.brasatech.flex.shared.Validator;
import es.brasatech.flex.shared.ValidatorManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ValidatorManager<Validator, User> validatorManager;
    private final ApplicationEventPublisher internalPublisher;

    public User save(Map<String, Object> data) {
        User user = new User();
        user.setType((String) data.get("type"));
        removeStandard(data);
        user.setCustomFields(data);
        if(user.getType() == null) {
            throw new RuntimeException("Type field is a mandatory data");
        }
        validatorManager.validate(user);
        user.prepareToSave();
        var savedUser = userRepository.save(user);
        internalPublisher.publishEvent(new InternalUserEvent<User>(savedUser, "New user[" + savedUser.getType() + "] was created!"));
        return savedUser;
    }

    public User update(String id, Map<String, Object> data) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        removeStandard(data);
        user.setCustomFields(data);
        validatorManager.validate(user);
        user.prepareToSave();
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public List<User> simpleSearch(Map<String, Object> searchCriteria) {
        log.info("Performing simple search with criteria: {}", searchCriteria);
        return userRepository.findByDynamicSimpleCriteria(searchCriteria);
    }

    public List<User> advancedSearch(Map<String, SearchCriteria> searchCriteria) {
        log.info("Performing advanced search with criteria: {}", searchCriteria);
        return userRepository.findByDynamicAdvancedCriteria(searchCriteria);
    }

    public Page<User> simpleSearchWithPaging(Map<String, Object> searchCriteria,
                                             int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated simple search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return userRepository.findByDynamicSimpleCriteriaWithPaging(searchCriteria, pageable);
    }

    public Page<User> advancedSearchWithPaging(Map<String, SearchCriteria> searchCriteria, int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated advanced search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return userRepository.findByDynamicAdvancedCriteriaWithPaging(searchCriteria, pageable);
    }

    public List<User> findRecentUsers(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        Map<String, Object> criteria = new HashMap<>();
        Map<String, Object> dateRange = new HashMap<>();
        dateRange.put("from", cutoff);
        criteria.put("creationDate", dateRange);

        return simpleSearch(criteria);
    }

    protected void removeStandard(Map<String, Object> data) {
        data.remove("id");
        data.remove("creationDate");
        data.remove("updateDate");
        data.remove("type");
    }
}
