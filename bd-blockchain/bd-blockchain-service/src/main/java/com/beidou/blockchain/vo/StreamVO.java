package com.beidou.blockchain.vo;

import lombok.Data;

/**
 * Created by fengguoqing on 2018/6/19.
 *
 */
@Data
public  class StreamVO {

    private Object[] publishers;
    private String key;
    private String data;
    private Long time;
    private String value;

}
