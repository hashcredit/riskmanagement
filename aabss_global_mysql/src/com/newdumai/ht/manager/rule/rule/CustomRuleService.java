package com.newdumai.ht.manager.rule.rule;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;
import com.newdumai.global.vo.Page;
import com.newdumai.global.vo.PageConfig;

public interface CustomRuleService extends BaseService {

	/**
	 * 分页查询
	 * @param config 分页配置
	 * @param map 查询条件
	 * @return
	 */
	public Page<Map<String,Object>> findAsPage(PageConfig config, Map<String, Object> map);


	/**
	 * 停用规则，本质是删除与规则集下规则的关联
	 * @param fk_guize_code
	 * @param sys_company_type_code
	 * @param rule_model:1          规则，2模型--对应所选的规则
	 * @return
	 */
	public boolean disableRule(String fk_guize_code, String sys_company_type_code, String rule_model);

    /**
     * 启用规则，本质是添加与规则集下规则的关联
     *
     * @param fk_guize_code
     * @param sys_company_type_code
     * @param rule_model:1          规则，2模型--对应所选的规则
     * @return
     */
    public boolean enableRule(String fk_guize_code, String sys_company_type_code, String rule_model);

	/**
	 * 通过subEntityId获取启用了规则功能的业务类型列表
	 * @param subEntityId
	 * @return
	 */
//	public List<Map<String, Object>> getRuleEnabledTypesBySubEntityId(String subEntityId);

	/**
	 * 根据业务类型获取规则集
	 * @param typeCode 业务类型code
	 * @return
	 */
	public List<Map<String, Object>> getRuleGroupsByTypeCode(String typeCode);


	/**
	 * 获取全部规则集
	 * @return
	 */
	public List<Map<String, Object>> getRuleGroups();

	/**
	 * 获取商户下所有业务类型的规则集
	 * @return
	 */
	List<Map<String, Object>> getRuleGroupsOfCompany(String subEntityId);

	/**
	 * 一般用户-贷前审核-规则设定：根据规则集code查询规则，并标记哪些规则商户已启用
	 *
	 * @param map
	 * @return
	 */
	String getGroupRules(Map<String, Object> map);

	/**
	 * 前台规则全部启停用
	 *
	 * @param map
	 * @return
	 */
    String modifyAllRules(Map<String, Object> map);
}
