package com.example.cm.cm_repository.alerts;

import com.example.cm.cm_model.domain.CMSUser;

public interface AlertService {
    void sendCMSAlert(CMSUser user);
}
