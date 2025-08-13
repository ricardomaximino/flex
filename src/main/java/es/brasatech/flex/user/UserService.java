package es.brasatech.flex.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserValidator validator;

    public User save(Map<String, Object> data) {
        removeStandard(data);
        User user = new User();
        user.setType((String) data.get("type"));
        user.setCustomFields(data);
        if(!validator.validate(user)) {
            throw new RuntimeException("Invalid data");
        }
        user.save();
        return repository.save(user);
    }

    public User update(String id, Map<String, Object> data) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        removeStandard(data);
        user.setCustomFields(data);
        if(!validator.validate(user)) {
            throw new RuntimeException("Invalid data");
        }
        user.save();
        return repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<User> search(Map<String, SearchCriteria> searchCriteria) {
        return repository.findByDynamicCriteriaWithOperators(searchCriteria);
    }

    public Page<User> search(Map<String, Object> searchCriteria, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findByDynamicCriteriaWithPaging(searchCriteria, pageable);
    }

    protected void removeStandard(Map<String, Object> data) {
        data.remove("id");
        data.remove("creationDate");
        data.remove("updateDate");
    }
}
