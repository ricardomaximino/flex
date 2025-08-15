package es.brasatech.flex.data;

import es.brasatech.flex.shared.*;
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
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;
    private final ValidatorManager validatorManager;
    private final ApplicationEventPublisher internalPublisher;

    @Override
    public Data save(Map<String, Object> data) {
        Data user = new Data();
        user.setType((String) data.get("type"));
        removeStandard(data);
        user.setCustomFields(data);
        if(user.getType() == null) {
            throw new ValidationException("Type field is a mandatory data");
        }
        validatorManager.validate(user);
        user.prepareToSave();
        var savedUser = dataRepository.save(user);
        internalPublisher.publishEvent(new InternalDataEvent<Data>(savedUser, "New [" + savedUser.getType() + "] was created!"));
        return savedUser;
    }

    @Override
    public Data update(String id, Map<String, Object> data) {
        Data user = dataRepository.findById(id).orElseThrow(() -> new NotFoundException("Data not found"));
        removeStandard(data);
        user.setCustomFields(data);
        validatorManager.validate(user);
        user.prepareToSave();
        return dataRepository.save(user);
    }

    @Override
    public List<Data> findAll() {
        return dataRepository.findAll();
    }

    @Override
    public Optional<Data> findById(String id) {
        return dataRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        dataRepository.deleteById(id);
    }

    @Override
    public List<Data> simpleSearch(Map<String, Object> searchCriteria) {
        log.info("Performing simple search with criteria: {}", searchCriteria);
        return dataRepository.findByDynamicSimpleCriteria(searchCriteria);
    }

    @Override
    public List<Data> advancedSearch(Map<String, SearchCriteria> searchCriteria) {
        log.info("Performing advanced search with criteria: {}", searchCriteria);
        return dataRepository.findByDynamicAdvancedCriteria(searchCriteria);
    }

    @Override
    public Page<Data> simpleSearchWithPaging(Map<String, Object> searchCriteria,
                                             int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated simple search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return dataRepository.findByDynamicSimpleCriteriaWithPaging(searchCriteria, pageable);
    }

    @Override
    public Page<Data> advancedSearchWithPaging(Map<String, SearchCriteria> searchCriteria, int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Performing paginated advanced search with criteria: {}, page: {}, size: {}",
                searchCriteria, page, size);
        return dataRepository.findByDynamicAdvancedCriteriaWithPaging(searchCriteria, pageable);
    }

    @Override
    public List<Data> findRecentUsers(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        Map<String, Object> criteria = new HashMap<>();
        Map<String, Object> dateRange = new HashMap<>();
        dateRange.put("from", cutoff);
        criteria.put("creationDate", dateRange);

        return simpleSearch(criteria);
    }

    @Override
    public void removeStandard(Map<String, Object> data) {
        data.remove("id");
        data.remove("creationDate");
        data.remove("updateDate");
        data.remove("type");
    }
}
