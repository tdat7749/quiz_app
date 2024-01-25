package com.example.backend.modules.filestorage.services;


import com.cloudinary.Cloudinary;
import com.example.backend.commons.AppConstants;
import com.example.backend.commons.ResponseSuccess;
import com.example.backend.modules.filestorage.exceptions.FileIsEmptyException;
import com.example.backend.modules.filestorage.exceptions.FileTooLargeException;
import com.example.backend.modules.filestorage.exceptions.NotAllowMimeTypeException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl  implements  FileStorageService{

    private final Cloudinary cloudinary;

    public FileStorageServiceImpl(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    @Override
    @Transactional
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            throw new FileIsEmptyException("File trống !");
        }

        if(multipartFile.getSize() > AppConstants.MAX_FILE){ // > 3mb
            throw new FileTooLargeException("Dung lượng file vượt quá 3mb");
        }

        Tika tika = new Tika();

        String detectedMineType = tika.detect(multipartFile.getInputStream());
        if(!Arrays.asList(AppConstants.MIME_TYPES).contains(detectedMineType)){
            throw new NotAllowMimeTypeException("Chỉ chấp nhận .png, .webp, .jpg, .jpeg");
        }

        var fileUrl = cloudinary.uploader()
                .upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();

        return fileUrl;
    }
}
