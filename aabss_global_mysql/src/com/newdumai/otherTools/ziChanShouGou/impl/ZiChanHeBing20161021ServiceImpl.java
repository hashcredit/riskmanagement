package com.newdumai.otherTools.ziChanShouGou.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.otherTools.ziChanShouGou.ZiChanHeBing20161021Service;


@Service("ziChanHeBing20161021Service")
public class ZiChanHeBing20161021ServiceImpl extends BaseServiceImpl implements ZiChanHeBing20161021Service {

	@Override
	public List<Map<String, Object>> listInterface_source() {
		return mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_interface_source where code<>'41170c26-f88f-446e-a660-2f1d829eaa0d'");
	}
	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		return mysqlSpringJdbcBaseDao.queryForList(sql);
	}
	@Override
	public Map<String, Object> queryForMap(String sql) {
		return mysqlSpringJdbcBaseDao.queryForMap(sql);
	}
}
