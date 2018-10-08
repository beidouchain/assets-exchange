package com.beidou.exchange.service.impl;

import com.beidou.exchange.service.CommonGenericService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


public class CommonGenericServiceImpl<T> implements CommonGenericService<T> {

    @Autowired
    protected Mapper<T> mapper;

    @Override
    public int insert(T t) {
        return mapper.insertSelective(t);
    }

    @Override
    public int delete(T t) {
        return mapper.delete(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return mapper.updateByPrimaryKey(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int updateByExample(T t, Example e) {
        return mapper.updateByExample(t, e);
    }

    @Override
    public int updateByExampleSelective(T t, Example e) {
        return mapper.updateByExampleSelective(t, e);
    }

    @Override
    public T selectOne(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public List<T> select() {
        return mapper.selectAll();
    }

    @Override
    public List<T> select(T t) {
        return mapper.select(t);
    }

    @Override
    public List<T> selectByExample(Example e) {
        return mapper.selectByExample(e);
    }

    @Override
    public T selectByPrimaryKey(Long pk) {
        return mapper.selectByPrimaryKey(pk);
    }

    @Override
    public PageInfo<T> selectPage(int pageNum, int pageSize, Example e) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> lst = mapper.selectByExample(e);
        PageInfo<T> pi = new PageInfo<>(lst);
        return pi;
    }

    @Override
    public int selectCountByExample(Example e) {
        return mapper.selectCountByExample(e);
    }

    @Override
    public int selectCount(T t) {
        return mapper.selectCount(t);
    }

    @Override
    public int deleteByExample(Example e) {
        return mapper.deleteByExample(e);
    }

    @Override
    public int deleteByPrimaryKey(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

}