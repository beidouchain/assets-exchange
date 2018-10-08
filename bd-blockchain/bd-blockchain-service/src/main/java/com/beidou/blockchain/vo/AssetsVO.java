package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 资产
 */
@Data
public  class AssetsVO {
    //资产地址
    private String address;
    //资产最大数量
    private BigDecimal maxCount;
    //资产名称
    private String name;
    //昵称
    private String nick;
    //单位
    private BigDecimal units;

    //基础
    private String asset;
    //要交易的
    private String priceAsset;
    //1=默认基础币 2=人民币 3=默认交易币
    private Integer type;

}
