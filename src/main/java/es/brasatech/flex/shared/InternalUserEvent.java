package es.brasatech.flex.shared;

import org.springframework.context.ApplicationEvent;


public class InternalUserEvent<T extends Flex> extends ApplicationEvent {

    private final String message;

    public InternalUserEvent(T source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
