package com.example.security_demo.service;

import com.example.security_demo.model.User;
import com.example.security_demo.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        String pwd = encoder.encode(user.getPassword());
        String name = user.getUserName();
        String email = user.getEmailAddress();
        System.out.println("password: "+pwd);
        user.setPassword(pwd);
        user.setUserName(name);
        user.setEmailAddress(email);
        System.out.println(user.getPassword());
        System.out.println(user.getUserName());
        System.out.println(user.getEmailAddress());
        userRepo.save(user);  //error here
        System.out.println(user.getPassword());
        System.out.println(user.getUserName());
        System.out.println(user.getEmailAddress());
        return user;
    }

    public String verify(User user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getUserName());

        return "fail";
    }
}