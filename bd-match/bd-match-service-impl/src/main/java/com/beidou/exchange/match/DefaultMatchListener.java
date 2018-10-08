package com.beidou.exchange.match;

import com.beidou.blockchain.service.AtomicService;
import com.beidou.blockchain.vo.AtomicExchangeVO;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.order.service.OrderService;
import com.beidou.exchange.service.IdWorkerService;
import com.beidou.exchange.transaction.entity.BdTransaction;
import com.beidou.exchange.transaction.entity.Exchange;
import com.beidou.exchange.transaction.service.BdTransactionService;
import com.beidou.exchange.transaction.service.ExchangeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jctools.queues.MpscUnboundedArrayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Slf4j
@Component("matchListener")
public class DefaultMatchListener extends Thread implements MatchListener {

    private MpscUnboundedArrayQueue<TransactionTask> taskQueue;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BdTransactionService bdTransactionService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private AtomicService atomicService;

    @Override
    public void onMatch(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice) {
        TransactionTask task = new TransactionTask(
                TransactionTask.TASK_OP_MATCH, awaitOrder, incomingOrder, matchAmount, matchTotalPrice);
        taskQueue.offer(task);
    }

    @Override
    public void onCancel(Order order) {
        TransactionTask transactionTask = new TransactionTask(TransactionTask.TASK_OP_CANCEL, null, order, null, null);
        taskQueue.offer(transactionTask);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {

        }
    }

    private AtomicExchangeVO buildAtomicExchange(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice) {
        AtomicExchangeVO atomicExchange = new AtomicExchangeVO();
        atomicExchange.setFromAddress(awaitOrder.getUid());
        atomicExchange.setToAddress(incomingOrder.getUid());
        if (awaitOrder.getMatchDirection() == Order.BUY) {
            atomicExchange.setFromAssetName(awaitOrder.getMatchPriceAsset());
            atomicExchange.setFromCount(matchTotalPrice);
            atomicExchange.setToAssetName(awaitOrder.getMatchAsset());
            atomicExchange.setToCount(matchAmount);
        } else {
            atomicExchange.setFromAssetName(awaitOrder.getMatchAsset());
            atomicExchange.setFromCount(matchAmount);
            atomicExchange.setToAssetName(awaitOrder.getMatchPriceAsset());
            atomicExchange.setToCount(matchTotalPrice);
        }
        return atomicExchange;
    }

    @Override
    public void run() {
        while (true) {
            if (taskQueue.size() <= 0) {
                sleep(10);
                continue;
            }
            TransactionTask task = taskQueue.poll();
            if (task == null) {
                sleep(10);
                continue;
            }
            try {
                if (task.getOp() == TransactionTask.TASK_OP_MATCH) {
                    //执行关系库交易
                    BdTransaction bdTransaction = exchangeService.createExchange(task.getAwaitOrder(),
                            task.getIncomingOrder(), task.getMatchAmount(), task.getMatchTotalPrice());
                    //执行链上交易
                    AtomicExchangeVO atomicExchange = buildAtomicExchange(
                            task.getAwaitOrder(), task.getIncomingOrder(), task.getMatchAmount(), task.getMatchTotalPrice());
                    String chainTxId = atomicService.atomicExchange(atomicExchange);
                    if (StringUtils.isBlank(chainTxId)) {
                        throw new BizException(ErrorEnum.TRANSACTION_CHAIN_ATOMIC_EXCHANGE_FAIL);
                    } else {
                        //回填链上事务id
                        bdTransaction.setChainTxid(chainTxId);
                        bdTransactionService.updateByPrimaryKeySelective(bdTransaction);
                    }
                } else {
                    orderService.doCancelOrder(task.getIncomingOrder().getId());
                }
            } catch (BizException e) {
                log.error("onMatch bizException: ", e);
            } catch (Exception e) {
                log.error("onMatch exception: ", e);
            }
        }
    }

    @PostConstruct
    public void init() {
        log.info("match listener init.");
        taskQueue = new MpscUnboundedArrayQueue<>(1024);
        start();
    }

    @AllArgsConstructor
    @Data
    private static class TransactionTask {
        public static final int TASK_OP_MATCH = 0;
        public static final int TASK_OP_CANCEL = 1;

        private int op;
        private Order awaitOrder;
        private Order incomingOrder;
        private BigDecimal matchAmount;
        private BigDecimal matchTotalPrice;
    }
}

