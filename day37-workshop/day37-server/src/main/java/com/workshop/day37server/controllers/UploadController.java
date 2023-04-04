package com.workshop.day37server.controllers;

import java.io.IOException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.Response;
import com.workshop.day37server.models.Post;
import com.workshop.day37server.services.S3Service;
import com.workshop.day37server.services.UploadSvc;

import jakarta.json.Json;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path="/api")
public class UploadController {
    
    @Autowired
    private UploadSvc uploadSvc;

    @Autowired
    private S3Service s3Svc;


    @PostMapping(path="/post",
        consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postImage(
        // variable names here need to match keys in FormData
        @RequestPart MultipartFile picture,
        @RequestPart String comment) throws IOException {
        
        // pass data to sql (store name, comment, blob)
        String rString = uploadSvc.uploadImage(comment, picture);

        // returning response
        if (rString != null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(rString);
        } else {
            String msg = Json.createObjectBuilder()
                .add("error", "Nothing inserted")
                .build().toString();
            return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(msg);
        }
        
    }

    // Return Post object (string, string, byte[])
    @GetMapping(path="/image/{id}")
    public ResponseEntity<String> getImage(@PathVariable String id) {
        System.out.println("Retrieving Image with ID: " + id);

        // Query from SQL through service
        Optional<Post> opt = uploadSvc.getImageById(id);

        if (opt.isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body("No image found");
        } else {
            // append "data:image/png;base64," to front of image
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(opt.get().toJson().toString());
        }
    }

    @PostMapping(path="/posts3",
    consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
    produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadS3(
    @RequestPart MultipartFile picture,
    @RequestPart String comment) throws IOException {
        
        String uid = s3Svc.upload(picture);

        String json = Json.createObjectBuilder()
            .add("uid", uid)
            .build().toString();
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping(path="/images3/{key}")
    public ResponseEntity<String> getMethodName(@PathVariable String key) throws IOException {
        
        // retrieve image with key from S3
        String img = s3Svc.download(key);
        System.out.println("Response" + img);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(img);
    }
    

}
