package com.newdumai.dumai_data.dm_label.interfaces.youfen;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.newdumai.util.JsonToMap;

public class ValidateMobile {

	@SuppressWarnings("unchecked")
	public static String realMobile(String result) {
		Map<String, Object> gson2Map = JsonToMap.gson2Map(result);
		Map<String, Object> data = (Map<String, Object>) gson2Map.get("data");
		String statusCode = (String) data.get("statusCode");
		Map<String, Object> map = new HashMap<String, Object>();
		if ("2005".equals(statusCode)) {
			map.put("msg", "1");
		} else if ("2006".equals(statusCode)) {
			map.put("msg", "0");
		} else {
			return "";
		}
		return new Gson().toJson(map);
	}
}
