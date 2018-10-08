package com.beidou.blockchain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 所有创世资产列表
 */
@Data
public  class WalletTransactionIsSueVO {
    private String name;

    private String assetref;

    private Integer multiple;

    private BigDecimal qty;

    private BigDecimal raw;

    private String[] addresses;
}
