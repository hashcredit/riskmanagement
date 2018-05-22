package com.newdumai.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.system.MenuService;

@Service("menuService")
public class MenuServiceImpl extends BaseServiceImpl implements MenuService {

	@Override
	public List<Map<String, Object>> getMenuById(String id) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql2(gen_getMenuById(id));
	}
	
	@Override
	public List<Map<String, Object>> getChildrenById(String pid) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql2(gen_getChildrenById(pid));
	}

	private String gen_getMenuById(String id) {
		if(id==null || "".equals(id)){
			return "SELECT * FROM sys_code WHERE code_ID='1'";
		}else{
			return "SELECT * FROM sys_code WHERE code_ID='"+id+"'";
		}
	}
	private String gen_getChildrenById(String pid) {
		if(pid==null || "".equals(pid)){
			return "SELECT * FROM sys_code WHERE code_SUPERIOR='1'";
		}else{
			return "SELECT * FROM sys_code WHERE code_SUPERIOR='"+pid+"'";
		}
	}

}
