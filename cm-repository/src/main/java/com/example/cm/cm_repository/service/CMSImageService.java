package com.example.cm.cm_repository.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author jvowng
 */
public interface CMSImageService {

    @Async
//    URI uploadAvatar(String username, MultipartFile avatar)
    public URI uploadAvatar(String username, InputStream in, String originalFilename)
            throws IOException;
}

