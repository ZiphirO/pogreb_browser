package com.ziphiro.browserBotPart.controller;

import com.ziphiro.browserBotPart.DTO.LogInDto;
import com.ziphiro.browserBotPart.DTO.UserDTO;
import com.ziphiro.browserBotPart.entityes.User;
import com.ziphiro.browserBotPart.response.LogInResponse;
import com.ziphiro.browserBotPart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


   private final UserService userService;

    @GetMapping
    public boolean checkUser(User user){
        return userService.checkUser(user);
    }

    @GetMapping("/{userEmail}")
    public String getCurrUserName(@PathVariable String userEmail){
        return userService.getCurrentUserName(userEmail);
    }

    @PostMapping
    public void registerUser(User user){
        userService.initUser(user);
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
