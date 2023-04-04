package com.workshop.day37server.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.day37server.models.Post;
import com.workshop.day37server.repositories.UploadRepo;
import com.workshop.day37server.services.UploadSvc;

@RestController
@CrossOrigin("*")
@RequestMapping(path="/api")
public class UploadController {
    
    @Autowired
    private UploadSvc uploadSvc;

    @Autowired
    private UploadRepo uploadRepo;

    @PostMapping(path="/post",
        consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin("*")
    public ResponseEntity<String> postImage(
        // variable names here need to match keys in FormData
        @RequestPart MultipartFile image,
        @RequestPart String comment) throws IOException {

        System.out.println("Entered Post Image Method");
        // pass data to sql (store name, comment, blob)
        // store blob to s3
        // FIXME: CHANGE THIS TO SERVICE -> REPO
        
        Integer result = uploadRepo.insertData(comment, image);

        if (result > 0) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Image added to database");
        } else {
            return ResponseEntity
            .status(HttpStatus.NOT_MODIFIED)
            .contentType(MediaType.APPLICATION_JSON)
            .body("Nothing Happened");
        }
        // return ResponseEntity
        //             .status(HttpStatus.CREATED)
        //             .contentType(MediaType.APPLICATION_JSON)
        //             .body("Image added to database");
    }

    @GetMapping(path="/image/{id}")
    public ResponseEntity<String> getImage(@PathVariable String id) {
        System.out.println("Retrieving Image with ID: " + id);
        Optional<Post> opt = uploadRepo.getDataById(id);

        String str = opt.get().toString();
        System.out.println(str);
        // System.out.println("Sending Post over: " + post);

        if (opt.isEmpty()) {
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body("No image found");
        } else {
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(opt.get().toJson().toString());
                // append "data:image/png;base64," to front of image
        }

    }
}
