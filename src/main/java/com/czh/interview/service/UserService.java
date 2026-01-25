package com.czh.interview.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czh.interview.common.dto.LoginRequest;
import com.czh.interview.common.dto.LoginResponse;
import com.czh.interview.common.dto.RegisterRequest;
import com.czh.interview.entity.User;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     */
    User register(RegisterRequest request);

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
}
