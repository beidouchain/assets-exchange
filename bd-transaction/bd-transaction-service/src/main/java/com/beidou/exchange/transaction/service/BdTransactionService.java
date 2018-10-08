package com.beidou.exchange.transaction.service;

import com.beidou.exchange.common.Result;
import com.beidou.exchange.service.CommonGenericService;
import com.beidou.exchange.transaction.entity.BdTransaction;

import java.util.List;

public interface BdTransactionService extends CommonGenericService<BdTransaction> {


    /**
     * 创建事务
     * @param bdTransaction
     * @return
     */
    Result createBdTransaction(BdTransaction bdTransaction);

    /**
     * 获取未确认事务
     * @return
     */
    List<BdTransaction> getUnconfirmTransactions();

}

