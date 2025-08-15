package es.brasatech.flex.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomDataRepository {
    List<Data> findByDynamicSimpleCriteria(Map<String, Object> searchCriteria);
    List<Data> findByDynamicAdvancedCriteria(Map<String, SearchCriteria> searchCriteria);
    Page<Data> findByDynamicSimpleCriteriaWithPaging(Map<String, Object> searchCriteria, Pageable pageable);
    Page<Data> findByDynamicAdvancedCriteriaWithPaging(Map<String, SearchCriteria> searchCriteria, Pageable pageable);
}
