package com.beidou.exchange.match;

import com.beidou.exchange.order.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchThreadGroup {

    private int threadCount;
    private List<MatchThread> threadList;
    private Map<String, MatchThread> threadMap;

    public MatchThreadGroup(int threadCount) {
        this.threadCount = threadCount;
    }

    public void init(MatchListener matchListener) {
        threadList = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            MatchThread matchThread = new MatchThread(matchListener);
            threadList.add(matchThread);
            matchThread.start();
        }
        threadMap = new ConcurrentHashMap<>();
    }

    public void submitOrder(Order order) {
        String exchangeKey = order.getExchangeKey();
        MatchThread matchThread = threadMap.get(exchangeKey);
        if (matchThread == null) {
            //使用hash值分配线程
            int idx = (exchangeKey.hashCode() % threadCount + threadCount) % threadCount;
            matchThread = threadList.get(idx);
            threadMap.put(exchangeKey, matchThread);
        }
        matchThread.submitOrder(order);
    }

    public void removeOrder(Order order) {
        String exchangeKey = order.getExchangeKey();
        MatchThread matchThread = threadMap.get(exchangeKey);
        if (matchThread != null) {
            matchThread.cancelOrder(order);
        }
    }

    public MatchQueue getMatchQueue(String asset1, String asset2) {
        String exchangeKey = Order.getExchangeKey(asset1, asset2);
        MatchThread matchThread = threadMap.get(exchangeKey);
        if (matchThread == null) {
            return null;
        }
        return matchThread.getMatchQueue(exchangeKey);
    }
}

