package com.beidou.market.controller;

import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.vo.*;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.entity.LastPrice;
import com.beidou.market.entity.MarketCategoryStateVO;
import com.beidou.market.entity.ResponseCoinPriceVO;
import com.beidou.market.service.DbAssetsService;
import com.beidou.market.service.LastPriceService;
import com.beidou.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@RestController
@RequestMapping("/assetsMarket")
public class AssetsMarketController {


    @Autowired
    private DbAssetsService dbAssetsService;

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private LastPriceService lastPriceService;
    /**
     * 发行资产
     * @param vo
     * @return
     */
    @RequestMapping("/issue")
     public ServiceResponse issue(@RequestBody AssetsVO vo) {
        AssetsVO resultVO = dbAssetsService.issue(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultVO);
    }


    /**
     * 发送资产
     * @param vo
     * @return
     */
    @RequestMapping("/sendassetfrom")
    public ServiceResponse sendassetfrom(@RequestBody SendAssetfromVO vo) {
        List<AddressBalancesVO> resultList = dbAssetsService.sendassetfrom(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultList);
    }

    /**
     * 原子交易
     * @param vo
     * @return
     */
    @RequestMapping("/exchange")
    public ServiceResponse exchange(@RequestBody AtomicExchangeVO vo) {
        List<AddressBalancesVO> resultList = dbAssetsService.exchange(vo);
        JSONObject json = new JSONObject();
        json.put("fromBalances",resultList);
        json.put("toBalances",assetsService.getaddressbalances(vo.getToAddress()));
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, json);
    }

    /**
     * 获取首页
     * @param
     * @return
     */
    @RequestMapping("/getHomePage")
    public ServiceResponse getHomePage(@RequestBody String coinName) {
        List<ResponseCoinPriceVO> resultList = dbAssetsService.getHomePage(coinName);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultList);
    }

    /**
     * 获取用户资产列表
     * @param
     * @return
     */
    @RequestMapping("/getUserAsset")
    public ServiceResponse getUserAsset(@RequestBody UserInfoVO vo) {
        List<AssetCategory> resultList = dbAssetsService.getUserAsset(vo.getAddress());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultList);
    }

    /**
     * 获取所有资产列表
     *
     * @return
     */
    @RequestMapping("/getAllAsset")
    public ServiceResponse getAllAsset()  {
        List<AssetCategory> resultList = dbAssetsService.getAllAsset();
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultList);
    }

    /**
     * 获取市场动态列表 - 名字 价格 涨幅
     *
     * @return
     */
    @RequestMapping("/getMarketCategoryStateList")
    public ServiceResponse getMarketCategoryStateList(@RequestBody AssetsVO vo)  {
        List<MarketCategoryStateVO> marketCategoryStateVOList = marketService.getMarketCategoryStateList(vo.getName());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, marketCategoryStateVOList);
    }

    /**
     * 获取当前币种 - 名字 价格 涨幅 成交量
     *
     * @return
     */
    @RequestMapping("/getCoinInfo")
    public ServiceResponse getCoinInfo(@RequestBody AssetsVO vo)  {
        List<MarketCategoryStateVO> marketCategoryStateVOList = marketService.getMarketCategoryStateList(vo.getName());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, marketCategoryStateVOList);
    }



    /**
     * 获取资产类型
     * @return
     */
    @RequestMapping("/getAssetCategoryForType")
    public ServiceResponse getAssetCategoryForType(@RequestBody AssetsVO vo)  {
        AssetCategory result = dbAssetsService.getAssetCategoryForType(vo.getType());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, result);
    }

    /**
     * 获取资产类型
     * @return
     */
    @RequestMapping("/saveLastPriceTest")
    public ServiceResponse saveLastPrice(@RequestBody LastPrice vo)  {
        lastPriceService.saveLastPrice(vo.getAsset1(),vo.getAsset2(),vo.getPrice(),vo.getDayAmount1(),vo.getDayAmount2());
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, null);
    }

    /**
     * 根据资产名称查昵称
     * @param
     * @return
     */
    @RequestMapping("/getAssetCategoryByName")
    public ServiceResponse getAssetCategory(@RequestBody UserInfoVO vo) {
        Map<String,AssetCategory> map = dbAssetsService.getAllAssetMap();
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, map.get(vo.getName()));
    }
}
