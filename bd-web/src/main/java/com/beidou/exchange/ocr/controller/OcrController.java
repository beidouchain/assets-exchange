package com.beidou.exchange.ocr.controller;

import com.alibaba.fastjson.JSONObject;
import com.beidou.blockchain.service.AssetsService;
import com.beidou.blockchain.vo.UserInfoVO;
import com.beidou.exchange.common.AESUtil;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.StringHexUtils;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.entity.Ocr;
import com.beidou.exchange.service.OcrService;
import com.beidou.exchange.service.ServiceResponse;
import com.beidou.exchange.service.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by fengguoqing on 2018/7/11.
 */
@Controller
@RequestMapping("/ocr")
public class OcrController {
    @Autowired
    private OcrService ocrService;

    @Autowired
    private AssetsService assetsService;

    @RequestMapping("/registCard")
    public void registCard(@RequestParam("file") MultipartFile multipartFile,@RequestParam("invationCode") String invationCode,HttpServletResponse response) {

        byte[] fileByte = null;
        try {
            fileByte = multipartFile.getBytes();
        } catch (Exception ex) {
            throw new BizException(ErrorEnum.USER_UPLOAD_FILE_CARD_ERROR);
        }
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        Ocr ocr = ocrService.getIdCard(fileByte,"front","","");
        System.out.println("ocr info=" + JSONObject.toJSONString(ocr));
        String card = ocr.getCard();
        String name = ocr.getName();
        if (StringUtils.isEmpty(card)) {
            throw new BizException(ErrorEnum.USER_UPLOAD_FILE_CARD_ERROR);
        }
        if (StringUtils.isNotEmpty(invationCode) && StringUtils.isNumeric(invationCode)) {
            invationCode = Integer.parseInt(invationCode) + "";
        }
        UserInfoVO user = new UserInfoVO();
        user.setCardId(card);
        user.setName(name);
        user.setInvationCode(invationCode);
        UserInfoVO ret = assetsService.regist(user);
        UserInfoVO clearRet = new UserInfoVO();
        clearRet.setToken(ret.getToken());
        clearRet.setAddress(ret.getAddress());
        clearRet.setPrivateKey(ret.getPrivateKey());
        clearRet.setName(ret.getName());
        clearRet.setUid(ret.getUid());
        clearRet.setEmail(ret.getEmail());
        String id = StringHexUtils.addZeroForNum5(ret.getId());
        clearRet.setId(id);
        WebUtil.writeToWebByGzip(response, JSONObject.toJSONString(ServiceResponse.buildResponse(clearRet)));
    }
}
