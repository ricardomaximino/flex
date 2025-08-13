package es.brasatech.flex.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomUserRepository {
    List<User> findByDynamicCriteriaWithOperators(Map<String, SearchCriteria> searchCriteria);
    Page<User> findByDynamicCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable);
}
