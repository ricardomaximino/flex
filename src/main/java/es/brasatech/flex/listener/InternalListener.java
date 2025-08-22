package es.brasatech.flex.listener;

import es.brasatech.flex.rabbitmq.MessageSender;
import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.InternalDataEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InternalListener {

    private final MessageSender messageSender;

    public InternalListener(@Autowired(required = false) MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @EventListener
    public void handleMessage(InternalDataEvent<Flex> event) {
        var message = String.format("Data listener received a message!%n%s", event.getMessage());
        log.info(message);
        if(messageSender != null) {
            messageSender.sendCreationMessage(message);
        }
    }

}
