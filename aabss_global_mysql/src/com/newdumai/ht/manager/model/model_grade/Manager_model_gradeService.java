package com.newdumai.ht.manager.model.model_grade;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface Manager_model_gradeService extends BaseService {
	Map<String, Object> getGradeByTypeCode(Map<String, Object> params);

	void saveOrUpdateGrade(Map<String, Object> params);
}
