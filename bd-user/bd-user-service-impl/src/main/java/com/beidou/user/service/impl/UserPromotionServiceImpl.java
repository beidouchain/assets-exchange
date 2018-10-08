package com.beidou.user.service.impl;

import com.beidou.exchange.common.BDConstants;
import com.beidou.exchange.common.DateUtility;
import com.beidou.exchange.redis.RedisUtil;
import com.beidou.user.entity.UserPromotion;
import com.beidou.user.mapper.UserPromotionMapper;
import com.beidou.user.service.UserPromotionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by fengguoqing on 2018/8/2.
 */
@Service("userPromotionService")
public class UserPromotionServiceImpl implements UserPromotionService {

    @Autowired
    private UserPromotionMapper userPromotionMapper;

    public void saveUserPromotion(String myAddress, String otherAddress) {
        UserPromotion userPromotion = new UserPromotion();
        userPromotion.setMyAddress(myAddress);
        userPromotion.setOtherAddress(otherAddress);
        Long currentTime = System.currentTimeMillis();
        userPromotion.setCreateon(currentTime);
        userPromotion.setUpdateon(currentTime);
        userPromotionMapper.insert(userPromotion);
    }

    public PageInfo<UserPromotion> getMyPromotion(String myAddress, int pageNo) {
        Example example = new Example(UserPromotion.class);
        example.createCriteria().andEqualTo("myAddress", myAddress);
        example.setOrderByClause("createon desc");
        PageHelper.startPage(pageNo, BDConstants.PAGE_SIZE);

        List<UserPromotion> assetCategoryList = userPromotionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(assetCategoryList)) {
            return null;
        }
        for (UserPromotion userPromotion : assetCategoryList) {

            userPromotion.setCreateTime(DateUtility.dateToStr(userPromotion.getCreateon()));
        }
        PageInfo<UserPromotion> page = new PageInfo<UserPromotion>(assetCategoryList);
        return page;
    }

    public Long promotionNextVal() {
        return RedisUtil.incrBy(BDConstants.PROMOTION_NEXTVAL, 1);
    }
}
