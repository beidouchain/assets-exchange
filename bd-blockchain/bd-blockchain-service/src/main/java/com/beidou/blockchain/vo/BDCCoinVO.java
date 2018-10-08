package com.beidou.blockchain.vo;

import lombok.Data;

/**
 * Created by fengguoqing on 2018/6/16.
 */
@Data
public class BDCCoinVO {
    private String rpcUserName;
    private String rpcUserPwd;
    private String rpcPort;
    private String rpcIp;
    private String rpcMethod;
    private String rpcParamters;
}
