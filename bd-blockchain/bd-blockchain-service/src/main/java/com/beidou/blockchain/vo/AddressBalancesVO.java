package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 钱包资产
 */
@Data
public  class AddressBalancesVO {
    //资产地址
    private String name;
    //资产数量
    private BigDecimal qty;
    //资产号
    private String assetref;
    //地址
    private String address;
    
}
