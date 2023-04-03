package com.workshop.day37server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UploadRepo {
    
    @Autowired
    private JdbcTemplate jdbcTemplate
    
}
