package es.brasatech.flex.shared;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InternalDataEvent<T extends Flex> extends ApplicationEvent {

    private final String message;

    public InternalDataEvent(T source, String message) {
        super(source);
        this.message = message;
    }
}
