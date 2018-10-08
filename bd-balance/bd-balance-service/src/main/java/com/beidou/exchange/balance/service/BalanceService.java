package com.beidou.exchange.balance.service;

import com.beidou.exchange.balance.entity.Balance;
import com.beidou.exchange.common.Result;
import com.beidou.exchange.service.CommonGenericService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BalanceService extends CommonGenericService<Balance> {

    /**
     * 创建、初始化账户指定资产余额，amount为总余额和可用余额
     * @param uid 用户id
     * @param asset 资产名
     * @param amount 初始资产数量
     * @return
     */
    Balance createBalance(String uid, String asset, BigDecimal amount);

    /**
     * 更新账户余额
     * @param balance 余额信息
     * @return
     */
    int updateBalance(Balance balance);

    /**
     * 获取指定用户指定资产余额
     * @param uid 用户id
     * @param asset 资产名
     * @return
     */
    Balance getBalanceByUidAndAsset(String uid, String asset);

    /**
     * 批量获取用户指定资产余额，返回每个资产及其对应的余额的映射
     * @param uid 用户id
     * @param assetList 资产列表
     * @return
     */
    Map<String, Balance> getBalanceByUidAndAsset(String uid, List<String> assetList);

    /**
     * 获取指定用户指定资产余额，并锁定记录
     * @param uid 用户uid
     * @param asset 资产名
     * @return
     */
    Balance getBalanceByUidAndAssetForUpdate(String uid, String asset);

    /**
     * 增加指定用户的可用资产的余额，同时加到总余额中
     * @param uid 用户uid
     * @param asset 资产名
     * @param amount 增加数量
     * @return
     */
    Result increaseAvlBalance(String uid, String asset, BigDecimal amount);

    /**
     * 锁定余额，从可用余额中扣减指定数量amount，加到锁定余额中
     * @param uid 用户uid
     * @param asset 资产名
     * @param amount 锁定数量
     * @return
     */
    Result lockBalance(String uid, String asset, BigDecimal amount);

    /**
     * 解锁余额，从锁定余额中扣减指定数量amount，加到总余额中
     * @param uid 用户uid
     * @param asset 资产名
     * @param amount 解锁数量
     * @return
     */
    Result unlockBalance(String uid, String asset, BigDecimal amount);


    /**
     * 扣减锁定余额，同时扣减总余额
     * @param uid 用户uid
     * @param asset 资产名
     * @param amount 扣减数量
     * @return
     */
    Result decreaseLockedBalance(String uid, String asset, BigDecimal amount);

    /**
     * 确认余额，从未确认余额中扣减amount加到可用余额和总余额中
     * @param uid 用户uid
     * @param asset 资产名
     * @param amount 确认数量
     * @return
     */
    Result confirmBalance(String uid, String asset, BigDecimal amount);

    /**
     * 增加未确认余额
     * @param uid 用户uid
     * @param asset 资产名
     * @param amount 未确认数量
     * @return
     */
    Result increaseUnconfirmedBalance(String uid, String asset, BigDecimal amount);

    /**
     * 执行交易
     * @param uid 用户uid
     * @param decreasedAsset 扣减的资产
     * @param amount 扣减资产数量
     * @param unconfirmedAsset 增加的未确认资产
     * @param unconfirmedAmount 增加的未确认资产数量
     * @return
     */
    Result exchange(String uid, String decreasedAsset, BigDecimal amount, String unconfirmedAsset, BigDecimal unconfirmedAmount);
}
