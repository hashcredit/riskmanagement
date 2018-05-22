package com.newdumai.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;

/**
 * @author zgl yuexiao
 * @datetime 2016-12-09
 */
public class HttpClientUtil {

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
	public static String exec(String url, String is_post, String is_json, Map<String, Object> inParams) {
		CloseableHttpClient httpClient = null;
		HttpResponse resp = null;
		try {
			if (url.startsWith("https")) {
				httpClient = exec_ignoreSSLHttpClient();
			} else {
				httpClient = HttpClients.createDefault();
			}

			/*
			 * setConnectTimeout：设置连接超时时间，单位毫秒
			 * setConnectionRequestTimeout：设置从connect Manager获取Connection
			 * 超时时间，单位毫秒 setSocketTimeout：请求获取数据的超时时间，单位毫秒
			 */
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();

			if ("1".equals(is_post)) {
				// post
				HttpPost httpost = new HttpPost(url);
				if (is_json.equals("0")) {// 0 非json
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					if (!CollectionUtils.isEmpty(inParams)) {
						// 把map转换成list传入UrlEncodedFormEntity
						for(String key : inParams.keySet()){
							list.add(new BasicNameValuePair(key, inParams.get(key) == null ? "" : String.valueOf(inParams.get(key))));
						}
					}
					// 表单形式
					UrlEncodedFormEntity urlFormEntity = new UrlEncodedFormEntity(list,"UTF-8");
					//urlFormEntity.setContentEncoding("UTF-8");
					httpost.setEntity(urlFormEntity);
					list = null;
				} else {// json
					httpost.setHeader("Content-Type", "application/json;charset=UTF-8");
					String json = new Gson().toJson(inParams);
					StringEntity entity = new StringEntity(json, "UTF-8");
					httpost.setEntity(entity);
				}
				httpost.setConfig(requestConfig);
				resp = httpClient.execute(httpost);
			} else {
				// get
				URIBuilder builderGet = new URIBuilder(url);
				builderGet.setCharset(Charset.forName("UTF-8"));
				if (!CollectionUtils.isEmpty(inParams)) {
					for (String key : inParams.keySet()) {
						builderGet.addParameter(key, inParams.get(key) == null ? "" : (String)inParams.get(key));
					}
				}
				HttpGet httpGet = new HttpGet(builderGet.build());
				httpGet.setConfig(requestConfig);
				resp = httpClient.execute(httpGet);
			}
			// 请求结果result
			int resultCode = resp.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(resp.getEntity(), "UTF-8");
			if (resultCode == 200) {
				return result;
			} else {
				throw new Exception("返回结果异常..." + resultCode+",服务器返回:"+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

}
