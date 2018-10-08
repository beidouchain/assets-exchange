package com.beidou.blockchain.controller;

import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.vo.AddressBalancesVO;
import com.beidou.blockchain.vo.AssetsVO;
import com.beidou.blockchain.vo.SendAssetfromVO;
import com.beidou.blockchain.vo.UserInfoVO;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.service.ServiceResponse;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private AssetsService assetsService;

    /**
     * 创建用户
     * @param vo
     * @return
     */
    @RequestMapping("/setUserInfo")
     public ServiceResponse setUserInfo(@RequestBody UserInfoVO vo) {
        UserInfoVO res = assetsService.setUserInfo(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, res);
    }

    /**
     * 获取用户信息
     * @param
     * @return
     */
    @RequestMapping("/getUserInfo")
    public ServiceResponse getUserInfo(@RequestBody UserInfoVO vo) {
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, assetsService.getUserInfo(vo.getAddress()));
    }


}
