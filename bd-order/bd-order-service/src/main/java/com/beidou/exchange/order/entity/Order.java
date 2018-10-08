package com.beidou.exchange.order.entity;

import com.beidou.exchange.common.Constants;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@Table(name = "BD_ORDER")
public class Order {
    public static final short BUY = 0;
    public static final short SELL = 1;

    public static final short ORDER_STATUS_WAITING = 0;
    public static final short ORDER_STATUS_COMPLETE = 1;
    public static final short ORDER_STATUS_CANCELED = 2;

    @Id
    private Long id;

    private String uid;

    /**
     * 0:限价单
     * 1:市价单
     */
    private Short type;

    /**
     * 0:buy
     * 1:sell
     */
    private Short direction;

    private String asset;

    @Transient
    private String assetNick;


    private BigDecimal amount;

    @Column(name = "price_asset")
    private String priceAsset;


    @Transient
    private String priceAssetNick;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private BigDecimal price;

    @Column(name = "origin_amount")
    private BigDecimal originAmount;

    @Column(name = "origin_price")
    private BigDecimal originPrice;

    @Column(name = "created_on")
    private Long createdOn;

    @Column(name = "updated_on")
    private Long updatedOn;

    /**
     * 0：等待交易
     * 1：已完成交易
     * 2：已取消
     */
    private Short status;

    @Transient
    private Boolean isTurnOver;

    @Transient
    private BigDecimal matchPrice;

    public Boolean isTurnOver() {
        if (isTurnOver == null) {
            isTurnOver = compare(asset, priceAsset) > 0;
        }
        return isTurnOver;
    }

    public String getMatchAsset() {
        if (isTurnOver()) {
            return priceAsset;
        } else {
            return asset;
        }
    }

    public String getMatchPriceAsset() {
        if (isTurnOver()) {
            return asset;
        } else {
            return priceAsset;
        }
    }

    public short getMatchDirection() {
        if (isTurnOver()) {
            return direction == BUY ? SELL : BUY;
        } else {
            return direction == BUY ? BUY : SELL;
        }
    }

    public BigDecimal getMatchPrice() {
        if (matchPrice != null) {
            return matchPrice;
        } else {
            if (isTurnOver()) {
                matchPrice = originAmount.divide(originPrice, Constants.DEFAULT_DECIMAL_SCALE, BigDecimal.ROUND_UP);
                return matchPrice;
            } else {
                matchPrice = getPrice();
                return matchPrice;
            }
        }
    }

    public BigDecimal getPrice() {
        if (price == null) {
            if (originPrice == null) {
                return null;
            }
            price = originPrice.divide(originAmount, Constants.DEFAULT_DECIMAL_SCALE, BigDecimal.ROUND_UP);
        }
        return price;
    }

    public BigDecimal getMatchAmount() {
        if (isTurnOver()) {
            return totalPrice;
        } else {
            return amount;
        }
    }

    public BigDecimal getMatchTotalPrice() {
        if (isTurnOver()) {
            return amount;
        } else {
            return totalPrice;
        }
    }

    public static int compare(String s1, String s2) {
        for (int i = 0; i < Math.min(s1.length(), s2.length()); ++i) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 > c2) {
                return 1;
            } else if (c1 < c2) {
                return -1;
            }
        }
        if (s1.length() == s2.length()) {
            return 0;
        } else if (s1.length() > s2.length()) {
            return 1;
        } else {
            return -1;
        }
    }

    public static String getExchangeKey(String asset1, String asset2) {
        if (compare(asset1, asset2) >= 0) {
            return asset2 + ":" + asset1;
        } else {
            return asset1 + ":" + asset2;
        }
    }

    public String getExchangeKey() {
        return getExchangeKey(asset, priceAsset);
    }

    public BigDecimal subtractMatchAmount(BigDecimal matchAmount) {
        if (isTurnOver()) {
            return subtractTotalPrice(matchAmount);
        } else {
            return subtractAmount(matchAmount);
        }
    }

    public BigDecimal subtractMatchTotalPrice(BigDecimal matchAmount) {
        if (isTurnOver()) {
            return subtractAmount(matchAmount);
        } else {
            return subtractTotalPrice(matchAmount);
        }
    }

    public BigDecimal subtractAmount(BigDecimal amount) {
        BigDecimal subtractPrice = amount.multiply(getPrice()).setScale(Constants.DEFAULT_DECIMAL_SCALE, BigDecimal.ROUND_UP);
        this.amount = this.amount.subtract(amount);
        if (this.amount.compareTo(BigDecimal.ZERO) == 0) {
            subtractPrice = totalPrice;
            totalPrice = BigDecimal.ZERO;
        } else {
            totalPrice = totalPrice.subtract(subtractPrice);
        }
        return subtractPrice;
    }

    public BigDecimal subtractTotalPrice(BigDecimal totalPrice) {
        BigDecimal subtractAmount = totalPrice.divide(getPrice(), Constants.DEFAULT_DECIMAL_SCALE, BigDecimal.ROUND_UP);
        this.totalPrice = this.totalPrice.subtract(totalPrice);
        if (this.totalPrice.compareTo(BigDecimal.ZERO) == 0) {
            subtractAmount = amount;
            amount = BigDecimal.ZERO;
        } else {
            amount = amount.subtract(subtractAmount);
        }
        return subtractAmount;
    }

    public static void main(String args[]) {
        System.out.println(compare("BEIDOU-USDT", "BEIDOU-XIN"));
        BigDecimal n1 = new BigDecimal("1.000000");
        BigDecimal n2 = new BigDecimal("0.470000");
        System.out.println(n1.divide(n2, Constants.DEFAULT_DECIMAL_SCALE + 1, BigDecimal.ROUND_UP));
    }

}

