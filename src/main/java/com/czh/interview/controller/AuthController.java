package com.czh.interview.controller;

import com.czh.interview.common.R;
import com.czh.interview.common.dto.LoginRequest;
import com.czh.interview.common.dto.LoginResponse;
import com.czh.interview.common.dto.RegisterRequest;
import com.czh.interview.entity.User;
import com.czh.interview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<User> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        // 不返回密码
        user.setPassword(null);
        return R.ok(user);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return R.ok(response);
    }
}
