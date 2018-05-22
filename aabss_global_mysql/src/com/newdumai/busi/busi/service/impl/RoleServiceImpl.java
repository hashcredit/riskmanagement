package com.newdumai.busi.busi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.busi.busi.service.RoleService;
import com.newdumai.global.service.impl.BaseServiceImpl;


@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {
	@Override
	public String list(String pid) {
		
		List<Map<String, Object>> data = super.mysqlSpringJdbcBaseDao.executeSelectSql2(gen_list("101"));
		List<Map<String, Object>> data2 = super.mysqlSpringJdbcBaseDao.executeSelectSql2(gen_list("102"));
		Map<String, Object> d = new HashMap<String, Object>();
//		d.put("101", data);
//		d.put("102", data2);
		d.put("1", data2);
//		return new Gson().toJson(d);
		return new Gson().toJson(data);
	}


	private String gen_list(String parent) {
		if("101".equals(parent)){
			return parent="select * from treeNodes where FIND_IN_SET(id, getChildLst(0));";
//			return parent="select code_ID id,code_NAME text from sys_code where 1=1  and code_SUPERIOR='101'";
		}else{
			return parent="select code_ID id,code_NAME text from sys_code where 1=1  and code_SUPERIOR='102'";
//			return parent="select code_ID id,code_NAME text from sys_code where 1=1  and code_ID='102' or code_SUPERIOR='102'";
		}
	}
}
