package com.newdumai.ht.manager.rule.rule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.rule.rule.RuleService;


@Service("ruleService")
public class RuleServiceImpl extends BaseServiceImpl implements RuleService {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
		
	}

	@Override
	public String ruleTypeInit() {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_type WHERE is_able='1'");
	}

	@Override
	public String ruleGroupInit(String sys_type_code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_rule_group WHERE is_able='1' and sys_type_code='"+sys_type_code+"'");
	}

	@Override
	public void add_rule(Map<String, Object> para) {
		addAndRet(para,"fk_guize");
	}

	@Override
	public String toUpdate(String code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(gen_toUpdate(code));
	}

	@Override
	public void upadte_rule(Map<String, Object> para) {
		Map<String, Object> where=new HashMap<String, Object>();
		String code = (String) para.remove("code");
		where.put("code", code);
		Update(para, "fk_guize", where);

		String description = (String) para.get("description");
		String sql = "update dh_item set dh_description = ? where manager_item_code = ? ";
		mysqlSpringJdbcBaseDao.update(sql,description,code);
	}

	@Override
	public String getSys_interface_company() {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_interface_company");
	}

	@Override
	public String getSys_interface(String sys_interface_company_code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_interface_source where sys_interface_company_code='"+sys_interface_company_code+ "'");
	}

	@Override
	public String getSys_interface_source_para(String Interface_source_code,String inOrOut) {
		if(StringUtils.isEmpty(inOrOut)){
			return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_interface_source_para where  Interface_source_code='"+Interface_source_code+ "'");
		}else if("in".equals(inOrOut)){
			return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_interface_source_para where `type`='0' and Interface_source_code='"+Interface_source_code+ "'");
		}else{
			return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_interface_source_para where `type`='1' and Interface_source_code='"+Interface_source_code+ "'");
		}
	}

	@Override
	public void add_logs(String guizeCode, String logs,String interfaces) {
		String guize = mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM fk_guiz_logs WHERE fk_guiz_logs.fk_guize_code='"+guizeCode+ "'");
		Map<String, Object> para=new HashMap<String, Object>();
		para.put("logs", logs);
		para.put("interfaces", interfaces);
		if("[]".equals(guize)){
			para.put("fk_guize_code", guizeCode);
			addAndRet(para,"fk_guiz_logs");
		}else{
			Map<String, Object> where=new HashMap<String, Object>();
			where.put("fk_guize_code", guizeCode);
			Update(para, "fk_guiz_logs", where);
		}
	}
	
	@Override
	public String getLogData(String guizeCode) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM fk_guiz_logs WHERE fk_guize_code ='"+guizeCode+ "'");
	}


	
	/**
	 * 
	 */


	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM fk_guize WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT * FROM fk_guize WHERE 1=1 "+condition+limit;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		String name = (String) map.get("name");
		if(!StringUtils.isEmpty(name)){
			sb.append(" AND name like ? ");
			list.add("%" + name + "%");
		}
		String is_able = (String) map.get("is_able");
		if(!StringUtils.isEmpty(is_able)){
			sb.append(" AND is_able=? ");
			list.add(is_able);
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
	private String gen_toUpdate(String code) {
		return "SELECT * FROM fk_guize WHERE code='"+code+"'";
	}

}
