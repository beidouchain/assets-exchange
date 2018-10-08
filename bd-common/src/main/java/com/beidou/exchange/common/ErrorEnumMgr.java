package com.beidou.exchange.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ErrorEnumMgr {

    private static Map<String, ErrorEnumType> ENUM_MAP;

    static {
        ENUM_MAP = new ConcurrentHashMap<>();
    }

    public static void registerErrorEnum(String errorEnumKey, ErrorEnumType errorEnumType) {
        ENUM_MAP.put(errorEnumKey, errorEnumType);
    }

    public static Boolean containsErrorEnum(String errorEnumKey) {
        return ENUM_MAP.containsKey(errorEnumKey);
    }

    public static ErrorEnumType getErrorByCodeKey(String errorEnumKey) {
        return ENUM_MAP.get(errorEnumKey);
    }
}
