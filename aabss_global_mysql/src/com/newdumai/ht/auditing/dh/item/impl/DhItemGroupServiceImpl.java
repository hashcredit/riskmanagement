package com.newdumai.ht.auditing.dh.item.impl;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.auditing.dh.item.DhItemGroupService;

@Service("dhItemGroupService")
public class DhItemGroupServiceImpl extends BaseServiceImpl implements DhItemGroupService {

	@Override
	public String listAll(String tableName, String code) {
		String sql = "select * from dh_item_group where code='" + code + "'";
		return new Gson().toJson(super.mysqlSpringJdbcBaseDao.queryForMap(sql));
	}
}