package com.newdumai.ht.auditing.dh.item;

import java.util.Map;

import com.newdumai.global.service.BaseService;

/**
 * 电核项配置Service
 *
 * @author zgl
 * @datetime Dec 27, 2016 10:42:27 AM
 */
public interface DhItemService extends BaseService {

    /**
     * 增加模型集的电核项
     *
     * @param params
     * @return
     */
    boolean addItems(Map<String, Object> params);

    /**
     * 查询指定模型下未设置电核的评分项
     *
     * @param map
     * @return
     * @zgl Dec 28, 2016 5:42:50 PM
     */
    public String getManagerItem(Map<String, Object> map);

    /**
     * 查询规则集下的规则已配置的电核项
     *
     * @param sys_rule_group_code
     * @return
     */
    String getRuleGroupDhItem(String sys_rule_group_code);

    /**
     * 查询规则集下未配置电核项的规则
     *
     * @param sys_rule_group_code
     * @return
     */
    String unAddDhItemRules(String sys_rule_group_code);

    /**
     * 增加规则集的电核项
     *
     * @param params
     * @return
     */
    boolean addRuleItems(Map<String, Object> params);
}
