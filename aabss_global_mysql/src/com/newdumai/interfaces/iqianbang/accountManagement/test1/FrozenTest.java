package com.newdumai.interfaces.iqianbang.accountManagement.test1;

import java.util.HashMap;
import java.util.Map;

import com.newdumai.interfaces.iqianbang.accountManagement.pk.AccountManagementService;

/**
 * 冻结查询测试
 */
public class FrozenTest {

	public static void main(String[] args) {
		Map<String, Object> param = new HashMap<String,Object>();
		param.put("appType","wechat");
		param.put("version","1.0");
		param.put("traceNo","20160816-123456");
		param.put("regId","13701259346");
		param.put("url","http://112.126.81.154/front.shandianx/bill/searchFrozen.do");
		String result = AccountManagementService.QueryForResult(param);
		System.out.println("result:" + result);
	}
	
}
