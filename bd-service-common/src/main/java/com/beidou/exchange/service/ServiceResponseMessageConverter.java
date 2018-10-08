package com.beidou.exchange.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


public class ServiceResponseMessageConverter extends AbstractHttpMessageConverter<Object> {

    public ServiceResponseMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    public boolean canRead(Class<?> contextClass, MediaType mediaType) {

        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;

    }


    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {

        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;

    }

    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }


    @Override
    public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        try {
            InputStream in = inputMessage.getBody();
            return JSON.parseObject(in, clazz);
        } catch (JSONException ex) {
            throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
        }
    }


    @Override
    public void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (o instanceof ServiceResponse) {
            if (((ServiceResponse) o).getData() == null) {
                ((ServiceResponse) o).setData(new JSONObject());
            }
            outputMessage.getBody().write(JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect).getBytes());
        } else {
            ServiceResponse<Object> sr = new ServiceResponse<Object>();
            if (o == null) {
                sr.setData(new JSONObject());
            } else {
                sr.setData(o);
            }
            outputMessage.getBody().write(JSON.toJSONString(sr, SerializerFeature.DisableCircularReferenceDetect).getBytes());
        }

    }


}
