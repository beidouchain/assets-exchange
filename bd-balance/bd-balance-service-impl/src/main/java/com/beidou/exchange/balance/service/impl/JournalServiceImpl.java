package com.beidou.exchange.balance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.beidou.exchange.balance.entity.Journal;
import com.beidou.exchange.balance.mapper.JournalMapper;
import com.beidou.exchange.balance.service.JournalService;
import com.beidou.exchange.service.impl.CommonGenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JournalServiceImpl extends CommonGenericServiceImpl<Journal> implements JournalService {

    @Autowired
    private JournalMapper journalMapper;

    @Override
    public int createJournal(Journal journal) {
        log.info("createJournal: " + JSONObject.toJSONString(journal));
        return insert(journal);
    }

    @Override
    public List<Journal> list(Journal journal) {
        List<String> status = new ArrayList<>();
        status.add("0");
        status.add("1");
        Example example = new Example(Journal.class);
        Example.Criteria criteria = example.createCriteria();
        //Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("asset", journal.getAsset())
                .andEqualTo("priceAsset", journal.getPriceAsset())
                .andEqualTo("uid", journal.getUid())
                .andEqualTo("type", 0).andIn("status", status);
        //criteria1.andEqualTo("asset", journal.getAsset())
        //        .andEqualTo("priceAsset", journal.getPriceAsset())
        //        .andEqualTo("counterpartUid", journal.getUid())
        //        .andEqualTo("type", 0).andIn("status", status);
        //example.or(criteria1);
        example.orderBy("updatedOn").desc();
        List<Journal> list = journalMapper.selectByExample(example);
        return list;
    }
}
