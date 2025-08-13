package es.brasatech.flex.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

enum SearchOperation {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL,
    CONTAINS,
    IN,
    NOT_IN,
    EXISTS,
    BETWEEN
}