package com.newdumai.setting.interface_source.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.setting.interface_source.Interface_sourceService;
import com.newdumai.util.InvokeMethod;
import com.newdumai.util.JsonToMap;


@Service("interface_sourceService")
public class Interface_sourceServiceImpl extends BaseServiceImpl implements Interface_sourceService {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
		
	}

	@Override
	public int add_Interface_sourceService(Map<String, Object> para) {
		return add(para,"sys_interface_source");
	}

	@Override
	public String para_toUpdate(String interface_source_code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(gen_para_toUpdate(interface_source_code));
	}

	@Override
	public int upadte_Interface_sourceService(Map<String, Object> para) {
		Map<String, Object> where=new HashMap<String, Object>();
		where.put("code", para.get("code"));
		para.remove("code");
		return Update(para, "sys_interface_source", where);
	}

	@Override
	public String toTestDS(String code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT * FROM sys_interface_source_para WHERE `type`='0' AND `Interface_source_code`='"+code+"'");
	}

	private void saveToSys_interface_source_detail(String sub_entity_id,String orderId,String interface_source_code,String in_para,String result,Map<String, Object> sys_interface_sourceMap){
		String result3 = result;
		if(result3.contains("#detail_info_code#")){
			result = result3.split("#detail_info_code#")[1];
		}
		Map<String, Object> save=new HashMap<String, Object>();
		save.put("interface_source_code", interface_source_code);
		save.put("in_para", in_para);
		save.put("sub_entity_id", sub_entity_id);
		String is = (String)sys_interface_sourceMap.get("out_interface");
		if(StringUtils.isNotEmpty(is)){
			String result2=formatOutPara(is,result);
			if(StringUtils.isEmpty(result2))return ;
			save.put("result", result2);
			save.put("result2", result);
		}else{
			save.put("result", result);
		}
		
		String sys_interface_source_detail_code = addAndRet(save,"sys_interface_source_detail");
		if(StringUtils.isNotEmpty(orderId)){
			Map<String, Object> save2=new HashMap<String, Object>();
			save2.put("sys_interface_source_detail_code", sys_interface_source_detail_code);
			save2.put("order_code", orderId);
			addAndRet(save2,"sys_interface_source_detail_orderinfo");
		}
		
		if(result3.contains("#detail_info_code#")){
			Map<String,Object> where = new HashMap<String,Object>();
			where.put("code", result3.subSequence(0, 36));
			Map<String,Object> update_detail_info = new HashMap<String,Object>();
			update_detail_info.put("sys_interface_source_detail_code", sys_interface_source_detail_code);
			Update(update_detail_info, "sys_interface_source_detail_info", where);
		}
		
		
	}
	
	@SuppressWarnings({ })
	@Override
	public String testDS(String sub_entity_id,String orderId,String interface_source_code, Map<String, Object> map) {
		Map<String, Object> sys_interface_sourceMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source WHERE `code`='"+interface_source_code+"'");
		/**
		 * 存入对账log表
		 */
		Map<String, Object> saveLog=new HashMap<String, Object>();
		saveLog.put("sub_entity_id", sub_entity_id);
		saveLog.put("interface_source_code", interface_source_code);
		saveLog.put("order_id", orderId);
		addAndRet(saveLog,"sys_interface_source_log");
		
		List<Map<String, Object>> ret;
		String result="";
		String in_para=new Gson().toJson(map);
		ret = super.mysqlSpringJdbcBaseDao.executeSelectSql2(gen_testDS_1(interface_source_code,in_para));
		if(CollectionUtils.isNotEmpty(ret)){
			String result2 = (String) ret.get(0).get("result2");
			return result2==null ? (String) ret.get(0).get("result") : result2;
		}else{
			//专用接口
			String is=(String)sys_interface_sourceMap.get("interface");
			if(StringUtils.isNotEmpty(is)){
				if(StringUtils.isNotEmpty((String) sys_interface_sourceMap.get("url"))){
					map.put("url", (String) sys_interface_sourceMap.get("url"));
				}
				try {
					map.put("interface_source_code", interface_source_code);
					result = InvokeMethod.invoke(is, map);
					if(StringUtils.isNotEmpty(result)){
						//没有异常执行保存
						saveToSys_interface_source_detail( sub_entity_id, orderId,interface_source_code, in_para, result,sys_interface_sourceMap);
					}
					return result;
				} catch (Exception e) {
					e.printStackTrace();
					result = null;
				}
			}
			//专用接口结束
			Map<String, Object> findDS = findDS(interface_source_code);
			findDS.put("params", getIn_para(sub_entity_id, interface_source_code, map));
			try {
				result = "";//DataSourceFactory.createCommonDataChannel().request(findDS);
				//没有异常执行保存
				if(StringUtils.isNotEmpty(result)){
					saveToSys_interface_source_detail( sub_entity_id,orderId, interface_source_code, in_para, result,sys_interface_sourceMap);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getMessage();
				return null;//出现异常不能存库
			}
		}
	}

	private String formatOutPara(String is,String result) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", result);
		try {
			return InvokeMethod.invoke(is, map);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public float getInterfacesCost(String interfacesCodes) {
		Map<String, Object> interfaceMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source WHERE `code`=?", interfacesCodes);
		return Float.parseFloat(interfaceMap.get("cost_tuomin").toString());
	}
	/*@SuppressWarnings("unchecked")
	private String getIn_para(String sub_entity_id,String interface_source_code, Map<String, Object> map) {
		//TODO
		Map<String, Object> interfaces = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source WHERE code='"+interface_source_code+"' ");
		if("1".equals(interfaces.get("in_type"))){
			
		}else{
//			in_para=new Gson().toJson(map);
		}
		Map<String, Object> para;
		Object obj=map;
		for (String key : map.keySet()) {
			para = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source_para WHERE Interface_source_code = `"+interface_source_code+"' AND `name`="+map.get(key)+"'");
			String para_group = para.get("para_group").toString();
			if(para_group!=null&&!"".equals(para_group)){
				String[] split = para_group.split("_");
				for (int i = 0; i < split.length; i++) {
					try {
						obj = auditOne_4_getKey(split[i],obj);
						if(i==split.length-1){
							Map<String, Object> m=new HashMap<String, Object>();
							m.put(key, map.get(key));
							((Map<String, Object>) obj).put(split[i],m);
						}
					} catch (Exception e) {
						if(i==split.length-1){
							Map<String, Object> m=new HashMap<String, Object>();
							m.put(key, map.get(key));
							((Map<String, Object>) obj).put(split[i],m);
						}else{
							((Map<String, Object>) obj).put(split[i], new HashMap<String, Object>());
						}
						continue;
					}
				}
				map.remove(key);
			}
		}
		return null;
	}*/
	@SuppressWarnings("unchecked")
	private Map<String, Object> getIn_para(String sub_entity_id,String interface_source_code, Map<String, Object> map) {
		String split = "_";
		List<Map<String, Object>> params =  mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_interface_source_para WHERE Interface_source_code = '"+interface_source_code+"' and type=0");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int size = params.size();
		for(int i=0; i < size; i ++){
			Map<String, Object> param = params.get(i);
			String paraGroup = (String) param.get("para_group");
			String name = (String)param.get("name");
			paraGroup = StringUtils.isEmpty(paraGroup)?name:(paraGroup+"_"+name);
			String paramPaths[] = paraGroup.split(split);
			Map<String,Object> current = resultMap;
			for(int j = 0; j < paramPaths.length; j ++){
				String key = paramPaths[j];
				if(j==paramPaths.length-1){
					String value = (String) param.get("value");
					if(!StringUtils.isEmpty(value)){
						current.put(key, value);
					}
					else{
						current.put(key, map.get(key));
					}
				}
				else{
					Object o = current.get(key);
					if(o!=null){
						if(o instanceof Map){
							current = (Map<String, Object>) o;
						}
						else{
							HashMap<String,Object> m = new HashMap<String,Object>();
							current.put(key,m);
							current = m;
						}
					}
					else{
						HashMap<String,Object> m = new HashMap<String,Object>();
						current.put(key,m);
						current = m;
					}
				}
			}
		}
		return resultMap;
	}
	
	private Map<String, Object> findDS(String code) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql2(gen_findDS(code)).get(0);
	}
	
	
	/**
	 * 
	 * @param sub_entity_id
	 * @param interface_source_code
	 * @return
	 */
	private String charge(String sub_entity_id,String interface_source_code){
		Map<String, Object> companyMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order WHERE sub_entity_id =?", sub_entity_id);
		//查询花费
		int cost = getDsCost(interface_source_code);
		Integer status = (Integer) companyMap.get("status");
		if(status==0)return"账户已停用!";
		Float todayBalance = (Float) companyMap.get("todayBalance");
		Float totalBalance = (Float) companyMap.get("totalBalance");
		if(totalBalance - cost<50){
			return "账户余额不足!";
		}else{
			if(todayBalance - cost <0){
				return "今日可用余额不足!";
			}else{
				mysqlSpringJdbcBaseDao.update("update company_order set todayBalance=todayBalance-?,totalBalance=totalBalance-? where sub_entity_id=?", cost,cost,sub_entity_id);
				Map<String,Object> save = new HashMap<String,Object>();
				save.put("sub_entity_id", sub_entity_id);
				save.put("fk_orderinfo_code",null );
				save.put("cost",cost );
				save.put("log", "进件验证！【扣除："+cost+"】");
				addAndRet(save, "company_order_log");
				return "true";
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public int validateMobile(String sub_entity_id,String name, String card_num, String mobile) {
		String code = "d70e91e4-dc23-466b-8b82-ef7b3ce9ac13";
		if(!"true".equals(charge(sub_entity_id,code))){
			return OrderValidResultConst.ERROR;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("name", name);
		map.put("idcard",card_num!=null ? card_num.toUpperCase():null);
		map.put("cellphone", mobile);
		map.put("account", "aiqianbang001");
		String s = testDS(sub_entity_id,null,code,map);
		s=s.replace("\"", "");
		s=s.replace("\\", "");
		Map<String, Object> result = JsonToMap.toMap(s);
		String resCode = ((JsonPrimitive) result.get("resCode")).getAsString();
		if("0000".equals(resCode)){
			String statusCode = ((JsonPrimitive) ((Map<String, Object>) result.get("data")).get("statusCode")).getAsString();
			if("2005".equals(statusCode)){
				return OrderValidResultConst.PASSED;
			}else {
				return OrderValidResultConst.REFUSED;
			}
		}
		else{
			return OrderValidResultConst.REFUSED;
		}
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public int validateBankCard(String sub_entity_id,String name,String card_num, String bank_num) {
		String code = "5687256d-ee6b-486c-8741-fc05af7533df";
		if(!"true".equals(charge(sub_entity_id,code))){
			return OrderValidResultConst.ERROR;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("name", name);
		map.put("idcard", card_num.toUpperCase());
		map.put("bankcard", bank_num);
		map.put("account", "aiqianbang001");
		String s = testDS(sub_entity_id,null,code,map);
		s=s.replace("\"", "");
		s=s.replace("\\", "");
		Map<String, Object> result = JsonToMap.toMap(s);
		String resCode = ((JsonPrimitive) result.get("resCode")).getAsString();
		if("0000".equals(resCode)){
			String statusCode = ((JsonPrimitive) ((Map<String, Object>) result.get("data")).get("statusCode")).getAsString();
			if("2005".equals(statusCode)){
				return OrderValidResultConst.PASSED;
			}else {
				return OrderValidResultConst.REFUSED;
			}
		}
		else{
			return OrderValidResultConst.REFUSED;
		}
	}
	
	/**
	 * 获取单个数据源的费用
	 * @param code 数据源code
	 */
	protected Integer getDsCost(String code){
		String sql = "select cost_tuomin from sys_interface_source where code=?";
		try {
			return mysqlSpringJdbcBaseDao.executeSelectSqlInt(sql, code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM sys_interface_source WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT * FROM sys_interface_source WHERE 1=1 "+condition+limit;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
//		String filter_headtype = (String) map.get("filter_headtype");
//		if(!StringUtils.isEmpty(filter_headtype)){
//			sb.append(" AND filter_headtype=? ");
//			list.add(filter_headtype);
//		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
	
	private String gen_para_toUpdate(String interface_source_code) {
		return "SELECT * FROM sys_interface_source WHERE code='"+interface_source_code+"'";
	}

	private String gen_testDS_1(String interface_source_code,String in_para) {
		return "SELECT * FROM sys_interface_source_detail WHERE `interface_source_code`='"+interface_source_code+"' and `in_para`='"+in_para+"' ";
	}

	private String gen_findDS(String code) {
		return "SELECT * FROM sys_interface_source where code='"+code+"'";
	}

	@Override
	public String getXiaoShiPhoto(String sub_entity_id,String name, String card_num) {
		String code = "93a69a72-d867-4f7d-a587-64ab3b8a3378";
		if(!"true".equals(charge(sub_entity_id,code))){
			return null;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("name", name);
		map.put("idCard", card_num.toUpperCase());
		try {
			String result = testDS(sub_entity_id,null,code,map);
			Map<String, Object> json = JsonToMap.gson2Map(result);
			return (String) json.get("PHOTO");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	


	public String testDSOnly(String sub_entity_id,String interface_source_code, Map<String, Object> map) {
		/**
		 * 此方法批量用
		 * 存入对账log表
		 */
		Map<String, Object> saveLog=new HashMap<String, Object>();
		saveLog.put("interface_source_code", interface_source_code);
		addAndRet(saveLog,"sys_interface_source_log");
		
		String result="";
		String in_para=new Gson().toJson(map);
		Map<String, Object> sys_interface_sourceMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source WHERE `code`='"+interface_source_code+"'");
		String is=(String)sys_interface_sourceMap.get("interface");
		if(StringUtils.isNotEmpty(is)){
			java.lang.String[] split = is.split("#");
			 Class<?> onwClass = null;
			 Method m1 = null ;
			try {
				onwClass = Class.forName(split[0]);
				m1 = onwClass.getDeclaredMethod(split[1],Map.class);
				if(StringUtils.isNotEmpty((String) sys_interface_sourceMap.get("url"))){
					map.put("url", (String) sys_interface_sourceMap.get("url"));
				}
				result = (java.lang.String) m1.invoke(onwClass, map) ;
				saveToSys_interface_source_detail( sub_entity_id,null, interface_source_code, in_para, result,sys_interface_sourceMap);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getMessage();
				return null;
			}
		}
		return result;
	}
}
