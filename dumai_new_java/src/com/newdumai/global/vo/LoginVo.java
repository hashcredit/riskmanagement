package com.newdumai.global.vo;

import java.util.Map;


/**
 * 登录用户信息
 * 
 * @author 岳晓
 *
 */
public class LoginVo {
	
	private String code;
	private String username;
	private String sub_entity_id;
	private String user_permission;
	private String isLeader;
	private String white_ips;
	private String function_settings;
	private Map<String, Map<String, Object>> bizFunction_settings;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSub_entity_id() {
		return sub_entity_id;
	}

	public void setSub_entity_id(String sub_entity_id) {
		this.sub_entity_id = sub_entity_id;
	}

	public String getUser_permission() {
		return user_permission;
	}

	public void setUser_permission(String user_permission) {
		this.user_permission = user_permission;
	}

	public String getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}

	public String getWhite_ips() {
		return white_ips;
	}

	public void setWhite_ips(String white_ips) {
		this.white_ips = white_ips;
	}

	public String getFunction_settings() {
		return function_settings;
	}

	public void setFunction_settings(String function_settings) {
		this.function_settings = function_settings;
	}

	public Map<String, Map<String, Object>> getBizFunction_settings() {
		return bizFunction_settings;
	}

	public void setBizFunction_settings(Map<String, Map<String, Object>> map) {
		this.bizFunction_settings = map;
	}

}
