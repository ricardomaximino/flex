package es.brasatech.flex.message.configuration;

import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.InternalUserEvent;
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
    public void handleMessage(InternalUserEvent<Flex> event) {
        var message = String.format("User listener received a message!%n%s", event.getMessage());
        log.info(message);
        messageSender.sendCreationMessage(message);
    }

}
