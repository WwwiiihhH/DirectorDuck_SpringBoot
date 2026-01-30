package org.example.directorduckservertest1.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.directorduckservertest1.common.Result;
import org.example.directorduckservertest1.entity.User;
import org.example.directorduckservertest1.repository.UserRepository;
import org.example.directorduckservertest1.service.UserService;
import org.example.directorduckservertest1.util.PasswordEncoderUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

    @Override
    public Result<List<User>> getAllUsers() {
        // 直接调用 Repository 查所有，实际项目中建议做分页，这里先简单做
        List<User> users = userRepository.findAll();
        //以此保护隐私，把密码设为空，不返回给前端
        users.forEach(u -> u.setPassword("******"));
        return Result.success(users);
    }

    @Override
    public Result<String> deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return Result.error("用户不存在");
        }
        userRepository.deleteById(userId);
        return Result.success("用户已删除");
    }
}
