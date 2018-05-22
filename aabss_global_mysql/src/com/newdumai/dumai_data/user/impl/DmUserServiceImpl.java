package com.newdumai.dumai_data.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.user.DmUserService;
import com.newdumai.global.service.impl.BaseServiceImpl;

@Service("dmUserService")
public class DmUserServiceImpl extends BaseServiceImpl implements DmUserService {

	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition, gen_list_1(condition.get("condition").toString()), gen_list_2(condition.get("condition").toString(), getLimitUseAtSelectPage(map)));
	}

	@Override
	public String getByCode(String code) {
		String sql = "SELECT * FROM dm_user WHERE CODE = '" + code + "'";
		return mysqlSpringJdbcBaseDao.executeSelectSql(sql);
	}

	@Override
	public int upadte_dm_user(Map<String, Object> para) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("code", para.get("code"));
		String password = (String) para.get("password");
		if (StringUtils.isEmpty(password)) {
			para.remove("password");
		} else {
			// TODO 对密码加密
		}
		para.remove("code");
		return Update(para, "dm_user", where);
	}

	@Override
	public int add_dm_user(Map<String, Object> para) {
		String password = (String) para.get("password");
		if (StringUtils.isNotEmpty(password)) {
			para.remove("password");
			// TODO 对密码加密

			// 设置加密后的密码
			para.put("password", password);
		}
		return add(para, "dm_user");
	}

	@Override
	public Map<String, Object> getUser(String username, String password) {
		String sql = "select * from dm_user where user_name = ? and password= ?";
		return mysqlSpringJdbcBaseDao.queryForMap(sql, username, password);
	}

	private Map<String, Object> getCondition_list(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		data.put("condition", "");
		data.put("args", list.toArray());
		return data;
	}

	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM dm_user WHERE 1=1 " + condition;
	}

	private String gen_list_2(String condition, String limit) {
		return "SELECT * FROM dm_user WHERE 1=1 " + condition + limit;
	}

}
