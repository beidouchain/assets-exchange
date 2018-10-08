package com.beidou.blockchain.controller;

import com.beidou.blockchain.service.ManagingWalletAddressesService;
import com.beidou.blockchain.vo.AddressesVO;
import com.beidou.blockchain.vo.RequestListaddressesVO;
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
@RequestMapping("/managingWalletAddresses")
public class ManagingWalletAddressesController {

    @Autowired
    private ManagingWalletAddressesService managingWalletAddressesService;

    @RequestMapping("/listaddresses")
    public ServiceResponse listaddresses(@RequestBody RequestListaddressesVO vo) {
        List<AddressesVO> addresesList = managingWalletAddressesService.listaddresses(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, addresesList);
    }

    @RequestMapping("/getaddresses")
    public ServiceResponse getaddresses(@RequestBody RequestListaddressesVO vo) {
        List<AddressesVO> addresesList = managingWalletAddressesService.getaddresses(vo);
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, addresesList);
    }

}
