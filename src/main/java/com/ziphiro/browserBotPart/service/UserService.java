package com.ziphiro.browserBotPart.service;

import com.ziphiro.browserBotPart.DTO.LogInDto;
import com.ziphiro.browserBotPart.DTO.UserDTO;
import com.ziphiro.browserBotPart.entityes.User;
import com.ziphiro.browserBotPart.repositories.UserRepository;
import com.ziphiro.browserBotPart.response.LogInResponse;
import com.ziphiro.browserBotPart.values.StV;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void initUser(User user){
        userRepository.save(user);
    }

    public String getCurrentUserName(String userEmail){
        if (userRepository.optFindByEmail(userEmail).isPresent()){
           User currentUser = userRepository.findByEmail(userEmail);
           return currentUser.getName();
        } else {
            return StV.UNKNOWN_USER;
        }
    }

    public boolean checkUser(User user){
        return userRepository.findByName(user.getName()).isPresent();
    }

    public String addUser(UserDTO userDTO){
        User user = new User(
                userDTO.getUserId(),
                userDTO.getUserName(),
                userDTO.getUserEmail(),
                this.passwordEncoder.encode(userDTO.getUserPass())
        );
        userRepository.save(user);
        return user.getName() + StV.REGISTERED;
    }
    //need to refactor
    public LogInResponse logInUser (LogInDto logInDto){
        User user = userRepository.findByEmail(logInDto.getUserEmail());
        if (user != null){
            String password = logInDto.getUserPass();
            String encodePassword = user.getPass();
            boolean isOk = passwordEncoder.matches(password, encodePassword);
            if (isOk){
                Optional<User> userOk  = userRepository.findOneByEmailAndPass(logInDto.getUserEmail(), encodePassword);
                if (userOk.isPresent()){
                    return new LogInResponse(StV.LOGIN_SUCCESS, true);
                }else {
                    return new LogInResponse(StV.LOGIN_FAILED, false);
                }
            }else {
                return new LogInResponse("Password not match", false);
            }
        }
        else {
            return new LogInResponse("Email not exists", false);
        }
    }
}
