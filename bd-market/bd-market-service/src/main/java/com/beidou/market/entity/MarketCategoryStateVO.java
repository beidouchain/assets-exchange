package com.beidou.market.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/9/3.
 */
@Data
public class MarketCategoryStateVO {
    private String nick;//币标识
    private String name;//显示名称
    private BigDecimal price;//价格
    private String priceRise;//涨幅
    private BigDecimal count;//成交量
    private BigDecimal minPrice;//最低价
    private BigDecimal maxPrice;//最高价
}
