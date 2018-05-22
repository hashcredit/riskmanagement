package com.newdumai.interfaces.iqianbang.accountManagement.test2;

import java.util.HashMap;
import java.util.Map;

import com.newdumai.interfaces.iqianbang.accountManagement.pk.AccountManagementService;

/**
 * 冻结查询测试
 */
public class FrozenTest {

	public static void main(String[] args) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("appType","wechat");
		params.put("version","1.0");
		params.put("traceNo","20160816-123456");
		params.put("regId","15101172308");
//		params.put("url", "http://112.126.81.154/front.shandianx/bill/searchFrozen.do");
		params.put("url", "http://www.huahuastore.com/front.shandianx/bill/searchFrozen.do");
		String result = AccountManagementService.QueryForResult(params);
		System.out.println("result:" + result);
	}
	
}
