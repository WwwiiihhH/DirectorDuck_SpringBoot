package org.example.directorduckservertest1.controller;


import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;
import org.example.directorduckservertest1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/login")
//    public Result<String> login(@RequestParam String username, @RequestParam String password) {
//        return userService.login(username, password);
//    }

    @PostMapping("/login")
    public Result<User> login(@RequestParam String phone, @RequestParam String password) {
        return userService.loginByPhone(phone, password);
    }


    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        return userService.register(user);
    }

}
