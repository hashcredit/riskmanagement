package com.newdumai.ht.auditing.dh.item.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.auditing.dh.item.DhItemService;
import com.newdumai.util.JsonToMap;

@Service("dhItemService")
public class DhItemServiceImpl extends BaseServiceImpl implements DhItemService {

	public String genCountSql(String tableName, String condition) {
		String sql = "SELECT count(*) FROM dh_item AS a INNER JOIN manager_item AS b ON a.manager_item_code = b.`code` "
				+ "INNER JOIN manager_model__manager_item AS c ON c.manager_item_code = b.`code` WHERE 1=1 and a.dh_source_type='2' " + condition;
		return sql;
	}

	public String genListSql(String tableName, String condition, String limit) {
		String sql = "SELECT a.*,b.`name` FROM dh_item AS a INNER JOIN manager_item AS b ON a.manager_item_code = b.`code` "
				+ "INNER JOIN manager_model__manager_item AS c ON c.manager_item_code = b.`code` WHERE 1=1 and a.dh_source_type='2'" + condition + limit;
		return sql;
	}

	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String manager_model_code = (String) map.get("manager_model_code");
		if (StringUtils.isNotEmpty(manager_model_code)) {
			sb.append(" AND c.manager_model_code=? ");
			list.add(manager_model_code);
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	@Override
	public boolean addItems(Map<String, Object> params) {
		String model_code = (String) params.get("model_code");
		String sql = "SELECT a.*,b.seperate_box FROM manager_item AS a INNER JOIN manager_model__manager_item AS b ON b.manager_item_code = a.`code` WHERE b.manager_model_code = ? and a.code = ?";
		List<String> item_codes = (List<String>) params.get("item_codes");
		Object[][] args = new Object[item_codes.size()][];
		for (int i = 0; i < args.length; i++) {
			Object[] arg = new Object[5];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = item_codes.get(i);
			Map<String, Object> resultMap = mysqlSpringJdbcBaseDao.queryForMap(sql, model_code, arg[1]);
			String seperate_box = (String) resultMap.get("seperate_box");
			arg[2] = resultMap.get("name");
			arg[3] = this.convert(seperate_box);
			arg[4] = resultMap.get("descripe");
			args[i] = arg;
		}
		mysqlSpringJdbcBaseDao.batchInsert("insert into dh_item (code,manager_item_code,manager_item_name,dh_content,dh_description,dh_source_type) values(?,?,?,?,?,'2')", args);
		return true;
	}

	// 转换分箱条件为电核内容
	private String convert(String seperate_box) {
		StringBuilder result = new StringBuilder();
		if (StringUtils.isNotEmpty(seperate_box)) {
			List<Map<String, Object>> listMap = JsonToMap.gson2List(seperate_box);
			Map<String, String> optMap = new HashMap<String, String>();
			optMap.put("=", "等于");
			optMap.put("!=", "不等于");
			optMap.put(">", "大于");
			optMap.put(">=", "大于等于");
			optMap.put("<", "小于");
			optMap.put("<=", "小于等于");
			optMap.put("contains", "包含");
			optMap.put("!contains", "不包含");
			optMap.put("other", "其它");
			optMap.put("null", "无结果");
			for (Map<String, Object> map : listMap) {
				String opt = (String) map.get("opt");
				Object value = map.get("value");
				if (null != value) {
                    if (value instanceof Number) {
                        value = ((Number) value).intValue();
                    }
                    if (StringUtils.isEmpty(result.toString())) {
						result.append(optMap.get(opt) + value);
					} else {
						result.append("_").append(optMap.get(opt) + value);
					}
				}
			}
		}
		return result.toString();
	}

	@Override
	public String getManagerItem(Map<String, Object> map) {
		String str = "SELECT a.* FROM manager_item AS a , manager_model__manager_item AS b WHERE a.`code`= b.manager_item_code AND a.is_able='1' AND b.seperate_box!='' AND a.`code` not in (select manager_item_code from dh_item ) AND b.manager_model_code = '"
				+ map.get("manager_model_code") + "' ";
		return new Gson().toJson(super.mysqlSpringJdbcBaseDao.queryForList(str));
	}

	@Override
	public String getRuleGroupDhItem(String sys_rule_group_code) {
		Map<String, Object> data = new HashMap<String, Object>();
		String sqlTotal = "SELECT count(*) FROM dh_item AS a INNER JOIN fk_guize AS b ON a.manager_item_code = b.`code` INNER JOIN sys_rule_group__fk_guize AS c ON c.fk_guize_code = b.`code` " +
				"INNER JOIN sys_rule_group AS d ON d.code = c.sys_rule_group_code " +
				"WHERE 1 = 1 and b.is_able='1' and d.is_able='1' and a.dh_source_type='1' and c.sys_rule_group_code = ?";
		String sql = "SELECT a.*, b.`name` FROM dh_item AS a " +
				"INNER JOIN fk_guize AS b ON a.manager_item_code = b.`code` " +
				"INNER JOIN sys_rule_group__fk_guize AS c ON c.fk_guize_code = b.`code` " +
				"INNER JOIN sys_rule_group AS d ON d.code = c.sys_rule_group_code " +
				"WHERE 1 = 1 and b.is_able='1' and d.is_able='1' and a.dh_source_type='1' and c.sys_rule_group_code = ?";
		data.put("total", mysqlSpringJdbcBaseDao.executeSelectSqlInt(sqlTotal, sys_rule_group_code));
		data.put("rows", mysqlSpringJdbcBaseDao.queryForList(sql, sys_rule_group_code));
		return new Gson().toJson(data);
	}

	@Override
	public String unAddDhItemRules(String sys_rule_group_code) {
		String sql = "select a.* from fk_guize a, sys_rule_group__fk_guize b,sys_rule_group c where a.code = b.fk_guize_code and c.code=b.sys_rule_group_code " +
				" and a.is_able='1' and c.is_able='1'and a.code not in (select manager_item_code FROM dh_item where dh_source_type = '1') and b.sys_rule_group_code = ?";
		return new Gson().toJson(mysqlSpringJdbcBaseDao.queryForList(sql, sys_rule_group_code));
	}

	@Override
	public boolean addRuleItems(Map<String, Object> params) {
		String ruleGroupCode = (String) params.get("ruleGroupCode");
		List<List<String>> ruleData = (List<List<String>>) params.get("ruleData");
		Object[][] args = new Object[ruleData.size()][];
		for (int i = 0; i < ruleData.size(); i++) {
			System.out.println(ruleData.get(i));
			List<String> rule = ruleData.get(i);
			Object[] arg = new Object[5];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = rule.get(0);
			arg[2] = rule.get(1);
			arg[3] = rule.get(2);
			arg[4] = rule.get(3);
			args[i] = arg;
		}
		mysqlSpringJdbcBaseDao.batchInsert("insert into dh_item (code,manager_item_code,manager_item_name,dh_content,dh_description,dh_source_type,dh_type) values(?,?,?,?,?,'1','1')", args);
		return true;
	}
}