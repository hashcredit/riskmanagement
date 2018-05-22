package com.newdumai.jinjian;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newdumai.global.service.BaseService;

public interface InputOrderService extends BaseService {

	public List<Map<String, Object>> getTypesOfAccount(String account);
	 
	public void addCase(Map<String, Object> person, Map<String, Object> order);

	public void updateCaseStep(Map<String, Object> personParams,
			Map<String, Object> personWhere, Map<String, Object> orderParams,
			Map<String, Object> orderWhere);
	
	public void addCaseStep(Map<String, Object> person, Map<String, Object> order);
	
	public Map<String, Object> getPersonStepByCode(String code);
	
	public Map<String, Object> getOrderStepByCode(String code);

	public Map<String, Object> getOrderByCode(String code);

	public boolean isTestCompanyUsedUp(String account);
	
}
