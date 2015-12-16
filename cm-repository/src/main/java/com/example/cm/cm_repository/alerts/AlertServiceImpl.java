package com.example.cm.cm_repository.alerts;

import com.example.cm.cm_repository.config.AMPQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {
    private static final Logger logger
            = LoggerFactory.getLogger(AlertServiceImpl.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public AlertServiceImpl(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCMSAlert(String message)
    {
        logger.info("AlertService::sendCMSAlert: " + message);
        rabbitTemplate.convertAndSend(
                AMPQConfig.exchangeName,
                AMPQConfig.routingKey,
                message);
    }
}
