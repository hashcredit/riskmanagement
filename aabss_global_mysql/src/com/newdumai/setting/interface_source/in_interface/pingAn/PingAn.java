package com.newdumai.setting.interface_source.in_interface.pingAn;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.newdumai.setting.interface_source.in_interface.pingAn.util.PingAnHttpHelper;

public class PingAn {

	
	/*@Test
	public void doTest(){
		Map<String,Object> params = new HashMap<String,Object>();
		
		String name = "井庆生";
		String idCard = "370102197508262532";
		String phone = "13581722270";
		params.put("name", name);
		params.put("idCard", idCard);
		params.put("phone", phone);
		String url = "https://i.trustutn.org/b/blacklist";
		params.put("url", url);
		String result = PingAn.QueryPingAn(params);
		System.out.println(result);
	}*/
	
	/**
	 * 凭安数据源查询
	 * @param params
	 * @return
	 */
	public static String QueryPingAn(Map<String,Object>params){
		String url = params.get("url").toString();
		String result = null;
		try {
			Map<String, String> sendData = setParams(params);
			PingAnHttpHelper dsh = new PingAnHttpHelper();
			CloseableHttpClient httpclient = dsh.getHttpClient(url);
			dsh.getBaseParams(sendData);
			HttpPost post = dsh.postForm(url, sendData);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000).build();
			post.setConfig(requestConfig);
			HttpResponse response = httpclient.execute(post);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * 过滤出我想要的发送参数，其他的参数随意放，但是会过滤掉无用的
	 * @param params
	 * @return
	 */
	private static Map<String,String> setParams(Map<String,Object>params){
		Map<String,String> sendData = new HashMap<String,String>();
		if(params.containsKey("name")){
			sendData.put("name", (String) params.get("name"));
		}
		if(params.containsKey("idCard")){
			sendData.put("idCard", params.get("idCard").toString());
		}
		if(params.containsKey("phone")){
			sendData.put("phone", params.get("phone").toString());
		}
		if(params.containsKey("contactMain")){
			if (StringUtils.isNotEmpty((String)params.get("contactMain"))) {
				sendData.put("contactMain",(String)params.get("contactMain")+",,");
			}
			if (StringUtils.isNotEmpty((String)params.get("contactPhone1"))) {
				sendData.put("contacts",(String)params.get("contactPhone1")+",,");
			}
			if (StringUtils.isNotEmpty((String)params.get("contactPhone2"))) {
				sendData.put("contacts",sendData.get("contacts")+","+(String)params.get("contactPhone2")+",,");
			}
		}
		return sendData;
	}
}
