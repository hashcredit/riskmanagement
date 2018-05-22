package com.newdumai.order.company;

import java.util.List;
import java.util.Map;

public interface CompanyOrderService {
	public String list(Map<String, Object> para);
	
	public List<Map<String, Object>> listAll();
	
	public String getSubEntityIdByCode(String code);

	public boolean addCompany(Map<String,Object> params);

	boolean updateCompany(Map<String, Object> params);

	boolean deleteCompany(String id);
	
	/**
	 * 获取商户信息
	 * @param subEntityId
	 * @return
	 */
	public Map<String, Object> getCompanyBySubEntityId(String subEntityId);
	/**
	 * 获取商户信息
	 * @param subEntityId
	 * @return
	 */
	public Map<String, Object> getCompanyByAccount(String account);
	
	/**
	 * 通过subEntityId判断商户是否被禁用
	 */
	public boolean isEnable(String subEntityId);
	
	/**
	 * 通过account判断商户是否被禁用
	 */
	public boolean isEnableByAccount(String account);

	/**
	 * 通过sub_entity_id判断商户是否启用鉴权
	 */
	boolean isValidateBySub_entity_id(String sub_entity_id);
}
