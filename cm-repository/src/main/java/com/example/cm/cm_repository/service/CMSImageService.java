package com.example.cm.cm_repository.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author jvowng
 */
public interface CMSImageService {

    URI uploadAvatar(String username, MultipartFile avatar)
            throws URISyntaxException, IOException, InterruptedException;
}

