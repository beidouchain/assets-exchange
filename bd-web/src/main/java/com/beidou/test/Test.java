package com.beidou.test;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.common.AESUtil;
import com.beidou.exchange.common.ByteToHexUtil;
import com.beidou.exchange.common.aes.AES;
import com.googlecode.jsonrpc4j.Base64;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.sun.crypto.provider.DESKeyGenerator;
import org.apache.commons.collections.CollectionUtils;
import sun.security.krb5.internal.crypto.Des;

import java.net.URL;
import java.util.*;

/**
 * Created by fengguoqing on 2018/6/19.
 */
public class Test {

    private String rpcUserName = "beidouchainrpc";
    private String rpcPwd = "BqSSbcvXPSMGESpLCFPTqCLNYu9R3CJmYoMADDfPzEAs";
    private String rpcIp = "120.27.23.110";
    private String rpcPort = "8333";
    private static final String DATA = "asiatravel";
//1EmsNhNzrSh3uNR2BSm7FxrtNnDHRUbSj1WE3a
    //1XRyZrETsTLsZF79LUqPhqXyg9FDRWDtfpBT7z
    public static void main(String args[]) throws Exception{
        /*Test t = new Test();
        List<Object> params = new ArrayList<Object>();
        params.add("1EmsNhNzrSh3uNR2BSm7FxrtNnDHRUbSj1WE3a");
        params.add(new Object[]{"stream","{'telPhone':'13051121113'}"});
        Object p = new Object[]{"1Bo9m5rd5epH7fNepBYfk3eQG2caLvqgTg3TaS","userInfo","{'phone':'13051212321232'}","48656C6C6F20576F726C64210A"};
        JSONObject result = t.invokeRPC(UUID.randomUUID().toString(),"publishfrom",p);
        System.out.println("result=" + result);*/

        // 加密原文
        String plaintext = "1123@2！@#￥%……32";
        // 密钥
        String key = "aaaa";
        //密文
        String ciphertext;
        try {
            ciphertext = AES.encrypt(plaintext.getBytes("UTF-8"), key);
            String de = AES.decrypt(ciphertext, key);
            System.out.println("原:" + plaintext);
            System.out.println("加密后:" + ciphertext);
            System.out.println("解密后:" + de);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        }
        return json;


    }
}
