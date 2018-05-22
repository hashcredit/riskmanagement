package test;

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

public class HFSUtils {
	
	/**
	 * 文件服务器根路径
	 */
	private static final String baseURL = "http://192.168.1.10:280/root/";
	
	/**
	 * 认证头信息
	 */
	private static final Header authHeader = new BasicHeader("Authorization","Basic YWRtaW46aWR1bWFp");
	
	/**
	 * 对HTTP路径进行编码和规范化<br/>
	 * '\'转为'/'，多个'/'转为一个'/'
	 * @param pathName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String escapeURLPath(String pathName) throws UnsupportedEncodingException{
		
		if(pathName==null) return "";
		
		String pathNames = pathName.replaceAll("\\\\","/").replaceAll("(\\/)+", "/");
		String[] paths = pathNames.split("/");
		StringBuilder standardFilePath = new StringBuilder();
		for(String path : paths){
			String str = URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20");
			standardFilePath.append(str).append("/");
		}
		return standardFilePath.length()==0?pathNames:standardFilePath.substring(0, standardFilePath.length()-1);
	}
	
	/**
	 * 
	 * @param args
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		//String a = "aa/aa///a\\\\/a".replaceAll("\\\\","/").replaceAll("(\\/)+", "/");
		//System.out.println("".split("/").length);
		//System.out.println(exists("/t1/t/"));
		
		boolean flag = uploadFile("d:/GreenSoft/MyEclipse Professional 2014.rar", "MyEclipse Professional 2014.rar");
		System.out.println(flag);
	}
	
	/**
	 * 删除文件夹
	 * @param folderName 要删除的文件夹
	 * @throws IOException
	 */
	public static void delFolder(String folderName) throws ClientProtocolException, IOException{
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
	 * 更新文件名称
	 * @param fileName
	 * @param newName
	 * @throws IOException
	 */
	public static void updateName(String fileName,String newName) throws ClientProtocolException, IOException{
		
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
	 * 创建目录
	 * @param folderName 文件夹名称
	 * @throws IOException
	 */
	public static void createFolder(String folderName) throws IOException{
		
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
	 * 上传文件
	 * @param filePath 本地文件路径
	 * @param serverFilePath 上传到服务器指定的文件路径(包含文件名)
	 * @throws IOException
	 */
	public static boolean uploadFile(String filePath,String serverFilePath) throws IOException{
		
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
			if(resp.getStatusLine().getStatusCode()==200){
				String result = EntityUtils.toString(resp.getEntity());
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
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return true存在，false不存在
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean exists(String filePath) throws IOException{
		
		CloseableHttpClient httpClient =HttpClients.custom().build();
		
		HttpGet get = new HttpGet (baseURL + escapeURLPath(filePath));
		get.addHeader(authHeader);
		
		return httpClient.execute(get).getStatusLine().getStatusCode()==200;
	}
	
	
	/**
	 * 下载文件
	 * @param filePath 服务器端文件路径
	 * @param downLoadPath 下载文件指定的路径(包含名称)
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void downloadFile(String filePath,String downLoadPath) throws ClientProtocolException, IOException{
		
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
}
