package com.czh.interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.interview.common.dto.LoginRequest;
import com.czh.interview.common.dto.LoginResponse;
import com.czh.interview.common.dto.RegisterRequest;
import com.czh.interview.entity.User;
import com.czh.interview.handler.BizException;
import com.czh.interview.mapper.UserMapper;
import com.czh.interview.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret:interview-secret-key-change-in-production}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24小时
    private Long jwtExpiration;

    @Override
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BizException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, request.getEmail());
            if (this.count(wrapper) > 0) {
                throw new BizException("邮箱已被注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        this.save(user);
        return user;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = this.getOne(wrapper);

        if (user == null) {
            throw new BizException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }

        // 生成JWT Token
        String token = generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());

        return response;
    }

    /**
     * 生成JWT Token
     */
    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
