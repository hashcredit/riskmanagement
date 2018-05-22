package com.newdumai.global.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.gson.Gson;
import com.newdumai.global.dao.Zichan_BaseDao;

/**
 * 
 * @author zfcqqqqa
 * 
 */

@Repository("zichan_BaseDao")
public class Zichan_BaseDaoImpl implements Zichan_BaseDao {

	@Autowired
	DruidDataSource dumai_zichan;

	public JdbcTemplate getTemplate() {
		return new JdbcTemplate(dumai_zichan);
	}

	@Override
	public void executeSql(String sql) {
		getTemplate().execute(sql);
	}

	@Override
	public String executeSelectSql(String sql) {
		return new Gson().toJson(getTemplate().queryForList(sql));
	}

	@Override
	public List<Map<String, Object>> executeSelectSql2(String sql) {
		return getTemplate().queryForList(sql);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int executeSelectSqlInt(String sql, Object... args) {
		return getTemplate().queryForInt(sql, args);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) {
		try {
			return getTemplate().queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		return getTemplate().queryForList(sql, args);
	}

	@Override
	public int update(String sql, Object... args) {
		return getTemplate().update(sql, args);
	}

	@Override
	public int insert(String sql, Object... args) {
		return getTemplate().update(sql, args);
	}

	@Override
	public int delete(String sql, Object... args) {
		return getTemplate().update(sql, args);
	}

	@Override
	public int[] batchInsert(String sql, Object[]... args) {
		List<Object[]> batchArgs = Arrays.asList(args);
		return getTemplate().batchUpdate(sql, batchArgs);
	}
}
