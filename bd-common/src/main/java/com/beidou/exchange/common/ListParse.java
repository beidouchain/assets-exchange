package com.beidou.exchange.common;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fengguoqing on 2018/6/19.
 */
public class ListParse {

    public static List<Object> parseMapToObject (List<Object> list,Class clazz) {
        List<Object> newList = new ArrayList<Object>();
        try {
            if (CollectionUtils.isNotEmpty(list)) {
                for (Object objs : list) {
                    Map<String,Object> obj = (Map<String,Object>)objs;
                    Object beanInstance = clazz.newInstance();
                    BeanUtils.populate(beanInstance,obj);
                    newList.add(beanInstance);
                }
                return newList;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
