package com.example.cm.cm_repository.service;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.alerts.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Implementation of CMSImageService
 */
@Service
public class CMSImageServiceImpl implements CMSImageService {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSImageServiceImpl.class);

    @Value( "${bucket.amazons3}" )
    private String imageS3Bucket;

    @Autowired
    private AlertService alertService;

    @Autowired
    private CMSUserService cmsUserService;

    private AmazonS3 amazonS3;
    private AmazonS3Client amazonS3Client;
    private TransferManager transferManager = new TransferManager(this.amazonS3);

    @Autowired
    public CMSImageServiceImpl(AmazonS3 amazonS3)
    {
        assert(amazonS3 != null);
        this.amazonS3 = amazonS3;
        this.transferManager
                = new TransferManager(this.amazonS3);
        this.amazonS3Client
                = (AmazonS3Client) this.transferManager.getAmazonS3Client();
    }

    @Override
    @Async
    public void uploadAvatar(String username,
                             InputStream in,
                             String originalFilename)
            throws IOException {

        String key = Paths.get(username,
                "avatar",
                UUID.randomUUID()
                        .toString()
                        .concat("_")
                        .concat(originalFilename)).toString();

        ObjectMetadata meta = new ObjectMetadata();

        Upload upload = this.transferManager.upload(
                imageS3Bucket,
                key,
                in,
                meta);


        upload.addProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                if(progressEvent.getEventType().equals(ProgressEventType.TRANSFER_COMPLETED_EVENT))
                {
                    logger.info("Status: " + ProgressEventType.TRANSFER_COMPLETED_EVENT);
                    CMSUser user = cmsUserService.getUser(username);
                    alertService.sendCMSAlert(user);
                }
            }
        });

        try
        {
            upload.waitForUploadResult();
            URI resourceUri = new URI(this.amazonS3Client.getResourceUrl(imageS3Bucket, key));
            logger.info("Async method resourceUri : " + resourceUri);
        }
        catch (URISyntaxException  | InterruptedException ignore){}
    }
}
