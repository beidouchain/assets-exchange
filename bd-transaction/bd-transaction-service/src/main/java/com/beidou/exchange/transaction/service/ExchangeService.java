package com.beidou.exchange.transaction.service;

import com.beidou.exchange.common.Result;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.service.CommonGenericService;
import com.beidou.exchange.transaction.entity.BdTransaction;
import com.beidou.exchange.transaction.entity.Exchange;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeService extends CommonGenericService<Exchange> {
    /**
     * 创建交换
     * @param exchange
     * @return
     */
    Result createExchange(Exchange exchange);

    /**
     * 创建交换
     * @param awaitOrder 等待匹配的订单
     * @param incomingOrder 到来的订单
     * @param matchAmount 成交量
     * @param matchTotalPrice 成交总价
     * @return
     */
    BdTransaction createExchange(
            Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice);


    /**
     * 查询交易list
     *
     * @param exchange
     * @return
     */
    List<Exchange> list(Exchange exchange);

}
