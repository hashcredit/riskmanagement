package com.newdumai.setting.interface_source.in_interface.tianchuang.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * API参数管理工具
 * 
 * @author lei.sun-1
 * @date 2016年4月18日
 */
public class ParamUtil {

	/**
	 * 生成参数字符串，参数key按字典序排列
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	public static String sortParam(Map<String, String> param) {
		if (null == param || 0 == param.size()) {
			return "";
		}
		// 排序键
		Iterator<String> iterator = param.keySet().iterator();
		String[] arr = new String[param.size()];
		for (int i = 0; iterator.hasNext(); i++) {
			arr[i] = iterator.next();
		}
		Arrays.sort(arr);
		// 生成字符串
		StringBuffer sb = new StringBuffer();
		for (String key : arr) {
			String value = param.get(key);
			if (StringUtils.isNotBlank(value)) {
				sb.append(key).append("=").append(value).append(",");
			}
		}
		// 检查结果
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * 获取请求IP
	 * 
	 * @param request
	 *            请求
	 * @return
	 */
	public static String getSourceIp(HttpServletRequest request) {
		String fromSource = "X-Real-IP";
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
			fromSource = "X-Forwarded-For";
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			fromSource = "Proxy-Client-IP";
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			fromSource = "WL-Proxy-Client-IP";
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			fromSource = "request.getRemoteAddr";
		}
		return String.format("%s:%s", fromSource, ip);
	}

	/**
	 * 生成TokenKey
	 * 
	 * @param url
	 *            地址
	 * @param tokenId
	 *            客户REST代码
	 * @param param
	 *            参数
	 * @return
	 */
	public static String getTokenKey(String url, String tokenId,
			Map<String, String> param) {
		String paramStr = ParamUtil.sortParam(param);
		System.out.println(paramStr);
		return MD5Util.md5(url + tokenId + paramStr);
	}

	/**
	 * 若参数不为空，则添加到参数列表中
	 * 
	 * @param param
	 *            参数列表
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void addParam(Map<String, String> param, String key,
			String value) {
		if (null != param && StringUtils.isNotBlank(key)
				&& StringUtils.isNotBlank(value)) {
			param.put(key, value.trim());
		}
	}
}
