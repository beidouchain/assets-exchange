package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 资产原子交易对象
 */
@Data
public  class AtomicExchangeVO {
    private String fromAddress;
    private String fromAssetName;
    private BigDecimal fromCount;
    private String toAddress;
    private String toAssetName;
    private BigDecimal toCount;

}
