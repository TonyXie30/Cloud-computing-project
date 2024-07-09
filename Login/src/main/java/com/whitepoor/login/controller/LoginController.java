package com.whitepoor.login.controller;


import com.whitepoor.login.config.Code;
import com.whitepoor.login.config.MyException;
import com.whitepoor.login.pojo.User;
import com.whitepoor.login.service.TokenStoreService;
import com.whitepoor.login.service.UserService;
import com.whitepoor.login.utils.JwtUtil;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    TokenStoreService tokenStoreService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/login")
    @Transactional
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody User user){
        if (user==null || user.getUsername() == null || user.getPassword()==null){
            throw new MyException(Code.MISSING_FIELD);
        }

        User dbUser = userService.getUser(user.getUsername());

        if (dbUser == null){
            throw new MyException(Code.USER_NOT_EXIST);
        }

        if (!Objects.equals(dbUser.getPassword(), user.getPassword())){
            throw new MyException(Code.LOGIN_FAILED_WRONG_PW);
        }

        // Generate JWT for the authenticated user
        String jwt = jwtUtil.generateToken(user.getUsername());

        // Store the JWT in Redis
        tokenStoreService.storeToken(user.getUsername(), jwt);

        // Create a response object that includes the JWT
        Map<String, String> response = new HashMap<>();
        response.put("jwt", jwt);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/register")
    @Transactional
    @CrossOrigin
    @ResponseBody
    public User register(@RequestBody User user){
        if (user==null || user.getUsername() == null || user.getPassword()==null){
            throw new MyException(Code.MISSING_FIELD);
        }

        User dbUser = userService.getUser(user.getUsername());
        if (dbUser!=null){
            throw new MyException(Code.REG_FAILED_USER_EXIST);
        }

        if(!user.getPassword().matches("\\w+")){
            throw new MyException(Code.REG_FAILED_BAD_PW);
        }

        return userService.register(user);
    }

}
