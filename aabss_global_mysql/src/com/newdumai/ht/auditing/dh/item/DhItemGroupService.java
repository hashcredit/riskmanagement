package com.newdumai.ht.auditing.dh.item;

import com.newdumai.global.service.BaseService;

/**
 * 电核集配置Service
 * 
 * @author zgl
 * @datetime Dec 27, 2016 10:42:27 AM
 */
public interface DhItemGroupService extends BaseService {

	public String listAll(String tableName, String code);
}
