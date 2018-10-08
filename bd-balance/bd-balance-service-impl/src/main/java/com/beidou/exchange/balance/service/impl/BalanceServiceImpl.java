package com.beidou.exchange.balance.service.impl;

import com.beidou.exchange.balance.entity.Balance;
import com.beidou.exchange.balance.entity.Journal;
import com.beidou.exchange.balance.mapper.BalanceMapper;
import com.beidou.exchange.balance.service.BalanceService;
import com.beidou.exchange.balance.service.JournalService;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.Result;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.service.IdWorkerService;
import com.beidou.exchange.service.impl.CommonGenericServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:bootstrap.properties")
public class BalanceServiceImpl extends CommonGenericServiceImpl<Balance> implements BalanceService {

    @Autowired
    private IdWorkerService idWorkerService;

    @Autowired
    private BalanceMapper balanceMapper;

    @Value("${beidou.transaction.needConfirmation}")
    private Boolean needConfirmation;

    /**
     * 创建、初始化账户指定资产余额，amount为总余额和可用余额
     *
     * @param uid    用户id
     * @param asset  资产名
     * @param amount 初始资产数量
     * @return
     */
    @Transactional
    @Override
    public Balance createBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = new Balance();
        Long id = idWorkerService.nextId();
        balance.setId(id);
        balance.setUid(uid);
        balance.setAsset(asset);
        balance.setBalance(amount);
        balance.setAvlAmount(amount);
        balance.setUcfAmount(BigDecimal.valueOf(0, 6));
        balance.setLocAmount(BigDecimal.valueOf(0, 6));
        balance.setCreatedOn(System.currentTimeMillis());
        balance.setUpdatedOn(System.currentTimeMillis());
        balanceMapper.insert(balance);
        return balance;
    }


    /**
     * 更新账户余额
     *
     * @param balance 余额信息
     * @return
     */
    @Transactional
    @Override
    public int updateBalance(Balance balance) {
        return balanceMapper.updateByPrimaryKeySelective(balance);
    }

    /**
     * 获取指定用户指定资产余额
     *
     * @param uid   用户id
     * @param asset 资产名
     * @return
     */
    @Override
    public Balance getBalanceByUidAndAsset(String uid, String asset) {
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(asset)) {
            throw new BizException(ErrorEnum.MISSING_PARAM);
        }
        Balance balanceQuery = new Balance();
        balanceQuery.setUid(uid);
        balanceQuery.setAsset(asset);
        return balanceMapper.selectOne(balanceQuery);
    }

    /**
     * 批量获取用户指定资产余额，返回每个资产及其对应的余额的映射
     * @param uid 用户id
     * @param assetList 资产列表
     * @return
     */
    @Override
    public Map<String, Balance> getBalanceByUidAndAsset(String uid, List<String> assetList) {
        if (StringUtils.isBlank(uid) || assetList == null) {
            throw new BizException(ErrorEnum.MISSING_PARAM);
        }
        Example example = new Example(Balance.class);
        example.createCriteria().andEqualTo("uid", uid).andIn("asset", assetList);
        List<Balance> balanceList = balanceMapper.selectByExample(example);
        Map<String, Balance> result = new HashMap<>(balanceList.size());
        for (Balance balance : balanceList) {
            result.put(balance.getAsset(), balance);
        }
        return result;
    }

    /**
     * 获取指定用户指定资产余额，并锁定记录
     *
     * @param uid   用户uid
     * @param asset 资产名
     * @return
     */
    @Transactional
    @Override
    public Balance getBalanceByUidAndAssetForUpdate(String uid, String asset) {
        Example example = new Example(Balance.class);
        example.createCriteria().andEqualTo("uid", uid)
                .andEqualTo("asset", asset);
        example.setForUpdate(true);
        List<Balance> balanceList = balanceMapper.selectByExample(example);
        if (balanceList.size() > 0) {
            return balanceList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 增加指定用户的可用资产的余额，同时加到总余额中
     *
     * @param uid    用户uid
     * @param asset  资产名
     * @param amount 增加数量
     * @return
     */
    @Transactional
    @Override
    public Result increaseAvlBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = getBalanceByUidAndAssetForUpdate(uid, asset);
        if (balance == null) {
            //如果没有该资产的账户则创建1个
            createBalance(uid, asset, amount);
            return new Result(ErrorEnum.SUCCESS, 1);
        }
        balance.setBalance(balance.getBalance().add(amount));
        balance.setAvlAmount(balance.getAvlAmount().add(amount));
        int rowCount = balanceMapper.updateByPrimaryKeySelective(balance);
        return new Result(ErrorEnum.SUCCESS, rowCount);
    }

    /**
     * 锁定余额，从可用余额中扣减指定数量amount，加到锁定余额中
     *
     * @param uid    用户uid
     * @param asset  资产名
     * @param amount 锁定数量
     * @return
     */
    @Transactional
    @Override
    public Result lockBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = getBalanceByUidAndAssetForUpdate(uid, asset);
        if (balance == null) {
            return new Result(ErrorEnum.BALANCE_NOT_EXIST);
        }
        if (balance.getAvlAmount().compareTo(amount) < 0) {
            return new Result(ErrorEnum.BALANCE_NOT_ENOUGH_AVL_BALANCE);
        }
        balance.setAvlAmount(balance.getAvlAmount().subtract(amount));
        balance.setLocAmount(balance.getLocAmount().add(amount));
        int rowCount = balanceMapper.updateByPrimaryKeySelective(balance);
        return new Result(ErrorEnum.SUCCESS, rowCount);
    }

    /**
     * 解锁余额，从锁定余额中扣减指定数量amount，加到总余额中
     *
     * @param uid    用户uid
     * @param asset  资产名
     * @param amount 解锁数量
     * @return
     */
    @Transactional
    @Override
    public Result unlockBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = getBalanceByUidAndAssetForUpdate(uid, asset);
        if (balance == null) {
            return new Result(ErrorEnum.BALANCE_NOT_EXIST);
        }
        if (balance.getLocAmount().compareTo(amount) < 0) {
            return new Result(ErrorEnum.BALANCE_NOT_ENOUGH_LOC_BALANCE);
        }
        balance.setAvlAmount(balance.getAvlAmount().add(amount));
        balance.setLocAmount(balance.getLocAmount().subtract(amount));
        int rowCount = balanceMapper.updateByPrimaryKeySelective(balance);
        return new Result(ErrorEnum.SUCCESS, rowCount);
    }

    /**
     * 扣减锁定余额，同时扣减总余额
     *
     * @param uid    用户uid
     * @param asset  资产名
     * @param amount 扣减数量
     * @return
     */
    @Transactional
    @Override
    public Result decreaseLockedBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = getBalanceByUidAndAssetForUpdate(uid, asset);
        if (balance == null) {
            return new Result(ErrorEnum.BALANCE_NOT_EXIST);
        }
        if (balance.getLocAmount().compareTo(amount) < 0) {
            return new Result(ErrorEnum.BALANCE_NOT_ENOUGH_LOC_BALANCE);
        }
        balance.setBalance(balance.getBalance().subtract(amount));
        balance.setLocAmount(balance.getLocAmount().subtract(amount));
        int rowCount = balanceMapper.updateByPrimaryKeySelective(balance);
        return new Result(ErrorEnum.SUCCESS, rowCount);
    }

    /**
     * 确认余额，从未确认余额中扣减amount加到可用余额和总余额中
     *
     * @param uid    用户uid
     * @param asset  资产名
     * @param amount 确认数量
     * @return
     */
    @Transactional
    @Override
    public Result confirmBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = getBalanceByUidAndAssetForUpdate(uid, asset);
        if (balance == null) {
            return new Result(ErrorEnum.BALANCE_NOT_EXIST);
        }
        if (balance.getUcfAmount().compareTo(amount) < 0) {
            return new Result(ErrorEnum.BALANCE_NOT_ENOUGH_UCF_BALANCE);
        }
        balance.setUcfAmount(balance.getUcfAmount().subtract(amount));
        balance.setBalance(balance.getBalance().add(amount));
        balance.setAvlAmount(balance.getAvlAmount().add(amount));
        int rowCount = balanceMapper.updateByPrimaryKeySelective(balance);
        return new Result(ErrorEnum.SUCCESS, rowCount);
    }

    /**
     * 增加未确认余额
     *
     * @param uid    用户uid
     * @param asset  资产名
     * @param amount 未确认数量
     * @return
     */
    @Transactional
    @Override
    public Result increaseUnconfirmedBalance(String uid, String asset, BigDecimal amount) {
        Balance balance = getBalanceByUidAndAssetForUpdate(uid, asset);
        if (balance == null) {
            balance = createBalance(uid, asset, BigDecimal.valueOf(0, 6));
        }
        balance.setUcfAmount(balance.getUcfAmount().add(amount));
        int rowCount = balanceMapper.updateByPrimaryKeySelective(balance);
        return new Result(ErrorEnum.SUCCESS, rowCount);
    }


    /**
     * 执行交易
     *
     * @param uid             用户uid
     * @param decreasedAsset  扣减的资产
     * @param decreasedAmount 扣减资产数量
     * @param increasedAsset  增加的未确认资产
     * @param increasedAmount 增加的未确认资产数量
     * @return
     */
    @Transactional
    @Override
    public Result exchange(String uid, String decreasedAsset, BigDecimal decreasedAmount, String increasedAsset, BigDecimal increasedAmount) {
        Result result = decreaseLockedBalance(uid, decreasedAsset, decreasedAmount);
        if (result.code != ErrorEnum.SUCCESS) {
            throw new BizException(result.code, result.payload, result.msg);
        }
        if (needConfirmation) {
            result = increaseUnconfirmedBalance(uid, increasedAsset, increasedAmount);
        } else {
            result = increaseAvlBalance(uid, increasedAsset, increasedAmount);
        }
        if (result.code != ErrorEnum.SUCCESS) {
            throw new BizException(result.code, result.payload, result.msg);
        }
        return result;
    }
}

