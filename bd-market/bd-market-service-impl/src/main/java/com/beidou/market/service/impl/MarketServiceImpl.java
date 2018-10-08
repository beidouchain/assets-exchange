package com.beidou.market.service.impl;

import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.service.AtomicService;
import com.beidou.exchange.common.BDConstants;
import com.beidou.exchange.common.DateUtility;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.entity.ExchangeHistory;
import com.beidou.market.entity.LastPrice;
import com.beidou.market.entity.MarketCategoryStateVO;
import com.beidou.market.mapper.AssetCategoryMapper;
import com.beidou.market.mapper.ExchangeHistoryMapper;
import com.beidou.market.mapper.LastExchangeMapper;
import com.beidou.market.mapper.LastPriceMapper;
import com.beidou.market.service.DbAssetsService;
import com.beidou.market.service.MarketService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengguoqing on 2018/8/2.
 */
@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private LastPriceMapper lastPriceMapper;


    @Autowired
    private AssetsService assetsService;

    @Autowired
    private ExchangeHistoryMapper exchangeHistoryMapper;

    @Autowired
    private LastExchangeMapper lastExchangeMapper;

    @Autowired
    private AtomicService atomicService;

    @Autowired
    private DbAssetsService dbAssetsService;

    public PageInfo<ExchangeHistory> getOrderList(String address, int pageNo, String type) {

        Map<String, AssetCategory> assetCategoryMap = dbAssetsService.getAllAssetMap();

        Example example = new Example(ExchangeHistory.class);
        example.createCriteria().andEqualTo("status", 0);
        if (StringUtils.isNotEmpty(type)) {
            Example.Criteria typeCri = null;
            if ("2".equals(type)) {
                typeCri = example.createCriteria().andEqualTo("type", type);
            } else {
                typeCri = example.createCriteria().andNotEqualTo("type", type);
            }
            example.and(typeCri);
        }

        Example.Criteria orCri = example.createCriteria().andEqualTo("fromAddress", address).orEqualTo("toAddress", address);
        example.and(orCri);
        example.setOrderByClause("createon desc");
        PageHelper.startPage(pageNo, BDConstants.PAGE_SIZE);
        List<ExchangeHistory> exchangeHistoryList = exchangeHistoryMapper.selectByExample(example);


        PageInfo<ExchangeHistory> page = new PageInfo<ExchangeHistory>(exchangeHistoryList);


        if (CollectionUtils.isEmpty(exchangeHistoryList)) {
            return null;
        }

        for (ExchangeHistory exchangeHistory : exchangeHistoryList) {
            String fromAssetName = exchangeHistory.getFromAssetName();
            String toAssetName = exchangeHistory.getToAssetName();
            AssetCategory fromAssetCategory = assetCategoryMap.get(fromAssetName);
            AssetCategory toAssetCategory = assetCategoryMap.get(toAssetName);
            exchangeHistory.setFromNickName(fromAssetCategory.getNick());
            exchangeHistory.setToNickName(toAssetCategory.getNick());
            exchangeHistory.setCreateTime(DateUtility.dateToStr(exchangeHistory.getCreateon()));
        }
        page.setList(exchangeHistoryList);
        return page;
    }

    /**
     * 根据资产名称获取所有的最后交易记录
     * @param coinName
     * @return
     */
    public Map<String,LastPrice> getCategoryLastPrice (String coinName) {
        Map<String,LastPrice> categoryLastPriceMap = new HashMap<>();
        Example example = new Example(LastPrice.class);
        Example.Criteria orCri = example.createCriteria().andEqualTo("asset1", coinName).orEqualTo("asset2", coinName);
        example.and(orCri);
        List<LastPrice> categoryLastPriceList = lastPriceMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(categoryLastPriceList)) {
            for (LastPrice lastPrice : categoryLastPriceList) {
                String asset1 = lastPrice.getAsset1();
                String asset2 = lastPrice.getAsset2();
                if (coinName.equals(asset1)) {
                    categoryLastPriceMap.put(asset2,lastPrice);
                } else {
                    categoryLastPriceMap.put(asset1,lastPrice);
                }
            }
        }
        return categoryLastPriceMap;
    }


    /**
     * 获取市场动态列表 - 名字 价格 涨幅
     * @param coinName
     */
    public List<MarketCategoryStateVO> getMarketCategoryStateList (String coinName) {
        List<AssetCategory> assetCategoryList = dbAssetsService.getAllAsset();
        List<MarketCategoryStateVO> marketCategoryStateVOList = new ArrayList<MarketCategoryStateVO>();
        Map<String,LastPrice> lastPriceMap = getCategoryLastPrice(coinName);
        for (AssetCategory assetCategory : assetCategoryList) {
            String name = assetCategory.getName();
            if (coinName.equals(name)) {
                continue;
            }
            MarketCategoryStateVO marketCategoryStateVO = new MarketCategoryStateVO();
            marketCategoryStateVO.setName(name);
            marketCategoryStateVO.setNick(assetCategory.getNick());
            //价格  涨幅 LAST_PRICE
            LastPrice lastPrice = lastPriceMap.get(name);
            if (lastPrice != null) {
                if (name.equals(lastPrice.getAsset1())) {
                    marketCategoryStateVO.setPriceRise(lastPrice.getIncrease() + "");
                    marketCategoryStateVO.setPrice(lastPrice.getPrice());
                    marketCategoryStateVO.setCount(lastPrice.getDayAmount1());
                } else {
                    marketCategoryStateVO.setPrice(lastPrice.getRecipPrice());
                    marketCategoryStateVO.setPriceRise(lastPrice.getRecipIncrease() + "");
                    marketCategoryStateVO.setCount(lastPrice.getDayAmount2());
                }
            } else {
                marketCategoryStateVO.setPrice(new BigDecimal(0));
                marketCategoryStateVO.setPriceRise("-");
                marketCategoryStateVO.setCount(new BigDecimal(0));
            }

            marketCategoryStateVOList.add(marketCategoryStateVO);
        }
        return marketCategoryStateVOList;
    }
}
