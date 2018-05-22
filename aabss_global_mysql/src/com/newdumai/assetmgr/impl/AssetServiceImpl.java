package com.newdumai.assetmgr.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.assetmgr.AssetService;
import com.newdumai.global.service.impl.BaseServiceImpl;


@Service("assetService")
public class AssetServiceImpl extends BaseServiceImpl implements AssetService{

	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getListConditiont(map);
		return listPageBase(condition,genCountSql(condition.get("condition").toString()),genListSql(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
	}

	@Override
	public String getHeadtype() {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(gen_getHeadtype());
	}
	
	private Map<String, Object> getListConditiont(Map<String, Object> map) {
		Map<String, Object> condition = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		String filter_headtype = (String) map.get("filter_headtype");
		if(!StringUtils.isEmpty(filter_headtype)){
			sb.append(" AND thetype=? ");
			list.add(filter_headtype);
		}
		String filter_keyword = (String) map.get("filter_keyword");
		if(!StringUtils.isEmpty(filter_keyword)){
			sb.append(" AND (fk_orderinfo.name like ? or fk_orderinfo.card_num like ?)");
			list.add("%" + filter_keyword + "%");
			list.add("%" + filter_keyword + "%");
		}
		String filter_checkStatus = (String) map.get("filter_checkStatus");
		if(!StringUtils.isEmpty(filter_checkStatus)){
			sb.append(" AND status1=? ");
			list.add(filter_checkStatus);
		}
		String filter_startTime = (String) map.get("filter_startTime");
		if(!StringUtils.isEmpty(filter_startTime)){
			sb.append(" AND fk_orderinfo.createtime > ? ");
			list.add(filter_startTime);
		}
		String filter_endTime = (String) map.get("filter_endTime");
		if(!StringUtils.isEmpty(filter_endTime)){
			sb.append(" AND fk_orderinfo.createtime < ? ");
			list.add(filter_endTime);
		}
		String sub_entity_id = (String) map.get("sub_entity_id");
		if(!StringUtils.isEmpty(sub_entity_id)){
			sb.append(" AND fk_orderinfo.sub_entity_id = ? ");
			list.add(sub_entity_id);
		}
		condition.put("condition", sb.toString());
		condition.put("args", list.toArray());
		return condition;
	}

	private String genListSql(String condition,String limit) {
		return "SELECT fk_orderinfo.*,sys_type.name type_name ,company_order.name company_name FROM fk_orderinfo left join sys_type on fk_orderinfo.thetype = sys_type.code left join company_order on fk_orderinfo.sub_entity_id  = company_order.sub_entity_id WHERE 1=1"+condition+" order by createtime desc"+ limit;
	}
	private String genCountSql(String condition) {
		return "SELECT count(*) FROM fk_orderinfo WHERE 1=1 "+condition;
		
	}

	@Override
	public int getTotal() {
		return mysqlSpringJdbcBaseDao.executeSelectSqlInt("SELECT count(*) FROM fk_orderinfo");
	}

	private String gen_getHeadtype() {
		return "SELECT * FROM sys_type ";
	}
	
}
