package com.beidou.exchange.match.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchVO {

    private String asset;
    private String assetName;
    private String priceAsset;
    private String priceAssetName;
    private BigDecimal price;
    private BigDecimal amount;

}
