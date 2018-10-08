package com.beidou.market.service.impl;

import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.service.AtomicService;
import com.beidou.blockchain.vo.*;
import com.beidou.exchange.balance.service.BalanceService;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.service.CommonGenericService;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.entity.ExchangeHistory;
import com.beidou.market.entity.LastExchange;
import com.beidou.market.entity.ResponseCoinPriceVO;
import com.beidou.market.mapper.AssetCategoryMapper;
import com.beidou.market.mapper.ExchangeHistoryMapper;
import com.beidou.market.mapper.LastExchangeMapper;
import com.beidou.market.service.DbAssetsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fengguoqing on 2018/8/2.
 */
@Service
public class DbAssetsServiceImpl  implements DbAssetsService {

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private ExchangeHistoryMapper exchangeHistoryMapper;

    @Autowired
    private LastExchangeMapper lastExchangeMapper;

    @Autowired
    private AtomicService atomicService;

    @Autowired
    private BalanceService balanceService;

    public void checkIssueRequestParams (AssetsVO vo) {
        if (vo == null) {
            throw new BizException(ErrorEnum.ISSUE_ASSET_PARAMTER_ERROR);
        }
        if (StringUtils.isEmpty(vo.getName())) {
            throw new BizException(ErrorEnum.ISSUE_ASSET_PARAMTER_ERROR);
        }
        if (StringUtils.isEmpty(vo.getNick())) {
            throw new BizException(ErrorEnum.ISSUE_ASSET_PARAMTER_ERROR);
        }
        if (vo.getMaxCount() == null || vo.getMaxCount().intValue() == 0) {
            throw new BizException(ErrorEnum.ISSUE_ASSET_PARAMTER_ERROR);
        }
        if (vo.getUnits() == null) {
            throw new BizException(ErrorEnum.ISSUE_ASSET_PARAMTER_ERROR);
        }
    }

    public AssetsVO issue(AssetsVO vo) {
        checkIssueRequestParams(vo);
        if (isExistAssetName(vo.getName())) {
            throw new BizException(ErrorEnum.ISSUE_ASSET_EXIST_ERROR);
        }
        List<AddressBalancesVO> assetsList = assetsService.issue(vo);
        AssetCategory ac = new AssetCategory();
        Long currentTime = System.currentTimeMillis();
        ac.setName(vo.getName());
        ac.setQty(vo.getMaxCount());
        ac.setNick(vo.getNick());
        ac.setAddress(vo.getAddress());
        ac.setUnits(vo.getUnits());
        ac.setCreateon(currentTime);
        ac.setStatus(0);
        ac.setUpdateon(currentTime);
        assetCategoryMapper.insert(ac);
        return vo;
    }

    public void checkSendassetfrom (SendAssetfromVO vo) {
        if (vo == null) {
            throw new BizException(ErrorEnum.CHECKSENDASSETFROM_ERROR);
        }
        if (StringUtils.isEmpty(vo.getName()) || StringUtils.isEmpty(vo.getFrom()) ||
                StringUtils.isEmpty(vo.getTo()) || vo.getQyt() == null) {
            throw new BizException(ErrorEnum.CHECKSENDASSETFROM_ERROR);
        }
        ValidateAddressVO fromVO = assetsService.validateaddress(vo.getFrom());
        if (!fromVO.isIsvalid()) {
            throw new BizException(ErrorEnum.CHECKSENDASSETFROM_FROM_NOEXIST_ERROR);
        }
        ValidateAddressVO toVO = assetsService.validateaddress(vo.getTo());
        if (!toVO.isIsvalid()) {
            throw new BizException(ErrorEnum.CHECKSENDASSETFROM_TO_NOEXIST_ERROR);
        }
    }

    public boolean isExistAssetName (String name) {
        Example example = new Example(AssetCategory.class);
        example.createCriteria().andEqualTo("name",name).andEqualTo("status",0);
        List<AssetCategory> assetCategoryList = assetCategoryMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(assetCategoryList)) {
            return false;
        } else {
            return true;
        }
    }

    public List<AddressBalancesVO> sendassetfrom (SendAssetfromVO vo) {
        checkSendassetfrom(vo);
        if (!isExistAssetName(vo.getName())) {
            throw new BizException(ErrorEnum.ASSET_SEND_NAME_NOTEXIST_ERROR);
        }
        List<AddressBalancesVO> resultList = assetsService.sendassetfrom(vo);
        Long currentTime = System.currentTimeMillis();

        ExchangeHistory exchangeHistory = new ExchangeHistory();
        exchangeHistory.setCreateon(currentTime);
        exchangeHistory.setUpdateon(currentTime);
        exchangeHistory.setFromAssetName(vo.getName());
        exchangeHistory.setFromAddress(vo.getFrom());
        exchangeHistory.setFromQty(vo.getQyt());
        exchangeHistory.setToAddress(vo.getTo());
        exchangeHistory.setToAssetName(vo.getName());
        exchangeHistory.setType(vo.getType());
        exchangeHistory.setToQty(vo.getQyt());
        exchangeHistory.setStatus(0);
        exchangeHistoryMapper.insert(exchangeHistory);

        balanceService.increaseAvlBalance(vo.getTo(), vo.getName(), vo.getQyt());

        return resultList;
    }

    public void checkExchange (AtomicExchangeVO vo) {
        if (vo == null) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(vo.getFromAddress())) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(vo.getFromAssetName())) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        if (vo.getFromCount() == null) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(vo.getToAssetName())) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(vo.getToAssetName())) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        if (vo.getToCount() == null) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_PARAM_ERROR);
        }
        ValidateAddressVO fromVO = assetsService.validateaddress(vo.getFromAddress());
        if (!fromVO.isIsvalid()) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_FROM_ADDRESS_ERROR);
        }
        ValidateAddressVO toVO = assetsService.validateaddress(vo.getToAddress());
        if (!toVO.isIsvalid()) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_TO_ADDRESS_ERROR);
        }
        if (!isExistAssetName(vo.getFromAssetName())) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_FROM_NAME_ERROR);
        }
        if (!isExistAssetName(vo.getToAssetName())) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_TO_NAME_ERROR);
        }
        Map<String,AddressBalancesVO> fromBalances = assetsService.getaddressbalancesMap(vo.getFromAddress());
        AddressBalancesVO fromBlancesVO = fromBalances.get(vo.getFromAssetName());
        if (fromBlancesVO == null) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_FROM_COIN_ERROR);
        }
        if (vo.getFromCount().compareTo(fromBlancesVO.getQty()) == 1) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_FROM_COIN_ERROR);
        }

        Map<String,AddressBalancesVO> toBalances = assetsService.getaddressbalancesMap(vo.getToAddress());
        AddressBalancesVO toBlancesVO = toBalances.get(vo.getToAssetName());
        if (toBlancesVO == null) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_TO_COIN_ERROR);
        }
        if (vo.getToCount().compareTo(toBlancesVO.getQty()) == 1) {
            throw new BizException(ErrorEnum.ATOMIC_EXCHANGE_TO_COIN_ERROR);
        }

    }
    public List<AddressBalancesVO> exchange (AtomicExchangeVO vo) {
        checkExchange(vo);
        String txid = atomicService.atomicExchange(vo);
        if (StringUtils.isEmpty(txid)) {
            throw new BizException(ErrorEnum.ATOMIC_RPC_ERROR);
        }
        Long currentTime = System.currentTimeMillis();

        ExchangeHistory exchangeHistory = new ExchangeHistory();
        exchangeHistory.setCreateon(currentTime);
        exchangeHistory.setUpdateon(currentTime);
        exchangeHistory.setFromAssetName(vo.getFromAssetName());
        exchangeHistory.setFromAddress(vo.getFromAddress());
        exchangeHistory.setFromQty(vo.getFromCount());
        exchangeHistory.setToAddress(vo.getToAddress());
        exchangeHistory.setToAssetName(vo.getToAssetName());
        exchangeHistory.setType(ExchangeHistory.TYPE_ATOMIC_EXCHANGE);
        exchangeHistory.setToQty(vo.getToCount());
        exchangeHistory.setTxid(txid);
        exchangeHistory.setStatus(0);
        exchangeHistory.setAssetName(sortName(exchangeHistory.getFromAssetName(), exchangeHistory.getToAssetName()));
        exchangeHistoryMapper.insert(exchangeHistory);
        this.insertLastExchange(exchangeHistory);
        List<AddressBalancesVO> resultList = assetsService.getaddressbalances(vo.getFromAddress());
        return resultList;
    }

    public String sortName (String fromName, String toName) {
        String name = "";
        List<String> list = new ArrayList<String>();
        list.add(fromName);
        list.add(toName);
        Collections.sort(list);
        name = list.get(0) + "-" + list.get(1);
        return name;
    }

    public void insertLastExchange (ExchangeHistory exchangeHistory) {
        String exchangeName = sortName(exchangeHistory.getFromAssetName(),exchangeHistory.getToAssetName());
        //删除
        LastExchange oldLastExchange = null;
        Example example = new Example(LastExchange.class);
        example.createCriteria().andEqualTo("assetName",exchangeName);
        List<LastExchange> list = lastExchangeMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            oldLastExchange = list.get(0);
            lastExchangeMapper.deleteByExample(example);
        }

        //插入
        LastExchange lastExchange = new LastExchange();
        lastExchange.setCreateon(exchangeHistory.getCreateon());
        lastExchange.setUpdateon(exchangeHistory.getUpdateon());
        lastExchange.setFromAssetName(exchangeHistory.getFromAssetName());
        lastExchange.setFromAddress(exchangeHistory.getFromAddress());
        lastExchange.setFromQty(exchangeHistory.getFromQty());
        lastExchange.setToAddress(exchangeHistory.getToAddress());
        lastExchange.setToAssetName(exchangeHistory.getToAssetName());
        lastExchange.setToQty(exchangeHistory.getToQty());
        lastExchange.setTxid(exchangeHistory.getTxid());
        lastExchange.setAssetName(exchangeName);
        lastExchange.setStatus(0);
        if (oldLastExchange != null) {
            lastExchange.setFromTotalNum(oldLastExchange.getFromTotalNum().add(lastExchange.getFromQty()));
            lastExchange.setToTotalNum(oldLastExchange.getToTotalNum().add(lastExchange.getToQty()));
        } else {
            lastExchange.setFromTotalNum(lastExchange.getFromQty());
            lastExchange.setToTotalNum(lastExchange.getToQty());
        }
        lastExchangeMapper.insert(lastExchange);
    }

    public LastExchange getLastExchangeForName (String fromName, String toName) {
        String exchangeName = sortName(fromName,toName);
        //删除
        Example example = new Example(LastExchange.class);
        example.createCriteria().andEqualTo("assetName",exchangeName);
        List<LastExchange> list = lastExchangeMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public List<AssetCategory> getAllAsset () {
        Example example = new Example(AssetCategory.class);
        example.createCriteria().andEqualTo("status",0);
        List<AssetCategory> assetCategoryList = assetCategoryMapper.selectByExample(example);
        return assetCategoryList;
    }

    public Map<String,AssetCategory> getAllAssetMap () {
        Example example = new Example(AssetCategory.class);
        example.createCriteria().andEqualTo("status",0);
        List<AssetCategory> assetCategoryList = getAllAsset();
        Map<String,AssetCategory> map = new HashMap<>();
        if (CollectionUtils.isEmpty(assetCategoryList)) {
            return null;
        }
        for (AssetCategory assetCategory : assetCategoryList) {
            map.put(assetCategory.getName(),assetCategory);
        }
        return map;
    }

    public List<AssetCategory> getUserAsset (String address) {
        Example example = new Example(AssetCategory.class);
        example.createCriteria().andEqualTo("status",0);
        List<AssetCategory> assetCategoryList = assetCategoryMapper.selectByExample(example);
        Map<String,AddressBalancesVO> userBalanceMap = assetsService.getaddressbalancesMap(address);
        Map<String,BigDecimal> priceMap = getPrice("");
        for (AssetCategory ac : assetCategoryList) {
            String name = ac.getName();
            AddressBalancesVO userVO = userBalanceMap.get(name);
            if (userVO == null) {
                ac.setQty(new BigDecimal(0));
            } else {
                ac.setQty(userVO.getQty());
            }
            ac.setPrice(priceMap.get(name));
        }
        return assetCategoryList;
    }

    /**
     * 获取今天的交易量
     * @param fromName
     * @param toName
     * @return
     */
    public BigDecimal getTodayExchangeNum (String fromName, String toName) {
        String exchangeName = sortName(fromName,toName);
        return null;
    }

    /**
     * 获取所有价格
     * @param coinName 对比参照
     * @return
     */
    public Map<String,BigDecimal> getPrice (String coinName) {
        Map<String,BigDecimal> retMap = new LinkedHashMap<>();
        List<AssetCategory> assetCategoryList = getAllAsset();
        AssetCategory cnyAc = null;
        AssetCategory defaultAc = null;
        Map<String,AssetCategory> acMap = new HashMap<String,AssetCategory>();
        for (AssetCategory ac : assetCategoryList) {
            acMap.put(ac.getName(),ac);
            if (ac.getType() != null) {
                if (1 == ac.getType()) {
                    defaultAc = ac;
                } else if (2 == ac.getType()) {
                    cnyAc = ac;
                }
            }
        }
        if (StringUtils.isEmpty(coinName) && cnyAc != null) {
            coinName = cnyAc.getName();
        }
        for (AssetCategory ac : assetCategoryList) {
            LastExchange lastExchange = getLastExchangeForName(ac.getName(),coinName);
            if (lastExchange != null) {
                BigDecimal price = lastExchange.getFromQty().divide(lastExchange.getToQty(),4);
                if (lastExchange.getToAssetName().equals(ac.getName())) {
                    price = lastExchange.getFromQty().divide(lastExchange.getToQty(),4);
                }
                retMap.put(ac.getName(),price);
            }
        }
        return retMap;
    }


    public List<ResponseCoinPriceVO> getHomePage (String coinName) {
        List<AssetCategory> assetCategoryList = getAllAsset();
        AssetCategory cnyAc = null;
        AssetCategory defaultAc = null;
        Map<String,AssetCategory> acMap = new HashMap<String,AssetCategory>();
        for (AssetCategory ac : assetCategoryList) {
            acMap.put(ac.getName(),ac);
            if (ac.getType() != null) {
                if (1 == ac.getType()) {
                    defaultAc = ac;
                } else if (2 == ac.getType()) {
                    cnyAc = ac;
                }
            }
        }
        if (StringUtils.isNotEmpty(coinName)) {
            defaultAc = acMap.get(coinName);
        }
        coinName = defaultAc.getName();
        List<ResponseCoinPriceVO> acList = new ArrayList<ResponseCoinPriceVO>();
        for (AssetCategory ac : assetCategoryList) {
            if (ac.getName().equals(coinName)) {
                continue;
            }
            if (ac.getType() != null && 2 == ac.getType()) {
                continue;
            }
            ResponseCoinPriceVO responseCoinPriceVO = new ResponseCoinPriceVO();
            responseCoinPriceVO.setName(ac.getName());
            responseCoinPriceVO.setNick(ac.getNick());
            responseCoinPriceVO.setQty(ac.getQty());
            //价格

            LastExchange lastExchange = getLastExchangeForName(ac.getName(),coinName);
            LastExchange cnyLastExchange = getLastExchangeForName(ac.getName(),cnyAc.getName());
            if (lastExchange != null) {
                BigDecimal price = lastExchange.getFromQty().divide(lastExchange.getToQty());
                if (lastExchange.getToAssetName().equals(ac.getName())) {
                    price = lastExchange.getToQty().divide(lastExchange.getFromQty());
                }
                responseCoinPriceVO.setPrice(price);
                //总交易量
                BigDecimal totalNum = lastExchange.getFromTotalNum();
                if (!ac.getName().equals(lastExchange.getFromAssetName())) {
                    totalNum = lastExchange.getToTotalNum();
                }
                responseCoinPriceVO.setTotalExchangeAmount(totalNum);
            }
            if (cnyLastExchange != null) {
                BigDecimal cnyPrice = cnyLastExchange.getFromQty().divide(cnyLastExchange.getToQty());
                if (cnyLastExchange.getToAssetName().equals(ac.getName())) {
                    cnyPrice = cnyLastExchange.getToQty().divide(cnyLastExchange.getFromQty());
                }
                responseCoinPriceVO.setCnyPrice(cnyPrice);
            }


            //24小时交易量

            //总市值
            //日涨幅
            //趋势图
            acList.add(responseCoinPriceVO);
        }
        return acList;
    }



    public AssetCategory getAssetCategoryForType (int type) {
        Example example = new Example(AssetCategory.class);
        example.createCriteria().andEqualTo("status",0).andEqualTo("type",type);
        List<AssetCategory> assetCategoryList = assetCategoryMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(assetCategoryList)) {
            return assetCategoryList.get(0);
        }
        return null;
    }

}
