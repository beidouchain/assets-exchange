package com.beidou.blockchain.controller;

import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.service.AtomicService;
import com.beidou.blockchain.vo.AddressBalancesVO;
import com.beidou.blockchain.vo.AssetsVO;
import com.beidou.blockchain.vo.AtomicExchangeVO;
import com.beidou.blockchain.vo.SendAssetfromVO;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@RestController
@RequestMapping("/atomic")
public class AtomicController {

    @Autowired
    private AtomicService atomicService;

    /**
     * 原子交易
     * @param vo
     * @return
     */
    @RequestMapping("/exchange")
     public ServiceResponse issue(@RequestBody AtomicExchangeVO vo) {
        String txid = atomicService.atomicExchange(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, txid);
    }


}
