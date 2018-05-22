package com.newdumai.loanFront.impl;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.MapObjUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class AuditServiceUtil {
	
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
	
	/**
	 * 取得数据源code（去重）
	 * @param ruleList
	 * @param columeName
	 * @return
	 */
	public static Set<String> getInterfacesCodes(List<Map<String, Object>> ruleList, String columeName) {
		Set<String> interfacesCode = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(ruleList)){
			for (Map<String, Object> rule : ruleList) {
				String[] split = ((String) rule.get(columeName)).split(",");
				for (String is : split) {
					interfacesCode.add(is);
				}
			}
		}
		return interfacesCode;
	}
	
	/**
	 *  取得大map
	 * @param dumai_sourceBaseDao
	 * @param dm_3rd_interfaceService
	 * @param orderMap
	 * @param interfacesCodeSet
	 * @return
	 */
	public static Map<String, Object> getDm_3rd_interfacesResult_jianquan(Dumai_sourceBaseDao dumai_sourceBaseDao,Dm_3rd_interfaceService dm_3rd_interfaceService,Map<String, Object> orderMap, Set<String> interfacesCodeSet){
		Map<String, Object> interfacesResultMap = new HashMap<String, Object>();
		Map<String, Object> inPara;
		for (String interfaceCode : interfacesCodeSet) {
			inPara = getInPara(dumai_sourceBaseDao,interfaceCode, orderMap);
			String is_result = (String) dm_3rd_interfaceService.testDS(interfaceCode,inPara);
			Map<String, Object> result;
			try {
				result = JsonToMap.jsonToMap(is_result);
			} catch (Exception e) {
				result = null ;
				e.printStackTrace();
			}
			interfacesResultMap.put(interfaceCode, result);
		}
		return interfacesResultMap;
	}

	/**
	 * 临时调用百度金融 小爱有信接口,未进行逻辑处理
	 *
	 * @param dumai_sourceBaseDao
	 * @param dm_3rd_interfaceService
	 * @param orderMap
	 * @return
	 */
	public static void callTemporaryInterface(Dumai_sourceBaseDao dumai_sourceBaseDao, Dm_3rd_interfaceService dm_3rd_interfaceService, Map<String, Object> orderMap) {
		Set<String> interfacesCodeSet = new HashSet<>();
		// 手动添加接口
		interfacesCodeSet.add("08ce18ac-7052-4e67-88e0-5e534ae97374");// 小爱有信 风险信息查询
		interfacesCodeSet.add("abf1c9f2-88fe-4652-821f-71f1140362e9");// 百度金融磐石系统-风险名单查询

		String sql = "insert into dm_3rd_interface_detail (code,dm_3rd_interface_code,in_para,result) values (?,?,?,?)";

		Map<String, Object> interfacesResultMap = new HashMap<String, Object>();
		Map<String, Object> inPara;
		for (String interfaceCode : interfacesCodeSet) {
			inPara = getInPara(dumai_sourceBaseDao, interfaceCode, orderMap);
			String is_result = dm_3rd_interfaceService.testDS0(interfaceCode, inPara);
			interfacesResultMap.put(interfaceCode, is_result);
			if ("08ce18ac-7052-4e67-88e0-5e534ae97374".equals(interfaceCode)) {
				System.out.println("小爱有信接口返回:" + is_result);
			}
			if (StringUtils.isNotEmpty(is_result)) {
				dumai_sourceBaseDao.insert(sql, UUID.randomUUID().toString(), interfaceCode, new Gson().toJson(inPara), is_result);
			}
		}
	}
	
	/**
	 * 取得输入参数
	 * @param dumai_sourceBaseDao
	 * @param dm_3rd_interface_code
	 * @param orderMap
	 * @return
	 */
	public static Map<String, Object> getInPara(Dumai_sourceBaseDao dumai_sourceBaseDao,String dm_3rd_interface_code, Map<String, Object> orderMap) {
		Map<String, Object> returnMap = MapObjUtil.getMapSortedByKey();
		List<Map<String, Object>> nameTransList = dumai_sourceBaseDao.queryForList("SELECT * FROM dm_3rd_interface_para WHERE 1=1 and `type`= '0' AND dm_3rd_interface_code= ? " ,new Object[]{dm_3rd_interface_code});
		if(CollectionUtils.isEmpty(nameTransList)){
			return null;
		}
		for (Map<String, Object> nameMap : nameTransList) {
			String or_name = (String) nameMap.get("fk_orderinfo_name");
			String is_name = (String) nameMap.get("name");
			// 根据参数映射转换参数
			if (orderMap.containsKey(or_name)) {
				returnMap.put(is_name, orderMap.get(or_name));
			}
		}
		return returnMap;
	}
	public static Boolean auditOne_execute(String v1, String v2, String ops) {
		int op = Integer.valueOf(ops);
		Bindings bindings = engine.createBindings();
		bindings.put("value", v1);
		bindings.put("value2", v2);
		boolean b = false;//默认和无结果未命中
		try {
			switch (op) {
			case 1:
				b = (Boolean) engine.eval("value>=value2", bindings);
				break;
			case 2:
				b = (Boolean) engine.eval("value<value2", bindings);
				break;
			case 3:
				b = (Boolean) engine.eval("value<=value2", bindings);
				break;
			case 4:
				b = (Boolean) engine.eval("value!=value2", bindings);
				break;
			// case 5:
			// b= (Boolean) engine.eval("value!=value2");break;
			case 7:
				b = v1.contains(v2);
				break;
			case 8:
				b = (Boolean) engine.eval("value>value2", bindings);
				break;
			default:
				b = v1.equals(v2);
			}
		} catch (ScriptException e) {
			b = false;
		}
		return b;
	}
}
