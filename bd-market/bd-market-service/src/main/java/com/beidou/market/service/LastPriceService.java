package com.beidou.market.service;

import com.beidou.exchange.service.CommonGenericService;
import com.beidou.market.entity.LastPrice;

import java.math.BigDecimal;
import java.util.List;

public interface LastPriceService extends CommonGenericService<LastPrice> {
    /**
     * 更新最新成交价格
     * @param asset1 资产1
     * @param asset2 资产2
     * @param price 价格=资产2/资产1
     * @param amount1 资产1数量
     * @param amount2 资产2数量
     * @return
     */
    int saveLastPrice(String asset1, String asset2, BigDecimal price,BigDecimal amount1,BigDecimal amount2);

    /**
     * 获取指定资产跟其他资产的最新成交价
     * @param asset 指定的资产
     * @return 先取当天最新成交价，如果当天没有成交，则取过去最新成交价
     */
    List<LastPrice> getLastPriceList(String asset);

    /**
     * 获取指定一对资产的最新成交价
     * @param asset1 资产1
     * @param asset2 资产2
     * @return 先取当天最新成交价，如果当天没有成交，则取过去没有成交，则取过去最最新成交价
     */
    LastPrice getLastPrice(String asset1, String asset2);
}

