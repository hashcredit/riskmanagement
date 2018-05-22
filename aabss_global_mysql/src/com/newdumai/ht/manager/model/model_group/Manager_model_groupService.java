package com.newdumai.ht.manager.model.model_group;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface Manager_model_groupService extends BaseService {

	String getByCode(String code);

	public List<Map<String, Object>> getCompanyTypeModel_group(String model_group_code);
	public List<Map<String, Object>> getBySys_type_code(String sys_type_code);

	/**
	 * 查询与Group关联的模型
	 *
	 * @param groupCode
	 * @return
	 */
    List<Map<String,Object>> getGroupToModel(String groupCode);

	void addModels(String groupCode, String[] modelCodes);
}
