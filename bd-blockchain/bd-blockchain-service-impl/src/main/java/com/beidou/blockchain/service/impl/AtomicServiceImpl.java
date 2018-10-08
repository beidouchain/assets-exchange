package com.beidou.blockchain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.core.RPCClient;
import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.service.AtomicService;
import com.beidou.blockchain.vo.*;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.StringHexUtils;
import com.beidou.exchange.common.UserEnum;
import com.beidou.exchange.common.exception.BizException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@Service
public class AtomicServiceImpl implements AtomicService {

    @Autowired
    private RPCClient rpcClient;


    public String createrawexchange (PreparelockunspentfromVO vo, String assetName,BigDecimal count) {
        JSONObject json = new JSONObject();
        json.put(assetName,count);
        Object[] objParams = new Object[]{vo.getTxid(),vo.getVout(),json};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(),"createrawexchange",objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        return (String)obj;
    }
    public String sendrawtransaction (AppendrawexchangeVO vo) {

        Object[] objParams = new Object[]{vo.getHex()};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(),"sendrawtransaction",objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        return (String)obj;
    }
    public AppendrawexchangeVO appendrawexchange (PreparelockunspentfromVO vo,String hex, String assetName,BigDecimal count) {
        JSONObject json = new JSONObject();
        json.put(assetName,count);
        Object[] objParams = new Object[]{hex,vo.getTxid(),vo.getVout(),json};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(),"appendrawexchange",objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        AppendrawexchangeVO retVo = JSONObject.parseObject(JSONObject.toJSONString(obj), AppendrawexchangeVO.class);
        return retVo;
    }

    public PreparelockunspentfromVO preparelockunspentfrom (String fromAddress,String assetName,BigDecimal count) {
        JSONObject json = new JSONObject();
        json.put(assetName,count);
        Object[] objParams = new Object[]{fromAddress,json};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(),"preparelockunspentfrom",objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        PreparelockunspentfromVO vo = JSONObject.parseObject(JSONObject.toJSONString(obj), PreparelockunspentfromVO.class);
        return vo;
    }

    public String atomicExchange(AtomicExchangeVO atomicVO) {
        boolean b = false;
        PreparelockunspentfromVO preLockFromVO = preparelockunspentfrom(atomicVO.getFromAddress(),atomicVO.getFromAssetName(),atomicVO.getFromCount());
        String hexFrom = createrawexchange(preLockFromVO,atomicVO.getToAssetName(),atomicVO.getToCount());
        PreparelockunspentfromVO preLockToVO = preparelockunspentfrom(atomicVO.getToAddress(),atomicVO.getToAssetName(),atomicVO.getToCount());
        AppendrawexchangeVO appendVO = appendrawexchange(preLockToVO,hexFrom,atomicVO.getFromAssetName(),atomicVO.getFromCount());
        if (appendVO.isComplete()) {
            String retTxId = sendrawtransaction(appendVO);
            if (StringUtils.isNotEmpty(retTxId)) {
                return retTxId;
            }
        }
        return null;
    }

}
