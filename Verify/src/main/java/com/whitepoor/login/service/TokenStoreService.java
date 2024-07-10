package com.whitepoor.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenStoreService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void storeToken(String username, String token) {
        redisTemplate.opsForValue().set(username, token);
    }

    public String getToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }
}
