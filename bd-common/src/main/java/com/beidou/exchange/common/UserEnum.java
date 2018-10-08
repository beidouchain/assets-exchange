package com.beidou.exchange.common;

public enum UserEnum  {

    name("name", "用户名"),
    //暂时不用
    phone("phone", "手机号"),

    email("email", "email"),
    cardId("cardId", "卡片ID"),
    cardType("cardType", "卡片类型"),
    pwd("pwd", "密码"),

    privateKey("privateKey", "私钥"),

    publicKey("publicKey", "公钥"),

    auth_1("AUTH_1", "一级认证"),
    address("address", "address"),
    //sign 签名
    uid("uid", "uid"),
    r1("r1", "r1"),
    r2("r2", "r2"),
    r3("r3", "r3"),
    hash("hash", "hash"),

    invationCode("invationCode", "邀请码"),
    id("id", "邀请码"),

    auth_2("AUTH_2", "二级认证");



    private String code;
    private String desc;

    UserEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}

