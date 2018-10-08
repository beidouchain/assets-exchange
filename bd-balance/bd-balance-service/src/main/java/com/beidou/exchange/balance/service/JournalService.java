package com.beidou.exchange.balance.service;

import com.beidou.exchange.balance.entity.Journal;
import com.beidou.exchange.service.CommonGenericService;

import java.util.List;

public interface JournalService extends CommonGenericService<Journal> {

    int createJournal(Journal journal);

    /**
     * 查询交易历史list
     *
     * @param journal
     * @return
     */
    List<Journal> list(Journal journal);
}
