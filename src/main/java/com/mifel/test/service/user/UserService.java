package com.mifel.test.service.user;

import com.mifel.test.dto.auth.LoginDto;
import com.mifel.test.dto.auth.SignUpDto;

public interface UserService {
    SignUpDto signUp(SignUpDto signUpDto);
    LoginDto login(LoginDto loginRequest);
}