package com.newdumai.setting.interface_source.in_interface.pingAn.util;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class PingAnHttpHelper {

	public CloseableHttpClient getHttpClient(String url) throws Exception {
		if (url.startsWith("https")) {
			String path = "";
			String CRET_FILE_PATH = "cacerts";
			path = this.getClass().getResource("PingAnHttpHelper.class").getFile();
			path = path.substring(0,path.indexOf("PingAnHttpHelper.class"))+ "/";
			return getSSLHttpClient(path + CRET_FILE_PATH, "123456");
		}
		return HttpClients.createDefault();
	}
	
	private CloseableHttpClient getSSLHttpClient(String ketstore,String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(
					new File(ketstore),
					password.toCharArray(),
					(TrustStrategy) new TrustSelfSignedStrategy()
					).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}
	
	public Map<String, String> getBaseParams(Map<String, String> params)throws NoSuchAlgorithmException {
		String APP_ID = "120160805001";
		String PKEY = "4465a59579cbfb01d564088fae77369e";
		String ptime = String.valueOf(System.currentTimeMillis());
		params.put("pname", APP_ID);
		params.put("ptime", ptime);
		params.put("vkey", MD5Util(PKEY, ptime));
		return params;
	}
	
	private String MD5Util(String pkey, String ptime)throws NoSuchAlgorithmException {
		String oriental = pkey + "_" + ptime + "_" + pkey;
		String vkey = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(oriental.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			vkey = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return vkey;
	}
	
	public HttpPost postForm(String url, Map<String, String> params)throws Exception {
		HttpPost httpost = new HttpPost(url.trim());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for (String key : params.keySet()) {
			builder.addPart(key, new StringBody(params.get(key),ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		}
		httpost.setEntity(builder.build());
		return httpost;
	}
	
	
}
