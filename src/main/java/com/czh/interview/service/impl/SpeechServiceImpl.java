package com.czh.interview.service.impl;

import com.czh.interview.service.SpeechService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpeechServiceImpl implements SpeechService {

    @Value("${speech.asr.url:http://localhost:8001/asr}")
    private String asrServiceUrl;

    @Value("${speech.tts.url:http://localhost:8002/tts}")
    private String ttsServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String asr(MultipartFile audioFile) {
        try {
            // 构建请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 调用ASR服务
            ResponseEntity<String> response = restTemplate.postForEntity(asrServiceUrl, requestEntity, String.class);

            // 解析响应
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                if (jsonNode.has("text")) {
                    return jsonNode.get("text").asText();
                } else if (jsonNode.has("data") && jsonNode.get("data").has("text")) {
                    return jsonNode.get("data").get("text").asText();
                }
            }

            log.warn("ASR服务返回异常: {}", response.getBody());
            return "";
        } catch (Exception e) {
            log.error("ASR识别失败", e);
            throw new RuntimeException("ASR识别失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String tts(String text, String voice, Double speed, Double pitch) {
        try {
            // 构建请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("text", text);
            if (voice != null) {
                requestBody.put("voice", voice);
            }
            if (speed != null) {
                requestBody.put("speed", speed);
            } else {
                requestBody.put("speed", 1.0);
            }
            if (pitch != null) {
                requestBody.put("pitch", pitch);
            } else {
                requestBody.put("pitch", 1.0);
            }

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 调用TTS服务
            ResponseEntity<String> response = restTemplate.postForEntity(ttsServiceUrl, requestEntity, String.class);

            // 解析响应
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                if (jsonNode.has("audioUrl")) {
                    return jsonNode.get("audioUrl").asText();
                } else if (jsonNode.has("data") && jsonNode.get("data").has("audioUrl")) {
                    return jsonNode.get("data").get("audioUrl").asText();
                } else if (jsonNode.has("url")) {
                    return jsonNode.get("url").asText();
                }
            }

            log.warn("TTS服务返回异常: {}", response.getBody());
            return "";
        } catch (Exception e) {
            log.error("TTS合成失败", e);
            throw new RuntimeException("TTS合成失败: " + e.getMessage(), e);
        }
    }
}
