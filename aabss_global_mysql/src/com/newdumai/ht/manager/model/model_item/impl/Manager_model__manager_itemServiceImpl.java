package com.newdumai.ht.manager.model.model_item.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.model.model_item.Manager_model__manager_itemService;

@Service(value = "manager_model__manager_itemServiceImpl")
public class Manager_model__manager_itemServiceImpl extends BaseServiceImpl implements Manager_model__manager_itemService {
	
	/* block listAll begin */
	@Override
	public String listAll(String tableName, Map<String, Object> map) {
		Map<String, Object> params = getCondition(map);
		String condition = (String) params.get("condition");
		String sql = genListSql( condition);
		return new Gson().toJson(super.mysqlSpringJdbcBaseDao.queryForList(sql, (Object[])params.get("args")));
	}

	public String genListSql(String condition) {
		String str = " SELECT";
		str += " manager_model__manager_item.*,";
		str += " manager_item.`name`,";
		str += " manager_item.descripe,";
		str += " manager_item.is_able,";
		str += " manager_item.dm_source_para_code,";
		str += " manager_item_class.`name` manager_item_class_name";
		str += " FROM";
		str += " manager_model__manager_item";
		str += " INNER JOIN manager_item ON manager_model__manager_item.manager_item_code = manager_item.`code`";
		str += " INNER JOIN manager_item_class ON manager_item.manager_item_class_code = manager_item_class.`code`";
		str += " WHERE";
		str += " 1=1 ";
		return str+condition;
	}

	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		 String code = (String) map.get("code");
		 if(!StringUtils.isEmpty(code)){
		 sb.append(" AND manager_model__manager_item.manager_model_code =  ? ");
		 list.add(code);
		 }
		 String is_able = (String) map.get("is_able");
		 if(!StringUtils.isEmpty(is_able)){
		 sb.append(" AND manager_item.is_able = ? ");
		 list.add(is_able);
		 }
		sb.append(" order by manager_item.id ");
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
	/* block list end */

	@Override
	public boolean update_seperate_box(String tableName, Map<String, Object> map) {
		saveOrUpdate(tableName, map);
		return true;
	}

	@Override
	public boolean delete(String tableName, Map<String, Object> map) {
		mysqlSpringJdbcBaseDao.delete("delete from  "+tableName+" where code=?",map.remove("code"));
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addModels(Map<String, Object> params) {
		String model_code = (String) params.get("model_code");
		List<String> item_codes = (List<String>) params.get("item_codes");
		Object[][] args = new Object[item_codes.size()][];
		for (int i = 0; i < args.length; i++) {
			Object[] arg = new Object[3];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = model_code;
			arg[2] = item_codes.get(i);
			args[i] = arg;
		}
		mysqlSpringJdbcBaseDao.batchInsert("insert into manager_model__manager_item (code,manager_model_code,manager_item_code) values(?,?,?)", args);
		return true;
	}

	@Override
	public String get_item_listAll(Map<String, Object> params) {
    	String str = "";
        str +=  " SELECT ";
        str +=  " manager_item.* ,";
        str +=  " manager_item_class.`name` as manager_item_class_name ";
        str +=  " FROM ";
        str +=  " manager_item ";
        str +=  " LEFT JOIN manager_model__manager_item ON manager_model__manager_item.manager_item_code = manager_item.`code` ";
        str +=  " AND manager_model__manager_item.manager_model_code = '"+(String)params.get("code")+"' ";
        str +=  " left join manager_item_class on manager_item_class.code = manager_item.manager_item_class_code ";
        str +=  " WHERE ";
        str +=  " manager_model__manager_item.manager_item_code IS NULL ";
        str +=  " AND is_able = '1' ";
        return new Gson().toJson(super.mysqlSpringJdbcBaseDao.queryForList(str));
	}

}
