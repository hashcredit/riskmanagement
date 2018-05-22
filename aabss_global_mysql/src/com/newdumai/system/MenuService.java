package com.newdumai.system;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface MenuService extends BaseService{
	public List<Map<String, Object>> getChildrenById(String pid);
	public List<Map<String, Object>> getMenuById(String id);
}
