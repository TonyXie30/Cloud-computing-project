package com.whitepoor.profile.service;

import com.whitepoor.profile.api.UserRepository;
import com.whitepoor.profile.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(String name) {
        return userRepository.findByUsername(name);
    }

    public void setSubject(User user, String subject) {
        user.setSubject(subject);
    }

    public void setGender(User user, String gender) {
        user.setGender(gender);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
