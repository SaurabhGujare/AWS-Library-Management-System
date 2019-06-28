package com.neu.cloudassign1.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;

public interface BaseClient {
    public String uploadFile(MultipartFile multipartFile, UUID bookId);

    public String deleteFile(String fileUrl);

    public URL GeneratePresignedURL(String objectKey);


}
