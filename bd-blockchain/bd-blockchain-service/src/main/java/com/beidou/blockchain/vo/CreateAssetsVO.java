package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 所有创世资产列表
 */
@Data
public  class CreateAssetsVO {
    private String name;

    private String issuetxid;

    private BigDecimal multiple;

    private BigDecimal units;

    private BigDecimal issueqty;

    private BigDecimal issueraw;

    private String assetref;
    //总量
    private BigDecimal qty;
    private String address;
    //昵称
    private String nick;
    //图标
    private String ico;
    //现价
    private BigDecimal price;
    //人民币价格
    private BigDecimal cnyPrice;
    //24小时交易量-单个
    private BigDecimal exchangeAmount;
    //24小时交易量-总共
    private BigDecimal totalExchangeAmount;
    //昨天最后交易价格
    private BigDecimal yesterdayEndPrice;
    //日涨幅百分比
    private String dailyIncreasesPer;
}
