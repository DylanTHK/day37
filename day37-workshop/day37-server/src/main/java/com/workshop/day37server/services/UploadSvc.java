package com.workshop.day37server.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.day37server.models.Post;
import com.workshop.day37server.repositories.UploadRepo;

import jakarta.json.Json;

@Service
public class UploadSvc {
    
    @Autowired
    private UploadRepo uploadRepo;

    public String uploadImage(String comment, MultipartFile file) throws IOException {
        Post results = uploadRepo.insertData(comment, file);
        if (null != results)
            return Json.createObjectBuilder()
                .add("uid", results.getUid())
                .build().toString();
        return null;
    }

    public Optional<Post> getImageById(String id) {
        return uploadRepo.getDataById(id);
    }

}
