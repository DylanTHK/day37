package com.workshop.day37server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.day37server.repositories.UploadRepo;

@Service
public class UploadSvc {
    
    @Autowired
    private UploadRepo uploadRepo;

    public void uploadImage(MultipartFile imageFile) {
        // uploadRepo.postData(null, null, imageFile);
    }

}
