package com.beidou.exchange.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.balance.entity.Journal;
import com.beidou.exchange.balance.service.BalanceService;
import com.beidou.exchange.balance.service.JournalService;
import com.beidou.exchange.common.Constants;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.Result;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.order.service.OrderService;
import com.beidou.exchange.service.IdWorkerService;
import com.beidou.exchange.service.impl.CommonGenericServiceImpl;
import com.beidou.exchange.transaction.entity.BdTransaction;
import com.beidou.exchange.transaction.entity.Exchange;
import com.beidou.exchange.transaction.mapper.ExchangeMapper;
import com.beidou.exchange.transaction.service.BdTransactionService;
import com.beidou.exchange.transaction.service.ExchangeService;
import com.beidou.market.service.LastPriceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@PropertySource("classpath:bootstrap.properties")
public class ExchangeServiceImpl extends CommonGenericServiceImpl<Exchange> implements ExchangeService {
    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private IdWorkerService idWorkerService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private LastPriceService lastPriceService;

    @Autowired
    private BdTransactionService bdTransactionService;

    @Value("${beidou.transaction.needConfirmation}")
    private Boolean needConfirmation;

    private Result checkExchange(Exchange exchange) {
        if (exchange.getId() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "id");
        }
        if (StringUtils.isBlank(exchange.getAsset1())) {
            return new Result(ErrorEnum.MISSING_PARAM, "asset1");
        }
        if (StringUtils.isBlank(exchange.getAsset2())) {
            return new Result(ErrorEnum.MISSING_PARAM, "asset2");
        }
        if (exchange.getAmount1() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "amount1");
        }
        if (exchange.getAmount2() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "amount2");
        }
        if (exchange.getPrice() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "price");
        }
        if (exchange.getInversePrice() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "inversePrice");
        }
        if (exchange.getOrder1() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "order1");
        }
        if (exchange.getOrder2() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "order2");
        }
        return new Result();
    }

    /**
     * 创建交换
     *
     * @param exchange
     * @return
     */
    @Override
    public Result createExchange(Exchange exchange) {
        log.info("to createExchange: " + JSONObject.toJSONString(exchange));
        Result checkResult = checkExchange(exchange);
        if (checkResult.code != ErrorEnum.SUCCESS) {
            return checkResult;
        }
        int insertCount = exchangeMapper.insertSelective(exchange);
        return new Result(ErrorEnum.SUCCESS, insertCount);
    }


    //构造交换事务
    private Exchange buildExchange(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice) {
        Exchange exchange = new Exchange();
        Long id = idWorkerService.nextId();
        exchange.setId(id);
        exchange.setAsset1(awaitOrder.getMatchAsset());
        exchange.setAsset2(awaitOrder.getMatchPriceAsset());
        exchange.setAmount1(matchAmount);
        exchange.setAmount2(matchTotalPrice);
        exchange.setPrice(awaitOrder.getMatchPrice());
        exchange.setInversePrice(BigDecimal.ONE.divide(
                awaitOrder.getMatchPrice(), Constants.DEFAULT_DECIMAL_SCALE, BigDecimal.ROUND_UP));
        exchange.setOrder1(awaitOrder.getId());
        exchange.setOrder2(incomingOrder.getId());
        exchange.setCreatedOn(System.currentTimeMillis());
        exchange.setUpdatedOn(System.currentTimeMillis());
        exchange.setDirection(incomingOrder.getMatchDirection());
        if (!needConfirmation) {
            exchange.setStatus(Exchange.EXCHANGE_STATUS_CONFIRMED);
        }
        return exchange;
    }

    private Journal buildJournal(Order order, String counterpartUid, BigDecimal matchAmount, BigDecimal matchTotalPrice, Long transactionId) {
        Journal journal = new Journal();
        Long id = idWorkerService.nextId();
        journal.setId(id);
        journal.setAsset(order.getAsset());
        journal.setPriceAsset(order.getPriceAsset());
        if (order.isTurnOver()) {
            journal.setAmount(matchTotalPrice);
            journal.setTotalPrice(matchAmount);
        } else {
            journal.setAmount(matchAmount);
            journal.setTotalPrice(matchTotalPrice);
        }
        journal.setUid(order.getUid());
        journal.setCounterpartUid(counterpartUid);
        journal.setDirection(order.getDirection());
        journal.setTransactionId(transactionId);
        journal.setType(Journal.JOURNAL_TYPE_EXCHANGE);
        if (needConfirmation) {
            journal.setStatus(Journal.JOURNAL_STATUS_UNCONFIRMED);
        } else {
            journal.setStatus(Journal.JOURNAL_STATUS_CONFIRMED);
        }
        journal.setCreatedOn(System.currentTimeMillis());
        journal.setUpdatedOn(System.currentTimeMillis());
        return journal;
    }

    //构造交易事务
    private BdTransaction buildBdTransaction(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice, Long exchangeId) {
        BdTransaction bdTransaction = new BdTransaction();
        Long id = idWorkerService.nextId();
        bdTransaction.setId(id);
        bdTransaction.setExchangeId(exchangeId);
        if (awaitOrder.getMatchDirection() == Order.BUY) {
            //incoming的uid用awaitOrder.getMatchPriceAsset()与await的uid交换awaitOrder.getMatchAsset()
            bdTransaction.setUid1(incomingOrder.getUid());
            bdTransaction.setUid2(awaitOrder.getUid());
        } else {
            //await的uid用awaitOrder.getMatchPriceAsset()与incoming的uid交换awaitOrder.getMatchAsset()
            bdTransaction.setUid1(awaitOrder.getUid());
            bdTransaction.setUid2(incomingOrder.getUid());
        }
        bdTransaction.setType(BdTransaction.TRANSACTION_TYPE_EXCHANGE);
        bdTransaction.setAsset1(awaitOrder.getMatchAsset());
        bdTransaction.setAsset2(awaitOrder.getMatchPriceAsset());
        bdTransaction.setAmount1(matchAmount);
        bdTransaction.setAmount2(matchTotalPrice);
        bdTransaction.setCreatedOn(System.currentTimeMillis());
        bdTransaction.setUpdatedOn(System.currentTimeMillis());
        if (needConfirmation) {
            bdTransaction.setStatus(BdTransaction.TRANSACTION_STATUS_UNCONFIRMED);
        } else {
            bdTransaction.setStatus(BdTransaction.TRANSACTION_STATUS_CONFIRMED);
        }
        return bdTransaction;
    }

    /**
     * 创建交换
     *
     * @param awaitOrder      等待匹配的订单
     * @param incomingOrder   到来的订单
     * @param matchAmount     成交量
     * @param matchTotalPrice 成交总价
     * @return
     */
    @Transactional
    @Override
    public BdTransaction createExchange(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice) {
        //创建成交记录
        Exchange exchange = buildExchange(awaitOrder, incomingOrder, matchAmount, matchTotalPrice);
        Result result = createExchange(exchange);
        if (result.code != ErrorEnum.SUCCESS) {
            throw new BizException(result.code, result.payload, result.msg);
        }

        //更新最新成交价
        lastPriceService.saveLastPrice(awaitOrder.getMatchAsset(), awaitOrder.getMatchPriceAsset(), awaitOrder.getMatchPrice(),matchAmount,matchTotalPrice);

        //更新挂单信息
        result = orderService.matchOrder(awaitOrder, incomingOrder, matchAmount, matchTotalPrice);
        if (result.code != ErrorEnum.SUCCESS) {
            throw new BizException(result.code, result.payload, result.msg);
        }

        //更新余额
        //更新等待成交者的余额
        if (awaitOrder.getMatchDirection() == Order.BUY) {
            balanceService.exchange(awaitOrder.getUid(), awaitOrder.getMatchPriceAsset(), awaitOrder.getMatchTotalPrice(),
                    awaitOrder.getMatchAsset(), awaitOrder.getMatchAmount());
        } else {
            balanceService.exchange(awaitOrder.getUid(), awaitOrder.getMatchAsset(), awaitOrder.getMatchAmount(),
                    awaitOrder.getMatchPriceAsset(), awaitOrder.getMatchTotalPrice());
        }
        //更新交易发起者的余额
        if (incomingOrder.getMatchDirection() == Order.BUY) {
            balanceService.exchange(incomingOrder.getUid(), incomingOrder.getMatchPriceAsset(), incomingOrder.getMatchTotalPrice(),
                    incomingOrder.getMatchAsset(), incomingOrder.getMatchAmount());
        } else {
            balanceService.exchange(incomingOrder.getUid(), incomingOrder.getMatchAsset(), incomingOrder.getMatchAmount(),
                    incomingOrder.getMatchPriceAsset(), incomingOrder.getMatchTotalPrice());
        }

        //创建事务
        BdTransaction bdTransaction = buildBdTransaction(awaitOrder, incomingOrder, matchAmount, matchTotalPrice, exchange.getId());
        result = bdTransactionService.createBdTransaction(bdTransaction);
        if (result.code != ErrorEnum.SUCCESS) {
            throw new BizException(result.code, result.payload, result.msg);
        }

        //创建流水
        //给等待成交者创建流水
        Journal awaitJournal = buildJournal(awaitOrder, incomingOrder.getUid(), matchAmount, matchTotalPrice, exchange.getId());
        journalService.createJournal(awaitJournal);
        //给交易发起者创建流水
        Journal incomingJournal = buildJournal(incomingOrder, awaitOrder.getUid(), matchAmount, matchTotalPrice, exchange.getId());
        journalService.createJournal(incomingJournal);

        return bdTransaction;
    }

    @Override
    public List<Exchange> list(Exchange exchange) {
        Example example = new Example(Exchange.class);
        example.createCriteria().andEqualTo("asset1", exchange.getAsset1())
                .andEqualTo("asset2", exchange.getAsset2());
        example.orderBy("id").desc();
        List<Exchange> list = exchangeMapper.selectByExample(example);
        return list;
    }

}
