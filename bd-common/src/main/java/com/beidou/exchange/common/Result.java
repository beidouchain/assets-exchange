package com.beidou.exchange.common;

public class Result<T> {
    public ErrorEnumType code;
    public T payload;
    public String msg;

    public Result() {
        this.code = ErrorEnum.SUCCESS;
    }

    public Result(ErrorEnumType code) {
        this(code, null, null);
    }

    public Result(ErrorEnumType code, T payload) {
        this(code, payload, null);
    }

    public Result(ErrorEnumType code, T payload, String msg) {
        this.code = code;
        this.payload = payload;
        this.msg = msg;
    }
}

