package com.example.cm.cm_repository.service;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.example.cm.cm_model.domain.CMSUser;
import com.example.cm.cm_repository.alerts.AlertService;
import com.example.cm.cm_repository.image.utils.ImageConverter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implementation of CMSImageService
 */
@Service
@PropertySource({ "classpath:aws.properties" })
public class CMSImageServiceImpl implements CMSImageService {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSImageServiceImpl.class);

    private final String AVATAR_BUCKET_VALUE = "avatar";
    private final String AVATAR_NAME_VALUE = "avatar";
    private final String AVATAR_IMAGE_FORMAT_VALUE = "jpg";

    @Value( "${bucket.amazons3}" )
    private String imageS3Bucket;

    @Autowired
    CMSUserService cmsUserService;

    private AmazonS3 amazonS3;
    private AmazonS3Client amazonS3Client;
    private TransferManager transferManager = new TransferManager(this.amazonS3);

    @Autowired
    private AlertService alertService;

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

        String key = getKey(username);
        ObjectMetadata meta = getMetadata(getBucket(username).toString(), key);

        try {
            URI resourceUri = null;

            if (meta == null) // bucket & key does not exist
            {
                meta = new ObjectMetadata();
            }

            // Blocking
            UploadResult result = upload(key, in, meta);

            if(result != null)
            {
                resourceUri = new URI(
                        this.amazonS3Client.getResourceUrl(result.getBucketName(), result.getKey())
                );
            }

            // fetch and update the User
            CMSUser fetched = cmsUserService.getUser(username);
            fetched.setAvatar(resourceUri);
            CMSUser saved = cmsUserService.save(fetched);

            // alert the update
            alertService.sendCMSAlert(saved);
        }
        catch (URISyntaxException use)
        {
            logger.error("Error building resource URI", use);
        }
    }

    private UploadResult upload(String key, InputStream in, ObjectMetadata meta)
    {
        UploadResult result = null;
        ByteArrayInputStream convertedIn;

        try
        {
            convertedIn = ImageConverter.convertToJPEG(in);
            Upload upload = this.transferManager.upload(
                    imageS3Bucket,
                    key,
                    convertedIn,
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
            result = upload.waitForUploadResult();
        }
        catch (InterruptedException ire){
            logger.error("Upload cancelled", ire);
        }
        catch (IOException ioe){
            logger.error("Upload error", ioe);
        }

        return result;
    }

    private String getKey(String username)
    {
        // Add the user to the bucket name
        File bucket = getBucket(username);

        // Normalize the image name
        String newFileName = AVATAR_NAME_VALUE.concat(".").concat(AVATAR_IMAGE_FORMAT_VALUE);

        String path = FilenameUtils.concat(bucket.getPath(), newFileName);
        return path;
    }

    private File getBucket(String username)
    {
        return new File(username, AVATAR_BUCKET_VALUE);
    }

    private ObjectMetadata getMetadata(String bucketName, String key)
    {
        try
        {
            ObjectMetadata metadata
                    = this.amazonS3Client
                    .getObjectMetadata(bucketName, key);

            return metadata;
        }
        catch (AmazonS3Exception ace)
        {
            if (ace.getStatusCode() == 404) {
                // i.e. 404: NoSuchKey
                return null;
            }
            else {
                throw ace;    // rethrow all S3 exceptions other than 404
            }
        }

    }
}
