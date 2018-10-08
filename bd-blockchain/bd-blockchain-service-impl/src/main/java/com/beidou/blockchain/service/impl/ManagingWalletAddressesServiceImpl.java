package com.beidou.blockchain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.core.RPCClient;
import com.beidou.blockchain.service.ManagingWalletAddressesService;
import com.beidou.blockchain.vo.AddressesVO;
import com.beidou.blockchain.vo.RequestListaddressesVO;
import com.beidou.exchange.common.ListParse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@Service
public class ManagingWalletAddressesServiceImpl implements ManagingWalletAddressesService {

    @Autowired
    private RPCClient rpcClient;

    public List<AddressesVO> listaddresses (RequestListaddressesVO vo) {
        Object[] objParams = null;
        List<AddressesVO> resultArray = null;
        if (vo != null) {
            objParams = new Object[]{vo.getAddresses(),vo.isVerbose(),vo.getCount(),vo.getStart()};
        }
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(),"listaddresses",objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj),AddressesVO.class);
        return resultArray;
    }

    public List<AddressesVO> getaddresses (RequestListaddressesVO vo) {
        Object[] objParams = null;
        List<AddressesVO> resultArray = null;
        if (vo != null) {
            objParams = new Object[]{vo.isVerbose()};
        }
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(),"getaddresses",objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj),AddressesVO.class);
        return resultArray;
    }


}
