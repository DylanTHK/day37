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

import com.workshop.day37server.models.Post;
import com.workshop.day37server.repositories.UploadRepo;
import com.workshop.day37server.services.UploadSvc;

import jakarta.json.Json;

@RestController
@RequestMapping(path="/api")
public class UploadController {
    
    @Autowired
    private UploadSvc uploadSvc;

    // FIXME: CHANGE THIS TO SERVICE -> REPO
    @Autowired
    private UploadRepo uploadRepo;

    @PostMapping(path="/post",
        consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
        produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postImage(
        // variable names here need to match keys in FormData
        @RequestPart MultipartFile picture,
        @RequestPart String comment) throws IOException {

        System.out.println("Entered Post Image Method");
        
        // pass data to sql (store name, comment, blob)
        String rString = uploadSvc.uploadImage(comment, picture);
        
        // TODO: store blob to s3

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
}
