package es.brasatech.flex.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, List<String>> context;

    public ValidationException(String message, Map<String, List<String>> context){
        super(message);
        this.context = context;
    }

    public ValidationException(String message) {
        super(message);
        this.context = new HashMap<>();
    }
}
