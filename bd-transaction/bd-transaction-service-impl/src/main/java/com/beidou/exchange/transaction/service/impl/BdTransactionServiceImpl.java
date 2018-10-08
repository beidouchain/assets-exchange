package com.beidou.exchange.transaction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.Result;
import com.beidou.exchange.service.impl.CommonGenericServiceImpl;
import com.beidou.exchange.transaction.entity.BdTransaction;
import com.beidou.exchange.transaction.mapper.BdTransactionMapper;
import com.beidou.exchange.transaction.service.BdTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BdTransactionServiceImpl extends CommonGenericServiceImpl<BdTransaction> implements BdTransactionService {

    @Autowired
    private BdTransactionMapper bdTransactionMapper;

    private Result checkTransaction(BdTransaction bdTransaction) {
        if (bdTransaction.getId() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "id");
        }
        if (bdTransaction.getType() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "type");
        }

        if (StringUtils.isBlank(bdTransaction.getUid1())) {
            return new Result(ErrorEnum.MISSING_PARAM, "uid1");
        }
        if (StringUtils.isBlank(bdTransaction.getAsset1())) {
            return new Result(ErrorEnum.MISSING_PARAM, "asset1");
        }
        if (bdTransaction.getAmount1() == null) {
            return new Result(ErrorEnum.MISSING_PARAM, "amount1");
        }
        int type = bdTransaction.getType();
        switch (type) {
            case BdTransaction.TRANSACTION_TYPE_EXCHANGE:
                if (StringUtils.isBlank(bdTransaction.getAsset2())) {
                    return new Result(ErrorEnum.MISSING_PARAM, "asset2");
                }
                if (bdTransaction.getAmount2() == null) {
                    return new Result(ErrorEnum.MISSING_PARAM, "amount2");
                }
            case BdTransaction.TRANSACTION_TYPE_TRANSFER:
                if (StringUtils.isBlank(bdTransaction.getUid2())) {
                    return new Result(ErrorEnum.MISSING_PARAM, "uid2");
                }
            default:
                break;
        }
        return new Result();
    }

    @Transactional
    @Override
    public Result createBdTransaction(BdTransaction transaction) {
        log.info("to createBdTransaction: " + JSONObject.toJSONString(transaction));
        Result checkResult = checkTransaction(transaction);
        if (checkResult.code != ErrorEnum.SUCCESS) {
            return checkResult;
        }
        int insertCount = bdTransactionMapper.insertSelective(transaction);
        return new Result(ErrorEnum.SUCCESS, insertCount);
    }

    @Override
    public List<BdTransaction> getUnconfirmTransactions() {
        return null;
    }

}

