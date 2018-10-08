package com.beidou.exchange.match;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.order.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.jctools.queues.MpscUnboundedArrayQueue;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MatchThread extends Thread {

    private Map<String, MatchQueue> queueMap;

    private MpscUnboundedArrayQueue<MatchTask> orderQueue;

    private MatchListener matchListener;

    public MatchThread(MatchListener matchListener) {
        this.orderQueue = new MpscUnboundedArrayQueue<>(1024);
        this.queueMap = new HashMap<>();
        this.matchListener = matchListener;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        while (true) {
            if (orderQueue.size() <= 0) {
                sleep(10);
                continue;
            }
            MatchTask task = orderQueue.poll();
            if (task == null) {
                sleep(10);
                continue;
            }
            Order order = task.getOrder();
            MatchQueue matchQueue = queueMap.get(order.getExchangeKey());
            if (matchQueue == null) {
                matchQueue = new MatchQueue(order.getMatchAsset(), order.getMatchPriceAsset(), matchListener);
                queueMap.put(order.getExchangeKey(), matchQueue);
            }
            if (task.getOp() == MatchTask.TASK_OP_SUBMIT) {
                matchQueue.match(order);
            } else {
                matchQueue.remove(order);
                matchListener.onCancel(order);
            }
        }
    }

    public void submitOrder(Order order) {
        boolean reuslt = orderQueue.offer(new MatchTask(MatchTask.TASK_OP_SUBMIT, order));
        log.info("submit order " + JSONObject.toJSONString(order) + " result: " + reuslt);
    }

    public void cancelOrder(Order order) {
        orderQueue.offer(new MatchTask(MatchTask.TASK_OP_CANCEL, order));
    }

    public MatchQueue getMatchQueue(String exchangeKey) {
        return queueMap.get(exchangeKey);
    }

}
