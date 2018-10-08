package com.beidou.market.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Table(name = "E_ASSET_CATEGORY")
@Data
public class AssetCategory {
    @Id
    private Long id;

    private String name;

    private String address;

    private String nick;

    private BigDecimal units;

    private BigDecimal qty;

    private Long createon;

    private Long updateon;
    //1=默认基础币 2=人民币 3=默认交易币
    private Integer type;
    //1=已删除
    private Integer status;

    private String icon;

    @Transient
    private BigDecimal price;
}