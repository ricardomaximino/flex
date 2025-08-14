package es.brasatech.flex.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomUserRepository {
    List<User> findByDynamicSimpleCriteria(Map<String, Object> searchCriteria);
    List<User> findByDynamicAdvancedCriteria(Map<String, SearchCriteria> searchCriteria);
    Page<User> findByDynamicSimpleCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable);
    Page<User> findByDynamicAdvancedCriteriaWithPaging(Map<String, SearchCriteria> searchCriteria, Pageable pageable);
}
