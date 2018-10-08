package com.beidou.exchange.test;

import com.baidu.aip.ocr.AipOcr;
import com.beidou.exchange.client.OcrBaiduFactory;
import org.json.JSONObject;

import java.util.HashMap;

public class OcrTest {


    public static void main(String[] args) {
        AipOcr client = OcrBaiduFactory.getAipOcr();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");

        // 参数为本地图片路径
        String image = "/Users/kexinlee/test.jpeg";
        JSONObject res = client.idcard(image, "front", options);
        System.out.println(res.toString(2));
        if(null != res){
            if(res.has("words_result1")){
                JSONObject words = res.getJSONObject("words_result");
                JSONObject nameJson = words.getJSONObject("姓名");
                System.out.println(nameJson.getString("words"));
                JSONObject nationalityJson = words.getJSONObject("民族");
                System.out.println(nationalityJson.getString("words"));
                JSONObject addressJson = words.getJSONObject("住址");
                System.out.println(addressJson.getString("words"));
                JSONObject cardJson = words.getJSONObject("公民身份号码");
                System.out.println(cardJson.getString("words"));
                JSONObject birthDateJson = words.getJSONObject("出生");
                System.out.println(birthDateJson.getString("words"));
                JSONObject sexJson = words.getJSONObject("性别");
                System.out.println(sexJson.getString("words"));
            }



        }
        // 参数为本地图片路径
        String image1 = "/Users/kexinlee/test1.jpeg";
        JSONObject res1 = client.idcard(image1, "back", options);
        System.out.println(res1.toString(2));
        if(null != res1){
            JSONObject words = res1.getJSONObject("words_result");
            JSONObject expiryDateJson = words.getJSONObject("失效日期");
            System.out.println(expiryDateJson.getString("words"));
            JSONObject issuingAuthorityJson = words.getJSONObject("签发机关");
            System.out.println(issuingAuthorityJson.getString("words"));
            JSONObject issueDateJson = words.getJSONObject("签发日期");
            System.out.println(issueDateJson.getString("words"));
        }
        // 参数为本地图片二进制数组
        //byte[] file = new byte[0];
        //try {
        //    file = FileUtil.readFileByBytes(image);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //res = client.idcard(file, "front", options);
        //System.out.println(res.toString(2));
    }

}
