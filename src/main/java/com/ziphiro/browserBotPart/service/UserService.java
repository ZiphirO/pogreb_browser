package com.ziphiro.browserBotPart.service;

import com.ziphiro.browserBotPart.DTO.LogInDto;
import com.ziphiro.browserBotPart.DTO.UserDTO;
import com.ziphiro.browserBotPart.auth.UserDetailsImpl;
import com.ziphiro.browserBotPart.entityes.User;
import com.ziphiro.browserBotPart.repositories.UserRepository;
import com.ziphiro.browserBotPart.response.LogInResponse;
import com.ziphiro.browserBotPart.values.StrV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String initUser(User user){
        if (user != null) {
            userRepository.save(user);
            return user.getName() + StrV.REGISTERED;
        }else return StrV.FAILED_REGISTRATION;
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
        logger.info(user.getName() + StrV.REGISTERED);
        return user.getName() + StrV.REGISTERED;
    }
    public LogInResponse logInUser (LogInDto logInDto){
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(logInDto.getUserEmail()));
        if (user.isPresent()) {
            String password = logInDto.getUserPass();
            String encodePassword = user.get().getPass();
            boolean isOk = passwordEncoder.matches(password, encodePassword);
            if (isOk) {
                logger.info(logInDto.getUserEmail() + StrV.LOGIN_SUCCESS);
                return new LogInResponse(StrV.LOGIN_SUCCESS, true);
            } else {
                logger.info(StrV.LOGIN_FAILED + logInDto.getUserEmail());
                return new LogInResponse("Password not match", false);
            }
        } else {
            logger.info(StrV.LOGIN_FAILED + logInDto.getUserEmail());
            return new LogInResponse("Email not exists", false);
        }
//        User user = userRepository.findByEmail(logInDto.getUserEmail());
//        if (user != null){
//            String password = logInDto.getUserPass();
//            String encodePassword = user.getPass();
//            boolean isOk = passwordEncoder.matches(password, encodePassword);
//            if (isOk){
//                Optional<User> userOk  = userRepository.findOneByEmailAndPass(logInDto.getUserEmail(), encodePassword);
//                if (userOk.isPresent()){
//                    return new LogInResponse("Login success", true);
//                }else {
//                    return new LogInResponse("Login failed", false);
//                }
//            }else {
//                return new LogInResponse("Password not match", false);
//            }
//        }else {
//            return new LogInResponse("Email not exists", false);
//        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username).orElseThrow(() ->
                                        new UsernameNotFoundException(String.format("User %s not found", username)));

        return UserDetailsImpl.build(user);
    }
}
