package com.jfcat.boottest.result;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class ResponseResult {
    // 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;

    public ResponseResult() {
    }
    public ResponseResult(Object data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
    }
    public ResponseResult(String message, Object data) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseResult ok() {
        return new ResponseResult(null);
    }
    public static ResponseResult ok(String message) {
        return new ResponseResult(message, null);
    }
    public static ResponseResult ok(Object data) {
        return new ResponseResult(data);
    }
    public static ResponseResult ok(String message, Object data) {
        return new ResponseResult(message, data);
    }

    public static ResponseResult build(Integer code, String message) {
        return new ResponseResult(code, message, null);
    }

    public static ResponseResult build(Integer code, String message, Object data) {
        return new ResponseResult(code, message, data);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 ResponseResult 对象
     * @param json
     * @return
     */
    public static ResponseResult format(String json) {
        try {
            return JSON.parseObject(json, ResponseResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
