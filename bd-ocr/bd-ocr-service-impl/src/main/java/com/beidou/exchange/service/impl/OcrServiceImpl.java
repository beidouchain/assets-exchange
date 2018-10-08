package com.beidou.exchange.service.impl;

import com.baidu.aip.ocr.AipOcr;
import com.beidou.exchange.client.OcrBaiduFactory;
import com.beidou.exchange.entity.Ocr;
import com.beidou.exchange.service.OcrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service("ocrService")
public class OcrServiceImpl implements OcrService {

    private static final Logger LOGGER = Logger.getLogger(OcrServiceImpl.class);

    @Override
    public Ocr getIdCard(String image, String idCardSide, String detectDirection, String detectRisk){
        AipOcr client = OcrBaiduFactory.getAipOcr();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        if(StringUtils.isNotBlank(detectDirection)){
            options.put("detect_direction", detectDirection);
        }else{
            options.put("detect_direction", "true");
        }
        if(StringUtils.isNotBlank(detectRisk)){
            options.put("detect_risk", detectRisk);
        }else{
            options.put("detect_risk", "false");
        }
        if(StringUtils.isBlank(idCardSide)){
            idCardSide = "back";
        }
        JSONObject res = client.idcard(image, idCardSide, options);
        if(LOGGER.isDebugEnabled()){
            LOGGER.info(res.toString(2));
        }
        return jsonToOcr(res, idCardSide);
    }

    @Override
    public Ocr getIdCard(byte[] image, String idCardSide, String detectDirection, String detectRisk){
        AipOcr client = OcrBaiduFactory.getAipOcr();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        if(StringUtils.isNotBlank(detectDirection)){
            options.put("detect_direction", detectDirection);
        }else{
            options.put("detect_direction", "true");
        }
        if(StringUtils.isNotBlank(detectRisk)){
            options.put("detect_risk", detectRisk);
        }else{
            options.put("detect_risk", "false");
        }
        if(StringUtils.isBlank(idCardSide)){
            idCardSide = "back";
        }
        JSONObject res = client.idcard(image, idCardSide, options);
        if(LOGGER.isDebugEnabled()){
            LOGGER.info(res.toString(2));
        }
        return this.jsonToOcr(res, idCardSide);
    }

    Ocr jsonToOcr(JSONObject res, String idCardSide){
        Ocr ocr = new Ocr();
        if(null != res && res.has("words_result")){
            JSONObject words = res.getJSONObject("words_result");
            if("front".equals(idCardSide)){
                if(words.has("姓名")){
                    JSONObject nameJson = words.getJSONObject("姓名");
                    ocr.setName(nameJson.getString("words"));
                }
                if(words.has("民族")){
                    JSONObject nationalityJson = words.getJSONObject("民族");
                    ocr.setNationality(nationalityJson.getString("words"));
                }
                if(words.has("住址")){
                    JSONObject addressJson = words.getJSONObject("住址");
                    ocr.setAddress(addressJson.getString("words"));
                }
                if(words.has("公民身份号码")){
                    JSONObject cardJson = words.getJSONObject("公民身份号码");
                    ocr.setCard(cardJson.getString("words"));
                }
                if(words.has("出生")){
                    JSONObject birthDateJson = words.getJSONObject("出生");
                    ocr.setBirthDate(birthDateJson.getString("words"));
                }
                if(words.has("性别")){
                    JSONObject sexJson = words.getJSONObject("性别");
                    ocr.setSex(sexJson.getString("words"));
                }
            }else if("back".equals(idCardSide)){
                if(words.has("失效日期")){
                    JSONObject expiryDateJson = words.getJSONObject("失效日期");
                    ocr.setExpiryDate(expiryDateJson.getString("words"));
                }
                if(words.has("签发机关")){
                    JSONObject issuingAuthorityJson = words.getJSONObject("签发机关");
                    ocr.setIssuingAuthority(issuingAuthorityJson.getString("words"));
                }
                if(words.has("签发日期")){
                    JSONObject issueDateJson = words.getJSONObject("签发日期");
                    ocr.setIssueDate(issueDateJson.getString("words"));
                }

            }
        }
        return ocr;
    }
}
