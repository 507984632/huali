package com.huali.utils.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回前端的对象
 *
 * @author Yang_my
 * @date 2021-01-20 23:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult<T> {

    public static final String SUCCESS_MSG = "成功";

    private String message;
    private T data;
    private final Long timestamp = System.currentTimeMillis();

    public JsonResult(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
