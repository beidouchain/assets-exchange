package com.beidou.market.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Table(name = "E_EXCHANGE_HISTORY")
@Data
public class ExchangeHistory {

    public static final int TYPE_ATOMIC_EXCHANGE = 0;
    public static final int TYPE_SEND = 1;
    public static final int TYPE_CNY = 2;
    @Id
    private Long id;

    //from-to 根据名称排序
    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "from_asset_name")
    private String fromAssetName;

    @Column(name = "from_address")
    private String fromAddress;

    @Column(name = "from_qty")
    private BigDecimal fromQty;

    @Column(name = "to_asset_name")
    private String toAssetName;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "to_qty")
    private BigDecimal toQty;

    @Column(name = "txid")
    private String txid;

    //资产类型 0=原子交易 1=发送 2=充值
    @Column(name = "type")
    private Integer type;

    @Column(name = "createon")
    private Long createon;

    @Column(name = "updateon")
    private Long updateon;

    //1=已删除
    private Integer status;
    @Transient
    private String fromNickName;
    @Transient
    private String toNickName;

    @Transient
    private String createTime;

}