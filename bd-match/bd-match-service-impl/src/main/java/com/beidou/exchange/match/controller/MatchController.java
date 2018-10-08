package com.beidou.exchange.match.controller;

import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.match.service.MatchService;
import com.beidou.exchange.match.vo.MatchVO;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.service.DbAssetsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/match")
@Slf4j
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private DbAssetsService dbAssetsService;

    /**
     * 盘口
     * @param
     * @return
     */
    @RequestMapping("/buyList")
    public ServiceResponse buyList(@RequestBody MatchVO vo) {
        List<Order> orderList = matchService.getBuyQueue(vo.getAsset(),vo.getPriceAsset());
        List<MatchVO> list = new ArrayList<>();
        MatchVO matchVO;
        Map<String,AssetCategory> map = dbAssetsService.getAllAssetMap();
        for (Order o : orderList) {
            matchVO = new MatchVO();
            if(o.getDirection() == 1) {
                matchVO.setPrice(o.getMatchPrice());
                matchVO.setAmount(o.getMatchAmount());
                matchVO.setAsset(o.getPriceAsset());
                if(null != map.get(o.getPriceAsset())){
                    matchVO.setAssetName(map.get(o.getPriceAsset()).getNick());
                }
                matchVO.setPriceAsset(o.getAsset());
                if(null != map.get(o.getAsset())){
                    matchVO.setPriceAssetName(map.get(o.getAsset()).getNick());
                }
            }else{
                matchVO.setAmount(o.getAmount());
                matchVO.setPrice(o.getPrice());
                matchVO.setAsset(o.getAsset());
                if(null != map.get(o.getAsset())){
                    matchVO.setAssetName(map.get(o.getAsset()).getNick());
                }
                matchVO.setPriceAsset(o.getPriceAsset());
                if(null != map.get(o.getPriceAsset())){
                    matchVO.setPriceAssetName(map.get(o.getPriceAsset()).getNick());
                }
            }
            list.add(matchVO);
        }
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, list);
    }

    /**
     * 盘口
     * @param
     * @return
     */
    @RequestMapping("/sellList")
    public ServiceResponse sellList(@RequestBody MatchVO vo) {
        List<Order> orderList = matchService.getSellQueue(vo.getAsset(),vo.getPriceAsset());
        List<MatchVO> list = new ArrayList<>();
        MatchVO matchVO;
        Map<String,AssetCategory> map = dbAssetsService.getAllAssetMap();
        for (Order o : orderList) {
            matchVO = new MatchVO();
            if(o.getDirection() == 0) {
                matchVO.setPrice(o.getMatchPrice());
                matchVO.setAmount(o.getMatchAmount());
                matchVO.setAsset(o.getPriceAsset());
                if(null != map.get(o.getPriceAsset())){
                    matchVO.setAssetName(map.get(o.getPriceAsset()).getNick());
                }
                matchVO.setPriceAsset(o.getAsset());
                if(null != map.get(o.getAsset())){
                    matchVO.setPriceAssetName(map.get(o.getAsset()).getNick());
                }
            }else{
                matchVO.setAmount(o.getAmount());
                matchVO.setPrice(o.getPrice());
                matchVO.setAsset(o.getAsset());
                if(null != map.get(o.getAsset())){
                    matchVO.setAssetName(map.get(o.getAsset()).getNick());
                }
                matchVO.setPriceAsset(o.getPriceAsset());
                if(null != map.get(o.getPriceAsset())){
                    matchVO.setPriceAssetName(map.get(o.getPriceAsset()).getNick());
                }
            }
            list.add(matchVO);
        }
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, list);
    }
}
