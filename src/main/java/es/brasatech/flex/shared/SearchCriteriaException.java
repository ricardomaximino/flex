package es.brasatech.flex.shared;

import es.brasatech.flex.data.SearchCriteria;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public class SearchCriteriaException extends RuntimeException {

    private final Map<String, SearchCriteria> searchCriteriaMap;

    public SearchCriteriaException(String message) {
        super(message);
        this.searchCriteriaMap = new HashMap<>();
    }

    public SearchCriteriaException(String message, Map<String, SearchCriteria> searchCriteria) {
        super(message);
        this.searchCriteriaMap = searchCriteria;

    }

}
