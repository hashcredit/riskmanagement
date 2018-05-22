package com.newdumai.ht.manager.model.report.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.dm_out.Dm_outService;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.global.dao.impl.mysqlSpringJdbcBaseDaoImpl;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.model.report.ReportService;
import com.newdumai.util.IdCardUtils;
import com.newdumai.util.JsonToMap;

/**
 * 模型报告
 */
@Service("reportService")
public class ReportServiceImpl extends BaseServiceImpl implements ReportService {

	@Autowired
	private Dm_outService dm_outService;
	@Autowired
	private Dumai_sourceBaseDao dumai_sourceBaseDao;

	@Override
	public Map<String, Object> findOrderByCode(String code) {
		String sql = "SELECT * FROM fk_orderinfo where code='" + code + "'";
		return mysqlSpringJdbcBaseDao.queryForMap(sql);
	}

	@Override
	public Map<String, Object> getResult(String orderCode, String type) {
		String sql = "SELECT * FROM manager_model_result where fk_orderinfo_code=? and type =? order by opttime desc limit 1";
		return mysqlSpringJdbcBaseDao.queryForMap(sql, orderCode, type);
	}

	@Override
	public Map<String, Object> getPersonInfo(Map<String,Object>orderMap) {
		//获取person表
		String sql = "SELECT * FROM fk_personinfo where code='" + (String)orderMap.get("personinfo_code") + "'";
		Map<String,Object> personMap = mysqlSpringJdbcBaseDao.queryForMap(sql);
		String card_num = (String)personMap.get("card_num");
		//解析身份证
		String age = String.valueOf(IdCardUtils.getAgeByIdCard(card_num));
		String sex = "M".equals(IdCardUtils.getGenderByIdCard(card_num))?"男":"女";
		personMap.put("sex", sex);
		personMap.put("age", age);
		//获取业务类型名字
		String sql2 = "SELECT * FROM sys_type where code='" + (String)orderMap.get("thetype") + "'";
		Map<String,Object> sys_typeMap = mysqlSpringJdbcBaseDao.queryForMap(sql2);
		personMap.put("thetype_name", sys_typeMap.get("name"));

		//获取读脉源信息
		Map<String,Object>params = new HashMap<String,Object>();
		params.put("aa959655-9ead-4884-91dd-2cb6ab04cac0",new String[]{"PHOTO"});
		params.put("4efb92bf-47cd-417c-b137-41b23ce762c7",new String[]{"education","maritalStatus","birthplace"});
		personMap = getData(personMap,orderMap,params);
		return personMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String,Object> getData(Map<String,Object>personMap,Map<String,Object>orderMap,Map<String,Object>params){
		Set<String>dm_source_codeSet = params.keySet();
		Map<String, Object> dmSourceResult = (Map<String, Object>) dm_outService.getAuditOrderResult(orderMap,dm_source_codeSet).get("data");
		for(String dm_source_code:dm_source_codeSet){
			String dresult = (String) dmSourceResult.get(dm_source_code);
			String[] param = (String[]) params.get(dm_source_code);
			for(String pa:param){
				String vresult = null;
				if(dresult==null){
					vresult = null ;
				}else{
					try {
						Map<String, Object> dm_result = JsonToMap.gson2Map(dresult);
						vresult = (String) dm_result.get(pa);
					} catch (Exception e) {
						vresult = null ;
					}
				}
				personMap.put(pa, vresult);
			}
		}
		return personMap;
	}

	@Override
	public List<Map<String, Object>> getDetailInterfaces(Map<String, Object> orderMap) {
		String typeCode = (String) orderMap.get("thetype");
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		System.out.println(typeCode+"|"+sub_entity_id+"||"+mysqlSpringJdbcBaseDao);
		String sqlInterfaces =
				"SELECT"
				+ "	group_concat(gl.interfaces) interfaces,ct.report_interfaces"
				+ " FROM"
				+ "	company_order co"
				+ "	join sys_company_type ct on co.sub_entity_id=ct.sub_entity_id"
				+ "	join sys_company_type__fk_guize ctfg on ctfg.sys_company_type_code=ct.code"
				+ " join fk_guize gz on ctfg.fk_guize_code=gz.code"
				+ " JOIN fk_guiz_logs gl ON gz.CODE = gl.fk_guize_code where ct.type_code=? and ct.sub_entity_id=?";
		Map<String, Object> interfaceMap = mysqlSpringJdbcBaseDao.queryForMap(sqlInterfaces,typeCode,sub_entity_id);
		String interfaces = (String) interfaceMap.get("interfaces");

		String sqlCompany = "select ct.report_interfaces from sys_company_type ct where ct.type_code=? and ct.sub_entity_id=?";
		Map<String, Object> companyMap = mysqlSpringJdbcBaseDao.queryForMap(sqlCompany,typeCode,sub_entity_id);

		String report_interfaces = (String) companyMap.get("report_interfaces");

		Set<String> set = new LinkedHashSet<String>();
		String[] temp = null;
		if(StringUtils.isNotEmpty(interfaces)){
			temp = interfaces.split(",");
			for(String s : temp){
				set.add(s);
			}
		}
		if(StringUtils.isNotEmpty(report_interfaces)){
			temp = report_interfaces.split(",");
			for(String s : temp){
				set.add(s);
			}
		}
		String codes = "('" + StringUtils.join(set, "','") + "')";
		return dumai_sourceBaseDao.queryForList("select code,SUBSTRING_INDEX(name,'_',-1) name from dm_3rd_interface where report is not null and code in " + codes);
	}
	@Override
	public List<Map<String, Object>> getDetailInterfaceMap(String orderCode){
		String sqlInterfaces = "SELECT interfaceCodes FROM fk_order_bigmap  where fk_orderinfo_code=?  limit 1";
		Map<String, Object> interfaceMap = mysqlSpringJdbcBaseDao.queryForMap(sqlInterfaces,orderCode);
		String interfaceCodes = (String) interfaceMap.get("interfaceCodes");

		Set<String> set = new LinkedHashSet<String>();
		String[] temp = null;
		if(StringUtils.isNotEmpty(interfaceCodes)){
			temp = interfaceCodes.split(",");
			for(String s : temp){
				set.add(s);
			}
		}
		String codes = "('" + StringUtils.join(set, "','") + "')";
		return dumai_sourceBaseDao.queryForList("select code,SUBSTRING_INDEX(name,'_',-1) name from dm_3rd_interface where report is not null and code in " + codes);
	}

	@Override
	public Map<String, Object> getOrderBill(String code) {
		String sql = "SELECT * FROM order_bill_info  where ordercode = ?  ";
		Map<String, Object> interfaceMap = mysqlSpringJdbcBaseDao.queryForMap(sql, code);
		return interfaceMap;
	}
}
