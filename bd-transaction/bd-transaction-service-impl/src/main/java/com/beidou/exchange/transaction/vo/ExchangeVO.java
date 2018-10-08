package com.beidou.exchange.transaction.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeVO {

    private String uid;
    private String asset;
    private String priceAsset;

    private BigDecimal price;
    private BigDecimal amount;
    private String time;
}
