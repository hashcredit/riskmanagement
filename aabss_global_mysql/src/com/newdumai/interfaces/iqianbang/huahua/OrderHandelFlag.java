package com.newdumai.interfaces.iqianbang.huahua;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.google.gson.GsonBuilder;
import com.newdumai.loanFront.AuditResultConstant;
import com.newdumai.util.AsynchronousThreadHelper;
import com.newdumai.util.DaoHelper;
import com.newdumai.util.HttpClientUtil;

/**
 * 字段名：order_handel_flag
 * 字段值：pro、test_s、test_f
 * 说明：pro（正式订单）、test_s（测试通过单）、test_f(测试拒绝单)
 */
public enum OrderHandelFlag {
	
	pro , test_s , test_f ;
	
	/**
	 * 测试单通道
	 * @param orderMap
	 * @param order_handel_flag
	 * @return
	 */
	public static Map<String,Object> goTestWay(Map<String,Object>orderMap,String order_handel_flag){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("return", 10008);
		result.put("message", "提交成功，等待审核");
		AsynchronousThreadHelper.runNewThread(OrderHandelFlag.class.getName()+"#sendAuditResult",new Class[]{Map.class,String.class},orderMap,order_handel_flag);
		return result;
	}
	
	/**
	 * 测试单发送风控回调结果
	 * @param orderMap
	 * @param order_handel_flag
	 */
	private static void sendAuditResult(Map<String,Object>orderMap,String order_handel_flag){
		if(!org.springframework.util.CollectionUtils.isEmpty(orderMap)){
			String url = (String) orderMap.get("noticeUrl");
			String orderid = (String) orderMap.get("orderid");
			Map<String,Object>inParams = new HashMap<String,Object>();
			inParams.put("orderId", orderid);
			if(OrderHandelFlag.test_s.name().equals(order_handel_flag)){
				inParams.put("riskStatus", 1);
				inParams.put("message", AuditResultConstant.PASS);
				inParams.put("messageInfo", "测试通过单");
				inParams.put("model_score", String.valueOf(348));
				inParams.put("model_grade", "A");
			}else if(OrderHandelFlag.test_f.name().equals(order_handel_flag)){ 
				inParams.put("riskStatus", 2);
				inParams.put("message", AuditResultConstant.REFUSE);
				inParams.put("messageInfo", "测试拒绝单");
			}
			String result = null;
			try {
				if(StringUtils.isNotEmpty(url)){
					Thread.sleep(45000);
					result = HttpClientUtil.exec(url,"1","1",inParams);
				}else{
					result = "url,不存在,未能发送回调结果！";
				}
			} catch (Exception e) {     
				e.printStackTrace();
				result = "发送或接受回传结果出错！";
			}
			System.out.println(result);
			//保存发送记录结束！
			DaoHelper.getDumai_newBaseDao().insert(" insert into send_message_log (code,fk_orderinfo_code,send_message,recieve_message) values (?,?,?,?) ", new Object[]{UUID.randomUUID().toString(),orderid,new GsonBuilder().serializeNulls().create().toJson(inParams),result});
			System.out.println("保存发送记录结束！");
		}
	}
}
