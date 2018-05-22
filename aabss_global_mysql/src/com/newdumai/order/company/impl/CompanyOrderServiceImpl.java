package com.newdumai.order.company.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.order.company.CompanyOrderService;

@Service("companyOrderService")
public class CompanyOrderServiceImpl extends BaseServiceImpl implements CompanyOrderService{

	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
	}
	
	@Override
	public List<Map<String,Object>> listAll() {
		return   mysqlSpringJdbcBaseDao.queryForList("select * from company_order where status='1' ");
	}
	
	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM company_order WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT * FROM company_order WHERE 1=1 order by updateTime desc "+condition+limit;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		
		
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	@Override
	public String getSubEntityIdByCode(String account) {
		String subEntityId = null ;
		String sql = "select * from company_order where account = ?";
		Map<String,Object> map = mysqlSpringJdbcBaseDao.queryForMap(sql, account);
		if(map!=null){
			subEntityId = (String) map.get("sub_entity_id");
		}
		return subEntityId;
	}
	
	@Override
	public boolean addCompany(Map<String,Object> params){
		/*String getMaxSubEntityId = "SELECT MAX(cast(subEntityId as signed)) as maxSubEntityId FROM company_order";
		List<Map<String, Object>> dataList = mysqlSpringJdbcBaseDao.executeSelectSql2(getMaxSubEntityId);
		int maxSubEntityId = 2;
		if(!CollectionUtils.isEmpty(dataList)&&((Map<String,Object>)dataList.get(0)).get("maxSubEntityId")!=null){
			maxSubEntityId = Integer.parseInt((String) ((Map<String,Object>)dataList.get(0)).get("maxSubEntityId"))+1;
		}
		params.put("subEntityId",String.valueOf(subEntityId));*/
		String sub_entity_id = UUID.randomUUID().toString();
		params.put("sub_entity_id",sub_entity_id);
		params.put("message","创建商户！");
		params.remove("id");
		params.put("createTime",new Date());
		super.add(params, "company_order");
		params = this.addSys_user(params);
		return true;
	}
	
	@Override
	public boolean updateCompany(Map<String,Object>params){
		Map<String,Object> where = new HashMap<String,Object>();
		params.put("updateTime", new Date());
		where.put("id", (String) params.remove("id"));
		super.Update(params, "company_order", where);
		return true;
		
	}
	
	@Override
	public boolean deleteCompany(String id) {
		super.delete("DELETE FROM company_order WHERE `id` = '" + id + "'");
		return true;
	}
	
	private Map<String,Object> addSys_user(Map<String,Object>params){
		Map<String,Object> Sys_user = new HashMap<String,Object>();
		Sys_user.put("USER_NAME", params.get("account"));
		Sys_user.put("PWD", "123456");
		Sys_user.put("SURNAME", params.get("name"));
		Sys_user.put("user_permission", "1:1:1");
		Sys_user.put("ISVALID", "1");
		Sys_user.put("ISLEADER", "1");
		Sys_user.put("sub_entity_id", params.get("sub_entity_id"));
		super.add(Sys_user, "Sys_user");
		return params;
	}

	@Override
	public Map<String, Object> getCompanyBySubEntityId(String subEntityId) {
		Map<String, Object> company = super.mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order  WHERE sub_entity_id = ?",subEntityId);
		return company;
	}
	@Override
	public Map<String, Object> getCompanyByAccount(String account) {
		Map<String, Object> company = super.mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order  WHERE account = ?",account);
		return company;
	}

	@Override
	public boolean isEnable(String subEntityId) {
		Map<String, Object> company = getCompanyBySubEntityId(subEntityId);
		 
		if(company!=null){
			return new Integer(1).equals(company.get("status"));
		} 
		 
		return false;
	}

	@Override
	public boolean isEnableByAccount(String account) {
		Map<String, Object> company = super.mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order  WHERE account = ?",account);
		if(company!=null){
			return new Integer(1).equals(company.get("status"));
		} 
		return false;
	}
	
	@Override
	public boolean isValidateBySub_entity_id(String sub_entity_id) {
		Map<String, Object> company = super.mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order  WHERE status = '1' and is_validate = '1' and sub_entity_id = '"+sub_entity_id+"' ");
		if(CollectionUtils.isEmpty(company)){
			return false;
		}
		return true;
	}
	
}
