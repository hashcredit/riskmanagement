package com.newdumai.login.qt;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface LoginQTService extends BaseService {

	/**
	 * 根据登录用户名获取用户信息（用于登录验证）
	 * 
	 * @param username
	 * @return
	 */
	public Map<String, Object> getByUserName(String username);
	
	
	/**
	 * 判断Ip是否在商户的白名单列表之内
	 * @param ip
	 * @param subEntityId
	 * @return
	 */
	public boolean inWhiteIps(String ip,String subEntityId);
	
	
	/**
	 * 登录日志
	 * 
	 * @param params
	 */
	public void addLog(Map<String, Object> params);
}
