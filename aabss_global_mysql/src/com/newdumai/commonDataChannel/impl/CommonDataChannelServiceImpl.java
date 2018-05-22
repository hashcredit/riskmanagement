package com.newdumai.commonDataChannel.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.commonDataChannel.CommonDataChannelService;
import com.newdumai.commonDataChannel.sql.SqlMap;
import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.global.dao.Dumai_sourceBaseDao;

@Service("commonDataChannelService")
class CommonDataChannelServiceImpl implements CommonDataChannelService{


	@Autowired
	private Dumai_sourceBaseDao dumai_sourceBaseDao;
	
	@Autowired
	private Dumai_newBaseDao dumai_newBaseDao;
	
	/* 基础构建服务 start */
	
	
	/* 基础构建服务 end   */
	
	@Override
	public List<Map<String,Object>> queryListBySql(Map<String, Object> params) {
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>() ;
		String dao_name = (String) params.get("dao_name");
		String sql = SqlMap.sqlMap.get((String) params.get("sql_key"));
		if("dumai_new".equals(dao_name)){
			returnList = dumai_newBaseDao.queryForList(sql);
		}else if("dumai_source".equals(dao_name)){
			returnList = dumai_sourceBaseDao.queryForList(sql);
		}else{
			returnList = null ;
		}
		return returnList;
	}
	
	@Override
	public Map<String,Object> queryMapBySql(Map<String, Object> params) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String dao_name = (String) params.get("dao_name");
		String sql = (String) params.get("sql");
		if("new".equals(dao_name)){
			returnMap = dumai_newBaseDao.queryForMap(sql);
		}else if("source".equals(dao_name)){
			returnMap = dumai_sourceBaseDao.queryForMap(sql);
		}else{
			returnMap = null ;
		}
		return returnMap;
	}

}
