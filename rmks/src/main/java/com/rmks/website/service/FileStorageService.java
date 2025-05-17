package com.rmks.website.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;
    Path loadFile(String filename);
    void deleteFile(String filename) throws IOException;
}