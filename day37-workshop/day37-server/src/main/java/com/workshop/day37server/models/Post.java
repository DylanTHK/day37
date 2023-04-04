package com.workshop.day37server.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Post {
    private String uid;
    private String comment;
    private byte[] image;

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Post [uid=" + uid + ", comment=" + comment + ", imageSize=" + Arrays.toString(image).length() + "]";
    }
    // Method to create Post with Resultset
    public static Post create(ResultSet rs) throws SQLException {
        Post p = new Post();
        p.setUid(rs.getString("uid"));
        p.setComment(rs.getString("comment"));
        p.setImage(rs.getBytes("blobc"));
        System.out.println("Post rs: " + p); // bytes too long to print
        return p;
    }

    public JsonObject toJson() {
        final String BASE64_PREFIX_DECODER = "data:image/png;base64,";
        // 1. converting bytes[] to encodedString
        String encodedString = Base64.getEncoder().encodeToString(image);
        return Json.createObjectBuilder()
            // 2. appending prefix to encodedString
            .add("image", BASE64_PREFIX_DECODER + encodedString) 
            .add("comment", comment)
            .add("uid", uid)
            .build();
    }
}
