package com.newdumai.sysmgr;

import java.util.List;
import java.util.Map;

import com.newdumai.global.vo.LoginVo;

/**
 * 功能设定service
 *
 * @author 岳晓
 */
public interface BizFunctionSettingsService {

    /**
     * 通过subEntityId获取功能配置数据，包含IP白名单和功能设置参数配置
     *
     * @param subEntityId
     * @return
     */
    public Map<String, Object> getFunctionSettingsBySubentityId(String subEntityId, String typeCode);

    /**
     * 通过subEntityId更新功能配置数据，包含IP白名单和功能设定参数配置
     *
     * @param updateParam
     * @param code
     * @return
     */
    public boolean update(Map<String, Object> updateParam, String code);

    /**
     * 通过功能key值判断是否有指定功能，功能设定参数来自LoginVo
     *
     * @param functionkey
     * @param functionkey
     * @return
     */
    public boolean hasFunctions(LoginVo login, String functionkey, String typeCode);

    /**
     * 通过功能key值判断是否有指定功能，功能设定参数来自subEntityI获取的商户信息
     *
     * @param subEntityId
     * @param functionkey
     * @return
     */
    public boolean hasFunctions(String subEntityId, String functionkey, String typeCode);

    /**
     * 通过功能key值判断是否有指定功能，功能设定参数来自参数fucntionSettingsString
     *
     * @param fucntionSettingsMap
     * @param functionkey
     * @param typeCode
     * @return
     */
    boolean hasFunctionsByFucntionSettings(Map<String, Map<String, Object>> fucntionSettingsMap, String functionkey, String typeCode);

    public Map<String, Map<String, Object>> getAsMapBySubentityId(String subEntityId);

	public List<Map<String, Object>> getViewDataSettings(String sub_entity_id, String typeCode);

	public boolean saveViewDataSettings(String sub_entity_id, String typeCode, String codes);

    /**
     * 新版业务类型规则模型选择
     *
     * @param subEntityId
     * @param type_code
     * @param rule_model
     * @return
     */
    int updateRuleOrModel(String subEntityId, String type_code, String rule_model);
}
