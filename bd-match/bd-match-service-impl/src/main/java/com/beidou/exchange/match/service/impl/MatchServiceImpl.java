package com.beidou.exchange.match.service.impl;

import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.match.MatchListener;
import com.beidou.exchange.match.MatchQueue;
import com.beidou.exchange.match.MatchThread;
import com.beidou.exchange.match.MatchThreadGroup;
import com.beidou.exchange.match.service.MatchService;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchListener matchListener;

    @Autowired
    private OrderService orderService;

    private MatchThreadGroup matchThreadGroup;

    /**
     * 提交订单
     * @param order
     */
    @Override
    public void submitOrder(Order order) {
        matchThreadGroup.submitOrder(order);
    }

    /**
     * 取消订单
     * @param order
     */
    @Override
    public void cancelOrder(Order order) {
        matchThreadGroup.removeOrder(order);
    }

    /**
     * 获取买单列表
     *
     * @param asset1
     * @param asset2
     * @return
     */
    @Override
    public List<Order> getBuyQueue(String asset1, String asset2) {
        if (StringUtils.isBlank(asset1) || StringUtils.isBlank(asset2)) {
            throw new BizException(ErrorEnum.MISSING_PARAM);
        }
        MatchQueue matchQueue = matchThreadGroup.getMatchQueue(asset1, asset2);
        if (matchQueue == null) {
            return new ArrayList<>(0);
        }
        ConcurrentSkipListSet<Order> orderList = asset1.compareTo(asset2) > 0 ?
                matchQueue.getSellOrderList() : matchQueue.getBuyOrderList();
        if (orderList == null) {
            return new ArrayList<>(0);
        }
        List<Order> resultList = new ArrayList<>(orderList.size());
        resultList.addAll(orderList);
        return resultList;
    }

    /**
     * 获取卖单列表
     *
     * @param asset1
     * @param asset2
     * @return
     */
    @Override
    public List<Order> getSellQueue(String asset1, String asset2) {
        if (StringUtils.isBlank(asset1) || StringUtils.isBlank(asset2)) {
            throw new BizException(ErrorEnum.MISSING_PARAM);
        }
        MatchQueue matchQueue = matchThreadGroup.getMatchQueue(asset1, asset2);
        if(null == matchQueue) {
            return new ArrayList<>(0);
        }
        ConcurrentSkipListSet<Order> orderList = asset1.compareTo(asset2) > 0 ?
                matchQueue.getBuyOrderList() : matchQueue.getSellOrderList();
        if (orderList == null) {
            return new ArrayList<>(0);
        }
        List<Order> resultList = new ArrayList<>(orderList.size());
        resultList.addAll(orderList);
        return resultList;
    }

    @PostConstruct
    public void init() {
        log.info("match service init.");
        matchThreadGroup = new MatchThreadGroup(4);
        matchThreadGroup.init(matchListener);
        List<Order> orderList = orderService.listAllOrderWaiting();
        for (Order order : orderList) {
            matchThreadGroup.submitOrder(order);
        }
    }
}

