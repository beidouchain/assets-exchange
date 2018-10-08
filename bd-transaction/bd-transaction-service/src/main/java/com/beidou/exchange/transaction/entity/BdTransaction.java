package com.beidou.exchange.transaction.entity;

import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "BD_TRANSACTION")
public class BdTransaction {
    /**
     * 交易
     */
    public static final int TRANSACTION_TYPE_EXCHANGE = 0;
    /**
     * 转账
     */
    public static final int TRANSACTION_TYPE_TRANSFER = 1;
    /**
     * 充值
     */
    public static final int TRANSACTION_TYPE_RECHARGE = 2;

    /**
     * 提现
     */
    public static final int TRANSACTION_TYPE_WITHDRAW = 3;

    /**
     * 未确认
     */
    public static final short TRANSACTION_STATUS_UNCONFIRMED = 0;
    /**
     * 已确认
     */
    public static final short TRANSACTION_STATUS_CONFIRMED = 1;
    /**
     * 已回滚
     */
    public static final short TRANSACTION_STATUS_ROLLBACK = 2;

    @Id
    private Long id;

    /**
     * 0：交易
1：转账
2：充值
3：提现
     */
    private Integer type;

    /**
     * type=交易时，交易id
     */
    @Column(name = "exchange_id")
    private Long exchangeId;

    /**
     * type=交易、转账、充值、提现时有效
     */
    private String uid1;

    /**
     * type=交易和转账时，表示uid1花费的资产
type=充值时，表示uid1充值的资产
type=提现时，表示uid1提现的资产
     */
    private String asset1;

    /**
     * asset1的数量
     */
    private BigDecimal amount1;

    /**
     * type=交易时，表示交易的对手方
type=转账时，表示资产接收方
type=充值和提现时无效
     */
    private String uid2;

    /**
     * type=交易时，表示对手方的资产
type=转账、充值、提现时无效
     */
    private String asset2;

    /**
     * asset2的数量
     */
    private BigDecimal amount2;

    /**
     * 链上事务id
     */
    @Column(name = "chain_txid")
    private String chainTxid;

    /**
     * 确认次数
     */
    @Column(name = "confirm_count")
    private Integer confirmCount;

    @Column(name = "created_on")
    private Long createdOn;

    @Column(name = "updated_on")
    private Long updatedOn;

    /**
     * 交易确认状态
0:未进行链上交易
1:已进行链上交易，但未完成确认
2:已完成链上交易确认
3:已回滚
     */
    private Short status;

}

