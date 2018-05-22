package com.newdumai.ht.manager.model.model_grade.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.model.model_grade.Manager_model_gradeService;

@Service("manager_model_gradeService")
public class Manager_model_gradeServiceImpl extends BaseServiceImpl implements Manager_model_gradeService {
	@Override
	public Map<String, Object> getGradeByTypeCode(Map<String, Object> params) {
		String sys_type_code = (String) params.get("sys_type_code");
		try {
			params = mysqlSpringJdbcBaseDao.queryForMap("select * from manager_model_grade where sys_type_code = ? order by opttime desc limit 1 ", sys_type_code);
			if(CollectionUtils.isEmpty(params)){
				params = new HashMap<String,Object>();
				params.put("result", "failure");
			}else{
				params.put("result", "success");
			}
		} catch (Exception e) {
			params.put("result", "failure");
		}
		return params;
	}

	@Override
	public void saveOrUpdateGrade(Map<String, Object> params) {
		try {
			super.saveOrUpdate("manager_model_grade", params);
			params.put("result", "success");
		} catch (Exception e) {
			params.put("result", "failure");
		}
	}
}
