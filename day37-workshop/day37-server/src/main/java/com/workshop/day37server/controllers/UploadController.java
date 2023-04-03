package com.workshop.day37server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path="/api")
public class UploadController {
    
    @PostMapping(path="/post",
        consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postImage(
        @RequestPart MultipartFile imageFile,
        @RequestPart String name,
        @RequestPart String comment) {

        // pass data to sql (store name, comment, blob)
        System.out.println("ImageFile: " + imageFile);
        System.out.println("name: " + name);
        System.out.println("comment: " + comment);

        // store blob to s3

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Controller is ok");
    }
}
