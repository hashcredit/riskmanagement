package com.newdumai.ht.manager.rule.ruleGroup.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.Page;
import com.newdumai.global.vo.PageConfig;
import com.newdumai.ht.manager.rule.ruleGroup.RuleGroupService;


@Service("ruleGroupService")
public class RuleGroupServiceImpl extends BaseServiceImpl implements RuleGroupService {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
		
	}
	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM sys_type a JOIN sys_rule_group b ON a.`code` = b.sys_type_code WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT b.*,a.name type_name FROM sys_type a JOIN sys_rule_group b ON a.`code` = b.sys_type_code WHERE 1=1 "+condition+limit;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		String name = (String) map.get("name");
		if(!StringUtils.isEmpty(name)){
			sb.append(" AND b.name like ? ");
			list.add("%" + name + "%");
		}
		String is_able = (String) map.get("is_able");
		if(!StringUtils.isEmpty(is_able)){
			sb.append(" AND b.is_able=? ");
			list.add(is_able);
		}
		String type = (String) map.get("type");
		if(!StringUtils.isEmpty(type)){
			sb.append(" AND b.sys_type_code=? ");
			list.add(type);
		}
		sb.append(" order by biz_range asc ");
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	@Override
	public List<Map<String, Object>> findRulesOfRuleGroup(String ruleGroupCode) {
		String sql = "select g.* from sys_rule_group__fk_guize rgr join  fk_guize g on rgr.fk_guize_code=g.code where g.is_able = '1' and rgr.sys_rule_group_code=?";
		
		return mysqlSpringJdbcBaseDao.queryForList(sql, ruleGroupCode);
		
	}
	@Override
	public Page<Map<String, Object>> findRuleListNotOfRuleGroup(String ruleGroupCode,PageConfig config) {
		String sqlTotal = "SELECT "
				+ " count(*) FROM fk_guize g WHERE 	"
				+ " NOT EXISTS (SELECT rgr.fk_guize_code FROM 	sys_rule_group__fk_guize rgr WHERE rgr.fk_guize_code = g.CODE AND rgr.sys_rule_group_code = ?)";
		String sql = "SELECT "
				+ " g.* FROM fk_guize g WHERE 	"
				+ " NOT EXISTS (SELECT rgr.fk_guize_code FROM sys_rule_group__fk_guize rgr WHERE rgr.fk_guize_code = g.CODE AND rgr.sys_rule_group_code = ?)";

		
		return findAsPage(config,sqlTotal, sql, ruleGroupCode);
		
	}

	@Override
	public boolean addRulesToRuleGroup(String ruleGroupCode,String[] ruleCodes) {
		
		
		Object[][] args = new Object[ruleCodes.length][];
		
		for(int i =0 ; i < args.length ; i++){
			Object[] arg = new Object[3];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = ruleCodes[i];
			arg[2] = ruleGroupCode;
			args[i] =arg;
		}
		
		mysqlSpringJdbcBaseDao.batchInsert("insert into sys_rule_group__fk_guize(code,fk_guize_code,sys_rule_group_code) values(?,?,?)",args);
		return true;
	}

	@Override
	public boolean removeRuleFromRuleGroup(String ruleGroupCode,String ruleCode) {
		mysqlSpringJdbcBaseDao.delete("delete from sys_rule_group__fk_guize where fk_guize_code=? and sys_rule_group_code=?",new Object[]{ruleCode,ruleGroupCode} );
		return true;
	}

	@Override
	public boolean addRuleGroup(Map<String, Object> request2Map) {
		
		request2Map.put("lastupdatetime", new Date());
		request2Map.remove("sub_entity_id");
		
		add(request2Map,"sys_rule_group");
		return true;
	}

	@Override
	public boolean deleteRuleGroup(String code) {
		//mysqlSpringJdbcBaseDao.delete("delete from sys_rule_group where code=?",code );
		mysqlSpringJdbcBaseDao.delete("update sys_rule_group set is_able='0' where code=?",code );
//		mysqlSpringJdbcBaseDao.delete("delete from sys_rule_group__fk_guize where sys_rule_group_code=?",code );
		return true;
	}
	protected Page<Map<String,Object>> findAsPage(PageConfig config,String sqlTotal,String sql,Object... args){
		Page<Map<String,Object>> page = new Page<Map<String,Object>>();
		page.setRows(mysqlSpringJdbcBaseDao.queryForList(sql + " limit " + (config.getCurrPage()-1)*config.getPageSize() + "," + config.getPageSize(),args));
		page.setTotal(mysqlSpringJdbcBaseDao.executeSelectSqlInt(sqlTotal,args));
		return page;
	}
	
	@Override
	public List<Map<String, Object>> getHeadtype() {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql2("SELECT * FROM sys_type ");
	}
	@Override
	public boolean updateRuleGroup(Map<String, Object> map) {
		
		Map<String,Object> where = new HashMap<String,Object>();
		where.put("code", map.remove("code"));
		Update(map, "sys_rule_group", where);
		return true;
	}
	@Override
	public boolean createRuleToRuleGroup(String ruleGroupCode,
			Map<String, Object> rule) {
		String ruleCode = addAndRet(rule,"fk_guize");
		if(ruleCode!=null){
			addRulesToRuleGroup(ruleGroupCode, new String[]{ruleCode});
			return true;
		}
		else return false;
	}
	public static void main(String[] args) {
		String[][] rows = new String[][]{{"a.b.c","0"},{"a.m.d","3"},{"a.m.a","3"},{"a.c","3"}};
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> cur=map;
		for(String[] row : rows){
			String param[] = row[0].split("\\.");
			 cur=map;
			for(int i = 0; i < param.length; i ++){
				String key = param[i];
				if(i==param.length-1){
					cur.put(key, row[1]);
				}
				else{
					Object o = cur.get(key);
					
					if(o!=null){
						if(o instanceof Map){
							cur = (Map<String, Object>) o;
						}
						else{
							HashMap<String,Object> m = new HashMap<String,Object>();
							cur.put(key,m);
							
							cur = m;
						}
					}
					else{
						
						HashMap<String,Object> m = new HashMap<String,Object>();
						cur.put(key,m);
						
						cur = m;
					}
				}
				
				
			}
			
		}
		System.out.println(new Gson().toJson(map));
	}

	@Override
	public Map<String, Object> getByCode(String code) {
		return super.mysqlSpringJdbcBaseDao.queryForMap("select * from sys_rule_group where code = ?", code);
	}

	@Override
	public String getGroupRules(String groupCode) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT a.* FROM fk_guize AS a INNER JOIN sys_rule_group__fk_guize AS b ON a.`code` = b.fk_guize_code WHERE a.is_able = '1' and b.`sys_rule_group_code` ='" + groupCode + "'");
	}

	@Override
	public String getUnAddGroupRules(String groupCode) {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql("SELECT a.* FROM fk_guize a where a.is_able = '1' and a.code not in (select b.fk_guize_code from sys_rule_group__fk_guize b where b.sys_rule_group_code ='" + groupCode + "')");
	}
}
