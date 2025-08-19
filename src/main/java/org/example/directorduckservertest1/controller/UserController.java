package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;
import org.example.directorduckservertest1.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String phone, @RequestParam String password) {
        return userService.loginByPhone(phone, password);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        return userService.register(user);
    }
}
