package es.brasatech.flex.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("mongodb")
public class MessageReceiver {

    @RabbitListener(queues = RabbitMQConfig.CREATION_QUEUE)
    public void receiveCreationMessage(String message) {
        log.info("Creation message received: {}", message);
    }

    @RabbitListener(queues = RabbitMQConfig.STATUS_QUEUE)
    public void receiveStatusMessage(String message) {
        log.info("Status message received: {}", message);
    }
}