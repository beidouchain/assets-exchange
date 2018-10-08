package com.beidou.exchange.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVO {

    private String uid;
    private String asset;
    private String priceAsset;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal totalPrice;
    /**
     * 0：等待交易
     * 1：已完成交易
     * 2：已取消
     */
    private Integer status;
    private Long createdOn;
    private Long id;
    private String idStr;
    private BigDecimal turnOver;
    private BigDecimal originAmount;
    private String operation;

}
