package com.newdumai.dumai_data.task;

import java.util.Map;

import com.newdumai.global.service.BaseService;

/**
 * 电核任务Service
 * 
 * @author zgl
 * @datetime Nov 30, 2016 4:52:07 PM
 */
public interface DmTaskService extends BaseService {

	public String list(Map<String, Object> para);

	/**
	 * 根据用户code查询任务
	 * 
	 * @param logincode
	 *            登录用户code
	 * @param flag
	 *            是否完成
	 * @return
	 * @zgl Dec 1, 2016 2:21:18 PM
	 */
	public Map<String, Object> getByUserCode(String logincode, String flag);

	/**
	 * 根据用户code获取电核任务的电核项信息
	 * 
	 * @param logincode
	 * @return
	 * @zgl Dec 1, 2016 3:26:44 PM
	 */
	public String getConfigByUserCode(String logincode);
}
