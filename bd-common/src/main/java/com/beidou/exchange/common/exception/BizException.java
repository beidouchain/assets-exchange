package com.beidou.exchange.common.exception;


import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.ErrorEnumType;

/**
 * 业务异常基类，所有业务异常需要继承本基类
 */
public class BizException extends RuntimeException {
    private ErrorEnumType error;

    private String errorCode;

    private Object payload;

    public BizException() {
        super();
        this.error = ErrorEnum.SUCCESS;
    }

    public BizException(ErrorEnumType error) {
        super(error.getDesc());
        this.error = error;
    }

    public BizException(ErrorEnumType error, Object payload) {
        this(error, payload, error.getDesc());
        this.error = error;
    }

    public BizException(ErrorEnumType error, String message) {
        super(message);
        this.error = error;
    }

    public BizException(ErrorEnumType error, Object payload, String message) {
        super(message);
        this.error = error;
        this.payload = payload;
    }

    public BizException(ErrorEnumType error, String message, Throwable cause) {
        this(error, null, message, cause);
    }

    public BizException(ErrorEnumType error, Object payload, String message, Throwable cause) {
        super(message);
        this.error = error;
        this.payload = payload;
    }


    public ErrorEnumType getError() {
        return error;
    }

    public String getExceptionCode() {
        return error.getErrorCode();
    }

    public String getExceptionDesc() {
        return error.getDesc();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setError(ErrorEnum error) {
        this.error = error;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
