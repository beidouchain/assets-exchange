package com.beidou.exchange.match;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.common.Constants;
import com.beidou.exchange.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;


@Slf4j
@Data
public class MatchQueue {
    private String asset;
    private String priceAsset;

    private ConcurrentSkipListSet<Order> buyOrderList;
    private ConcurrentSkipListSet<Order> sellOrderList;

    private MatchListener matchListener;

    public MatchQueue(@NonNull String asset, @NonNull String priceAsset, @NonNull MatchListener matchListener) {
        this.asset = asset;
        this.priceAsset = priceAsset;
        buyOrderList = new ConcurrentSkipListSet<>(new OrderComparator());
        sellOrderList = new ConcurrentSkipListSet<>(new OrderComparator());
        this.matchListener = matchListener;
    }

    private MatchResult matchAmountWithAmount(Order awaitOrder, Order incomingOrder) {
        if (awaitOrder.getMatchAmount().compareTo(incomingOrder.getMatchAmount()) > 0) {
            BigDecimal matchAmount = incomingOrder.getMatchAmount();
            BigDecimal matchTotalPrice = awaitOrder.subtractMatchAmount(matchAmount);
            incomingOrder.subtractMatchAmount(matchAmount);
            return new MatchResult(awaitOrder, matchAmount, matchTotalPrice);
        } else {
            BigDecimal matchAmount = awaitOrder.getMatchAmount();
            BigDecimal matchTotalPrice = awaitOrder.subtractMatchAmount(matchAmount);
            incomingOrder.subtractMatchAmount(matchAmount);
            return new MatchResult(incomingOrder, matchAmount, matchTotalPrice);
        }
    }

    private MatchResult matchTotalPriceWithTotalPrice(Order awaitOrder, Order incomingOrder) {
        if (awaitOrder.getMatchTotalPrice().compareTo(incomingOrder.getMatchTotalPrice()) > 0) {
            BigDecimal matchTotalPrice = incomingOrder.getMatchTotalPrice();
            BigDecimal matchAmount = awaitOrder.subtractMatchTotalPrice(matchTotalPrice);
            incomingOrder.subtractMatchTotalPrice(matchTotalPrice);
            return new MatchResult(awaitOrder, matchAmount, matchTotalPrice);
        } else {
            BigDecimal matchTotalPrice = awaitOrder.getMatchTotalPrice();
            BigDecimal matchAmount = awaitOrder.subtractMatchTotalPrice(matchTotalPrice);
            incomingOrder.subtractMatchTotalPrice(matchTotalPrice);
            return new MatchResult(incomingOrder, matchAmount, matchTotalPrice);
        }
    }

    private MatchResult match(Order awaitOrder, Order incomingOrder) {
        if (incomingOrder.getMatchDirection() == Order.BUY) {
            //当前订单是个买单，花费totalPrice个资产就可以
            if (awaitOrder.getMatchPrice().compareTo(incomingOrder.getMatchPrice()) > 0) {
                return null;
            }
            return matchTotalPriceWithTotalPrice(awaitOrder, incomingOrder);
        } else {
            //当前订单是个卖单，卖出amount个资产就可以
            if (awaitOrder.getMatchPrice().compareTo(incomingOrder.getMatchPrice()) < 0) {
                return null;
            }
            return matchAmountWithAmount(awaitOrder, incomingOrder);
        }
    }

    public void match(Order order) {
        while (true) {
            ConcurrentSkipListSet<Order> belongOrderList = order.getMatchDirection() == Order.BUY ?
                    buyOrderList : sellOrderList;
            ConcurrentSkipListSet<Order> awaitOrderList = order.getMatchDirection() == Order.BUY ?
                    sellOrderList : buyOrderList;
            //没有对应的待交易单
            if (awaitOrderList.size() <= 0) {
                belongOrderList.add(order);
                break;
            }
            Order awaitOrder = order.getMatchDirection() == Order.BUY ? sellOrderList.first() : buyOrderList.last();
            System.out.println("===before match, awaitOrder: " + JSONObject.toJSONString(awaitOrder)
                    + ", incomingOrder: " + JSONObject.toJSONString(order));
            MatchResult matchResult = match(awaitOrder, order);
            if (matchResult == null) {
                System.out.println("===do not match");
                //没有合适的订单撮合
                belongOrderList.add(order);
                break;
            } else {
                System.out.println("===matchAmount: " + matchResult.getMatchAmount()
                        + ", matchTotalPrice: " + matchResult.getMatchTotalPrice()
                        + ", matchPrice: " + awaitOrder.getMatchPrice());
                System.out.println("===after match, awaitOrder: " + JSONObject.toJSONString(awaitOrder)
                        + ", incomingOrder: " + JSONObject.toJSONString(order));
                //撮合成功，回调
                Order remainOrder = matchResult.getRemainOrder();
                //余下的订单不是当前订单，表示当前订单已全部撮合完
                boolean hasOrderComplete = false;
                if (remainOrder == awaitOrder) {
                    order.setStatus(Order.ORDER_STATUS_COMPLETE);
                    if (remainOrder.getMatchAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        awaitOrder.setStatus(Order.ORDER_STATUS_COMPLETE);
                        awaitOrderList.remove(awaitOrder);
                    }
                    hasOrderComplete = true;
                } else {
                    awaitOrder.setStatus(Order.ORDER_STATUS_COMPLETE);
                    awaitOrderList.remove(awaitOrder);
                    //当前订单已全部撮合
                    if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        order.setStatus(Order.ORDER_STATUS_COMPLETE);
                        hasOrderComplete = true;
                    }
                }
                matchListener.onMatch(awaitOrder, order, matchResult.getMatchAmount(), matchResult.getMatchTotalPrice());
                if (hasOrderComplete) {
                    break;
                }
            }
        }
    }

    public void remove(Order order) {
        ConcurrentSkipListSet<Order> belongOrderList = order.getMatchDirection() == Order.BUY ?
                buyOrderList : sellOrderList;
        belongOrderList.remove(order);
    }

    @AllArgsConstructor
    @Data
    private static class MatchResult {
        private Order remainOrder;
        private BigDecimal matchAmount;
        private BigDecimal matchTotalPrice;
    }

    private static class OrderComparator implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            int priceCompare = o1.getMatchPrice().compareTo(o2.getMatchPrice());
            return priceCompare == 0 ? o1.getId().compareTo(o2.getId()) : priceCompare;
        }
    }

    public static Order createTestOrder(String asset, String priceAsset, BigDecimal amount, BigDecimal totalPrice,
                                        short direction) {
        Order order = new Order();
        order.setAsset(asset);
        order.setPriceAsset(priceAsset);
        order.setAmount(amount);
        order.setTotalPrice(totalPrice);
        order.setDirection(direction);
        return order;
    }

    public static void main(String args[]) {
        ConcurrentSkipListSet<Order> orderSet = new ConcurrentSkipListSet<>(new OrderComparator());
        orderSet.add(createTestOrder("a", "b", new BigDecimal("2.0"), new BigDecimal("1"), Order.BUY));
        orderSet.add(createTestOrder("a", "b", new BigDecimal("1.5"), new BigDecimal("1"), Order.BUY));
        orderSet.add(createTestOrder("a", "b", new BigDecimal("1.0"), new BigDecimal("1"), Order.BUY));

        Order order = orderSet.pollFirst();
        System.out.println(JSONObject.toJSONString(order));
        order = orderSet.first();
        System.out.println(JSONObject.toJSONString(order));

        order = orderSet.pollLast();
        System.out.println(JSONObject.toJSONString(order));
        order = orderSet.last();
        System.out.println(JSONObject.toJSONString(order));

        orderSet.pollFirst();
        orderSet.first();
    }

}

