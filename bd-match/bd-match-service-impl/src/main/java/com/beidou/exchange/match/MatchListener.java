package com.beidou.exchange.match;

import com.beidou.exchange.order.entity.Order;

import java.math.BigDecimal;

public interface MatchListener {

    void onMatch(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice);

    void onCancel(Order order);
}

