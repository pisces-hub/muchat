package io.pisceshub.muchat.server.exception;

import io.pisceshub.muchat.common.core.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaochangbai
 * @date 2023-06-17 16:58
 */
@Data
public class BusinessException extends RuntimeException implements Serializable {

    protected Integer code;
    protected String message;

    public BusinessException(){
        this(ResultCode.COMMON_ERROR);
    }

    public BusinessException(Integer code, String message){
        this.code=code;
        this.message=message;
    }

    public BusinessException(ResultCode resultCode, String message){
        this.code = resultCode.getCode();
        this.message=message;
    }

    public BusinessException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }

    public BusinessException(String message){
        this.code= ResultCode.PROGRAM_ERROR.getCode();
        this.message=message;
    }


}
