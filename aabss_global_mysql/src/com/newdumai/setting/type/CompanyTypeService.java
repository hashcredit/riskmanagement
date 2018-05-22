package com.newdumai.setting.type;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface CompanyTypeService extends BaseService {
	
	public List<Map<String, Object>> getTypeAll();
	
	public Map<String, Object> getList(Map<String, Object> params);
	
	public void enable(Map<String, Object> companyType);
	
	public void disable(Map<String, Object> params);
}
