package com.newdumai.global.service;

import java.util.Map;


public interface BaseService {
	public String listAll(String tableName,Map<String, Object> map);
	public String list(String tableName,Map<String, Object> request2Map);
	public boolean saveOrUpdate(String tableName,Map<String, Object> map);
	public int optIsAble(String tableName, String code);
	public String findByPara(String tableName,String para);
	public void delete(String tableName,String code);
	
//	public void add(String sql);
//	public void delete(String sql);
//	public void update(String tableName,Map<String,String> map, int id);
//	public void update(String sql);
//	public void exec(String sql);
//	public String find(String tableName);
//	public String findBySql(String sql);
//	public String findByPage(String tableName,String para,PageData p);
//	public PageData findByPage3(String tableName,String para,PageData p);
//	public List<Map<String, Object>> findByPage2(String tableName,String para,PageData p);
//	public PageData findByPageCount2(String tableName,String para,PageData p);
//	public int findByPageCount(String tableName,String para,PageData p);
//	public int Update(Map<String, Object> para,String tableName,Map<String, Object> where);
	
	public void add(String tableName,Map<String,String> map);
	public void update(String tableName,Map<String,String> map, String code);
	public int del_disable(String tableName,String code);}
