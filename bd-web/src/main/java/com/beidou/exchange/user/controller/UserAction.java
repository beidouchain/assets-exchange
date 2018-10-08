package com.beidou.exchange.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.vo.UserInfoVO;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.exchange.service.WebUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by fengguoqing on s2018/6/27.
 */
@Controller
@RequestMapping("/user")
public class UserAction {
    private static final Logger LOG = Logger.getLogger(UserAction.class);

    @Autowired
    private AssetsService assetsService;


    @RequestMapping("/regist")
    public void regist(HttpServletResponse response,@RequestBody UserInfoVO user) {
        UserInfoVO ret = assetsService.regist(user);
        LOG.info("regist info=" + JSONObject.toJSONString(ret));
        WebUtil.writeToWebByGzip(response, JSONObject.toJSONString(ServiceResponse.buildResponse("")));
    }

    @RequestMapping("/login")
    public void login(HttpServletResponse response,@RequestBody UserInfoVO user) {
        UserInfoVO ret = assetsService.login(user);
        LOG.info("login info=" + JSONObject.toJSONString(ret));
        System.out.println("login info=" + JSONObject.toJSONString(ret));
        WebUtil.writeToWebByGzip(response, JSONObject.toJSONString(ServiceResponse.buildResponse(ret)));
    }

}