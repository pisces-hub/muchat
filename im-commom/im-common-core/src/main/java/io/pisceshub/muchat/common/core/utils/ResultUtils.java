package io.pisceshub.muchat.common.core.utils;

import io.pisceshub.muchat.common.core.enums.ResultCode;

public class ResultUtils {

    public static final <T> Result<T> success() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMsg());
        return result;
    }

    public static final <T> Result<T> success(T data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static final <T> Result<T> success(T data, String messsage) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(messsage);
        result.setData(data);
        return result;
    }

    public static final <T> Result<T> successMessage(String messsage) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(messsage);
        return result;
    }

    public static final <T> Result<T> error(Integer code, String messsage) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(messsage);
        return result;
    }

    public static final <T> Result<T> error() {
        return error(ResultCode.COMMON_ERROR);
    }

    public static final <T> Result<T> error(ResultCode resultCode, String messsage) {
        Result result = new Result();
        result.setCode(resultCode.getCode());
        result.setMessage(messsage);
        return result;
    }

    public static final <T> Result<T> error(ResultCode resultCode, String messsage, T data) {
        Result result = new Result();
        result.setCode(resultCode.getCode());
        result.setMessage(messsage);
        result.setData(data);
        return result;
    }

    public static final <T> Result<T> error(ResultCode resultCode) {
        Result result = new Result();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMsg());
        return result;
    }

}
