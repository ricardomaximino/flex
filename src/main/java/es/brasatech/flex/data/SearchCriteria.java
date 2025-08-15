package es.brasatech.flex.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private SearchOperation operation;
    private Object value;

    public SearchCriteria(Object value) {
        this.operation = SearchOperation.EQUALS;
        this.value = value;
    }

    // Convenience static factory methods
    public static SearchCriteria equalsCriteria(Object value) {
        return new SearchCriteria(SearchOperation.EQUALS, value);
    }

    public static SearchCriteria contains(String value) {
        return new SearchCriteria(SearchOperation.CONTAINS, value);
    }

    public static SearchCriteria greaterThan(Object value) {
        return new SearchCriteria(SearchOperation.GREATER_THAN, value);
    }

    public static SearchCriteria lessThan(Object value) {
        return new SearchCriteria(SearchOperation.LESS_THAN, value);
    }

    public static SearchCriteria between(Object from, Object to) {
        Map<String, Object> range = new HashMap<>();
        range.put("from", from);
        range.put("to", to);
        return new SearchCriteria(SearchOperation.BETWEEN, range);
    }

    public static SearchCriteria in(Object... values) {
        return new SearchCriteria(SearchOperation.IN, Arrays.asList(values));
    }

    public static SearchCriteria exists(boolean exists) {
        return new SearchCriteria(SearchOperation.EXISTS, exists);
    }
}