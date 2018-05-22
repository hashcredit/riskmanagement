package com.newdumai.global.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.global.service.Dumai_sourceBaseService;
import com.newdumai.global.vo.SqlSavePatrsVo;
import com.newdumai.global.vo.SqlUpdatePatrsVo;

@Service("dumai_sourceBaseService")
public class Dumai_sourceBaseServiceImpl implements Dumai_sourceBaseService {
	@Autowired
	protected Dumai_sourceBaseDao dumai_sourceBaseDao;

	public String listAll(String tableName, Map<String, Object> map) {
		String condition = "";
		if(!CollectionUtils.isEmpty(map)){
			Map<String, Object> conditionMap = getCondition(map);
			condition = (String) conditionMap.get("condition");
		}
		String sql = "SELECT * FROM " + tableName + " where 1=1  " + condition;
		List<Map<String, Object>> queryForList = dumai_sourceBaseDao.queryForList(sql);
		return new Gson().toJson(queryForList);
	}

	/* block list begin */
	@Override
	public String list(String tableName, Map<String, Object> map) {
		Map<String, Object> conditionMap = getCondition(map);
		String condition = (String) conditionMap.get("condition");
		return listPageBase(conditionMap, genCountSql(tableName, condition), genListSql(tableName, condition, getLimitUseAtSelectPage(map)));
	}

	public String genCountSql(String tableName, String condition) {
		return "SELECT count(*) FROM " + tableName + " where 1=1  " + condition;
	}

	public String genListSql(String tableName, String condition, String limit) {
		return "SELECT * FROM " + tableName + " where 1=1  " + condition + limit;
	}

	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		// String filter_headtype = (String) map.get("filter_headtype");
		// if(!StringUtils.isEmpty(filter_headtype)){
		// sb.append(" AND filter_headtype=? ");
		// list.add(filter_headtype);
		// }
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	/* block list end */

	@Override
	public boolean saveOrUpdate(String tableName, Map<String, Object> map) {
		String code = (String) map.get("code");
		if (StringUtils.isEmpty(code)) {
			add(map, tableName);
		} else {
			Map<String, Object> where = new HashMap<String, Object>();
			where.put("code", map.remove(("code")));
			Update(map, tableName, where);
		}
		return true;
	}

	@Override
	public int optIsAble(String tableName, String code) {
		String sql = "select * from " + tableName + " where code ='" + code + "'";
		Map<String, Object> queryForMap = dumai_sourceBaseDao.queryForMap(sql);
		String is_able = (String) queryForMap.get("is_able");
		if ("1".equals(is_able)) {
			is_able = "0";
		} else {
			is_able = "1";
		}
		String updateSql = "update " + tableName + " set is_able = '" + is_able + "' where code ='" + code + "'";
		return dumai_sourceBaseDao.update(updateSql);
	}

	// @Override
	public void add(String tableName, Map<String, String> map) {
		Set<String> set = map.keySet();
		StringBuilder colm = new StringBuilder();
		StringBuilder val = new StringBuilder();
		for (Object object : set) {
			/*
			 * //key--瀛楁 colm.append(object.toString()+","); //val--鎻掑叆鐨勫�
			 * val.append("'"+map.get(object)+"',");
			 */

			String column = object.toString();
			String value = map.get(object);
			colm.append(column + ",");
			if (!"motherId".equals(column)) {
				val.append("'" + value + "',");
			} else {
				val.append(value + ",");
			}
		}
		String s1 = colm.toString();
		String s2 = val.toString();
		s1 = s1.substring(0, s1.length() - 1);
		s2 = s2.substring(0, s2.length() - 1);
		add("INSERT INTO " + tableName + "(" + s1 + ") VALUES(" + s2 + ")");
	}

	// @Override
	public void add(String sql) {
		dumai_sourceBaseDao.executeSql(sql);
	}

	// @Override
	public void delete(String tableName, int id) {
		delete("delete from " + tableName + " where id=" + id);
	}

	// @Override
	public void delete(String sql) {
		dumai_sourceBaseDao.executeSql(sql);
	}

	// @Override
	public void update(String tableName, Map<String, String> map, int id) {
		Set<String> set = map.keySet();
		StringBuilder val = new StringBuilder();
		for (Object object : set) {
			if (map.get(object) == null) {
				val.append(object.toString() + "=" + map.get(object) + ",");
			} else if (map.get(object).equals("")) {
				val.append("");
			} else {
				val.append(object.toString() + "='" + map.get(object) + "',");
			}
		}
		String s = val.toString();
		if (!s.equals("")) {
			s = s.substring(0, s.length() - 1);
			update("update " + tableName + "  set " + s + "  where id = " + id);
		}
	}

	// @Override
	public void update(String tableName, Map<String, String> map, String code) {
		Set<String> set = map.keySet();
		StringBuilder val = new StringBuilder();
		for (Object object : set) {
			if (map.get(object) == null) {
				val.append(object.toString() + "=" + map.get(object) + ",");
			} else if (map.get(object).equals("")) {
				val.append("");
			} else {
				val.append(object.toString() + "='" + map.get(object) + "',");
			}
		}
		String s = val.toString();
		if (!s.equals("")) {
			s = s.substring(0, s.length() - 1);
			update("update " + tableName + "  set " + s + "  where code = '" + code + "'");
		}
	}

	// @Override
	public void update(String sql) {
		dumai_sourceBaseDao.executeSql(sql);
	}

	// @Override
	public String find(String tableName) {
		return dumai_sourceBaseDao.executeSelectSql("SELECT * FROM " + tableName);
	}

	// @Override
	public String findByPara(String tableName, String para) {
		if ("".equals(para)) {
			return dumai_sourceBaseDao.executeSelectSql("SELECT * FROM " + tableName);
		} else {
			return dumai_sourceBaseDao.executeSelectSql("SELECT * FROM " + tableName + " WHERE " + para);
		}
	}

	// @Override
	public void exec(String sql) {
		dumai_sourceBaseDao.executeSql(sql);
	}

	// @Override
	public String findBySql(String sql) {
		return dumai_sourceBaseDao.executeSelectSql(sql);
	}

	@Override
	public int add(Map<String, Object> para, String tableName) {
		para.put("code", UUID.randomUUID().toString());
		SqlSavePatrsVo vo = gen_add(para, tableName);
		return dumai_sourceBaseDao.update(vo.getSql(), vo.getVal().toArray());
	}

	@Override
	public String addAndRet(Map<String, Object> para, String tableName) {
		String code = null;
		try {
			code = UUID.randomUUID().toString();
			para.put("code", code);
			SqlSavePatrsVo vo = gen_add(para, tableName);
			dumai_sourceBaseDao.update(vo.getSql(), vo.getVal().toArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return code;
	}

	public SqlSavePatrsVo gen_add(Map<String, Object> data, String tableName) {
		SqlSavePatrsVo vo = new SqlSavePatrsVo();
		StringBuilder colum = new StringBuilder(" ");
		StringBuilder mark = new StringBuilder(" ");
		List<Object> list = new ArrayList<Object>();
		Set<String> keySet = data.keySet();
		for (String string : keySet) {
			colum.append("`" + string + "`,");
			mark.append("?,");
			list.add(data.get(string));
		}
		colum.deleteCharAt(colum.length() - 1);
		mark.deleteCharAt(mark.length() - 1);
		String sql = "INSERT INTO `" + tableName + "` (" + colum + ") VALUES (" + mark + ")";
		vo.setColum(colum);
		vo.setMark(mark);
		vo.setVal(list);
		vo.setSql(sql);
		return vo;
		// "INSERT INTO tablename ("+colum+") VALUES ("+mark+")";
	}

	@Override
	public int Update(Map<String, Object> para, String tableName, Map<String, Object> where) {
		SqlUpdatePatrsVo vo = gen_update(para, tableName, where);
		return dumai_sourceBaseDao.update(vo.getSql(), vo.getVal().toArray());
	}

	public SqlUpdatePatrsVo gen_update(Map<String, Object> data, String tableName, Map<String, Object> where) {
		SqlUpdatePatrsVo vo = new SqlUpdatePatrsVo();
		StringBuilder sql = new StringBuilder("UPDATE `" + tableName + "` SET ");
		List<Object> list = new ArrayList<Object>();
		Set<String> keySet = data.keySet();
		for (String string : keySet) {
			sql.append("`" + string + "`=?,");
			list.add(data.get(string));
		}
		sql.deleteCharAt(sql.length() - 1);
		if (where != null && !where.isEmpty()) {
			sql.append(" WHERE ");
			keySet = where.keySet();
			for (String string : keySet) {
				sql.append(" `" + string + "` =? ");
				sql.append(" and ");
				list.add(where.get(string));
			}
		} else {
			return null;
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.deleteCharAt(sql.length() - 1);
		sql.deleteCharAt(sql.length() - 1);
		sql.deleteCharAt(sql.length() - 1);
		vo.setVal(list);
		vo.setSql(sql.toString());
		System.out.println(sql.toString());
		return vo;
		// UPDATE tablename SET FirstName = 'Fred' WHERE LastName = 'Wilson'
	}

	public int del_disable(String tableName, String code) {
		String sql = "UPDATE `" + tableName + "` SET `is_able` = '0' WHERE `code` = ? ";
		return dumai_sourceBaseDao.update(sql, code);
	}

	public String getLimitUseAtSelectPage(Map<String, Object> map) {
		try {
			String page = (String) map.get("page");
			String rows = (String) map.get("rows");
			String limit = "";
			if ((page != null && !"".equals(page)) && (rows != null && !"".equals(rows))) {
				int strart = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);
				// int end=(Integer.parseInt(page))*Integer.parseInt(rows)-1;
				limit = " limit " + strart + " , " + rows;
			} else {
				limit = " limit " + 0 + " , " + 10;
			}
			return limit;
		} catch (Exception e) {
			return " limit 0,10 ";
		}
	}

	public String listPageBase(Map<String, Object> condition, String sql1, String sql2) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("total", dumai_sourceBaseDao.executeSelectSqlInt(sql1, (Object[]) condition.get("args")));
		data.put("rows", dumai_sourceBaseDao.queryForList(sql2, (Object[]) condition.get("args")));
		return new Gson().toJson(data);
	}

}
