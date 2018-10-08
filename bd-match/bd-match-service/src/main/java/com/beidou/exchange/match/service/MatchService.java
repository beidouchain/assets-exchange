package com.beidou.exchange.match.service;

import com.beidou.exchange.order.entity.Order;

import java.util.List;

public interface MatchService {

    /**
     * 提交订单
     * @param order
     */
    void submitOrder(Order order);

    /**
     * 取消订单
     * @param order
     */
    void cancelOrder(Order order);

    /**
     * 获取买单列表
     * @param asset1
     * @param asset2
     * @return
     */
    List<Order> getBuyQueue(String asset1, String asset2);

    /**
     * 获取卖单列表
     * @param asset1
     * @param asset2
     * @return
     */
    List<Order> getSellQueue(String asset1, String asset2);
}

