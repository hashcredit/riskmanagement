//package com.newdumai.ht.auditing.task.impl;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.newdumai.global.service.impl.BaseServiceImpl;
//import com.newdumai.ht.auditing.task.AuditingTaskDetailService;
//
//@Service("auditingTaskDetailService")
//public class AuditingTaskDetailServiceImpl extends BaseServiceImpl implements AuditingTaskDetailService {
//
//	@Override
//	public String list(Map<String, Object> map) {
//		Map<String, Object> condition = getCondition_list(map);
//		return listPageBase(condition, gen_list_1(condition.get("condition").toString()), gen_list_2(condition.get("condition").toString(), getLimitUseAtSelectPage(map)));
//	}
//
//	private String gen_list_1(String condition) {
//		return "SELECT count(*) FROM dh_task_detail WHERE 1=1 " + condition;
//	}
//
//	private String gen_list_2(String condition, String limit) {
//		return "SELECT * FROM dh_task_detail WHERE 1=1 " + condition + limit;
//	}
//
//	private Map<String, Object> getCondition_list(Map<String, Object> map) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		List<Object> list = new ArrayList<Object>();
//		StringBuilder sb = new StringBuilder();
//		String code = (String) map.get("code");
//		if (StringUtils.isNotBlank(code)) {
//			sb.append(" and code=?");
//			list.add(code);
//		}
//		data.put("condition", sb.toString());
//		data.put("args", list.toArray());
//		return data;
//	}
//}
