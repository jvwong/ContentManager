package com.example.cm.cm_repository.alerts;

import com.example.cm.cm_model.domain.CMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMSAlertHandler  {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSAlertHandler.class);

    public void handleMessage(CMSUser user) {
        logger.info("handleMessage received: " + user.toString());
    }
}
