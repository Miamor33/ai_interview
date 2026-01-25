package com.czh.interview.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class LocalChatClient {

    private final WebClient webClient;

    public LocalChatClient() {
        // LM Studio 地址
        this.webClient = WebClient.create("http://192.168.1.14:1234");
    }

    /**
     * 直接发送 prompt，返回字符串
     */
    public String chat(String prompt) {
        // LM Studio API body
        Map<String, Object> body = Map.of(
                "model", "qwen-7b-chat", // LM Studio 模型名
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        // 发送请求并返回结果
        return webClient.post()
                .uri("/api/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 阻塞返回字符串
    }
}
