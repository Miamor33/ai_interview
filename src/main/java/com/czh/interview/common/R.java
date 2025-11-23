package com.czh.interview.common;

import lombok.Data;

@Data
public class R<T> {

    private Integer code;   // 状态码 200=成功，其它=失败
    private String msg;     // 提示信息
    private T data;         // 返回数据

    // 成功（无返回数据）
    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        return r;
    }

    // 成功（有返回数据）
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    // 失败
    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    // 自定义
    public static <T> R<T> build(Integer code, String msg, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
