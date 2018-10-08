package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 验证地址是否正确
 */
@Data
public  class ValidateAddressVO {
    private boolean isvalid;
    private String address;
    private boolean ismine;
    private boolean iswatchonly;
    private boolean isscript;
    private String pubkey;
    private boolean iscompressed;
    private String account;
}
