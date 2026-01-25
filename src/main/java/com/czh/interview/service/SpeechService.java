package com.czh.interview.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 语音服务接口
 */
public interface SpeechService {
    /**
     * ASR语音识别
     *
     * @param audioFile 音频文件
     * @return 识别结果文本
     */
    String asr(MultipartFile audioFile);

    /**
     * TTS语音合成
     *
     * @param text 要合成的文本
     * @param voice 音色（可选）
     * @param speed 语速（可选，默认1.0）
     * @param pitch 音调（可选，默认1.0）
     * @return 音频文件URL
     */
    String tts(String text, String voice, Double speed, Double pitch);
}
