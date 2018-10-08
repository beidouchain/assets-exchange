package com.beidou.exchange.order.service;

import com.beidou.exchange.common.Result;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.service.CommonGenericService;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService extends CommonGenericService<Order> {

    Result createOrder(Order order);

    int updateOrder(Order order);

    Order getOrderForUpdate(Long orderId);

    /**
     * 撤销订单
     *
     * @param orderId
     * @return
     */
    Result cancelOrder(Long orderId);

    Result doCancelOrder(Long orderId);

    /**
     * 查看指定用户指定交易对的订单
     *
     * @param uid        指定用户
     * @param asset      交易主资产
     * @param priceAsset 交易等价物资产
     * @return
     */
    List<Order> listOrder(String uid, String asset, String priceAsset);

    /**
     * 查询所有等待交易的订单
     * @return
     */
    List<Order> listAllOrderWaiting();

    /**
     * 匹配订单
     *
     * @param awaitOrder    在队列里等待匹配的限价单
     * @param incomingOrder 新进来的订单
     * @param matchAmount   成交量
     * @Param matchTotalPrice 成交总价
     * @return
     */
    Result matchOrder(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice);

    /**
     * 标记已入队等待匹配的订单，用于恢复队列
     *
     * @param orderId 订单id
     * @return
     */
    int markOrderEnqueue(long orderId);

    /**
     * 获取个人今天的订单列表
     * @param uid
     * @param asset
     * @param priceAsset
     * @param status  0：等待交易 1：已完成交易 2：已取消
     * @return
     */
    public List<Order> listOrderNowForStatus(String uid,String asset,String priceAsset,Integer status);

}
