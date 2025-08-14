package es.brasatech.flex.message.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STATUS_QUEUE = "statusQueue";
    public static final String CREATION_QUEUE = "creationQueue";
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String STATUS_ROUTING_KEY = "statusRoutingKey";
    public static final String CREATION_ROUTING_KEY = "creationRoutingKey";

    @Bean
    public Queue creationQueue() {
        return new Queue(CREATION_QUEUE, false);
    }

    @Bean
    public Queue statusQueue() {
        return new Queue(STATUS_QUEUE, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding statusBinding(Queue statusQueue, DirectExchange exchange) {
        return BindingBuilder.bind(statusQueue).to(exchange).with(STATUS_ROUTING_KEY);
    }

    @Bean
    public Binding creationBinding(Queue creationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(creationQueue).to(exchange).with(CREATION_ROUTING_KEY);
    }
}
