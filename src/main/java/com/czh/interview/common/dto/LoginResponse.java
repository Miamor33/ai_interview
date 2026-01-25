package com.czh.interview.common.dto;

import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String username;
}
