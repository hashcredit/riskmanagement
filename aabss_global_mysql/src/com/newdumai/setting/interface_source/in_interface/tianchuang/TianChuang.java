package com.newdumai.setting.interface_source.in_interface.tianchuang;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.junit.Test;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.setting.interface_source.impl.Interface_sourceServiceImpl;
import com.newdumai.setting.interface_source.in_interface.tianchuang.util.AESUtil;
import com.newdumai.setting.interface_source.in_interface.tianchuang.util.JsoupUtil;
import com.newdumai.setting.interface_source.in_interface.tianchuang.util.ParamUtil;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.SpringApplicationContextHolder;

public class TianChuang {
	
	//授权码 appId
	//Rest代码 tokenId
	private static final String url = "http://api.tcredit.com/driver/authMotorVehicleInfo";
//	private static final String appId = "f6d2bf78-8a73-4296-b432-ba825f3a9468" ;
//	private static final String tokenId = "6146491a-cc4a-473f-808b-47dbdb223fa1";
	
	private static final String appId = "72c97740-6be0-412b-8362-1359dc3a3a57" ;
	private static final String tokenId = "036c8c16-9f6f-47fa-bd34-0bb051a80ecc";
	

	private static BaseServiceImpl baseService ;

	/**
	 * 需求1：调试“天创API - 车辆驾驶信息”接口，并根据附件样式展示查得结果，注意返回结果中，cllx hpzl jdczt等字段需要参照文档附件的字典解读。
	 * 通过输入的机动车牌号和号牌种类，获取机动车认证信息。
	 * @param plate
	 * @param plateType
	 * @param url
	 * @param appId
	 * @param tokenId
	 */
	public static String authMotorVehicleInfo(Map<String, Object> map){
		String plate = "";
		String plateType ="";
		if(map.containsKey("plate")){
			plate = map.get("plate").toString();
		}else{
			return "plate 参数不正确";
		}
		if(map.containsKey("plateType")){
			plateType = map.get("plateType").toString();
		}else{
			return "plateType 参数不正确";
		}
		Map<String,String> params = new HashMap<String,String>();
		params.put("plate", AESUtil.encode(appId.replace("-", ""),plate));
		params.put("plateType", plateType);
		String tokenKey = ParamUtil.getTokenKey(url, tokenId, params);
		params.put("tokenKey", tokenKey);
		params.put("appId", appId);
		return exec(params, url);
	}

	/**
	 * 需求2：根据查得结果查询“天创API－车辆驾驶信息－车辆维修保养报告”接口，并根据附件样式展示查得结果。注意该接口需要使用第一个接口的返回结果
	 * 
	 * @param plate
	 * @param plateType
	 * @param url
	 * @param appId
	 * @param tokenId
	 */
	
	@SuppressWarnings({ })
	public static String getVehicleRepairReport(Map<String,Object> map){
		String interface_source_code = (String) map.remove("interface_source_code");
		Map<String, Object> save_detail_info = new HashMap<String,Object>();
		String in_para=new Gson().toJson(map);
		
		//通过输入的机动车牌号和号牌种类，获取机动车认证信息。
		String vehicleInfo =authMotorVehicleInfo(map);
		String authStatus = getInfoByKey(vehicleInfo, "authStatus");
		save_detail_info.put("result", vehicleInfo);
		save_detail_info.put("interface_source_code", interface_source_code);
		save_detail_info.put("in_para", in_para);
		setBaseService();
		String detail_info_code = baseService.addAndRet(save_detail_info,"sys_interface_source_detail_info");
		Map<String,Object> where = new HashMap<String,Object>();
		where.put("code", detail_info_code);
		if(!"0".equals(authStatus))return detail_info_code+"#detail_info_code#"+vehicleInfo;
		
		//根据车辆识别代码vin查询是否支持车辆品牌
		String vin = getInfoByKey(vehicleInfo, "clsbdh");
		String fdjh = getInfoByKey(vehicleInfo, "fdjh");
		String brandInfo = checkBrand(vin, appId, tokenId);
		String checkBrand = getInfoByKey(brandInfo, "result");
		update_detail_info("result2", brandInfo, where);
		if(!"2".equals(checkBrand))return detail_info_code+"#detail_info_code#"+brandInfo;
		
		//输⼊入vin码、车牌号、发动机号购买报告获取订单号
		String orderInfo = getOrderId(map.get("plate").toString(), fdjh, vin, appId, tokenId);
		String orderResult = getInfoByKey(orderInfo, "result");
		update_detail_info("result3", orderInfo, where);
		if(!"1".equals(orderResult))return detail_info_code+"#detail_info_code#"+orderInfo;
		
		//查询订单状态
		String orderId = getInfoByKey(orderInfo, "orderId");
		String orderStatus = getOrderStatus(orderId, appId, tokenId);
		String orderStatusResult = getInfoByKey(orderStatus, "result");
		update_detail_info("result4", orderStatus, where);
		if(!"2".equals(orderStatusResult))return detail_info_code+"#detail_info_code#"+orderStatus;
		
		//通过订单号获取审核通过的报告
		String orderReport = getReport(orderId, appId, tokenId);
		update_detail_info("result5", orderReport, where);
		return detail_info_code+"#detail_info_code#"+orderReport ;
	}
	
	/**
	 * 获取个人公积金缴纳信息
	 * @param step
	 * @param result
	 * @param where
	 */
	public static String getFundInfo(Map<String,Object> map){
		Map<String,String> params = new HashMap<String,String>();
		String name = "";
		String idcard ="";
		if(map.containsKey("name")){
			name = (String)map.get("name");
			params.put("name", name);
		}
		if(map.containsKey("idcard")){
			idcard = (String)map.get("idcard");
			params.put("idcard", idcard);
		}
		String url = "http://api.tcredit.com/identity/getFundInfo";
		String tokenKey = ParamUtil.getTokenKey(url, tokenId, params);
		params.put("tokenKey", tokenKey);
		params.put("appId", appId);
		return exec(params,url);
	}
	
	private static void update_detail_info(String step,String result,Map<String,Object>where) {
		Map<String,Object> update_detail_info = new HashMap<String,Object>();
		update_detail_info.put(step, result);
		baseService.Update(update_detail_info, "sys_interface_source_detail_info", where);
	}

	private static void setBaseService() {
		baseService = (BaseServiceImpl) SpringApplicationContextHolder.getBean(Interface_sourceServiceImpl.class);
	}
	
	@SuppressWarnings({ "unchecked" })
	private static String getInfoByKey(String data,String key){
		String result = "";
		try {
			result = ((Map<String,Object>) JsonToMap.gson2Map(data).get("data")).get(key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}
	
	private static String checkBrand(String vin,String appId ,String tokenId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("vin", vin);
		String url = "http://api.tcredit.com/driver/maintenance/checkBrand";
		String tokenKey = ParamUtil.getTokenKey(url, tokenId, params);
		params.put("tokenKey", tokenKey);
		params.put("appId", appId);
		return exec(params,url);
	}
	
	private static String getOrderId(String plate,String engine ,String vin,String appId ,String tokenId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("vin", vin);
		params.put("plate", plate);
		params.put("engine", engine);
		String url = "http://api.tcredit.com/driver/maintenance/getOrderId";
		String tokenKey = ParamUtil.getTokenKey(url, tokenId, params);
		params.put("tokenKey", tokenKey);
		params.put("appId", appId);
		return exec(params,url);
	}

	private static String getOrderStatus(String orderId,String appId ,String tokenId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("orderId", orderId);
		String url = "http://api.tcredit.com/driver/maintenance/getOrderStatus";
		String tokenKey = ParamUtil.getTokenKey(url, tokenId, params);
		params.put("tokenKey", tokenKey);
		params.put("appId", appId);
		return exec(params,url);
	}
	
	private static String getReport(String orderId,String appId ,String tokenId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("orderId", orderId);
		String url = "http://api.tcredit.com/driver/maintenance/getReport";
		String tokenKey = ParamUtil.getTokenKey(url, tokenId, params);
		params.put("tokenKey", tokenKey);
		params.put("appId", appId);
		return exec(params,url);
	}
	
	private static String exec(Map<String, String> params,String url){
		Response content = null;
		String result = null;
		try {
			content = JsoupUtil.getContent(url, params, null, Method.POST);
			result = content.body();
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;	
	}
	
	@Test
	public void testAuthMotorVehicleInfo(){
		//{"data":{"authResult":"查询成功_有数据","authStatus":"0","ccdjrq":"2015/9/2 1","cllx":"K31","clsbdh":"LSVYB65L4E2014558","clxh":"SVW6452FGD","cphm":"晋A1709U","cpxh":"斯柯达牌SVW6452FGD","csys":"I","fdjh":"6811","fdjxh":"CSS","hpzl":"02","jdcsyr":"张学梅","jdczt":"G","jyyxqz":"2017/9/30","syxz":"A","zwpp":"斯柯达牌"},"message":"成功","seqNum":"0cbb0cf66d2d4c0283c691307e99cf52","status":0}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("plate", "晋A1709U");
		map.put("plateType", "02");
		System.out.println(authMotorVehicleInfo(map));
		
	}
	/*@Test
	public void test(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", "张继涛");
		map.put("idcard", "230119197505063913");
		System.out.println(TianChuang.getFundInfo(map));
	}*/
	
}
