package com.example.cm.cm_repository.service;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jvowng
 */
public interface CMSImageService {

    @Async
    void uploadAvatar(String username,
                      InputStream in,
                      String originalFilename)
            throws IOException;
}

