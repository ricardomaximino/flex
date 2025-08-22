package es.brasatech.flex.data;

import es.brasatech.flex.shared.Flex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SearchDataRepository<T extends Flex> {
    List<T> findByDynamicSimpleCriteria(Map<String, Object> searchCriteria);
    List<T> findByDynamicAdvancedCriteria(Map<String, SearchCriteria> searchCriteria);
    Page<T> findByDynamicSimpleCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable);
    Page<T> findByDynamicAdvancedCriteriaWithPaging(Map<String, SearchCriteria> searchCriteria, Pageable pageable);
}
