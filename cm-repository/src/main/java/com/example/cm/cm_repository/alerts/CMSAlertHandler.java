package com.example.cm.cm_repository.alerts;

import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.service.CMSUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CMSAlertHandler  {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSAlertHandler.class);

    @Autowired
    CMSUserService cmsUserService;

    public void handleMessage(CMSUser user) {
        logger.info("handleMessage received: " + user.toString());
//        CMSUser saved = cmsUserService.save(user);
//        logger.info("saved: " + saved.toString());
    }
}
