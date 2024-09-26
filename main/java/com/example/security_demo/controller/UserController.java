package com.example.security_demo.controller;

import com.example.security_demo.model.User;
import com.example.security_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ready-set-cert-api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/healthcheck")
    public String healthCheckMessage() {
        return "this is my tomcat server, up and working.";
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user); // error here
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    }
}