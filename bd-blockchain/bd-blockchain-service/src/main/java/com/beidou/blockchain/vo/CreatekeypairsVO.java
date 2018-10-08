package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 创建地址
 */
@Data
public  class CreatekeypairsVO {
    //资产地址
    private String address;
    //资产数量
    private String pubkey;
    //资产号
    private String privkey;
    
}
