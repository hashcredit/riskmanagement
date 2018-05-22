package com.newdumai.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;

/**
 * HTTP File Server 客户端<br/>
 * 通过构造方法指定根路径，用户名，密码创建客户端<br/>
 * 此客户端有以下功能:
 * <ul>
 * 	<li>创建目录</li>
 * 	<li>删除目录</li>
 * 	<li>下载文件</li>
 * 	<li>上传文件</li>
 * 	<li>判断文件是否存在</li>
 * </ul>
 * @author 岳晓
 * @version 1.0.0
 */
public class HFSClient {
	
	/**
	 * 文件服务器根路径
	 */
	private String baseURL;
	/**
	 * 认证头信息
	 */
	private Header authHeader;
	
	public HFSClient(String baseURL, String userName,String pwd) {
		super();
		this.baseURL = baseURL;
		String authHeaderValue = null;
		try {
			authHeaderValue = "Basic " + new String(new BASE64Encoder().encode((userName +":" + pwd).getBytes("utf-8")));
			System.out.println(authHeaderValue);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.authHeader = new BasicHeader("Authorization",authHeaderValue);
	}

	/**
	 * 创建目录
	 * @param folderName 文件夹名称
	 * @since 1.0.0
	 * @throws IOException
	 */
	public void createFolder(String folderName) throws IOException{
		
		CloseableHttpClient httpClient =HttpClients.custom().build();
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		HttpPost httpost = new HttpPost(baseURL + "?mode=section&id=ajax.mkdir");
		httpost.addHeader(authHeader);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		builder.addPart("name", new StringBody(folderName,ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		builder.addPart("token", new StringBody("0.643863540980965",ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		
		httpost.setEntity(builder.build());
		CloseableHttpResponse resp = httpClient.execute(httpost);
		System.out.println(resp.getStatusLine().getReasonPhrase());
		String result = EntityUtils.toString(resp.getEntity(),"UTF-8");
		System.out.println(result);
		httpClient.close();
	}
	
	/**
	 * 删除文件夹
	 * @param folderName 要删除的文件夹
	 * @throws IOException
	 */
	public  void delFolder(String folderName) throws ClientProtocolException, IOException{
		CloseableHttpClient httpClient =HttpClients.custom().build();
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		HttpPost httpost = new HttpPost(baseURL);
		
		
		httpost.addHeader(authHeader);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		builder.addPart("selection", new StringBody(folderName,ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		builder.addPart("action", new StringBody("delete",ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		
		httpost.setEntity(builder.build());
		CloseableHttpResponse resp = httpClient.execute(httpost);
		System.out.println(resp.getStatusLine().getReasonPhrase());
		String result = EntityUtils.toString(resp.getEntity(),"UTF-8");
		System.out.println(result);

		httpClient.close();
	}
	
	/**
	 * 下载文件
	 * @param filePath 服务器端文件路径
	 * @param downLoadPath 下载文件指定的路径(包含名称)
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public  void downloadFile(String filePath,String downLoadPath) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpClient =HttpClients.custom().build();
		HttpGet  get = new HttpGet (baseURL + escapeURLPath(filePath));
		get.addHeader(authHeader);
		
		CloseableHttpResponse response = httpClient.execute(get);
		if(response.getStatusLine().getStatusCode()==200){
			String contentType = response.getFirstHeader("Content-Type").getValue();
			if(contentType.equals("text/html")){
				throw new IOException(filePath + "是目录");
			}
			else if(contentType.equals("application/octet-stream")){
				InputStream in = response.getEntity().getContent();
				IOUtils.copy(in, new FileOutputStream(downLoadPath));
				in.close();
			}
		}
		else{
			throw new IOException(filePath + " 不存在");
		}
		
	}
	
	/**
	 * 对HTTP路径进行编码和规范化<br/>
	 * '\'转为'/'，多个'/'转为一个'/'
	 * @param pathName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String escapeURLPath(String pathName) throws UnsupportedEncodingException{
		
		if(pathName==null) return "";
		
		String pathNames = pathName.replaceAll("\\\\","/").replaceAll("(\\/)+", "/");
		String[] paths = pathNames.split("/");
		StringBuilder standardFilePath = new StringBuilder();
		for(String path : paths){
			//URLEncoder.encode会把空格会转化为'+'，HFS服务器不会解码，需要替换为%20
			String str = URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20");
			standardFilePath.append(str).append("/");
		}
		return standardFilePath.length()==0?pathNames:standardFilePath.substring(0, standardFilePath.length()-1);
	}
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return true存在，false不存在
	 * @throws HttpException
	 * @throws IOException
	 */
	public  boolean exists(String filePath) throws IOException{
		
		CloseableHttpClient httpClient =HttpClients.custom().build();
		
		HttpGet get = new HttpGet (baseURL + escapeURLPath(filePath));
		get.addHeader(authHeader);
		
		return httpClient.execute(get).getStatusLine().getStatusCode()==200;
	}
	
	/**
	 * 更新文件名称
	 * @param fileName 原文件名称
	 * @param newName 新文件名称
	 * @throws IOException
	 */
	public void updateName(String fileName,String newName) throws ClientProtocolException, IOException{
		
		CloseableHttpClient httpClient =HttpClients.custom().build();
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		HttpPost httpost = new HttpPost(baseURL + "?mode=section&id=ajax.rename");
		httpost.addHeader(authHeader);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		builder.addPart("from", new StringBody(fileName,ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		builder.addPart("to", new StringBody(newName,ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		builder.addPart("token", new StringBody("0.643863540980965",ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		
		httpost.setEntity(builder.build());
		CloseableHttpResponse resp = httpClient.execute(httpost);
		System.out.println(resp.getStatusLine().getReasonPhrase());
		String result = EntityUtils.toString(resp.getEntity(),"UTF-8");
		System.out.println(result);
		httpClient.close();
	}
	
	
	/**
	 * 上传文件
	 * @param filePath 本地文件路径
	 * @param serverFilePath 上传到服务器指定的文件路径(包含文件名)
	 * @throws IOException
	 */
	public boolean uploadFile(String filePath,String serverFilePath) throws IOException{
		
		if(filePath==null){
			throw new IllegalArgumentException("filePath can't be null!");
		}
		
		CloseableHttpClient httpClient =HttpClients.custom().build();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		int index = serverFilePath.lastIndexOf("/");
		String parentPath = "";
		String fileName = serverFilePath;
		if(index>=0){
			parentPath = serverFilePath.substring(0,serverFilePath.lastIndexOf("/"));
			fileName = serverFilePath.substring(serverFilePath.lastIndexOf("/")+1);
		}
		
		HttpPost httpost = new HttpPost(baseURL + escapeURLPath(parentPath));
		httpost.addHeader(authHeader);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		builder.addPart(fileName, new FileBody(new File(filePath),ContentType.DEFAULT_BINARY, fileName));
		
		httpost.setEntity(builder.build());
		
		CloseableHttpResponse resp = httpClient.execute(httpost);
		try {
			String result = EntityUtils.toString(resp.getEntity());
			System.out.println(result);
			if(resp.getStatusLine().getStatusCode()==200){
				return result.contains("1 files uploaded correctly.");
			}
			else{
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			httpClient.close();
		}
	}
}
