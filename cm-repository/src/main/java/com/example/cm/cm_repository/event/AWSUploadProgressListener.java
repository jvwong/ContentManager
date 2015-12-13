package com.example.cm.cm_repository.event;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.service.CMSUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

public class AWSUploadProgressListener implements ProgressListener {

    private static final Logger logger
            = LoggerFactory.getLogger(AWSUploadProgressListener.class);

    @Autowired
    CMSUserService cmsUserService;

    private ProgressEventType status;
    private URI resourceUri;
    private String username;

    public AWSUploadProgressListener(String username, URI resourceUri)
    {
        assert(resourceUri != null && username != null);
        this.username = username;
        this.resourceUri = resourceUri;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.ProgressListener#update(long, long, int)
     */
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        this.status = progressEvent.getEventType();

        if(getStatus().equals(ProgressEventType.TRANSFER_COMPLETED_EVENT))
        {
            logger.info("Status: " + ProgressEventType.TRANSFER_COMPLETED_EVENT);

            // None of this works - thread?
            CMSUser user = cmsUserService.getUser(this.username);
            logger.info("user retrieved: " + user.getUsername());
            user.setAvatar(this.resourceUri);
            cmsUserService.save(user);
            logger.info("user avatar available @ " + this.resourceUri);
        }
    }

    public ProgressEventType getStatus()
    {
        return this.status;
    }

    public void setStatus(ProgressEventType status) {
        this.status = status;
    }
}
