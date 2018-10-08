package com.beidou.exchange.common.sign;

import lombok.Data;


@Data
public class BDUserPwd {

    private String uid;


    private String r1;


    private String r2;


    private String r3;


    private String hash;


    private Long createdOn;


    private Long lastUpdatedOn;


    private String createdBy;


    private String lastUpdatedBy;

    private int status;
}
