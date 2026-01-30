package org.example.directorduckservertest1.controller;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;
import org.example.directorduckservertest1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理-用户管理控制器
 */
@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin
public class AdminUserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户列表
     */
    @GetMapping
    public Result<List<User>> list() {
        return userService.getAllUsers();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}