package com.newdumai.dumai_data.user;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface DmUserService extends BaseService {

	public String list(Map<String, Object> para);

	public String getByCode(String code);

	/**
	 * 更新用户信息
	 * 
	 * @param para
	 * @zgl Nov 30, 2016 1:58:49 PM
	 */
	public int upadte_dm_user(Map<String, Object> para);

	/**
	 * 新增用户信息
	 * 
	 * @param para
	 * @zgl Nov 30, 2016 1:59:02 PM
	 */
	public int add_dm_user(Map<String, Object> para);

	/**
	 * 根据用户名和密码查询
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @zgl Dec 6, 2016 5:31:03 PM
	 */
	public Map<String, Object> getUser(String username, String password);
}
