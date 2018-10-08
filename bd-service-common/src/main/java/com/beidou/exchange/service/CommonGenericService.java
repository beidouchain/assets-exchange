package com.beidou.exchange.service;

import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 基于实体的通用CRUD操作
 * 
 * @author zzj
 *
 * @param <T>
 */
public interface CommonGenericService<T> {
	int insert(T t);

	int delete(T t);

	int updateByPrimaryKey(T t);

	int updateByPrimaryKeySelective(T t);

	int updateByExample(T t, Example e);

	int updateByExampleSelective(T t, Example e);

	T selectOne(T t);

	List<T> select();

	List<T> select(T t);

	List<T> selectByExample(Example e);

	T selectByPrimaryKey(Long pk);

	public PageInfo<T> selectPage(int pageNum, int pageSize, Example e);

	int selectCount(T t);

	int selectCountByExample(Example e);

	int deleteByExample(Example e);

	int deleteByPrimaryKey(Object key);

}