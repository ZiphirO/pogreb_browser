package com.ziphiro.browserBotPart.controller;

import com.ziphiro.browserBotPart.DTO.LogInDto;
import com.ziphiro.browserBotPart.DTO.UserDTO;
import com.ziphiro.browserBotPart.entityes.User;
import com.ziphiro.browserBotPart.response.LogInResponse;
import com.ziphiro.browserBotPart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
   private UserService userService;

    @GetMapping
    public boolean checkUser(User user){
        return userService.checkUser(user);
    }
    @PostMapping
    public User registerUser(User user){
        return userService.initUser(user);
    }

    @PostMapping("/add")
    public String addUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    }
    @PostMapping("/logIn")
    public ResponseEntity<?> logInUser(@RequestBody LogInDto logInDto){
        LogInResponse logInResponse = userService.logInUser(logInDto);
        return ResponseEntity.ok(logInResponse);
    }
}
