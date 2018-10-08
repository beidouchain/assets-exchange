package com.beidou.exchange.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.vo.BDCCoinVO;
import com.beidou.exchange.redis.RedisUtil;
import com.beidou.exchange.rpc.WalletUtil;
import com.beidou.exchange.service.IdWorkerService;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.datasource.DataBaseConfiguration;
import com.beidou.exchange.demo.entity.Test;
import com.beidou.exchange.service.TestService;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.service.DbAssetsService;
import com.esotericsoftware.minlog.Log;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.googlecode.jsonrpc4j.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class DemoController {

    @Autowired
    private DataBaseConfiguration dataBaseConfiguration;

    //@Autowired
    //private TestService testService;

    @Autowired
    private DbAssetsService dbAssetsService;

    @Autowired
    private IdWorkerService idWorkerService;

    @Autowired
    private WalletUtil walletUtil;



    @RequestMapping("/dbConfig")
    public ServiceResponse dbConfig(@RequestBody BDCCoinVO vo) {
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, "hello");
    }

    @RequestMapping("/create")
    @Transactional
    public ServiceResponse create(@RequestBody Test test) {
        test.setId(idWorkerService.nextId());
        //testService.insert(test);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, test);
    }



    @RequestMapping("/test")
    public ServiceResponse test(@RequestBody BDCCoinVO vo) {

        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, vo);
    }

    @RequestMapping("/demo")
    public ServiceResponse demo(HttpServletRequest request, HttpServletResponse response) {
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS);
    }

}

