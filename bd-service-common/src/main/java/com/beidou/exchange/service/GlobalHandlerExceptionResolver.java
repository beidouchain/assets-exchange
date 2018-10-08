package com.beidou.exchange.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.exception.BizException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger logger = LogManager.getLogger(GlobalHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        ServiceResponse<Object> sr = new ServiceResponse<Object>();
        if (e instanceof BizException) { //业务异常数据只需要返回错误码和描述即可
            BizException bizException = (BizException) e;
            if (bizException.getError() != null) {
                sr.setResult(bizException.getError().getErrorCode());
            } else {
                sr.setResult(bizException.getErrorCode());
            }
            sr.setDesc(bizException.getMessage());
            sr.setData(bizException.getPayload());
            logger.info(bizException.getMessage(), e);
            WebUtil.writeToWeb(httpServletResponse, JSON.toJSONString(sr, SerializerFeature.DisableCircularReferenceDetect));
        } else { //系统异常统一处理
            sr.setResult(ErrorEnum.SERVER_ERROR.getErrorCode());
            sr.setData(new Object());
            sr.setDesc(getErrorMsg(e));
            logger.error(e.getMessage(), e);
            WebUtil.writeToWeb(httpServletResponse, JSON.toJSONString(sr, SerializerFeature.DisableCircularReferenceDetect));
        }

        return null;
    }

    private String getErrorMsg(Exception e) {
        String msg = null;
        try {
            if (e.getMessage() == null || e.getMessage().isEmpty()) {
                if (e.getCause() == null) {
                    msg = e.toString();
                } else {
                    msg = e.getClass().getName() + ":" + e.getCause().getMessage();
                }
            } else {
                msg = e.getClass().getName() + ":" + e.getMessage();
            }
        } catch (Exception ex) {
            logger.error("未知异常，", e);
            msg = "未知异常";
        }

        return msg;
    }

}
