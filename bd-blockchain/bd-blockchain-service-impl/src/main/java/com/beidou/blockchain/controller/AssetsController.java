package com.beidou.blockchain.controller;

import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.service.ManagingWalletAddressesService;
import com.beidou.blockchain.vo.*;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private AssetsService assetsService;

    /**
     * 发行资产
     * @param vo
     * @return
     */
    @RequestMapping("/issue")
     public ServiceResponse issue(@RequestBody AssetsVO vo) {
        List<AddressBalancesVO> assetsList = assetsService.issue(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, assetsList);
    }

    /**
     * 获取新钱包地址并且授权
     * @param vo
     * @return
     */
    @RequestMapping("/getnewaddressAndGrant")
    public ServiceResponse getnewaddressAndGrant() {
        String address = assetsService.getnewaddressAndGrant();
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, address);
    }

    /**
     * 发送资产
     * @param vo
     * @return
     */
    @RequestMapping("/sendassetfrom")
    public ServiceResponse sendassetfrom(@RequestBody SendAssetfromVO vo) {
        List<AddressBalancesVO> resultList = assetsService.sendassetfrom(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultList);
    }

    /**
     * 获取资产
     * @param
     * @return
     */
    @RequestMapping("/getAsset")
    public ServiceResponse getAsset(@RequestBody String address) {
        List<AddressBalancesVO> resultList = assetsService.getaddressbalances(address);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, resultList);
    }

}
