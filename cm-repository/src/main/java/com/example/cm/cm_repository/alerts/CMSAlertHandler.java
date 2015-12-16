package com.example.cm.cm_repository.alerts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMSAlertHandler  {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSAlertHandler.class);

    public void handleMessage(String message) {
        logger.info("handleMessage received: " + message);
    }
}
