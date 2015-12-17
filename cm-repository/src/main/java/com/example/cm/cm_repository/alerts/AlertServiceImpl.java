package com.example.cm.cm_repository.alerts;

import com.example.cm.cm_model.domain.CMSUser;
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

    public void sendCMSAlert(CMSUser user)
    {
        logger.info("AlertService::sendCMSAlert: " + user.toString());
        rabbitTemplate.convertAndSend(
                AMPQConfig.exchangeName,
                AMPQConfig.routingKey,
                user);
    }
}
