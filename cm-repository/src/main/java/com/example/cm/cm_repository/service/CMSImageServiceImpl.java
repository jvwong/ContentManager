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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    CMSUserService cmsUserService;

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
    public URI uploadAvatar(String username, MultipartFile avatar)
            throws IOException {
        // check if an avatar exists already

        String key = Paths.get(username, "avatar", UUID.randomUUID().toString().concat("_").concat(avatar.getOriginalFilename())).toString();
        ObjectMetadata meta = new ObjectMetadata();
        Upload upload = this.transferManager.upload(
                imageS3Bucket,
                key,
                avatar.getInputStream(),
                meta);


        upload.addProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                if(progressEvent.getEventType().equals(ProgressEventType.TRANSFER_COMPLETED_EVENT))
                {
                    logger.info("Status: " + ProgressEventType.TRANSFER_COMPLETED_EVENT);
                }
            }
        });

        URI resourceUri = null;

        try {
            resourceUri = new URI(this.amazonS3Client.getResourceUrl(imageS3Bucket, key));
            upload.waitForUploadResult(); //background thread?

            Thread.sleep(5_000L);

            CMSUser user = cmsUserService.getUser(username);
            logger.info("user retrieved: " + user.getUsername());

            user.setAvatar(resourceUri);
            logger.info("user avatar set @ " + resourceUri);

            cmsUserService.save(user);
            logger.info("user avatar saved @ " + resourceUri);

        }
        catch (URISyntaxException  | InterruptedException ignore) {}

        return resourceUri;
    }
}
