package com.example.cm.cm_repository.event;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.example.cm.cm_model.domain.Updatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWSUploadProgressListener implements ProgressListener {

    private static final Logger logger
            = LoggerFactory.getLogger(AWSUploadProgressListener.class);

    private ProgressEventType status;
    private Updatable target;

    public AWSUploadProgressListener(Updatable target)
    {
        this.target = target;
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
            // Set some flags here
            logger.info("Status: " + ProgressEventType.TRANSFER_COMPLETED_EVENT);
             target.setStatus(Updatable.STATUS.LIVE);
            // service.save(target);
            logger.info("Object Status: " + target.getStatus().toString());
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
