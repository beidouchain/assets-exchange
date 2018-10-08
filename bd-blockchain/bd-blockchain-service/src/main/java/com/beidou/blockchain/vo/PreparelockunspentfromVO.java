package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 原子交易之前锁定返回
 */
@Data
public  class PreparelockunspentfromVO {
    private String txid;
    private Long vout;

}
