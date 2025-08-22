package es.brasatech.flex.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("mongodb")
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendCreationMessage(String message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.CREATION_ROUTING_KEY,
                message
        );
    }

    public void sendStatusMessage(String message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.STATUS_ROUTING_KEY,
                message
        );
    }
}