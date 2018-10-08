package com.beidou.market.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "E_LAST_EXCHANGE")
@Data
public class LastExchange {

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


    @Column(name = "createon")
    private Long createon;

    @Column(name = "updateon")
    private Long updateon;

    //交易总量
    @Column(name = "from_total_num")
    private BigDecimal fromTotalNum;

    @Column(name = "to_total_num")
    private BigDecimal toTotalNum;

    //1=已删除
    private Integer status;
}