package com.beidou.market.service;

import com.beidou.blockchain.vo.AddressBalancesVO;
import com.beidou.blockchain.vo.AssetsVO;
import com.beidou.blockchain.vo.AtomicExchangeVO;
import com.beidou.blockchain.vo.SendAssetfromVO;
import com.beidou.exchange.common.utils.PageBean;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.entity.ExchangeHistory;
import com.beidou.market.entity.MarketCategoryStateVO;
import com.beidou.market.entity.ResponseCoinPriceVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by fengguoqing on 2018/8/2.
 */
public interface MarketService {

    /**
     *
     * @param address
     * @param pageNo
     * @param type  0=原子交易 1=发送 2=充值
     * @return
     */
    public PageInfo<ExchangeHistory> getOrderList(String address,int pageNo,String type);

    /**
     * 获取市场动态列表 - 名字 价格 涨幅
     * @param coinName
     */
    public List<MarketCategoryStateVO> getMarketCategoryStateList (String coinName);


}
