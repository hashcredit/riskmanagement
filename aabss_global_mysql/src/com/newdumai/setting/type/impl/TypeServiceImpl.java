package com.newdumai.setting.type.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.setting.type.TypeService;


@Service("typeService")
public class TypeServiceImpl extends BaseServiceImpl implements TypeService {

	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
		
	}
	
	@Override
	public List<Map<String,Object>> getType() {
		String sql = "select * from sys_type where is_able = '1' ";
		return mysqlSpringJdbcBaseDao.queryForList(sql);
		
	}

	@Override
	public List<Map<String,Object>> getTypesBySubEntityId(String sub_entity_id) {
		String sql = "select t.* from sys_company_type ct join sys_type t on ct.type_code = t.code  where t.is_able = '1' and ct.is_able = '1' and ct.sub_entity_id = ? ";
		return mysqlSpringJdbcBaseDao.queryForList(sql, sub_entity_id);
	}

	@Override
	public List<Map<String, Object>> comboList() {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql2("SELECT * FROM sys_type ");
	}

	@Override
	public int addType(Map<String, Object> params) {
		return super.add(params, "sys_type");
	}

	@Override
	public void deleteType(String code) {
		super.delete("DELETE FROM sys_type WHERE `code` = '" + code + "'");
	}

	@Override
	public int updateType(Map<String, Object> params, Map<String, Object> where) {
		return super.Update(params, "sys_type", where);
	}
	
	/*@Override
	public List<Map<String, Object>> getChildType(String code) {
		
		return super.mysqlSpringJdbcBaseDao.executeSelectSql2("SELECT * FROM sys_type WHERE code_superior = '"+code+"'");
	}*/

	/**
	 * 
	 */

	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM sys_type WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT * FROM sys_type WHERE 1=1 "+condition+limit;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
//		String filter_headtype = (String) map.get("filter_headtype");
//		if(!StringUtils.isEmpty(filter_headtype)){
//			sb.append(" AND filter_headtype=? ");
//			list.add(filter_headtype);
//		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
}
