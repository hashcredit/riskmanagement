package com.newdumai.global.dao;

import java.util.List;
import java.util.Map;

public interface Dumai_sourceBaseDao {
	public void executeSql(String sql);
	public String executeSelectSql(String sql);
	
	public List<Map<String, Object>> executeSelectSql2(String sql);
	public List<Map<String, Object>> queryForList(String sql, Object... args);

	public Map<String, Object> queryForMap(String sql, Object... args);
	
	public int executeSelectSqlInt(String sql,Object... args);
	public int update(String sql, Object... args);
	public int insert(String sql, Object... args);
	public int delete(String sql, Object... args);
	
	public int[] batchInsert(String sql, Object[]... args);
}
