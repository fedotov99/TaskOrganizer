package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public final static String NEW_UNCHECKED_TASK_FOR_MANAGER_QUEUE_NAME = "newUncheckedTaskForManagerQueue";
    public final static String NEW_TASK_ASSIGNED_TO_SUBORDINATE_QUEUE_NAME = "newTaskAssignedToSubordinateQueue";
    public final static String EXCHANGE_NAME = "directExchange";
    private final ObjectMapper objectMapper;
    @Autowired
    public RabbitMqConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean(name = NEW_UNCHECKED_TASK_FOR_MANAGER_QUEUE_NAME)
    Queue createUncheckedTaskQueue() {
        return new Queue(NEW_UNCHECKED_TASK_FOR_MANAGER_QUEUE_NAME);
    }

    @Bean(name = NEW_TASK_ASSIGNED_TO_SUBORDINATE_QUEUE_NAME)
    Queue createTaskAssignedQueue() {
        return new Queue(NEW_TASK_ASSIGNED_TO_SUBORDINATE_QUEUE_NAME);
    }

    @Bean
    DirectExchange createExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding UncheckedTaskQueueBinding(@Qualifier(NEW_UNCHECKED_TASK_FOR_MANAGER_QUEUE_NAME) Queue queueName, DirectExchange exchangeName) {
        return BindingBuilder.bind(queueName).to(exchangeName).with(NEW_UNCHECKED_TASK_FOR_MANAGER_QUEUE_NAME);
    }

    @Bean
    Binding TaskAssignedQueueBinding(@Qualifier(NEW_TASK_ASSIGNED_TO_SUBORDINATE_QUEUE_NAME) Queue queueName, DirectExchange exchangeName) {
        return BindingBuilder.bind(queueName).to(exchangeName).with(NEW_TASK_ASSIGNED_TO_SUBORDINATE_QUEUE_NAME);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
