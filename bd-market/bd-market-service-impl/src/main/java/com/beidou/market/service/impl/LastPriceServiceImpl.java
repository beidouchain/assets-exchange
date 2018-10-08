package com.beidou.market.service.impl;

import com.beidou.exchange.common.DateUtility;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.service.IdWorkerService;
import com.beidou.exchange.service.impl.CommonGenericServiceImpl;
import com.beidou.market.entity.LastPrice;
import com.beidou.market.service.LastPriceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class LastPriceServiceImpl extends CommonGenericServiceImpl<LastPrice> implements LastPriceService {


    private static final int roundingMode = 5;

    @Autowired
    private IdWorkerService idWorkerService;
    /**
     * 更新最新成交价格
     *  计算 成交量 最低价 最高价
     * @param asset1s 资产1
     * @param asset2s 资产2
     * @param prices  价格=资产2/资产1
     * @param amount1s 资产1数量
     * @param amount2s 资产2数量
     * @return
     */
    @Override
    public int saveLastPrice(String asset1s, String asset2s, BigDecimal prices,BigDecimal amount1s,BigDecimal amount2s) {
        //String asset1s, String asset2s, BigDecimal prices,BigDecimal amount1s,BigDecimal amount2s
        boolean isRecip = false;//是否相反
        if (Order.compare(asset1s,asset2s) != -1) {
            isRecip = true;
        }
        String asset1 = asset1s;
        String asset2 = asset2s;
        BigDecimal price = prices;
        BigDecimal amount1 = amount1s;
        BigDecimal amount2 = amount2s;
        if (prices.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        if (isRecip) {
            asset1 = asset2s;
            asset2 = asset1s;
            amount1 = amount2s;
            amount2 = amount1s;
            price = new BigDecimal(1).divide(prices);
        }

        Long startTimeDay = DateUtility.getStartTimeOfDay(new Date());
        Example example = new Example(LastPrice.class);
        Example.Criteria cri = example.createCriteria().andEqualTo("asset1", asset1)
                .andEqualTo("asset2", asset2);
        cri.andEqualTo("dateTime", startTimeDay);
        example.and(cri);
        List<LastPrice> lastPriceList = mapper.selectByExample(example);
        LastPrice lastPrice = null;
        Long currentTime = System.currentTimeMillis();
        BigDecimal increase = null;
        if (CollectionUtils.isEmpty(lastPriceList)) {
            lastPrice = new LastPrice();
            lastPrice.setCreatedOn(currentTime);
            lastPrice.setDateTime(startTimeDay);
            lastPrice.setAsset1(asset1);
            lastPrice.setAsset2(asset2);
            increase = getIncrease(asset1,asset2,price);
        } else {
            lastPrice = lastPriceList.get(0);
            if (lastPrice.getLastPrice().compareTo(BigDecimal.ZERO) == 1) {
                increase = price.divide(lastPrice.getLastPrice(),roundingMode);
            }

        }
        lastPrice.setUpdatedOn(currentTime);
        lastPrice.setPrice(price);
        if (price.compareTo(BigDecimal.ZERO) == 1) {
            lastPrice.setRecipPrice(new BigDecimal(1).divide(price, roundingMode));
        }

        //增长 反增长
        lastPrice.setIncrease(increase);
        if (increase.compareTo(BigDecimal.ZERO) == 1) {
            lastPrice.setRecipIncrease(new BigDecimal(1).divide(increase, roundingMode));
        } else {
            lastPrice.setRecipIncrease(BigDecimal.ZERO);
        }
        if (lastPrice.getDayAmount1().compareTo(BigDecimal.ZERO) == 1) {
            lastPrice.setDayAmount1(amount1.add(lastPrice.getDayAmount1()));
        } else {
            lastPrice.setDayAmount1(amount1);
        }
        if (lastPrice.getDayAmount2().compareTo(BigDecimal.ZERO) == 1) {
            lastPrice.setDayAmount2(amount2.add(lastPrice.getDayAmount2()));
        } else {
            lastPrice.setDayAmount2(amount2);
        }

        if (lastPrice.getId() == null) {
            lastPrice.setId(idWorkerService.nextId());
            mapper.insert(lastPrice);
        } else {
            mapper.updateByPrimaryKey(lastPrice);
        }
        return 0;
    }

    /**
     * 获取增长值
     *   获取当天之前的最新数据 如果不存在 返回0
     * @param asset1
     * @param asset2
     * @param price
     * @return
     */
    public BigDecimal getIncrease (String asset1, String asset2, BigDecimal price) {
        Long startTimeDay = DateUtility.getStartTimeOfDay(new Date());
        Example example = new Example(LastPrice.class);
        Example.Criteria cri = example.createCriteria().andEqualTo("asset1", asset1)
                .andEqualTo("asset2", asset2);
        cri.andLessThan("dateTime",startTimeDay);
        example.and(cri);
        example.setOrderByClause(" DATE_TIME desc ");
        List<LastPrice> lastPriceList = mapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(lastPriceList)) {
            LastPrice lastPrice = lastPriceList.get(0);
            if (asset1.equals(lastPrice.getAsset1())) {
                return price.divide(lastPrice.getPrice(),roundingMode);
            } else {
                return price.divide(lastPrice.getRecipPrice(),roundingMode);
            }

        }
        return new BigDecimal(0);
    }

    /**
     * 获取指定资产跟其他资产的最新成交价
     *
     * @param asset 指定的资产
     * @return 先取当天最新成交价，如果当天没有成交，则取过去最新成交价
     */
    @Override
    public List<LastPrice> getLastPriceList(String asset) {
        return null;
    }

    /**
     * 获取指定一对资产的最新成交价
     *
     * @param asset1 资产1
     * @param asset2 资产2
     * @return 先取当天最新成交价，如果当天没有成交，则取过去最新成交价
     */
    @Override
    public LastPrice getLastPrice(String asset1, String asset2) {
        return null;
    }
}
