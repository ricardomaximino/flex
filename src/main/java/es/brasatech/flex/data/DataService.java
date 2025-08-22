package es.brasatech.flex.data;

import es.brasatech.flex.shared.Flex;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataService <T extends Flex> {
    T save(Map<String, Object> data);

    T update(String id, Map<String, Object> data);

    List<T> findAll();

    Optional<T> findById(String id);

    void deleteById(String id);

    List<T> simpleSearch(Map<String, Object> searchCriteria);

    List<T> advancedSearch(Map<String, SearchCriteria> searchCriteria);

    Page<T> simpleSearchWithPaging(Map<String, Object> searchCriteria,
                                      int page, int size, String sortBy);

    Page<T> advancedSearchWithPaging(Map<String, SearchCriteria> searchCriteria, int page, int size, String sortBy);

}
