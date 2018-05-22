package com.newdumai.sysmgr;

import java.util.Map;

import com.newdumai.global.vo.LoginVo;

/**
 * 功能设定service
 * @author 岳晓
 *
 */
public interface FunctionSettingsService {
	
	/**
	 * 通过subEntityId获取功能配置数据，包含IP白名单和功能设置参数配置
	 * @param subEntityId
	 * @return
	 */
	public Map<String,Object> getFunctionSettingsBySubentityId(String subEntityId);
	
	/**
	 * 通过subEntityId更新功能配置数据，包含IP白名单和功能设定参数配置
	 * @param updateParam
	 * @param subEntityId
	 * @return
	 */
	public boolean updateBySubentityId(Map<String,Object> updateParam,String subEntityId);
	
	/**
	 * 通过功能key值判断是否有指定功能，功能设定参数来自LoginVo
	 * @param functionkey
	 * @param functionkey
	 * @return
	 */
	public boolean hasFunctions(LoginVo login,String functionkey);
	
	/**
	 * 通过功能key值判断是否有指定功能，功能设定参数来自subEntityI获取的商户信息
	 * @param subEntityId
	 * @param functionkey
	 * @return
	 */
	public boolean hasFunctions(String subEntityId,String functionkey);
	
	/**
	 * 通过功能key值判断是否有指定功能，功能设定参数来自参数fucntionSettingsString
	 * @param fucntionSettingsString
	 * @param functionkey
	 * @return
	 */
	boolean hasFunctionsByFucntionSettings(String fucntionSettingsString,String functionkey);
}
