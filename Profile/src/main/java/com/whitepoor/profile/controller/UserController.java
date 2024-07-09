package com.whitepoor.profile.controller;

import com.whitepoor.profile.config.Code;
import com.whitepoor.profile.config.MyException;
import com.whitepoor.profile.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.whitepoor.profile.service.UserService;
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "api/profile")
    @ResponseBody
    public User profile(@RequestParam String username) {
        return userService.getUser(username);
    }

    @CrossOrigin
    @PostMapping(value = "api/updateProfile")
    @ResponseBody
    public User updateProfile(@RequestParam String username,
                              @RequestParam(required = false) String password,
                              @RequestParam(required = false) String picture,
                              @RequestParam(required = false) Integer age,
                              @RequestParam(required = false) String subject,
                              @RequestParam(required = false) String gender) {
        User user = userService.getUser(username);
        if (user == null) throw new MyException(Code.USER_NOT_EXIST);
        if (picture != null) user.setPicture(picture);
        if (age != null) user.setAge(age);
        if (subject != null) userService.setSubject(user, subject);
        if (gender != null) userService.setGender(user,gender);

        return userService.updateUser(user);
    }
}
