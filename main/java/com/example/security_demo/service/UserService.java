package com.example.security_demo.service;

import com.example.security_demo.model.Users;
import com.example.security_demo.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public Users register(Users user) {
        Users newUser = new Users();
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setUserName(user.getUserName());
        newUser.setEmailAddress(user.getEmailAddress());
        newUser = userRepo.save(newUser);
        entityManager.flush();
        return newUser;
    }

    @Transactional
    public String verify(Users user) {
        Users storedUser = userRepo.findByUserName(user.getUserName());
        if (storedUser == null) {
            logger.error("User not found: {}", user.getUserName());
            return "User not found";
        }
        try {
            if (encoder.matches(user.getPassword(), storedUser.getPassword())) {
                String token = jwtService.generateToken(storedUser.getUserName());
                //logger.info("token is: {}", token);
                return token;
            } else {
                logger.error("Invalid password: {}", user.getPassword());
                return "Invalid password";
            }
        } catch (Exception e) {
            System.out.println("Exception is:" + e);
            logger.debug("Authentication failed: {}", e.getStackTrace());
            return "Authentication failed";
        }
    }
}