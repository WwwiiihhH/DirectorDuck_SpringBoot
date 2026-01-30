package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;

import java.util.List;

public interface UserService {
    Result<User> register(User user);
    Result<User> loginByPhone(String phone, String password);
    Result<List<User>> getAllUsers();
    Result<String> deleteUser(Long userId);
}
