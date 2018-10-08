package com.beidou.user.service;

import com.beidou.user.entity.UserPromotion;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by fengguoqing on 2018/8/23.
 */
public interface UserPromotionService {


    /**
     * 保存推荐表
     * @param myAddress 邀请人
     * @param otherAddress 被邀请人
     */
    void saveUserPromotion (String myAddress,String otherAddress);

    /**
     * 获取我的推荐表
     * @param myAddress
     * @return
     */
    PageInfo<UserPromotion> getMyPromotion (String myAddress,int pageNo);

    /**
     * 自增长序列
     * @return
     */
    Long promotionNextVal ();
}
