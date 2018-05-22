package com.newdumai.sysmgr.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.sysmgr.FunctionSettingsService;
import com.newdumai.util.JsonToMap;

@Service("functionSettingsService")
public class FunctionSettingsServiceImpl extends BaseServiceImpl implements FunctionSettingsService {

	@Override
	public Map<String, Object> getFunctionSettingsBySubentityId(
			String subEntityId) {
		return mysqlSpringJdbcBaseDao.queryForMap("select function_settings,white_ips from company_order where sub_entity_id=?", subEntityId);
	}

	@Override
	public boolean updateBySubentityId(Map<String, Object> updateParam,
			String subEntityId) {
		Map<String,Object> where = new HashMap<String, Object>();
		where.put("sub_entity_id",subEntityId);
		Update(updateParam, "company_order", where);
		return true;
	}

	@Override
	public boolean hasFunctions(LoginVo login, String functionkey) {
		boolean flag = false;
		if(login!=null){
			if(functionkey!=null){
				String fucntionSettingsString = login.getFunction_settings();
				flag = hasFunctionsByFucntionSettings(fucntionSettingsString, functionkey);
			}
		}
		return flag;
	}

	@Override
	public boolean hasFunctions(String subEntityId, String functionkey) {
		Map<String, Object> map =  mysqlSpringJdbcBaseDao.queryForMap("select function_settings from company_order where sub_entity_id=?", subEntityId);
		boolean flag = false;
		if(map!=null){
			if(functionkey!=null){
				String fucntionSettingsString = (String) map.get("function_settings");
				flag = hasFunctionsByFucntionSettings(fucntionSettingsString, functionkey);
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param fucntionSettingsString
	 * @param functionkey
	 * @return
	 */
	@Override
	public boolean hasFunctionsByFucntionSettings(String fucntionSettingsString, String functionkey){
		boolean flag = false;
		if(functionkey!=null){
			try{
				Map<String,Object> fucntionSettings = JsonToMap.gson2Map(fucntionSettingsString);
				flag = "1".equals(fucntionSettings.get(functionkey));
			}catch(Exception e){
				//异常时忽略，可能未配置任何功能
			}
		}
		return flag;
	}
}
