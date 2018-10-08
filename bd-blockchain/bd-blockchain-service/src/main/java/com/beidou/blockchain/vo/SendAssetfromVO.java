package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 发送资产
 */
@Data
public  class SendAssetfromVO {
    private String from;
    private String to;
    //资产
    private BigDecimal qyt;
    //资产
    private String name;
    //类型 1=发送 2=充值
    private Integer type;

}
