package com.newdumai.interfaces.iqianbang.accountManagement.test2;

import java.util.HashMap;
import java.util.Map;

import com.newdumai.interfaces.iqianbang.accountManagement.pk.AccountManagementConfigFile;
import com.newdumai.interfaces.iqianbang.accountManagement.pk.AccountManagementService;
import com.newdumai.interfaces.iqianbang.accountManagement.util.DigestUtils;
import com.newdumai.interfaces.iqianbang.accountManagement.util.HttpUtils;
import com.newdumai.interfaces.iqianbang.accountManagement.util.SignUtil;



/**
 * 黑名单查询测试
 */
public class BlackTest {

	public static void main(String[] args) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("appType", "IOS");
		params.put("version", "1.0");
		params.put("traceNo", "20150812-20161214");
		params.put("regId", "18846845286");
//		params.put("url", "http://112.126.81.154/front.shandianx/bill/searchBalck.do");
		params.put("url", "http://www.huahuastore.com/front.shandianx/bill/searchBalck.do");
		String result = AccountManagementService.QueryForResult(params);
		System.out.println("result:" + result);
	}
	
}
