package com.newdumai.setting.interface_source.in_interface.yixin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.newdumai.util.JsonToMap;

public class YiXin {
	/*@Test
	public void doTest() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name","井庆生");//去除传入的空格
		params.put("idNo","320506197107277790");//去除传入的空格
//		params.put("idType", "101");
//		params.put("queryReason","10");
		params.put("url","http://www.zhichengcredit.com/CreditPortal/api/queryLoan/v2");
//		params.put("userId","iqianbang_testusr");
//		params.put("password","e6c013750508fff2");
		String s = QueryYiXin(params);
		System.out.println(s);
	}*/
	
	public static String QueryYiXin(Map<String,Object> params){
		params.put("idType", "101");
		params.put("queryReason","10");
		
		params.put("userId","iqianbang_testusr");
		params.put("password","e6c013750508fff2");
		
		//params.put("userId","iqianbang");
		//params.put("password","0ce4945e802f16bb");
		MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<String, String>();
		String encryptedParams = null;
		try {
			encryptedParams = RC4_128_V2.encode(URLEncoder.encode(new Gson().toJson(params), "utf-8"), (String) params.get("password"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		paraMap.add("userid",  getEncryptUserId((String) params.get("userId")));
		paraMap.add("params", encryptedParams);
		//1、借款信息查询
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject((String) params.get("url"), paraMap,String.class);
		return decodeResult(result,(String) params.get("password"));
	}
	
	private static String decodeResult(String result,String password){
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
	private static String getEncryptUserId(String userId){
		RSAPublicKey rsaPublicKey = RSA_1024_V2.gainRSAPublicKeyFromCrtFile(YiXin.class.getResource("ZC_PublicKey_V2.crt").getFile());
		String encryptedUserID = RSA_1024_V2.encodeByPublicKey(rsaPublicKey, userId);
		return encryptedUserID;
	}
}
