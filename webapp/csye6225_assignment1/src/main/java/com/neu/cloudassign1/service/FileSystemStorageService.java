package com.neu.cloudassign1.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileSystemStorageService {
    String store(MultipartFile file);
    public void deleteFile(String url);
}
