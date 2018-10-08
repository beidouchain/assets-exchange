package com.beidou.exchange.client;

import com.baidu.aip.ocr.AipOcr;
import com.beidou.exchange.config.OcrBaidu;

public class OcrBaiduFactory {

    private OcrBaiduFactory() {
        // 禁止实例化
    }

    public static AipOcr getAipOcr() {
        String APP_ID = OcrBaidu.APP_ID;
        String API_KEY = OcrBaidu.API_KEY;
        String SECRET_KEY = OcrBaidu.SECRET_KEY;
        return new AipOcr(APP_ID, API_KEY, SECRET_KEY);
    }

}
