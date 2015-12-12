package com.example.cm.cm_repository.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.example.cm.cm_model.domain.CMSImage;
import com.example.cm.cm_repository.event.AWSUploadProgressListener;
import com.example.cm.cm_repository.repository.CMSImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of CMSImageService
 */
@Service
@Transactional
public class CMSImageServiceImpl implements CMSImageService {
    private static final Logger logger
            = LoggerFactory.getLogger(CMSImageServiceImpl.class);
//
//    @Autowired
//    private AmazonS3 amazonS3;
//
//    @Autowired
//    private CMSImageRepository cmsImageRepository;
//
//    @Value( "${bucket.amazons3}" )
//    private String imageS3Bucket;
//
//    public void save(CMSImage cmsImage){
//        CMSImage saved = cmsImageRepository.save(cmsImage);
//
//        logger.info(saved.toString());
//
//        TransferManager transferManager = new TransferManager(this.amazonS3);
//        Path destination = Paths.get(saved.getCreatedBy(), saved.getFilename());
//        ObjectMetadata meta = new ObjectMetadata();
//        ByteArrayInputStream bis = new ByteArrayInputStream(saved.getImage());
//
//        Upload upload = transferManager.upload(imageS3Bucket, destination.toString(), bis, meta);
//        AWSUploadProgressListener progressListener
//                = new AWSUploadProgressListener(saved);
//        upload.addProgressListener(progressListener);
//        return saved;
//    }
}
