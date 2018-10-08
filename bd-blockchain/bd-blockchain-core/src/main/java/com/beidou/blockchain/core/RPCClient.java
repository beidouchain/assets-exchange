package com.beidou.blockchain.core;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.exception.BizException;
import com.googlecode.jsonrpc4j.Base64;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengguoqing on 2018/6/17.
 */
@Component("rpcClient")
public class RPCClient {
    private String rpcUserName = "beidouchainrpc";
    private String rpcPwd = "BqSSbcvXPSMGESpLCFPTqCLNYu9R3CJmYoMADDfPzEAs";
    //private String rpcIp = "120.27.23.110";//120.27.23.110 127.0.0.1
    private String rpcIp = "127.0.0.1";
    private String rpcPort = "8333";


    public <T> JSONObject invokeRPC(String id, String method) {
        return invokeRPC(id,method,new ArrayList<Object>());
    }

    /**
     * 调用钱包 rpc 基础方法
     * @param id
     * @param method
     * @param params
     * @return
     */
    public <T> JSONObject invokeRPC(String id, String method, T params) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("method", method);
        String cred = Base64.encodeBytes((rpcUserName + ":" + rpcPwd).getBytes());
        Map<String, String> headers = new HashMap<String, String>(1);
        // 身份认证
        headers.put("Authorization", "Basic " + cred);
        try {
            JsonRpcHttpClient client = new JsonRpcHttpClient(
                    new URL("http://" + rpcIp + ":" + rpcPort), headers);
            Object paramObj = null;
            if (params instanceof List) {
                List<T> listParam = (List<T>)params;
                if (CollectionUtils.isNotEmpty(listParam)) {
                    paramObj = listParam.toArray();
                }
            } else {
                paramObj = params;
            }
            Object result =  client.invoke(method, paramObj, Object.class);
            json.put("result",result);
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new BizException(ErrorEnum.UNKNOW_ERROR);
        }
        return json;
    }
}
