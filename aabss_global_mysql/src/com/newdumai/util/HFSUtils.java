package com.newdumai.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.ClientProtocolException;

public class HFSUtils {
	
	/**
	 * 文件服务器根路径
	 */
	private static final String baseURL = "http://192.168.1.10:280/root/";
	
	/**
	 * 用户名
	 */
	private static final String username = "admin";
	
	/**
	 * 密码
	 */
	private static final String password = "idumai";
	
	private static final HFSClient hfsClient = new HFSClient(baseURL, username, password);
	
	
	/**
	 * 测试
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
		hfsClient.delFolder(folderName);
	}
	
	/**
	 * 更新文件名称
	 * @param fileName
	 * @param newName
	 * @throws IOException
	 */
	public static void updateName(String fileName,String newName) throws ClientProtocolException, IOException{
		hfsClient.updateName(fileName, newName);
	}
	
	/**
	 * 创建目录
	 * @param folderName 文件夹名称
	 * @throws IOException
	 */
	public static void createFolder(String folderName) throws IOException{
		hfsClient.createFolder(folderName);
	}
	
	/**
	 * 上传文件
	 * @param filePath 本地文件路径
	 * @param serverFilePath 上传到服务器指定的文件路径(包含文件名)
	 * @throws IOException
	 */
	public static boolean uploadFile(String filePath,String serverFilePath) throws IOException{
		return hfsClient.uploadFile(filePath, serverFilePath);
	}
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return true存在，false不存在
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean exists(String filePath) throws IOException{
		return hfsClient.exists(filePath);
	}
	
	
	/**
	 * 下载文件
	 * @param filePath 服务器端文件路径
	 * @param downLoadPath 下载文件指定的路径(包含名称)
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void downloadFile(String filePath,String downLoadPath) throws ClientProtocolException, IOException{
		hfsClient.downloadFile(filePath, downLoadPath);
	}
}
