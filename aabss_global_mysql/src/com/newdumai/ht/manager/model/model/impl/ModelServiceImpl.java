package com.newdumai.ht.manager.model.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.dm_out.Dm_outService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.model.model.ModelService;

/**
 * 
 * @author 田新洋
 * 
 */
@Service("modelService")
public class ModelServiceImpl extends BaseServiceImpl implements ModelService {

	@Autowired
	Dm_outService dm_outService;

	@Override
	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String name = (String) map.get("name");
		String is_able = (String) map.get("is_able");
		if (StringUtils.isNotEmpty(name)) {
			list.add("%" + name + "%");
			sb.append(" and manager_model.name like ? ");
		}
		if (StringUtils.isNotEmpty(is_able)) {
			list.add(is_able);
			sb.append(" and manager_model.is_able=? ");
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDmSources() {
		return (List<Map<String, Object>>) dm_outService.getDmSources().get("data");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDmSourceOutParas(String dm_source_code) {
		Map<String,Object>params = new HashMap<String,Object>();
		params.put("dm_source_code", dm_source_code);
		return (List<Map<String, Object>>) dm_outService.getOutParasByDmSourceCode(params).get("data");
	}

	@Override
	public List<Map<String, Object>> getModel_classList() {
		String sql = "select code,name from manager_item_class ";
		return mysqlSpringJdbcBaseDao.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getAllModel() {
		String sql = "select * from manager_model ";
		return mysqlSpringJdbcBaseDao.queryForList(sql);
	}

}
