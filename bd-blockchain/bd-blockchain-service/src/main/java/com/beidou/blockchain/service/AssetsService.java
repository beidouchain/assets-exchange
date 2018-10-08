package com.beidou.blockchain.service;

import com.beidou.blockchain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * Created by fengguoqing on 2018/6/20.
 */
public interface AssetsService {
    /**
     * 获取新地址
     * @return
     */
    public String getnewaddress ();

    /**
     * 授权
     * @param address 地址
     * @param isIssue 是否有发行权限
     * @return
     */
    public String grant (String address,boolean isIssue);

    /**
     * 获取地址资产
     * @param address
     * @return
     */
    public List<AddressBalancesVO> getaddressbalances (String address);

    /**
     * 发行资产
     * @param vo
     * @return
     */
    public List<AddressBalancesVO> issue (AssetsVO vo);

    /**
     * 发送资产
     * @param vo
     * @return
     */
    public List<AddressBalancesVO> sendassetfrom (SendAssetfromVO vo);

    /**
     * 创建地址并且授权
     * @return
     */
    public String getnewaddressAndGrant ();
    /**
     * 获取用户信息
     * @param address
     * @return
     */
    public UserInfoVO getUserInfo (String address);

    /**
     * 更新用户信息
     * @param vo
     * @return
     */
    public UserInfoVO setUserInfo (UserInfoVO vo);

    /**
     * 注册用户
     * @param vo
     * @return
     */
    public UserInfoVO regist (UserInfoVO vo);


    /**
     * 注册私钥用户
     * @param vo
     * @return
     */
    public UserInfoVO registPrivateKey (UserInfoVO vo);
    /**
     * 登录
     * @param vo
     * @return
     */
    public UserInfoVO login (UserInfoVO vo);

    /**
     * 验证地址
     * @param address
     * @return
     */
    public ValidateAddressVO validateaddress (String address);

    /**
     * 获取余额
     * @param address
     * @return
     */
    public Map<String,AddressBalancesVO> getaddressbalancesMap (String address);

}
