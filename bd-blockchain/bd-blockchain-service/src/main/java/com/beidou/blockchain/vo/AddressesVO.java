package com.beidou.blockchain.vo;

import lombok.Data;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@Data
public  class AddressesVO {
    private String address;
    private boolean ismine;
    private boolean iswatchonly;
    private boolean isscript;
    private String pubkey;
    private boolean iscompressed;
    private String account;
    //private boolean synchronizedAs;
}
