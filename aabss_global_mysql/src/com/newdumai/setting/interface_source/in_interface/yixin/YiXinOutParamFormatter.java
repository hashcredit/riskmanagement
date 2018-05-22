package com.newdumai.setting.interface_source.in_interface.yixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.newdumai.util.JsonToMap;



public class YiXinOutParamFormatter{

	public static String aa(Map<String,Object>params){
		String data = (String)params.get("result");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("blackCounts", getOverdueTimes(data));
		return new Gson().toJson(result);
	}
	//不要删
	/*public static String formatLoan(Map<String,Object>params){
		String data = (String)params.get("result");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("overdueTimes", getOverdueTimes(data));
		return new Gson().toJson(result);
	}*/
	
	/**
	 * 可以查到数据的{"name":"王天伟","idNo":"610103197604041613"}
	 * @param params
	 * @return
	 */
	public static String formatBlackList(Map<String,Object>params){
		String data = (String)params.get("result");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("blackCounts", getBlackCounts(data));
		return new Gson().toJson(result);
	}
	
	@SuppressWarnings("unchecked")
	private static String getOverdueTimes(String jsonResult){
		Integer result = 0;
		Map<String,Object> map = JsonToMap.jsonToMap(jsonResult);
		if (map.containsKey("errorcode") && "0000".equals(map.get("errorcode"))) {
			Map<String,Object> data = (Map<String, Object>) map.get("data");
			if(data!=null){
				Map<String,Object> overdue = (Map<String, Object>) data.get("overdue");
				Object overdueTimes = overdue.get("overdueTimes");
				if(overdueTimes instanceof Number){
					result += ((Number)overdueTimes).intValue();
				}
			}
		}
		return result.toString();
	}
	
	@SuppressWarnings({ "unchecked" })
	private static String getBlackCounts(String jsonResult){
		Integer result = 0;
		Map<String,Object> map = JsonToMap.jsonToMap(jsonResult);
		if (map.containsKey("errorcode") && "0000".equals(map.get("errorcode"))) {
			Map<String,Object> data = (Map<String, Object>) map.get("data");
			if(data!=null){
				List<Map<String,Object>> overdues = (List<Map<String, Object>>) data.get("riskItems");
				for(Map<String,Object> overdue : overdues){
					String riskType = (String) overdue.get("riskType");
					if(!StringUtils.isEmpty(riskType)){
						result ++;
					}
				}
			}
			return result.toString();
		}else{
			return null;
		}
	}
}
