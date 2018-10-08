package com.beidou.exchange.balance.entity;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "BALANCE")
public class Balance {

    @Id
    private Long id;

    private String uid;

    private String asset;

    /**
     * 余额总量=可用余额+锁定余额
     */
    private BigDecimal balance;

    /**
     * 可用余额
     */
    @Column(name = "avl_amount")
    private BigDecimal avlAmount;

    /**
     * 锁定的余额
     */
    @Column(name = "loc_amount")
    private BigDecimal locAmount;

    /**
     * 未确认的余额总量
     */
    @Column(name = "ucf_amount")
    private BigDecimal ucfAmount;

    @Column(name = "created_on")
    private Long createdOn;

    @Column(name = "updated_on")
    private Long updatedOn;

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
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return asset
     */
    public String getAsset() {
        return asset;
    }

    /**
     * @param asset
     */
    public void setAsset(String asset) {
        this.asset = asset;
    }

    /**
     * 获取余额总量=可用余额+锁定余额
     *
     * @return balance - 余额总量=可用余额+锁定余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置余额总量=可用余额+锁定余额
     *
     * @param balance 余额总量=可用余额+锁定余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取可用余额
     *
     * @return avl_amount - 可用余额
     */
    public BigDecimal getAvlAmount() {
        return avlAmount;
    }

    /**
     * 设置可用余额
     *
     * @param avlAmount 可用余额
     */
    public void setAvlAmount(BigDecimal avlAmount) {
        this.avlAmount = avlAmount;
    }

    /**
     * 获取锁定的余额
     *
     * @return loc_amount - 锁定的余额
     */
    public BigDecimal getLocAmount() {
        return locAmount;
    }

    /**
     * 设置锁定的余额
     *
     * @param locAmount 锁定的余额
     */
    public void setLocAmount(BigDecimal locAmount) {
        this.locAmount = locAmount;
    }

    /**
     * 获取未确认的余额总量
     *
     * @return ucf_amount - 未确认的余额总量
     */
    public BigDecimal getUcfAmount() {
        return ucfAmount;
    }

    /**
     * 设置未确认的余额总量
     *
     * @param ucfAmount 未确认的余额总量
     */
    public void setUcfAmount(BigDecimal ucfAmount) {
        this.ucfAmount = ucfAmount;
    }

    /**
     * @return created_on
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
     * @return updated_on
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
}