package com.workshop.day37server.services;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import jakarta.json.Json;

@Service
public class S3Service {
    
    @Autowired
    private AmazonS3 s3Client;

    @Value("${do.storage.bucket.name}")
    private String bucketName;

    // push data to cloud
    public String upload(MultipartFile file) throws IOException{
        // User data
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "kenneth");
        userData.put("uploadTime", new Date().toString());
        userData.put("originalFilename", file.getOriginalFilename());
        
        // Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);
        String key = UUID.randomUUID().toString()
            .substring(0, 8);
        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");
        int count = 0;
        String filenameExt = "";
        while(tk.hasMoreTokens()){
            if(count == 1){
                    filenameExt = tk.nextToken();
                break;
            }else{
                filenameExt = tk.nextToken();
            }
            count++;
        }
        System.out.println("myobjects/%s.%s".formatted(key, filenameExt));
        if(filenameExt.equals("blob"))
            filenameExt = filenameExt + ".png";
        PutObjectRequest putRequest = 
            new PutObjectRequest(
                this.bucketName, 
                "myobjects/%s.%s".formatted(key, filenameExt), 
                file.getInputStream(), 
                metadata);
        putRequest.withCannedAcl(
                CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);
        return "myobjects/%s.%s".formatted(key, filenameExt);
    }
    
    public String download(String key) throws IOException {
        GetObjectRequest getRequest = 
            new GetObjectRequest(bucketName, "myobjects/" + key + ".png");
        S3Object object = s3Client.getObject(getRequest);
        byte[] imageBytes = IOUtils.toByteArray(object.getObjectContent());

        object.close();

        final String BASE64_PREFIX_DECODER = "data:image/png;base64,";
        // 1. converting bytes[] to encodedString
        String encodedString = Base64.getEncoder().encodeToString(imageBytes);
        return Json.createObjectBuilder()
            // 2. appending prefix to encodedString
            .add("image", BASE64_PREFIX_DECODER + encodedString) 
            .add("uid", key)
            .build().toString();

    }
}
