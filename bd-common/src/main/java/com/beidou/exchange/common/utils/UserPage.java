package com.beidou.exchange.common.utils;

import lombok.Data;

/**
 * Created by fengguoqing on 2018/8/22.
 */
@Data
public class UserPage {
    private String address;
    private Integer pageNum;
    private Integer startPage;
    private String type;//0=原子交易 1=发送 2=充值
}
