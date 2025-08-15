package es.brasatech.flex.shared;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.data.SearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataService {
    Data save(Map<String, Object> data);

    Data update(String id, Map<String, Object> data);

    List<Data> findAll();

    Optional<Data> findById(String id);

    void deleteById(String id);

    List<Data> simpleSearch(Map<String, Object> searchCriteria);

    List<Data> advancedSearch(Map<String, SearchCriteria> searchCriteria);

    Page<Data> simpleSearchWithPaging(Map<String, Object> searchCriteria,
                                      int page, int size, String sortBy);

    Page<Data> advancedSearchWithPaging(Map<String, SearchCriteria> searchCriteria, int page, int size, String sortBy);

    List<Data> findRecentUsers(int days);

    void removeStandard(Map<String, Object> data);
}
