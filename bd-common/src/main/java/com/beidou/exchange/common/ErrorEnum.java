package com.beidou.exchange.common;

public enum ErrorEnum implements ErrorEnumType {

    SUCCESS("0", "请求成功"),
    FAILURE("1", "请求失败"),

    MISSING_PARAM("10001", "缺少参数"),
    SERVER_ERROR("10002", "服务器异常"),
    RESUBMIT_ERROR("10003", "重复提交"),
    INVALID_PARAM("10006", "参数不合法"),
    DATABASE_ERROR("10007", "数据库异常"),

    REGIST_EXIST_ERROR("10100", "用户账号已存在"),
    REGIST_EMAIL_EXIST_ERROR("10101", "用户email已存在"),

    LOGIN_NULL_ERROR("10103", "登录用户名和密码不能为空"),
    LOGIN_NAME_NOTEXIST_ERROR("10104", "用户名或邮箱不存在"),
    LOGIN_NAME_NOTEXIST1_ERROR("10105", "用户名或邮箱不存在"),
    LOGIN_PWD_ERROR("10106", "密码错误"),
    USER_UPLOAD_FILE_CARD_ERROR("10107", "上传文件错误"),

    REGIST_ERROR("10102", "注册用户名 密码 邮箱不能为空"),

    //资产
    ASSETS_INTERFACE_ERROR("20000", "资产rpc错误"),
    ISSUE_ASSET_EXIST_ERROR("20001", "资产名称已经存在"),
    ISSUE_ASSET_PARAMTER_ERROR("20002", "参数错误"),
    ASSET_SEND_NAME_NOTEXIST_ERROR("20003", "资产名称不存在"),
    CHECKSENDASSETFROM_ERROR("20004", "发送资产参数错误"),
    CHECKSENDASSETFROM_FROM_NOEXIST_ERROR("20005", "发送资产发送者地址不存在"),
    CHECKSENDASSETFROM_TO_NOEXIST_ERROR("20006", "发送资产发送目标地址不存在"),

    ATOMIC_EXCHANGE_PARAM_ERROR("20007", "交易参数错误"),
    ATOMIC_EXCHANGE_FROM_ADDRESS_ERROR("20008", "交易资产发送者地址不存在"),
    ATOMIC_EXCHANGE_TO_ADDRESS_ERROR("20009", "交易资产发送目标地址不存在"),
    ATOMIC_EXCHANGE_FROM_NAME_ERROR("20010", "交易资产发送资产不存在"),
    ATOMIC_EXCHANGE_TO_NAME_ERROR("20011", "交易资产发送目标资产不存在"),
    ATOMIC_EXCHANGE_FROM_COIN_ERROR("20012", "交易资产发送者余额不足"),
    ATOMIC_EXCHANGE_TO_COIN_ERROR("20013", "交易资产发送目标余额不足"),
    ATOMIC_RPC_ERROR("20014", "原子交易rpc错误"),

    AES_ERROR("20015", "加密错误"),

    BALANCE_NOT_EXIST("20100", "账户不存在"),
    BALANCE_NOT_ENOUGH_AVL_BALANCE("20101", "没有足够的可用余额"),
    BALANCE_NOT_ENOUGH_LOC_BALANCE("20102", "没有足够的锁定余额"),
    BALANCE_NOT_ENOUGH_UCF_BALANCE("20103", "没有足够的未确认余额"),

    TRANSACTION_CHAIN_ATOMIC_EXCHANGE_FAIL("20200", "链上交易执行失败"),

    ORDER_CACELED("20300", "订单已取消"),

    MATCH_SAME_DIRECTION("30100", "同买同卖无法撮合"),

    UNKNOW_ERROR("80000", "rpc服务调用错误");
    private String errorCode;
    private String desc;

    ErrorEnum() {
        this.errorCode = "0";
        this.desc = "";
    }

    ErrorEnum(String errorCode, String desc) {
        this.errorCode = errorCode;
        this.desc = desc;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Class<ErrorEnum> getEnumClazz() {
        return ErrorEnum.class;
    }

    static {
        StringBuilder sbKeyGen = new StringBuilder();
        for (ErrorEnumType instance : ErrorEnum.values()) {
            sbKeyGen.setLength(0);
            sbKeyGen.append(ErrorEnum.class.getName()).append(":").append(instance.getErrorCode());
            if (ErrorEnumMgr.containsErrorEnum(sbKeyGen.toString())) {
                throw new RuntimeException("duplicated field type");
            }
            ErrorEnumMgr.registerErrorEnum(sbKeyGen.toString(), instance);
        }
    }

    public static ErrorEnumType getErrorByCode(String errorCode) {
        return ErrorEnumMgr.getErrorByCodeKey(ErrorEnum.class.getName() + ":" + errorCode);
    }

}

