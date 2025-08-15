package es.brasatech.flex.message.configuration;

import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.InternalDataEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InternalListener {

    private final MessageSender messageSender;

    @EventListener
    public void handleMessage(InternalDataEvent<Flex> event) {
        var message = String.format("Data listener received a message!%n%s", event.getMessage());
        log.info(message);
        messageSender.sendCreationMessage(message);
    }

}
