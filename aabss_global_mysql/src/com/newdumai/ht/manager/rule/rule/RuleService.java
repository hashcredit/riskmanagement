package com.newdumai.ht.manager.rule.rule;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface RuleService extends BaseService {
	public String list(Map<String, Object> para);

	public String ruleTypeInit();

	public String ruleGroupInit(String sys_type_code);

	public void add_rule(Map<String, Object> para);

	public String toUpdate(String code);

	public void upadte_rule(Map<String, Object> para);

	public String getSys_interface_company();

	public String getSys_interface(String sys_interface_company_code);

	public void add_logs(String guizeCode, String logs,String interfaces);

	public String getLogData(String guizeCode);

	String getSys_interface_source_para(String Interface_source_code,String inOrOut);

}
