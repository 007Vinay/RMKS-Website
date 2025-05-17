package com.rmks.website.service.impl;

import com.rmks.website.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path uploadDir;

    public FileStorageServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        try {
            // Trim the input and normalize it to ensure it's safe
            this.uploadDir = Paths.get(uploadDir.trim()).toAbsolutePath().normalize();
            
            // Create directories if they don't exist
            if (!Files.exists(this.uploadDir)) {
                Files.createDirectories(this.uploadDir);
                System.out.println("Created upload directory at: " + this.uploadDir);
            } else {
                System.out.println("Using existing upload directory at: " + this.uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid upload directory path: " + uploadDir, e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        try {
            // Generate a unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // Resolve the file path
            Path targetLocation = uploadDir.resolve(filename);
            System.out.println("Storing file at: " + targetLocation);

            // Copy the file
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File stored successfully");

            return filename;
        } catch (IOException e) {
            System.err.println("Failed to store file: " + e.getMessage());
            throw new IOException("Failed to store file", e);
        }
    }

    @Override
    public Path loadFile(String filename) {
        return uploadDir.resolve(filename);
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        Path file = loadFile(filename);
        Files.deleteIfExists(file);
    }
}
