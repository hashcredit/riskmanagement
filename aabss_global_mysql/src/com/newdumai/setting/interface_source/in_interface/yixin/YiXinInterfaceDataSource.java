package com.newdumai.setting.interface_source.in_interface.yixin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.newdumai.util.JsonToMap;

public class YiXinInterfaceDataSource {
	private static final String apiUrlForLoanQuery= "http://www.zhichengcredit.com/CreditPortal/api/queryLoan/v2";
	private static final String apiUrlForBlackListQuery="http://www.zhichengcredit.com/CreditPortal/api/queryBlackList/v2";
	
	/*private static final String userId="iqianbang_testusr";
	private static final String	password="e6c013750508fff2";*/
	
	private static final String userId="iqianbang";
	private static final String	password="0ce4945e802f16bb";
	
	
	public static String getEncryptUserId(){
		RSAPublicKey rsaPublicKey = RSA_1024_V2.gainRSAPublicKeyFromCrtFile(YiXinInterfaceDataSource.class.getResource("ZC_PublicKey_V2.crt").getFile());
		String encryptedUserID = RSA_1024_V2.encodeByPublicKey(rsaPublicKey, userId);
		return encryptedUserID;
	}
	
	private String decodeResult(String result){
		
		Map<String,Object> json = JsonToMap.jsonToMap(result);
		if (json.containsKey("errorcode") && "0000".equals(json.get("errorcode"))) {
			// 解密解码返回结果
			String decryptResult = RC4_128_V2.decode((String) json.get("data"), password);
			try {
				decryptResult = URLDecoder.decode(decryptResult, "utf-8");
				json.put("data", JsonToMap.jsonToMap(decryptResult));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new Gson().toJson(json);
	}
	
	public String queryLoan(Map<String,Object> map){
		RestTemplate restTemplate = new RestTemplate();
		String idType = "101"; //身份证
		String queryReason = "10";
	
		
		MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<String, String>();
		paraMap.add("userid", getEncryptUserId());
		// 加密参数部分
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", map.get("name"));//去除传入的空格
		params.put("idNo",map.get("idNo"));//去除传入的空格
		params.put("idType", idType);
		params.put("queryReason",queryReason);
		String encryptedParams = null;
		try {
			encryptedParams = RC4_128_V2.encode(URLEncoder.encode(new Gson().toJson(params), "utf-8"), password);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		paraMap.add("params", encryptedParams);
		
		//1、借款信息查询
		String result = restTemplate.postForObject(apiUrlForLoanQuery, paraMap,String.class);
		return decodeResult(result);
	}
	public String queryBlackList(Map<String,Object> map){
		RestTemplate restTemplate = new RestTemplate();
		String idType = "101"; //身份证
		String queryReason = "10";
		
		
		MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<String, String>();
		paraMap.add("userid", getEncryptUserId());
		// 加密参数部分
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", map.get("name"));//去除传入的空格
		params.put("idNo",map.get("idNo"));//去除传入的空格
		params.put("idType", idType);
		params.put("queryReason",queryReason);
		String encryptedParams = null;
		try {
			encryptedParams = RC4_128_V2.encode(URLEncoder.encode(new Gson().toJson(params), "utf-8"), password);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		paraMap.add("params", encryptedParams);
		
		//黑名单
		String message = restTemplate.postForObject(apiUrlForBlackListQuery, paraMap,String.class);
		return decodeResult(message);
	}
	public static void main(String[] args) {
//		Map<String,Object> map = new HashMap<String, Object>();
//		
//		map.put("name", "熊巍");
//		map.put("idNo", "320506197107277790");
//		
//		System.out.println(new YiXinInterfaceDataSource().queryLoan(map));
		
		//System.out.println(new YiXinInterfaceDataSource().decodeResult("{\"errorcode\":\"0000\",\"message\":\"查询成功\",\"data\":\"sN+Hobxn82u0GnbyNSKe1suNAa44wMKuBtB5c5EFUJc817fqNGRMa3OabtExd8Vu6lejPasVduat\"}"));
	}
}
