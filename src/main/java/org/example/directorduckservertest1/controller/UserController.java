package org.example.directorduckservertest1.controller;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;
import org.example.directorduckservertest1.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户
 * 用户管理：登录与注册
 *
 * @module 用户模块
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     * 使用手机号与密码登录（query 参数）
     *
     * @param phone 手机号
     * @param password 密码
     * @return 登录结果（User）
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam String phone, @RequestParam String password) {
        return userService.loginByPhone(phone, password);
    }


    /**
     * 用户注册
     * 创建新用户账号（JSON 请求体）
     *
     * @param user 用户信息（username/phone/email/password 等）
     * @return 注册结果（User）
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        return userService.register(user);
    }
}
