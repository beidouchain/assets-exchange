package com.beidou.blockchain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 所有创世资产列表
 */
@Data
public  class WalletTransactionVO {
    private JSONObject balance;

    private String[] myaddresses;

    private  WalletTransactionIsSueVO issue;

    private Integer confirmations;

    private String txid;

    private Long time;

    private Long timereceived;
}
