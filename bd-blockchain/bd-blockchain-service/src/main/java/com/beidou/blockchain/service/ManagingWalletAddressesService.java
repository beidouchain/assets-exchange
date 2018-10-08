package com.beidou.blockchain.service;


import com.beidou.blockchain.vo.AddressesVO;
import com.beidou.blockchain.vo.RequestListaddressesVO;

import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 */
public interface ManagingWalletAddressesService {


    /**
     * 获取钱包中的地址信息
     * (addresses=*) (verbose=false) (count=MAX) (start=-count)
     * @param vo
     * @return
     */
    public List<AddressesVO> listaddresses(RequestListaddressesVO vo);


    /**
     * 获取地址列表 verbose=true
     * @param vo
     * @return
     */
    public List<AddressesVO> getaddresses (RequestListaddressesVO vo);
}
