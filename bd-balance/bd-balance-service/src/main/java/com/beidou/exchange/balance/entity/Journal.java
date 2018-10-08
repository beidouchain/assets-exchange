package com.beidou.exchange.balance.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "JOURNAL")
public class Journal {

    public static final short JOURNAL_TYPE_EXCHANGE = 0;
    public static final short JOURNAL_TYPE_SEND = 1;
    public static final short JOURNAL_TYPE_RECEIVE = 2;
    public static final short JOURNAL_TYPE_RECHARGE = 3;
    public static final short JOURNAL_TYPE_WITHDRAW = 4;

    public static final short JOURNAL_STATUS_UNCONFIRMED = 0;
    public static final short JOURNAL_STATUS_CONFIRMED = 1;
    public static final short JOURNAL_STATUS_ROLLBACK = 2;

    @Id
    private Long id;

    private String uid;

    /**
     * 交易的对手方
     */
    @Column(name = "counterpart_uid")
    private String counterpartUid;

    /**
     * 标的资产名
     */
    private String asset;

    /**
     * 本记录标的资产总量
     */
    private BigDecimal amount;

    /**
     * 交易等价物（当type=0，即记录类型为"交易"时有效）
     */
    @Column(name = "price_asset")
    private String priceAsset;

    /**
     * 总价（当type=0，即记录类型为"交易"时有效）
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 成交单id（当type=0，即记录类型为"交易"时有效）
     */
    @Column(name = "transaction_id")
    private Long transactionId;

    /**
     * 交易费
     */
    private BigDecimal fee;

    /**
     * 0:buy
     * 1:sell
     */
    private Short direction;

    /**
     * 记录类型
     * 0:交易
     * 1:发送
     * 2:接收
     * 3:充值
     * 4:提现
     */
    private Short type;

    @Column(name = "created_on")
    private Long createdOn;

    @Column(name = "updated_on")
    private Long updatedOn;

    /**
     * 交易状态
     * 0:未确认
     * 1:已确认
     * 2:已回滚
     */
    private Short status;

}