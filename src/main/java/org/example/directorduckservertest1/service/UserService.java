package org.example.directorduckservertest1.service;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;

public interface UserService {
    Result<User> register(User user);
    Result<User> loginByPhone(String phone, String password);
}
