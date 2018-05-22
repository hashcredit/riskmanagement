package com.newdumai.setting.score.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.config.UrlConfig;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.setting.score.ScoreService;
import com.newdumai.util.HttpClientUtil;
import com.newdumai.util.JsonToMap;

@Service("scoreService")
public class ScoreServiceImpl extends BaseServiceImpl implements ScoreService {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> conditionMap = getCondition(map);
		
		String condition = (String) conditionMap.get("condition");
		
		return listPageBase(conditionMap,genCountSql(condition),genListSql(condition,getLimitUseAtSelectPage(map)));
		
	}
	private String genCountSql(String condition) {
		return "SELECT count(*) FROM sys_score " + condition;
	}
	private String genListSql(String condition, String limit) {
		return "SELECT * FROM sys_score where 1=1 " + condition + limit;
	}

	public Map<String, Object> getCondition(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
	
	@Override
	public boolean add(Map<String, Object> scoreMap) {
		add(scoreMap, "sys_score");
		return true;
	}
	@Override
	public boolean delete(Map<String, Object> map) {
		mysqlSpringJdbcBaseDao.delete("delete from sys_score where code=?",map.get("code"));
		return true;
	}
	
	@Override
	public boolean update(Map<String, Object> map) {
		Map<String,Object> where = new HashMap<String, Object>();
		where.put("code", map.remove(("code")));
		Update(map, "sys_score", where);
		return true;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDmSources() {
		String url = UrlConfig.getDm_source_interface_url()+"getDmSources.do";
		String result = HttpClientUtil.exec(url, "0", "0", null);
		Map<String ,Object> data = JsonToMap.jsonToMap(result) ;
		return (List<Map<String, Object>>) data.get("data");
		//return mysqlSpringJdbcBaseDao.queryForList("select * from dm_source where is_able='1'");
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDmSourceOutParas(String dm_source_code) {
		String url = UrlConfig.getDm_source_interface_url()+"getDmSourceOutParas.do";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dm_source_code", dm_source_code);
		String result = HttpClientUtil.exec(url, "1", "1", params);
		Map<String ,Object> data = JsonToMap.jsonToMap(result) ;
		return (List<Map<String, Object>>) data.get("data");
		//return mysqlSpringJdbcBaseDao.queryForList("select * from dm_source_para  where type='1' and name <> '--' and dm_source_code=?",dm_source_code);
	}
	
	
	
	
	
	
	
}
