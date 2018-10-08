package com.beidou.exchange.transaction.controller;

import com.beidou.exchange.common.DateUtility;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.exchange.transaction.entity.Exchange;
import com.beidou.exchange.transaction.service.ExchangeService;
import com.beidou.exchange.transaction.vo.ExchangeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    /**
     * 成交历史
     * @param
     * @return
     */
    @RequestMapping("/list")
    public ServiceResponse list(@RequestBody ExchangeVO vo) {
        String asset1 = vo.getAsset();
        String asset2 = vo.getPriceAsset();

        Exchange exchange = new Exchange();
        boolean f = Order.compare(asset1, asset2) >= 0;
        if (f) {
            exchange.setAsset1(asset2);
            exchange.setAsset2(asset1);
        } else {
            exchange.setAsset1(asset1);
            exchange.setAsset2(asset2);
        }
        List<Exchange> exchangeList = exchangeService.list(exchange);
        List<ExchangeVO> list = new ArrayList<>();
        ExchangeVO exchangeVO;
        for (Exchange ec : exchangeList) {
            exchangeVO = new ExchangeVO();
            if(f) {
                exchangeVO.setAmount(ec.getAmount1());
                exchangeVO.setPrice(ec.getPrice());
            }else {
                exchangeVO.setAmount(ec.getAmount2());
                exchangeVO.setPrice(ec.getInversePrice());
            }
            exchangeVO.setTime(DateUtility.dateTimeToStr(new Date(ec.getCreatedOn()), "HH:mm:ss"));
            list.add(exchangeVO);
        }
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, list);
    }
}
