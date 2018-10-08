package com.beidou.user.controller;

import com.beidou.blockchain.vo.*;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.utils.UserPage;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.user.entity.UserPromotion;
import com.beidou.user.service.UserPromotionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@RestController
@RequestMapping("/userPromotion")
public class UserPromotionController {


    @Autowired
    private UserPromotionService userPromotionsService;


    @RequestMapping("/save")
     public ServiceResponse save(@RequestBody UserInfoVO vo) {
        userPromotionsService.saveUserPromotion(vo.getAddress(),vo.getOtherMyaddress());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, null);
    }
    @RequestMapping("/list")
    public ServiceResponse list(@RequestBody UserPage vo) {
        PageInfo<UserPromotion> resultVO = userPromotionsService.getMyPromotion(vo.getAddress(),vo.getPageNum());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultVO);
    }

    @RequestMapping("/nextVal")
    public ServiceResponse nextVal(@RequestBody UserInfoVO vo) {
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, null);
    }

}
