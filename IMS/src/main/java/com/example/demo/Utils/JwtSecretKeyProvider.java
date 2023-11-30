package com.example.demo.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretKeyProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}