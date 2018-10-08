package com.beidou.market.entity;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "LAST_PRICE")
public class LastPrice {
    @Id
    private Long id;

    @Column(name = "ASSET1")
    private String asset1;

    @Column(name = "ASSET2")
    private String asset2;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "LAST_PRICE")
    private BigDecimal lastPrice;

    @Column(name = "INCREASE")
    private BigDecimal increase;

    @Column(name = "RECIP_INCREASE")
    private BigDecimal recipIncrease;

    @Column(name = "RECIP_PRICE")
    private BigDecimal recipPrice;


    @Column(name = "DATE_TIME")
    private Long dateTime;

    @Column(name = "CREATED_ON")
    private Long createdOn;

    @Column(name = "UPDATED_ON")
    private Long updatedOn;

    @Column(name = "DAY_AMOUNT1")
    private BigDecimal dayAmount1;

    @Column(name = "DAY_AMOUNT2")
    private BigDecimal dayAmount2;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return ASSET1
     */
    public String getAsset1() {
        return asset1;
    }

    /**
     * @param asset1
     */
    public void setAsset1(String asset1) {
        this.asset1 = asset1;
    }

    /**
     * @return ASSET2
     */
    public String getAsset2() {
        return asset2;
    }

    /**
     * @param asset2
     */
    public void setAsset2(String asset2) {
        this.asset2 = asset2;
    }

    /**
     * @return PRICE
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    /**
     * @return DATE_TIME
     */
    public Long getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime
     */
    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return CREATED_ON
     */
    public Long getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn
     */
    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return UPDATED_ON
     */
    public Long getUpdatedOn() {
        return updatedOn;
    }

    /**
     * @param updatedOn
     */
    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }

    public BigDecimal getIncrease() {
        return increase;
    }

    public void setIncrease(BigDecimal increase) {
        this.increase = increase;
    }


    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getDayAmount1() {
        return dayAmount1;
    }

    public void setDayAmount1(BigDecimal dayAmount1) {
        this.dayAmount1 = dayAmount1;
    }

    public BigDecimal getDayAmount2() {
        return dayAmount2;
    }

    public void setDayAmount2(BigDecimal dayAmount2) {
        this.dayAmount2 = dayAmount2;
    }

    public BigDecimal getRecipIncrease() {
        return recipIncrease;
    }

    public void setRecipIncrease(BigDecimal recipIncrease) {
        this.recipIncrease = recipIncrease;
    }

    public BigDecimal getRecipPrice() {
        return recipPrice;
    }

    public void setRecipPrice(BigDecimal recipPrice) {
        this.recipPrice = recipPrice;
    }
}