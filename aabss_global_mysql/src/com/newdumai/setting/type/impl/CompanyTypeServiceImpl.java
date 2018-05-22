package com.newdumai.setting.type.impl;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.setting.type.CompanyTypeService;
import org.springframework.util.CollectionUtils;

@Service("companyTypeService")
public class CompanyTypeServiceImpl extends BaseServiceImpl implements CompanyTypeService{

	private final String COMPANY_TYPE_STATUS_ABLE = "able";
	private final String COMPANY_TYPE_STATUS_DISABLE = "disable";
	
	@Override
	public Map<String, Object> getList(Map<String, Object> params) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> condition = new ArrayList<Object>();
		condition.add(params.get("type"));
		String sql = "SELECT c.`code`,c.`sub_entity_id`,c.`name`,ct.is_able,ct.opttime FROM company_order c LEFT JOIN sys_company_type ct ON c.sub_entity_id=ct.sub_entity_id AND ct.type_code=? WHERE c.status = 1 ";
		String sqlTotal = "SELECT COUNT(c.id) FROM company_order c LEFT JOIN sys_company_type ct ON c.sub_entity_id=ct.sub_entity_id AND ct.type_code=? WHERE c.status = 1 ";
		String where = "";
		if(COMPANY_TYPE_STATUS_ABLE.equals(params.get("status"))){
			where = " AND ct.opttime IS NOT NULL ";
		}else if(COMPANY_TYPE_STATUS_DISABLE.equals(params.get("status"))){
			where = " AND (ct.opttime IS NULL OR ct.is_able='0') ";
		}
		
		if(!StringUtils.isEmpty((String) params.get("companyName"))){
			where = where + " AND c.`name` LIKE ? ";
			condition.add("%" + params.get("companyName") + "%");
		}
		sql = sql + where;
		sqlTotal = sqlTotal + where;
		
		data.put("total", mysqlSpringJdbcBaseDao.executeSelectSqlInt(sqlTotal,condition.toArray()));
		data.put("rows", mysqlSpringJdbcBaseDao.queryForList(sql, condition.toArray()));
		
		return data;
	}

	@Override
	public List<Map<String, Object>> getTypeAll() {
		
		return super.mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_type WHERE is_able = '1'");
	}

	@Override
	public void enable(Map<String, Object> companyType) {
		String sub_entity_id = (String) companyType.get("sub_entity_id");
		String type_code = (String) companyType.get("type_code");
		Map<String, Object> sys_company_typeMap = super.mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_company_type WHERE sub_entity_id = ? and type_code = ?", sub_entity_id, type_code);
		if (CollectionUtils.isEmpty(sys_company_typeMap)) {
			super.add(companyType, "sys_company_type");
		} else {
			super.mysqlSpringJdbcBaseDao.executeSql("update sys_company_type set is_able = '1' WHERE sub_entity_id = '" + sub_entity_id + "' and type_code = '" + type_code + "'");
		}
	}

	@Override
	public void disable(Map<String, Object> params) {
		String sql = "update sys_company_type set is_able = '0' where sub_entity_id=? and type_code=?";
		super.mysqlSpringJdbcBaseDao.update(sql, params.get("sub_entity_id"), params.get("type_code"));
	}

}
