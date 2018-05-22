package com.newdumai.setting.interface_source.para.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.setting.interface_source.para.Interface_sourceService_para;


@Service("interface_sourceService_para")
public class Interface_sourceService_paraImpl extends BaseServiceImpl implements Interface_sourceService_para {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
		
	}

	@Override
	public int add_Interface_sourceService_para(Map<String, Object> para) {
		return add(para,"sys_interface_source_para");
	}

	@Override
	public String para_toUpdate(String interface_source_code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(gen_para_toUpdate(interface_source_code));
	}

	@Override
	public int upadte_Interface_sourceService_para(Map<String, Object> para) {
		Map<String, Object> where=new HashMap<String, Object>();
		where.put("code", para.get("code"));
		para.remove("code");
		return Update(para, "sys_interface_source_para", where);
	}
	
	
	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM sys_interface_source_para WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT * FROM sys_interface_source_para WHERE 1=1 "+condition+limit;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		
		String code = (String)map.get("code");
		
		sb.append(" and interface_source_code=?");
		
		list.add(code);
		
		
//		String filter_headtype = (String) map.get("filter_headtype");
//		if(!StringUtils.isEmpty(filter_headtype)){
//			sb.append(" AND filter_headtype=? ");
//			list.add(filter_headtype);
//		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	private String gen_para_toUpdate(String interface_source_code) {
		return "SELECT * FROM sys_interface_source_para WHERE code='"+interface_source_code+"'";
	}
	
//	@SuppressWarnings("unused")
//	private String gen_para_update(String interface_source_code) {
//		return "SELECT * FROM sys_interface_source_para WHERE code='"+interface_source_code+"'";
//	}

}
