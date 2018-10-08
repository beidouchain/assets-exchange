package com.beidou.blockchain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.core.CoinBaseUtil;
import com.beidou.blockchain.core.RPCClient;
import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.vo.*;
import com.beidou.exchange.common.*;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.redis.RedisUtil;
import com.beidou.user.service.UserPromotionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@Service
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private RPCClient rpcClient;

    @Autowired
    private UserPromotionService userPromotionService;

    public String getnewaddress() {
        Object[] objParams = null;
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "getnewaddress");
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

    public String grant(String address, boolean isIssue) {
        Object[] objParams = null;
        String grantAuth = "connect,send,receive,activate,admin";
        if (isIssue) {
            grantAuth += ",issue";
        }
        objParams = new Object[]{address, grantAuth};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "grant", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

    public List<AddressBalancesVO> getaddressbalances(String address) {
        Object[] objParams = new Object[]{address};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "getaddressbalances", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        List<AddressBalancesVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), AddressBalancesVO.class);
        if (CollectionUtils.isNotEmpty(resultArray)) {
            for (AddressBalancesVO vo : resultArray) {
                vo.setAddress(address);
            }
        }
        return resultArray;
    }

    public Map<String, AddressBalancesVO> getaddressbalancesMap(String address) {
        Map<String, AddressBalancesVO> map = new HashMap<String, AddressBalancesVO>();
        List<AddressBalancesVO> list = getaddressbalances(address);
        if (CollectionUtils.isNotEmpty(list)) {
            for (AddressBalancesVO vo : list) {
                map.put(vo.getName(), vo);
            }
        }
        return map;
    }

    public CreatekeypairsVO createkeypairs() {
        Object[] objParams = new Object[]{};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "createkeypairs", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        List<CreatekeypairsVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), CreatekeypairsVO.class);
        if (CollectionUtils.isNotEmpty(resultArray)) {
            return resultArray.get(0);
        }
        return null;
    }

    public ValidateAddressVO validateaddress(String address) {
        Object[] objParams = new Object[]{address};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "validateaddress", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        ValidateAddressVO resultVO = JSONObject.parseObject(JSONObject.toJSONString(obj), ValidateAddressVO.class);
        return resultVO;
    }

    public List<AddressBalancesVO> issue(AssetsVO vo) {
        Object[] objParams = null;
        if (vo == null) {
            vo = new AssetsVO();
        }
        String address = "";
        if (StringUtils.isEmpty(vo.getAddress())) {
            address = getnewaddress();
            vo.setAddress(address);
        }
        //赋权限
        String grantRet = grant(vo.getAddress(), true);
        //发行资产
        List<AddressBalancesVO> resultArray = null;
        if (vo != null) {
            objParams = new Object[]{vo.getAddress(), vo.getName(), vo.getMaxCount(), vo.getUnits()};
        }
        Object issueRet = rpcClient.invokeRPC(UUID.randomUUID().toString(), "issue", objParams);
        return getaddressbalances(vo.getAddress());
    }

    public List<AddressBalancesVO> sendassetfrom(SendAssetfromVO vo) {
        Object[] objParams = null;
        if (vo == null) {
            return null;
        }
        if (vo != null) {
            objParams = new Object[]{vo.getFrom(), vo.getTo(), vo.getName(), vo.getQyt()};
        }
        Object issueRet = rpcClient.invokeRPC(UUID.randomUUID().toString(), "sendassetfrom", objParams);
        return getaddressbalances(vo.getFrom());
    }

    public String getnewaddressAndGrant() {
        String address = getnewaddress();
        grant(address, false);
        return address;
    }

    public CreatekeypairsVO getnewaddressAndGrantPrivateKey() {
        CreatekeypairsVO createkeypairsVO = createkeypairs();
        String address = createkeypairsVO.getAddress();
        grant(address, false);
        return createkeypairsVO;
    }

    public UserInfoVO setUserInfo(UserInfoVO vo) {
        String address = vo.getAddress();
        if (StringUtils.isEmpty(address)) {
            address = getnewaddressAndGrant();
        }
        vo.setAddress(address);
        publishfrom(vo);
        return getUserInfo(address);
    }

    public UserInfoVO setUserInfoPrivateKey(UserInfoVO vo) {
        String address = vo.getAddress();
        CreatekeypairsVO createkeypairsVO = null;
        if (StringUtils.isEmpty(address)) {
            createkeypairsVO = getnewaddressAndGrantPrivateKey();
        }
        vo.setAddress(createkeypairsVO.getAddress());
        vo.setPrivateKey(createkeypairsVO.getPrivkey());
        vo.setPublicKey(createkeypairsVO.getPubkey());
        publishfrom(vo);
        return getUserInfo(address);
    }

    /**
     * 保存用户信息
     *
     * @param vo
     * @return
     */
    public boolean publishfrom(UserInfoVO vo) {
        Object[] objParams = null;
        if (vo == null) {
            return false;
        }
        if (StringUtils.isEmpty(vo.getAddress())) {
            return false;
        }
        String saveData = "";
        if (StringUtils.isNotEmpty(vo.getName())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getName());
            json.put("k", UserEnum.name.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getPhone())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getPhone());
            json.put("k", UserEnum.phone.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getCardId())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getCardId());
            json.put("k", UserEnum.cardId.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getCardType())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getCardType());
            json.put("k", UserEnum.cardType.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getEmail())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getEmail());
            json.put("k", UserEnum.email.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getPwd())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getPwd());
            json.put("k", UserEnum.pwd.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getPrivateKey())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getPrivateKey());
            json.put("k", UserEnum.privateKey.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getPublicKey())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getPublicKey());
            json.put("k", UserEnum.publicKey.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getUid())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getUid());
            json.put("k", UserEnum.uid.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getR1())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getR1());
            json.put("k", UserEnum.r1.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getR2())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getR2());
            json.put("k", UserEnum.r2.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getR3())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getR3());
            json.put("k", UserEnum.r3.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getHash())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getHash());
            json.put("k", UserEnum.hash.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        if (StringUtils.isNotEmpty(vo.getInvationCode())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getInvationCode());
            json.put("k", UserEnum.invationCode.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
            //查询通过邀请码查询邀请人的address
            UserInfoVO invationVO = isExist(vo.getInvationCode(), UserEnum.id.getCode());
            if (invationVO != null) {
                userPromotionService.saveUserPromotion(invationVO.getAddress(), vo.getAddress());
            }
        }

        if (StringUtils.isNotEmpty(vo.getId())) {
            JSONObject json = new JSONObject();
            json.put("v", vo.getId());
            json.put("k", UserEnum.id.getCode());
            saveData = json.toJSONString();
            publishfromInvokeRPC(vo.getAddress(), saveData);
        }
        return true;
    }

    public UserInfoVO getUserInfo(String address) {
        UserInfoVO vo = new UserInfoVO();
        vo.setAddress(address);
        Map<String, StreamVO> map = new HashMap<>();
        liststreampublisheritems(address, 0, map);
        if (map.size() == 0) {
            return null;
        }
        for (String k : map.keySet()) {
            StreamVO streamVO = map.get(k);
            if (k.equals(UserEnum.cardId.getCode())) {
                vo.setCardId(streamVO.getValue());
            } else if (k.equals(UserEnum.cardType.getCode())) {
                vo.setCardType(streamVO.getValue());
            } else if (k.equals(UserEnum.email.getCode())) {
                vo.setEmail(streamVO.getValue());
            } else if (k.equals(UserEnum.name.getCode())) {
                vo.setName(streamVO.getValue());
            } else if (k.equals(UserEnum.phone.getCode())) {
                vo.setPhone(streamVO.getValue());
            } else if (k.equals(UserEnum.pwd.getCode())) {
                vo.setPwd(streamVO.getValue());
            } else if (k.equals(UserEnum.publicKey.getCode())) {
                vo.setPublicKey(streamVO.getValue());
            } else if (k.equals(UserEnum.privateKey.getCode())) {
                vo.setPrivateKey(streamVO.getValue());
            } else if (k.equals(UserEnum.uid.getCode())) {
                vo.setUid(streamVO.getValue());
            } else if (k.equals(UserEnum.r1.getCode())) {
                vo.setR1(streamVO.getValue());
            } else if (k.equals(UserEnum.r2.getCode())) {
                vo.setR2(streamVO.getValue());
            } else if (k.equals(UserEnum.r3.getCode())) {
                vo.setR3(streamVO.getValue());
            } else if (k.equals(UserEnum.hash.getCode())) {
                vo.setHash(streamVO.getValue());
            } else if (k.equals(UserEnum.invationCode.getCode())) {
                vo.setInvationCode(streamVO.getValue());
            } else if (k.equals(UserEnum.id.getCode())) {
                vo.setId(streamVO.getValue());
            }
        }
        return vo;
    }

    public void liststreampublisheritems(String address, int startNum, Map<String, StreamVO> map) {
        Object[] objParams = new Object[]{"userinfo", address, true, 10, startNum};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "liststreampublisheritems", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return;
        }
        List<StreamVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), StreamVO.class);
        if (resultArray == null || resultArray.size() == 0) {
            return;
        }
        for (StreamVO vo : resultArray) {
            Long time = vo.getTime();
            String data = vo.getKey();
            try {
                JSONObject json = (JSONObject) JSON.parse(data);
                String k = json.getString("k");
                String v = json.getString("v");
                vo.setValue(v);
                if (map.get(k) == null) {
                    map.put(k, vo);
                } else {
                    StreamVO eqVO = map.get(k);
                    if (eqVO.getTime() <= vo.getTime()) {
                        map.put(k, vo);
                    }
                }
            } catch (Exception ex) {
            }
        }
        if (resultArray.size() < 10) {
            return;
        } else {
            startNum = startNum == 0 ? 1 : startNum;
            liststreampublisheritems(address, startNum, map);
        }
    }

    public Object publishfromInvokeRPC(String address, String saveData) {
        Object[] objParams = new Object[]{address, "userinfo", saveData, StringHexUtils.strTo16(saveData)};
        Object issueRet = rpcClient.invokeRPC(UUID.randomUUID().toString(), "publishfrom", objParams);
        return issueRet;
    }

    public UserInfoVO isExist(String value, String userEmumCodeType) {
        boolean b = false;
        JSONObject queryJson = new JSONObject();
        queryJson.put("k", userEmumCodeType);
        queryJson.put("v", value);
        Object[] objParams = new Object[]{"userinfo", queryJson.toJSONString(), true};
        JSONObject issueRet = rpcClient.invokeRPC(UUID.randomUUID().toString(), "liststreamkeyitems", objParams);
        Object obj = issueRet.get("result");
        if (obj == null) {
            return null;
        }
        List<StreamVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), StreamVO.class);
        if (resultArray == null || resultArray.size() == 0) {
            return null;
        } else {
            StreamVO streamVO = resultArray.get(0);
            Object[] objs = streamVO.getPublishers();
            if (objs.length == 1) {
                String address = (String) objs[0];
                return getUserInfo(address);
            }
            return null;
        }
    }

    public UserInfoVO regist(UserInfoVO vo) {
        String privateKey = new StringRandom().getStringRandom(64);
        vo.setPrivateKey(privateKey);
        if (vo == null || StringUtils.isEmpty(vo.getCardId())) {
            throw new BizException(ErrorEnum.REGIST_ERROR);
        }
        /*
        if (isExist(vo.getCardId(), UserEnum.cardId.getCode()) != null) {
            throw new BizException(ErrorEnum.REGIST_EXIST_ERROR);
        }*/
        vo.setId(userPromotionService.promotionNextVal() + "");
        return setUserInfo(vo);
    }

    public void setUserInfoSign(UserInfoVO vo) {
        /*
        String privateKey = vo.getPrivateKey();
        String pwd = BDSecurityKeyGenerator.getMessageDigestBase62(privateKey, "SHA-256").substring(0, 32);
        String r1 = new StringRandom().getStringRandom(9);
        vo.setR1(r1);
        String pwdDigest = BDSecurityKeyGenerator.getMessageDigestBase62(
                pwd + "-" + r1, "SHA-1");
        String r2 = new StringRandom().getStringRandom(10);
        vo.setR2(r2);
        String hash1 = BDSecurityKeyGenerator.bcryptHash(pwdDigest, r2);
        String r3 = "JHyyyldJL6NV7IcQ";
        String hash2 = BDSecurityKeyGenerator.getMessageDigestBase64(
                hash1 + "-" + r3, "SHA-256");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hash", hash1);
        String encryptContent = BDSecurityKeyGenerator.aesEncrypt(jsonObject.toJSONString(), hash2);
        */
    }

    public UserInfoVO registPrivateKey(UserInfoVO vo) {
        if (vo == null || StringUtils.isEmpty(vo.getCardId())) {
            throw new BizException(ErrorEnum.REGIST_ERROR);
        }
        if (isExist(vo.getCardId(), UserEnum.cardId.getCode()) != null) {
            throw new BizException(ErrorEnum.REGIST_EXIST_ERROR);
        }
        return setUserInfoPrivateKey(vo);
    }

    public UserInfoVO login(UserInfoVO vo) {
        if (vo == null || StringUtils.isEmpty(vo.getAddress()) || StringUtils.isEmpty(vo.getPrivateKey())) {
            throw new BizException(ErrorEnum.LOGIN_NULL_ERROR);
        }
        UserInfoVO retUserInfo = null;
        UserInfoVO addressVO = isExist(vo.getPrivateKey(), UserEnum.privateKey.getCode());
        if (addressVO != null) {
            retUserInfo = addressVO;
        } else {
            throw new BizException(ErrorEnum.LOGIN_NAME_NOTEXIST_ERROR);
        }
        if (!retUserInfo.getAddress().equals(vo.getAddress())) {
            throw new BizException(ErrorEnum.LOGIN_NAME_NOTEXIST_ERROR);
        }
        String redisKey = BDConstants.TOKEN_KEY_REDIS + vo.getAddress();
        String token = vo.getAddress() + "_" + new StringRandom().getStringRandom(8) + "_" + System.currentTimeMillis();
        retUserInfo.setToken(token);
        retUserInfo.setId(StringHexUtils.addZeroForNum(retUserInfo.getId(), 5));
        RedisUtil.setStringEx(redisKey, 30 * 60, token);
        return retUserInfo;
    }


    public List<TotalBalancesVO> getTotalBalancesList() {
        Object[] objParams = new Object[]{};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "gettotalbalances", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        List<TotalBalancesVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), TotalBalancesVO.class);
        return resultArray;
    }

    public List<CreateAssetsVO> listassets() {
        Object[] objParams = new Object[]{};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "listassets", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            return null;
        }
        List<CreateAssetsVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), CreateAssetsVO.class);
        return resultArray;
    }


    public WalletTransactionVO getwallettransaction(String txid) {
        Object[] objParams = new Object[]{txid};
        JSONObject result = null;
        Object obj = null;
        try {
            result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "getwallettransaction", objParams);
            obj = result.get("result");
            if (obj == null) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
        WalletTransactionVO resultVO = JSONObject.parseObject(JSONObject.toJSONString(obj), WalletTransactionVO.class);
        return resultVO;
    }


    /**
     * 获取这个地址下所有的交易
     * 放到redis里面 以便扩展
     *
     * @param address
     * @return
     */
    public void listaddresstransactions(String address, Integer pageNum) {
        if (pageNum == null) {
            pageNum = 0;
        }
        String redisKey = CoinBaseUtil.REDIS_COIN_ASSETS_START_KEY + address;
        if (pageNum == 0) {
            RedisUtil.del(redisKey);
        }
        int pageSize = 10;

        Object[] objParams = new Object[]{address, pageSize, pageNum.intValue()};
        JSONObject result = rpcClient.invokeRPC(UUID.randomUUID().toString(), "listaddresstransactions", objParams);
        Object obj = result.get("result");
        if (obj == null) {
            throw new BizException(ErrorEnum.ASSETS_INTERFACE_ERROR);
        }
        List<ListAddressTransactionsVO> resultArray = JSONObject.parseArray(JSONObject.toJSONString(obj), ListAddressTransactionsVO.class);
        if (CollectionUtils.isNotEmpty(resultArray)) {
            List<ListAddressTransactionsVO> list = null;
            if (RedisUtil.existKey(redisKey)) {
                list = (List<ListAddressTransactionsVO>) RedisUtil.getObj(redisKey, List.class);
            } else {
                list = new ArrayList<ListAddressTransactionsVO>();
            }
            list.addAll(resultArray);
            RedisUtil.setObj(redisKey, list);
            if (resultArray.size() == pageSize) {
                listaddresstransactions(address, pageNum++);
            }
        }
    }

}
