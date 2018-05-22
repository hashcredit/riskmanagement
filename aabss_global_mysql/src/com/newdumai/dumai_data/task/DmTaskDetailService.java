package com.newdumai.dumai_data.task;

import java.util.Map;

import com.newdumai.global.service.BaseService;

/**
 * 电核任务详细信息Service
 * 
 * @author zgl
 * @datetime Dec 1, 2016 10:13:53 AM
 */
public interface DmTaskDetailService extends BaseService {

	public String list(Map<String, Object> para);
}
