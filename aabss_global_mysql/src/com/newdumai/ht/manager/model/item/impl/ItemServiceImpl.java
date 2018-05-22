package com.newdumai.ht.manager.model.item.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.dm_label.DmLabelService;
import com.newdumai.dumai_data.dm_out.Dm_outService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.model.item.ItemService;

/**
 * 
 * @author 岳晓
 * 
 */
@Service("itemService")
public class ItemServiceImpl extends BaseServiceImpl implements ItemService {

	@Autowired
	Dm_outService dm_outService;
	
	@Autowired
	DmLabelService dmLabelService;
	
	/* block list begin */
	public String genCountSql(String tableName, String condition) {
		return "SELECT count(*) FROM manager_item mm join manager_item_class mmc on mm.manager_item_class_code=mmc.code where 1=1 " + condition;
	}

	public String genListSql(String tableName, String condition, String limit) {
		return "SELECT mm.*,mmc.name manger_model_class_name " + " FROM manager_item mm join manager_item_class mmc on mm.manager_item_class_code=mmc.code " + " where 1=1 " + condition + " order by id asc " + limit;
	}
	/* block list end */

	@Override
	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String manager_item_class_code = (String) map.get("manager_item_class_code");
		String name = (String) map.get("name");
		String is_able = (String) map.get("is_able");
		if (StringUtils.isNotEmpty(manager_item_class_code)) {
			list.add(manager_item_class_code);
			sb.append(" and mm.manager_item_class_code=? ");
		}
		if (StringUtils.isNotEmpty(name)) {
			list.add("%" + name + "%");
			sb.append(" and mm.name like ? ");
		}
		if (StringUtils.isNotEmpty(is_able)) {
			list.add(is_able);
			sb.append(" and mm.is_able=? ");
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDmSources() {
		return dmLabelService.getGroupName();
	}

	@Override
	public List<Map<String, Object>> getDmSourceOutParas(String dm_source_code) {
		Map<String,Object>params = new HashMap<String,Object>();
		params.put("dm_source_code", dm_source_code);
		return (List<Map<String, Object>>) dmLabelService.findByGroupCode(dm_source_code);
	}

	@Override
	public List<Map<String, Object>> getModel_classList() {
		String sql = "select code,name from manager_item_class ";
		return mysqlSpringJdbcBaseDao.queryForList(sql);
	}

}
