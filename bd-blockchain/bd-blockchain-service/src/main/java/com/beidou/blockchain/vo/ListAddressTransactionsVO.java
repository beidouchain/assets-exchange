package com.beidou.blockchain.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 * 交易对象
 */
@Data
public class ListAddressTransactionsVO {
    private Balance balance;
    private String[] myaddresses;
    private String[] addresses;
    private Integer confirmations;
    private String txid;
    private boolean valid;
    private Long time;
    private Long timereceived;

    @Data
    public class Balance{
        private BigDecimal amount;
        private List<Assets> assetsList;
        private JSONArray assets;
    }

    @Data
    public class Assets{
        private String name;
        private String assetref;
        private BigDecimal qty;
    }
}
