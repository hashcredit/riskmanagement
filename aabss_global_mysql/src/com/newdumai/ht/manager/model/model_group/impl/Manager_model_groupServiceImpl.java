package com.newdumai.ht.manager.model.model_group.impl;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.model.model_group.Manager_model_groupService;

@Service("manager_model_groupService")
public class Manager_model_groupServiceImpl extends BaseServiceImpl implements Manager_model_groupService {

	/* block list begin */
	public String genCountSql(String tableName, String condition) {
		return "SELECT count(*) FROM manager_model_group AS a INNER JOIN sys_type AS b ON a.sys_type_code = b.`code` WHERE 1 = 1 "	+ condition;
	}

	public String genListSql(String tableName, String condition, String limit) {
		return "SELECT a.*, b.`name` AS typeName FROM manager_model_group AS a INNER JOIN sys_type AS b ON a.sys_type_code = b.`code` WHERE 1 = 1 " + condition + limit;
	}

	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String sys_type_code = (String) map.get("sys_type_code");
		if (StringUtils.isNotEmpty(sys_type_code)) {
			list.add(sys_type_code);
			sb.append(" and b.code = ? ");
		}
		String name = (String) map.get("name");
		String is_able = (String) map.get("is_able");
		if (StringUtils.isNotEmpty(name)) {
			list.add("%" + name + "%");
			sb.append(" and a.name like ? ");
		}
		if (StringUtils.isNotEmpty(is_able)) {
			list.add(is_able);
			sb.append(" and a.is_able=? ");
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	/* block list end */

	@Override
	public String getByCode(String code) {
		String sql = "SELECT * FROM manager_model_group WHERE code='" + code + "'";
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(sql);
	}

	@Override
	public List<Map<String, Object>> getCompanyTypeModel_group(String model_group_code) {
		String sql = "SELECT CONCAT(company_order.`name`,'_',sys_type.`name`,'_',manager_model_group.`name`) name,manager_model_group.`code` code FROM company_order INNER JOIN manager_model_group ON manager_model_group.sub_entity_id = company_order.sub_entity_id INNER JOIN sys_type ON manager_model_group.sys_type_code = sys_type.`code` ";
		if (StringUtils.isNotEmpty(model_group_code)) {
			sql += " where manager_model_group.code='" + model_group_code + "'";
		}
		return mysqlSpringJdbcBaseDao.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getBySys_type_code(String sys_type_code) {
		return mysqlSpringJdbcBaseDao.queryForList("select * from manager_model_group where 1=1 and sys_type_code = '"+sys_type_code+"'");
	}

	@Override
	public List<Map<String, Object>> getGroupToModel(String groupCode) {
		String sql = "SELECT a.* FROM manager_model AS a INNER JOIN manager_model__manager_model_group AS b ON b.manager_model_code = a.`code` WHERE a.is_able='1' and b.manager_model_group_code = ?";
		return mysqlSpringJdbcBaseDao.queryForList(sql, groupCode);
	}

	@Override
	public void addModels(String groupCode, String[] modelCodes) {
		mysqlSpringJdbcBaseDao.delete("delete from manager_model__manager_model_group where manager_model_group_code= ?", groupCode);
		Object[][] args = new Object[modelCodes.length][];
		for (int i = 0; i < args.length; i++) {
			Object[] arg = new Object[3];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = modelCodes[i];
			arg[2] = groupCode;
			args[i] = arg;
		}
		mysqlSpringJdbcBaseDao.batchInsert("insert into manager_model__manager_model_group(code,manager_model_code,manager_model_group_code) values(?,?,?)", args);
	}
}
