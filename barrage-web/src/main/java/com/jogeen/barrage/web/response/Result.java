package com.jogeen.barrage.web.response;

import com.jogeen.barrage.common.Message;
import com.sun.net.httpserver.Authenticator;
import lombok.Data;

@Data
public class Result {
    private Integer code = 200;
    private String message = "";
    private Object data;

    public Result() {
    }


    public Result(String message) {
        this.setCode(200);
        this.setMessage(message);
    }
    public Result(String message, Object data) {
        this.setCode(200);
        this.setMessage(message);
        this.setData(data);
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result BuildFailedResult(String message) {
        Result result = new Result();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }
    public static Result BuildFailedResult(String message,Object o) {
        Result result = new Result();
        result.setCode(400);
        result.setMessage(message);
        result.setData(o);
        return result;
    }
}
