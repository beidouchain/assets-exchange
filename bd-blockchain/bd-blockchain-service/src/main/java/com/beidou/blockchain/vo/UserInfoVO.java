package com.beidou.blockchain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by fengguoqing on 2018/6/19.
 * 用户信息
 */
@Data
public class UserInfoVO {

    private String name;
    private String phone;
    private String email;
    private String cardId;
    private String cardType;
    private String pwd;
    private String address;
    private String token;
    private String cardPhotoPath;
    //自动生成的私钥
    private String privateKey;
    //公钥
    private String publicKey;

    //sign 签名生成
    private String uid;
    private String r1;
    private String r2;
    private String r3;
    private String hash;

    //自增长id
    private String id;

    private String otherMyaddress;

    //邀请code
    private String invationCode;


}
