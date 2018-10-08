package com.beidou.exchange.service;

import com.beidou.exchange.entity.Ocr;
import sun.security.provider.certpath.OCSPResponse;

public interface OcrService {

    /**
     * 获取身份证信息
     *
     * @param image 本地图片路径或者图片二进制数据
     * @param idCardSide front：身份证含照片的一面；back：身份证带国徽的一面
     * @param detectDirection 是否检测图像朝向，默认不检测，即：false。朝向是指输入图像是正常方向、逆时针旋转90/180/270度。可选值包括:
     * - true：检测朝向；
     * - false：不检测朝向。
     * @param detectRisk 是否开启身份证风险类型(身份证复印件、临时身份证、身份证翻拍、修改过的身份证)功能，默认不开启，即：false。可选值:true-开启；false-不开启
     * @return
     */
    Ocr getIdCard(String image, String idCardSide, String detectDirection, String detectRisk);

    /**
     * 获取身份证信息
     *
     * @param image
     * @param idCardSide
     * @param detectDirection
     * @param detectRisk
     * @return
     */
    Ocr getIdCard(byte[] image, String idCardSide, String detectDirection, String detectRisk);
}
