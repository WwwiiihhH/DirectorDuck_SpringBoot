package org.example.directorduckservertest1.service.impl;

import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;
import org.example.directorduckservertest1.repository.UserRepository;
import org.example.directorduckservertest1.service.UserService;
import org.example.directorduckservertest1.util.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result<User> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return Result.error("用户名已存在");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return Result.error("邮箱已被注册");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            return Result.error("手机号已被注册");
        }

        user.setPassword(PasswordEncoderUtil.INSTANCE.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null); // 不返回密码

        return Result.success(savedUser);
    }

    @Override
    public Result<User> loginByPhone(String phone, String password) {
        return userRepository.findByPhone(phone)
                .map(user -> {
                    if (PasswordEncoderUtil.INSTANCE.matches(password, user.getPassword())) {
                        user.setPassword(null);
                        return Result.success(user);
                    } else {
                        return Result.<User>error("密码错误");
                    }
                })
                .orElseGet(() -> Result.<User>error("手机号未注册"));
    }
}

