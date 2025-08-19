package org.example.directorduckservertest1.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtil {
    public static final PasswordEncoder INSTANCE = new BCryptPasswordEncoder();
}
