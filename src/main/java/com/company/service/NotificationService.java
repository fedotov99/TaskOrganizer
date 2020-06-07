package com.company.service;

import com.company.RabbitMqConfig;
import com.company.model.notification.TaskAssignedNotificationMessage;
import com.company.model.notification.UncheckedTaskNotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    void sendMessageToUncheckedTaskForManagerQueue(UncheckedTaskNotificationMessage uncheckedTaskNotificationMessage) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.NEW_UNCHECKED_TASK_FOR_MANAGER_QUEUE_NAME, uncheckedTaskNotificationMessage);
    }

    void sendMessageToTaskAssignedQueue(TaskAssignedNotificationMessage taskAssignedNotificationMessage) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.NEW_TASK_ASSIGNED_TO_SUBORDINATE_QUEUE_NAME, taskAssignedNotificationMessage);
    }
}
