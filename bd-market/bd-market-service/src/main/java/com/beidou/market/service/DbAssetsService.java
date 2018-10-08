package com.beidou.market.service;

import com.beidou.blockchain.vo.AddressBalancesVO;
import com.beidou.blockchain.vo.AssetsVO;
import com.beidou.blockchain.vo.AtomicExchangeVO;
import com.beidou.blockchain.vo.SendAssetfromVO;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.entity.ResponseCoinPriceVO;

import java.util.List;
import java.util.Map;

/**
 * Created by fengguoqing on 2018/8/2.
 */
public interface DbAssetsService {

    /**
     * 发行货币
     * @param vo
     * @return
     */
    public AssetsVO issue(AssetsVO vo);


    /**
     * 发送资产
     * @param vo
     * @return
     */
    public List<AddressBalancesVO> sendassetfrom (SendAssetfromVO vo);

    /**
     * 原子交易
     * @param vo
     * @return
     */
    public List<AddressBalancesVO> exchange (AtomicExchangeVO vo);

    /**
     * 获取首页
     * @param coinName
     * @return
     */
    public List<ResponseCoinPriceVO> getHomePage (String coinName);

    /**
     * 获取用户资产列表
     * @param address
     * @return
     */
    public List<AssetCategory> getUserAsset (String address);

    /**
     * 获取所有资产
     * @return map
     */
    public Map<String,AssetCategory> getAllAssetMap ();

    /**
     * 获取所有资产
     *
     * @return list
     */
    List<AssetCategory> getAllAsset ();

    /**
     * 根据类型获取资产类型
     * @param type
     * @return
     */
    public AssetCategory getAssetCategoryForType (int type);
}
