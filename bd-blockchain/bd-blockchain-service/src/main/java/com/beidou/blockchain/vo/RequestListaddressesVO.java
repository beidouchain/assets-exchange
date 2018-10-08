package com.beidou.blockchain.vo;

import lombok.Data;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@Data
public class RequestListaddressesVO {
    private String addresses;
    private boolean verbose;
    private int count;
    private int start;
}
