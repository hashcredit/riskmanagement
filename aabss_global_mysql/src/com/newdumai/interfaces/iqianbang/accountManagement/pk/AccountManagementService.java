package com.newdumai.interfaces.iqianbang.accountManagement.pk;

import java.util.HashMap;
import java.util.Map;

import com.newdumai.interfaces.iqianbang.accountManagement.util.DigestUtils;
import com.newdumai.interfaces.iqianbang.accountManagement.util.HttpUtils;
import com.newdumai.interfaces.iqianbang.accountManagement.util.SignUtil;

public class AccountManagementService {

	public static String QueryForResult(Map<String,Object>params){
		Map<String, String> sendParams = setSendParams(params);
		String sign = SignUtil.sign(DigestUtils.getSignData("sign", sendParams), AccountManagementConfigFile.getFilePath());
        sendParams.put("sign", sign);
        return HttpUtils.sendGetRequest((String)params.get("url"), sendParams, "utf-8");
	}
	
	private static Map<String,String>setSendParams(Map<String,Object>params){
		Map<String, String> sendParams = new HashMap<String, String>();
		if(params.containsKey("appType")){
			sendParams.put("appType", (String)params.get("appType"));
		}
		if(params.containsKey("version")){
			sendParams.put("version", (String)params.get("version"));
		}
		if(params.containsKey("traceNo")){
			sendParams.put("traceNo", (String)params.get("traceNo"));
		}
		if(params.containsKey("regId")){
			sendParams.put("regId", (String)params.get("regId"));
		}
		if(params.containsKey("stmtYearMonth")){
			sendParams.put("stmtYearMonth", (String)params.get("stmtYearMonth"));
		}
		return sendParams;
	}
	
}
