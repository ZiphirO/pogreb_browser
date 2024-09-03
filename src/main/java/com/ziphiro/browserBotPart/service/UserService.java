package com.ziphiro.browserBotPart.service;

import com.ziphiro.browserBotPart.DTO.LogInDto;
import com.ziphiro.browserBotPart.DTO.UserDTO;
import com.ziphiro.browserBotPart.entityes.User;
import com.ziphiro.browserBotPart.repositories.UserRepository;
import com.ziphiro.browserBotPart.response.LogInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User initUser(User user){
        return userRepository.save(user);
    }

    public boolean checkUser(User user){
        boolean check = false;
        if (userRepository.findByName(user.getName()).isPresent()){
            check = true;
        }
        return check;
    }

    public String addUser(UserDTO userDTO){
        User user = new User(
                userDTO.getUserId(),
                userDTO.getUserName(),
                userDTO.getUserEmail(),
                this.passwordEncoder.encode(userDTO.getUserPass())
        );
        userRepository.save(user);
        return user.getName();
    }
    public LogInResponse logInUser (LogInDto logInDto){
        String msg = "";
        User user = userRepository.findByEmail(logInDto.getUserEmail());
        if (user != null){
            String password = logInDto.getUserPass();
            String encodePassword = user.getPass();
            Boolean isOk = passwordEncoder.matches(password, encodePassword);
            if (isOk){
                Optional<User> userOk  = userRepository.findOneByEmailAndPass(logInDto.getUserEmail(), encodePassword);
                if (userOk.isPresent()){
                    return new LogInResponse("Login success", true);
                }else {
                    return new LogInResponse("Login failed", false);
                }
            }else {
                return new LogInResponse("Password not match", false);
            }
        }else {
            return new LogInResponse("Email not exists", false);
        }
    }
}
