package com.beidou.exchange.service;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.ErrorEnumType;

import java.io.Serializable;

/**
 * 服务器返回数据对象
 */

public class ServiceResponse<T> implements Serializable {

    private static final long serialVersionUID = 1232322262223333L;

    private T data;
    private String result = "0";
    private String desc;
    private long time = System.currentTimeMillis();

    @JSONField(serialize = false)
    private ErrorEnumType resultType;

    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long now) {
        this.time = now;
    }

    public ErrorEnumType getResultType() {
        return resultType;
    }

    public void setResultType(ErrorEnumType resultType) {
        this.resultType = resultType;
    }

    public static <R> ServiceResponse<R> buildResponse(ErrorEnumType result) {
        return buildResponse(result, null);
    }

    public static <R> ServiceResponse<R> buildResponse(R data) {
        return buildResponse(ErrorEnum.SUCCESS, data);
    }

    @JSONField(serialize = false)
    public static <R> ServiceResponse<R> buildResponse(String result, R data) {
        ErrorEnumType resultType = ErrorEnum.getErrorByCode(result);
        return buildResponse(resultType, data, null);
    }

    @JSONField(serialize = false)
    public static <R> ServiceResponse<R> buildResponse(String result, R data, String desc) {
        ErrorEnumType resultType = ErrorEnum.getErrorByCode(result);
        return buildResponse(resultType, data, desc);
    }

    @JSONField(serialize = false)
    public static <R> ServiceResponse<R> buildResponse(ErrorEnumType result, R data) {
        return buildResponse(result, data, null);
    }

    @JSONField(serialize = false)
    public static <R> ServiceResponse<R> buildResponse(ErrorEnumType result, R data, String desc) {
        ServiceResponse<R> sr = new ServiceResponse<R>();
        sr.setResultType(result);
        sr.setResult(result.getErrorCode());
        sr.setData(data);
        sr.setDesc(desc == null ? result.getDesc() : desc);
        return sr;
    }
}
