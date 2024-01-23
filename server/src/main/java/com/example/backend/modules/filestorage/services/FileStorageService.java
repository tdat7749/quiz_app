package com.example.backend.modules.filestorage.services;

import com.example.backend.commons.ResponseSuccess;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileStorageService {
    public ResponseSuccess<String> uploadFile(MultipartFile multipartFile) throws IOException;
}
