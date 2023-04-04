package com.workshop.day37server.repositories;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.day37server.models.Post;

@Repository
public class UploadRepo {
    
    private final String SQL_INSERT_IMAGE = "INSERT INTO posts (uid, blobc, comment) VALUES (?,?,?)";
    private final String SQL_GET_IMAGE_BY_ID = "SELECT * FROM posts WHERE uid = ?";


    @Autowired
    private JdbcTemplate jdbcTemplate;

    // method to post data to sql
    public Integer insertData(String comment, MultipartFile file) throws IOException {
        String uid = generateUUID();
        int result = jdbcTemplate.update(SQL_INSERT_IMAGE, uid, file.getInputStream(), comment);
        System.out.println("Number of rows updated: " + result);
        return result;
    }

    // TODO: method to get data (Result set) from sql
    public Optional<Post> getDataById(String id) {

        return jdbcTemplate.query(
            SQL_GET_IMAGE_BY_ID,
            (ResultSet rs) -> {
                if(!rs.next())
                    return Optional.empty();
                final Post post = Post.create(rs);
                return Optional.of(post);
            }, 
            id
        );


        // create a Post Object
        // if(rowSet.next()) {
        //     ResultSet rs = rowSet.getResultSet();
        //     String uid = rs.getString("uid");
        //     String comment = rs.getString("comment");
        //     byte[] blobc = rs.getBytes("blobc");
        //     System.out.println(rs.getString("blobc"));
        //     System.out.println(rs.getString("comment"));
        // }

    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        System.out.println("UUID: " + uuid.toString());
        return uuid.toString().substring(0,8);
    }
    
}
