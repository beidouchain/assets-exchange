package com.beidou.blockchain.service;

import com.beidou.blockchain.vo.*;

import java.util.List;

/**
 * Created by fengguoqing on 2018/6/20.
 */
public interface AtomicService {
    /**
     * 原子交易
     * @param atomicVO
     * @return
     */
    public String atomicExchange(AtomicExchangeVO atomicVO);

}
