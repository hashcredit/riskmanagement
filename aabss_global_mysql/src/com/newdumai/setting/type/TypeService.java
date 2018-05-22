package com.newdumai.setting.type;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface TypeService extends BaseService {
	
	public String list(Map<String, Object> para);
	public List<Map<String, Object>> comboList();
	int addType(Map<String, Object> params);
	int updateType(Map<String, Object> params, Map<String, Object> where);
	public void deleteType(String code);

	/**
	 * 获取启用业务类型
	 *
	 * @return
	 */
	List<Map<String, Object>> getType();

	/**
	 * 根据商户sub_entity_id获取业务类型
	 *
	 * @param sub_entity_id
	 * @return
	 */
	List<Map<String, Object>> getTypesBySubEntityId(String sub_entity_id);
}
