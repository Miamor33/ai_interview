package com.czh.interview.controller;

import com.czh.interview.common.R;
import com.czh.interview.service.SpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音服务控制器
 */
@RestController
@RequestMapping("/api/speech")
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    /**
     * ASR语音识别
     */
    @PostMapping("/asr")
    public R<Map<String, Object>> asr(@RequestParam("file") MultipartFile file) {
        try {
            String text = speechService.asr(file);
            
            Map<String, Object> result = new HashMap<>();
            result.put("text", text);
            result.put("confidence", 0.95); // 可以从ASR服务返回中获取
            result.put("duration", 0); // 可以从ASR服务返回中获取
            
            return R.ok(result);
        } catch (Exception e) {
            return R.error("ASR识别失败: " + e.getMessage());
        }
    }

    /**
     * TTS语音合成
     */
    @PostMapping("/tts")
    public R<Map<String, Object>> tts(
            @RequestParam String text,
            @RequestParam(required = false) String voice,
            @RequestParam(required = false, defaultValue = "1.0") Double speed,
            @RequestParam(required = false, defaultValue = "1.0") Double pitch
    ) {
        try {
            String audioUrl = speechService.tts(text, voice, speed, pitch);
            
            Map<String, Object> result = new HashMap<>();
            result.put("audioUrl", audioUrl);
            result.put("duration", 0); // 可以从TTS服务返回中获取
            
            return R.ok(result);
        } catch (Exception e) {
            return R.error("TTS合成失败: " + e.getMessage());
        }
    }
}
