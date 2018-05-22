package com.newdumai.log.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.log.LogService;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl implements LogService{

	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getListConditiont(map);
		return listPageBase(condition,genCountSql(condition.get("condition").toString()),genListSql(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
	}

	private Map<String, Object> getListConditiont(Map<String, Object> map) {
		Map<String, Object> condition = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		String sub_entity_id = (String) map.get("sub_entity_id");
		if(!StringUtils.isEmpty(sub_entity_id)){
			sb.append(" AND sub_entity_id = ? ");
			list.add(sub_entity_id);
		}
		condition.put("condition", sb.toString());
		condition.put("args", list.toArray());
		return condition;
	}

	private String genListSql(String condition,String limit) {
		return "SELECT * FROM sys_log WHERE 1=1 "+condition + " order by logtime desc " +limit;
	}
	private String genCountSql(String condition) {
		return "SELECT count(*) FROM sys_log WHERE 1=1 "+condition;
		
	}

	@Override
	public String list(Map<String, Object> map, String subEntityId) {
		Map<String, Object> condition = getListConditiont(map,subEntityId);
		return listPageBase(condition,genCountSql(condition.get("condition").toString()),genListSql(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
	}
	
	private Map<String, Object> getListConditiont(Map<String, Object> map,String subEntityId) {
		Map<String, Object> condition = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append(" and sub_entity_id=? ");
		list.add(subEntityId);
		condition.put("condition", sb.toString());
		condition.put("args", list.toArray());
		return condition;
	}
	
	
}
