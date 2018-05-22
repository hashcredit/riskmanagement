package com.newdumai.interfaces.iqianbang.accountManagement.test2;

import java.util.HashMap;
import java.util.Map;

import com.newdumai.interfaces.iqianbang.accountManagement.pk.AccountManagementService;

/**
 * 账单查询测试
 */
public class QueryBillTest {

    public static void main(String[] args) {
        Map<String, Object> params= new HashMap<String, Object>();
        params.put("appType","IOS");
        params.put("version","1.1");
        params.put("traceNo","20160113-20170313");
        params.put("regId","13508302050");
        params.put("stmtYearMonth", "201701");
//      params.put("url", "http://112.126.81.154/front.shandianx/bill/queryBill.do");
        params.put("url", "http://www.huahuastore.com/front.shandianx/bill/queryBill.do");
        String result = AccountManagementService.QueryForResult(params);
        System.out.println("result:" + result);
    }

}
