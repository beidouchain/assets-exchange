package com.beidou.exchange.transaction.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "EXCHANGE")
public class Exchange {

    public static final short EXCHANGE_STATUS_UNCONFIRMED = 0;
    public static final short EXCHANGE_STATUS_CONFIRMED = 1;
    public static final short EXCHANGE_STATUS_ROLLBACK = 2;

    @Id
    private Long id;

    /**
     * 参与交易的资产名1
     */
    private String asset1;

    /**
     * 参与交易的资产名2
     */
    private String asset2;

    /**
     * 参与交易的源订单1
     */
    private Long order1;

    /**
     * 参与交易的源订单2
     */
    private Long order2;

    /**
     * 成交量
     */
    private BigDecimal amount1;

    private BigDecimal amount2;

    /**
     * 成交价price=amount2/amount1
     */
    private BigDecimal price;

    /**
     * 成交价的倒数inversePrice = amount1/amount2
     */
    private BigDecimal inversePrice;

    private BigDecimal fee1;

    private BigDecimal fee2;

    @Column(name = "created_on")
    private Long createdOn;

    @Column(name = "updated_on")
    private Long updatedOn;

    /**
     * 0：未确认
     * 1：已确认
     * 2：已回滚
     */
    private Short status;

    /**
     * 0：来单为买（涨）
     * 1：来单为卖（跌）
     */
    private Short direction;
}

