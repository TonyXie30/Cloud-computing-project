package com.whitepoor.login.service;

import com.whitepoor.login.api.UserRepository;
import com.whitepoor.login.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUser(String name){
        return userRepository.findByUsername(name);
    }

    public User register(User user) {
        return userRepository.save(user);
    }
}