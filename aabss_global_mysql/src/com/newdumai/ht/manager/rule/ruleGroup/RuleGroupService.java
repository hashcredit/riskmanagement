package com.newdumai.ht.manager.rule.ruleGroup;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;
import com.newdumai.global.vo.Page;
import com.newdumai.global.vo.PageConfig;

public interface RuleGroupService extends BaseService {
	public String list(Map<String, Object> para);

	public List<Map<String,Object>> findRulesOfRuleGroup(String ruleGroupCode);

	Page<Map<String, Object>> findRuleListNotOfRuleGroup(String ruleGroupCode,
			PageConfig config);

	public boolean addRulesToRuleGroup(String ruleGroupCode,String[] ruleCodes);
	public boolean createRuleToRuleGroup(String ruleGroupCode,Map<String,Object> rule);

	public boolean removeRuleFromRuleGroup(String ruleGroupCode,
			String ruleCode);

	public boolean addRuleGroup(Map<String, Object> request2Map);

	public boolean deleteRuleGroup(String code);
	
	public List<Map<String, Object>> getHeadtype();

	public boolean updateRuleGroup(Map<String, Object> map);

	/**
	 * 根据code查询信息
	 *
	 * @param code
	 * @return
	 */
    Map<String,Object> getByCode(String code);

	/**
	 * 根据规则集code查询规则
	 *
	 * @param groupCode
	 * @return
	 */
	String getGroupRules(String groupCode);

	/**
	 * 尚未添加到规则集的规则列表
	 *
	 * @param groupCode
	 * @return
	 */
	String getUnAddGroupRules(String groupCode);
}
