package test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.newdumai.util.JsonToMap;



public class GpsTest {

	public static void main(String[] args) throws Exception {
		
		/*String html = exec("http://localhost:8080/dumai_qt/", "0", "0", null);
		System.out.println(html);
		Map<String, Object> inParams1 = new HashMap<String, Object>();
		inParams1.put("name", "ksd");
		inParams1.put("pass", "456123");
		String loginResult = exec("http://localhost:8080/dumai_qt/login.do", "1", "0", inParams1);
		System.out.println(loginResult);
		String loanfront = exec("http://localhost:8080/dumai_qt/sysmgr/userList.do", "1", "0", null);
		System.out.println(loanfront);*/
		String is_post1 = "0";
		String is_json1 = "0";
		Map<String, Object> inParams1 = new HashMap<String, Object>();
		inParams1.put("puser.userName", "aiqibang");
		inParams1.put("puser.userPwd", "123456");
		inParams1.put("puser.serverId", "10854");
		inParams1.put("pt", "1");
		String url1 = "http://vip4.exlive.cn/synthReports/mobile_to_synth/zdyloginAction_autologin.action";
		String result1 = exec(url1, is_post1, is_json1, inParams1);
		System.out.println("结果1：" + result1);
		Map<String, Object> result1Map = JsonToMap.gson2Map(result1);
		//----------------------------------
		
		String is_post2 = "0";
		String is_json2 = "1";
		Map<String, Object> inParams2 = new HashMap<String, Object>();
		inParams2.put("tableName", "vehicle");
		inParams2.put("pager.offset", "0");
		inParams2.put("pageSize", "8");
		inParams2.put("", "0");
		inParams2.put("vehicle.gprs", "816090501052516");
		inParams2.put("exkey", result1Map.get("exkey"));
		
		String url2 = "http://vip4.exlive.cn/synthReports/mobile_to_synth/mobileTosynthAction_find.action";
		String result2 = exec(url2, is_post2, is_json2, inParams2);
		System.out.println("结果2：" + result2);
		
		//----------------------------------
		
		/*String is_post3 = "0";
		String is_json3 = "0";
		Map<String, Object> inParams3 = new HashMap<String, Object>();
		inParams3.put("version", "1");
		inParams3.put("method", "vLoginSystem");
		inParams3.put("name", "chaiwang1");
		inParams3.put("pwd", "000000");
		
		String url3 = "http://139.129.196.78:89/gpsonline/GPSAPI";
		String result3 = exec(url3, is_post3, is_json3, inParams3);
		System.out.println("结果3：" + result3);
		
		//----------------------------------
		
		String is_post4 = "0";
		String is_json4 = "0";
		Map<String, Object> inParams4 = new HashMap<String, Object>();
		inParams4.put("version", "1");
		inParams4.put("method", "loadLocation");
		inParams4.put("vid", "8422597");
		inParams4.put("vKey", "e86546887eef7804188ce18b851e442e");
		
		String url4 = "http://139.129.196.78:89/gpsonline/GPSAPI";
		String result4 = exec(url4, is_post4, is_json4, inParams4);
		System.out.println("结果4：" + result4);*/
		                        
	}
	
	public static Header[] header;
	/**
	 * 执行http(s)请求,最初用于第三方数据源
	 * 
	 * @param url
	 * @param is_post
	 *            : 0 get 1 post
	 * @param is_json
	 *            : 0 非json 1 json
	 * @param inParams
	 *            ：传入参数map
	 * @return
	 */
	public static CloseableHttpClient httpClient = null;
	public static String exec(String url, String is_post, String is_json, Map<String, Object> inParams) {
		
		HttpResponse resp = null;
		try {
			if(httpClient==null){
				if (url.startsWith("https")) {
					httpClient = exec_ignoreSSLHttpClient();
				} else {
					HttpHost host = new HttpHost("localhost", 8888, "http");
					httpClient = HttpClients.custom().setProxy(host).build();
				}
			}
			/*
			 * setConnectTimeout：设置连接超时时间，单位毫秒
			 * setConnectionRequestTimeout：设置从connect Manager获取Connection
			 * 超时时间，单位毫秒 setSocketTimeout：请求获取数据的超时时间，单位毫秒
			 */
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setConnectTimeout(60000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
			HttpUriRequest request = null;
			if ("1".equals(is_post)) {
				// post
				HttpPost httpost = new HttpPost(url);
				
				if (is_json.equals("0")) {// 0 非json
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					if (null != inParams) {
						// 把map转换成list传入UrlEncodedFormEntity
						Iterator<Entry<String, Object>> iterator = inParams.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, Object> elem = (Entry<String, Object>) iterator.next();
							list.add(new BasicNameValuePair(elem.getKey(), elem.getValue().toString()));
						}
					}
					// 表单形式
					UrlEncodedFormEntity urlFormEntity = new UrlEncodedFormEntity(list);
					urlFormEntity.setContentEncoding("UTF-8");
					httpost.setEntity(urlFormEntity);
					list = null;
				} else {// json
					httpost.setHeader("Content-Type", "application/json");
					String json = new Gson().toJson(inParams);
					StringEntity entity = new StringEntity(json, "UTF-8");
					httpost.setEntity(entity);
				}
				httpost.setConfig(requestConfig);
				request = httpost;
				//resp = httpClient.execute(httpost);
			} else {
				// get
				URIBuilder builderGet = new URIBuilder(url);
				builderGet.setCharset(Charset.forName("UTF-8"));
				if (inParams != null) {
					Set<String> keySet = inParams.keySet();
					for (String key : keySet) {
						Object value = inParams.get(key);
						builderGet.addParameter(key, value == null ? "" : value.toString());
					}
				}
				HttpGet httpGet = new HttpGet(builderGet.build());
				httpGet.setConfig(requestConfig);
				request = httpGet;
				//resp = httpClient.execute(httpGet);
			}
			
			/*if(header!=null && header.length!=0){
				
				String headerString = "";
				for(Header head : header){
					headerString += head.getValue().split(";")[0] +"; ";
				}
				request.addHeader(new BasicHeader("Cookie", headerString.substring(0,headerString.length()-2)));
			}
			*/
			resp = httpClient.execute(request);
			// 请求结果result
			int resultCode = resp.getStatusLine().getStatusCode();
			if (resultCode == 200) {
				//header = resp.getHeaders("Set-Cookie");
				String result = EntityUtils.toString(resp.getEntity(), "UTF-8");
				return result;
			} else {
				throw new Exception("返回结果异常..." + resultCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			/*if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
		}

	}

	/**
	 * 获取忽略证书的https协议的HttpClient
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private static CloseableHttpClient exec_ignoreSSLHttpClient() throws Exception {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return true;
			}
		}).build();
		HttpHost host = new HttpHost("localhost", 8888, "http");
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return HttpClients.custom().setProxy(host).setSSLSocketFactory(sslsf).build();
	}
}
