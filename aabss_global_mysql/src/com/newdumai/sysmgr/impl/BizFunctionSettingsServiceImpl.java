package com.newdumai.sysmgr.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.sysmgr.BizFunctionSettingsService;
import com.newdumai.util.JsonToMap;

@Service("bizFunctionSettingsService")
public class BizFunctionSettingsServiceImpl extends BaseServiceImpl implements BizFunctionSettingsService {
	
	@Autowired
	private Dumai_sourceBaseDao dumai_sourceBaseDao;
	
	@Override
	public Map<String, Object> getFunctionSettingsBySubentityId(String subEntityId, String typeCode) {
		return mysqlSpringJdbcBaseDao.queryForMap("select ct.code,ct.rule_model,ct.report_para from sys_company_type ct where ct.sub_entity_id=? and ct.type_code=?", subEntityId, typeCode);
	}

	@Override
	public Map<String, Map<String, Object>> getAsMapBySubentityId(String subEntityId) {
		List<Map<String, Object>> list = mysqlSpringJdbcBaseDao.queryForList("select ct.type_code,ct.report_para from sys_company_type ct where ct.sub_entity_id = ?", subEntityId);
		Map<String, Map<String, Object>> functionSettings = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : list) {
			functionSettings.put((String) map.get("type_code"), JsonToMap.jsonToMap((String) map.get("report_para")));
		}
		return functionSettings;
	}
	
	@Override
	public boolean update(Map<String, Object> updateParam,String code) {
		Map<String,Object> where = new HashMap<String, Object>();
		where.put("code",code);
		Update(updateParam, "sys_company_type", where);
		return true;
	}

	@Override
	public boolean hasFunctions(LoginVo login, String functionkey,String typeCode) {
		boolean flag = false;
		if(login!=null){
			if(functionkey!=null){
				Map<String, Map<String, Object>> fucntionSettingsString = login.getBizFunction_settings();
				flag = hasFunctionsByFucntionSettings(fucntionSettingsString, functionkey,typeCode);
			}
		}
		return flag;
	}

	@Override
	public boolean hasFunctions(String subEntityId, String functionkey,String typeCode) {
		Map<String, Object> map =  mysqlSpringJdbcBaseDao.queryForMap("select ct.report_para from sys_company_type ct where ct.sub_entity_id=? and ct.type_code=?", subEntityId,typeCode);
		boolean flag = false;
		if(map != null){
			if(functionkey!=null){
				String fucntionSettingsString = (String) map.get("report_para");
				
				try {
					Map<String,Object> functionSettingMap = JsonToMap.jsonToMap(fucntionSettingsString);
					if(functionSettingMap!=null){
						flag = "1".equals(functionSettingMap.get(functionkey));
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					//异常忽略，可能未配置任何功能
				}
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param functionkey
	 * @return
	 */
	@Override
	public boolean hasFunctionsByFucntionSettings(Map<String, Map<String, Object>> fucntionSettingsMap, String functionkey,String typeCode){
		boolean flag = false;
		if(functionkey!=null){
			try{
				Map<String,Object> map = fucntionSettingsMap.get(typeCode);
				if(map!=null){
					flag = "1".equals(map.get(functionkey));
				}
			}catch(Exception e){
				//异常时忽略，可能未配置任何功能
			}
		}
		return flag;
	}

	/**
	 * @designed-by tianxinyang
	 */
	@Override
	public List<Map<String, Object>> getViewDataSettings(String sub_entity_id, String typeCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" dm_3rd_interface.*,dm_3rd_interface.cost*s3.cost_rate cost_out, ");
		sb.append(" find_in_set(dm_3rd_interface. CODE ,s3.interfaces) checkedByGuize, ");
		sb.append(" find_in_set(dm_3rd_interface. CODE ,s3.report_interfaces) checked ");
		sb.append(" FROM dumai_source.dm_3rd_interface LEFT JOIN ");
		sb.append(" ( SELECT * FROM ");
		sb.append(" ( ");
		sb.append(" SELECT group_concat(gl.interfaces) interfaces ");
		sb.append(" FROM company_order co ");
		sb.append(" JOIN sys_company_type ct ON co.sub_entity_id = ct.sub_entity_id ");
		sb.append(" JOIN sys_company_type__fk_guize ctfg ON ctfg.sys_company_type_code = ct. CODE ");
		sb.append(" JOIN fk_guize gz ON ctfg.fk_guize_code = gz. CODE ");
		sb.append(" JOIN fk_guiz_logs gl ON gz. CODE = gl.fk_guize_code ");
		sb.append(" WHERE ");
		sb.append(" ct.type_code = '"+typeCode+"' ");
		sb.append(" AND ct.sub_entity_id = '"+sub_entity_id+"' ");
		sb.append(" ) AS s1 ");
		sb.append(" INNER JOIN ( ");
		sb.append(" SELECT ");
		sb.append(" co.cost_rate, ");
		sb.append(" ct.report_interfaces ");
		sb.append(" FROM ");
		sb.append(" company_order co ");
		sb.append(" JOIN sys_company_type ct ON co.sub_entity_id = ct.sub_entity_id ");
		sb.append(" WHERE ");
		sb.append(" ct.type_code = '"+typeCode+"' ");
		sb.append(" AND co.sub_entity_id = '"+sub_entity_id+"' ");
		sb.append(" ) AS s2 ON s1.interfaces <> '' ");
		sb.append(" ) AS s3 on dm_3rd_interface.code <> '' ");
		sb.append(" WHERE is_able = '1'  ");
		return mysqlSpringJdbcBaseDao.queryForList(sb.toString());
	}

	@Override
	public boolean saveViewDataSettings(String sub_entity_id, String typeCode, String codes) {
		mysqlSpringJdbcBaseDao.update("update sys_company_type set report_interfaces=? where sub_entity_id=? and type_code=? ", codes,sub_entity_id,typeCode);
		return true;
	}

	@Override
	public int updateRuleOrModel(String subEntityId, String type_code, String rule_model) {
		String sql = "update sys_company_type set rule_model = ? where sub_entity_id = ? and type_code = ? ";
		return super.mysqlSpringJdbcBaseDao.update(sql, rule_model, subEntityId, type_code);
	}
}
