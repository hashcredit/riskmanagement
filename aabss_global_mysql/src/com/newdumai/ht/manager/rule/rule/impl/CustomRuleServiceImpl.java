package com.newdumai.ht.manager.rule.rule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.Page;
import com.newdumai.global.vo.PageConfig;
import com.newdumai.ht.manager.rule.rule.CustomRuleService;

@Service("customRuleService")
public class CustomRuleServiceImpl extends BaseServiceImpl implements CustomRuleService{

	@Deprecated
	@Override
	public Page<Map<String, Object>> findAsPage(PageConfig config,Map<String, Object> map) {

		List<String> wheres = new ArrayList<String>();
		List<Object> args = new ArrayList<Object>();

		String type_code = (String) map.get("type_code");
		String ruleGroupCode = (String) map.get("rule_group_code");
		String status = (String) map.get("status");
		String subEntityId = (String) map.get("sub_entity_id");

		String sqlTotal = "SELECT"
				+"	count(*) "
				+" FROM"
				+"	sys_rule_group__fk_guize rgr"
				+" LEFT JOIN custom_rule cr ON rgr.`code` = cr.rule_group_rule_code and cr.sub_entity_id= ?"
				+" JOIN fk_guize gz ON rgr.fk_guize_code = gz.`code`"
				+" JOIN sys_rule_group rg ON rgr.sys_rule_group_code = rg.`code`"
				+" JOIN sys_type t ON rg.sys_type_code = t.`code`"
				+" WHERE"
				+" 	rg.biz_range='1' "
				//+"	AND rgr.rule_group_code = 'b3149ffc-94dc-4695-afb5-69d7f7af18a0'"
				;
		String sql = "SELECT"
				+"	gz.*,rg.name rule_group_name,t.name type_name,cr.rule_group_rule_code,rgr.`code` rgr_code"
				+" FROM"
				+"	sys_rule_group__fk_guize rgr"
				+" LEFT JOIN custom_rule cr ON rgr.`code` = cr.rule_group_rule_code and cr.sub_entity_id= ?"
				+" JOIN fk_guize gz ON rgr.fk_guize_code = gz.`code`"
				+" JOIN sys_rule_group rg ON rgr.sys_rule_group_code = rg.`code`"
				+" JOIN sys_type t ON rg.sys_type_code = t.`code`"
				+" WHERE"
				+" 	rg.biz_range='1' "
				//+"	AND rgr.rule_group_code = 'b3149ffc-94dc-4695-afb5-69d7f7af18a0'"
				;

		args.add(subEntityId);

		if(!StringUtils.isEmpty(type_code)){
			wheres.add("rg.sys_type_code= ?");
			args.add(type_code);
		}
		else{
			String sqlTypeCodes = "select type_code from sys_company_type ct where ct.sub_entity_id=? and ct.report_para like '%\"loanfront_rule\":\"1\"%'";

			List<Map<String,Object>> typeCodes =  mysqlSpringJdbcBaseDao.queryForList(sqlTypeCodes, subEntityId);

			StringBuilder sb = new StringBuilder();

			for(Map<String,Object> typeCode : typeCodes){
				sb.append("'").append(typeCode.get("type_code")).append("',");
			}
			if(sb.length()!=0){
				sb.delete(sb.length()-1,sb.length());
			}
			wheres.add("rg.sys_type_code in("+sb+")");
		}
		if(!StringUtils.isEmpty(ruleGroupCode)){
			wheres.add("rgr.sys_rule_group_code= ?");
			args.add(ruleGroupCode);
		}
		if(!StringUtils.isEmpty(status)){
			if("1".equals(status)){
				wheres.add("cr.rule_group_rule_code IS NOT NULL");
			}
			else if("0".equals(status)){
				wheres.add("cr.rule_group_rule_code IS NULL");
			}
		}

		String whereSql =wheres.size()==0? "" :(" and "  + StringUtils.join(wheres, " and "));

		return findAsPage(config, sqlTotal + whereSql +" order by rg.id,gz.id" , sql + whereSql,args.toArray());
	}

	@Override
	public boolean disableRule(String fk_guize_code, String sys_company_type_code,String rule_model) {
		try {
			mysqlSpringJdbcBaseDao.delete("delete from sys_company_type__fk_guize where sys_company_type_code=? and fk_guize_code=? and rule_model = ?", sys_company_type_code, fk_guize_code,rule_model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean enableRule(String fk_guize_code, String sys_company_type_code, String rule_model) {
		try {
			if ("2".equals(rule_model)) {//模型
				mysqlSpringJdbcBaseDao.update("insert into sys_company_type__fk_guize(sys_company_type_code,fk_guize_code,rule_model) values(?,?,?)", sys_company_type_code, fk_guize_code, rule_model);
			} else {//规则(默认)
				mysqlSpringJdbcBaseDao.update("insert into sys_company_type__fk_guize(sys_company_type_code,fk_guize_code) values(?,?)", sys_company_type_code, fk_guize_code);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected Page<Map<String,Object>> findAsPage(PageConfig config,String sqlTotal,String sql,Object... args){
		Page<Map<String,Object>> page = new Page<Map<String,Object>>();
		page.setRows(mysqlSpringJdbcBaseDao.queryForList(sql + " limit " + (config.getCurrPage()-1)*config.getPageSize() + "," + config.getPageSize(),args));
		page.setTotal(mysqlSpringJdbcBaseDao.executeSelectSqlInt(sqlTotal,args));
		return page;
	}

	@Override
	public List<Map<String, Object>> getRuleGroupsByTypeCode(String typeCode) {
		return super.mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_rule_group WHERE is_able='1' and  sys_type_code=? and biz_range='1'",typeCode);
	}

	@Override
	public List<Map<String, Object>> getRuleGroups() {
		return super.mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_rule_group ");
	}

	@Override
	public List<Map<String, Object>> getRuleGroupsOfCompany(String subEntityId) {
		String sql = "SELECT rg.* FROM sys_rule_group rg "
				+ " join sys_company_type ct on rg.sys_type_code=ct.type_code "
				+ " where ct.sub_entity_id=? and rg.biz_range='1' and ct.rule_model = '1'";
		return super.mysqlSpringJdbcBaseDao.queryForList(sql,subEntityId);
	}

//	@Override
//	public List<Map<String, Object>> getRuleEnabledTypesBySubEntityId(String subEntityId) {
//		String sql = "select t.* from sys_company_type ct join sys_type t on ct.type_code = t.code where ct.sub_entity_id = ? and ct.rule_model = '1' and ct.is_able = '1'";
//		return super.mysqlSpringJdbcBaseDao.queryForList(sql, subEntityId);
//	}

	public String genCountSql(String tableName, String condition) {
		return "SELECT count(*) FROM fk_guize where 1=1 and is_able = '1' " + condition;
	}

	public String genListSql(String tableName, String condition, String limit) {
		return "SELECT * FROM fk_guize where 1=1 and is_able = '1' " + condition + limit;
	}

	public Map<String, Object> getCondition(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String rule_group_code = (String) map.get("rule_group_code");
		if (StringUtils.isNotEmpty(rule_group_code)) {
			sb.append(" AND code in (select fk_guize_code from sys_rule_group__fk_guize where sys_rule_group_code = ?) ");
			list.add(rule_group_code);
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	@Override
	public String getGroupRules(Map<String, Object> para) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String type_code = (String) para.get("type_code");
		String sub_entity_id = (String) para.get("sub_entity_id");
		String status = (String) para.get("status");
		//鉴权sql
		String sql = "SELECT * FROM fk_guize where code in (SELECT b.fk_guize_code FROM sys_rule_group AS a INNER JOIN sys_rule_group__fk_guize AS b ON a.`code` = b.sys_rule_group_code WHERE a.biz_range='0' AND a.sys_type_code = ?)";
		List<Map<String, Object>> authenticationList = super.mysqlSpringJdbcBaseDao.queryForList(sql, type_code);
		//规则sql
		String ruleSql = getGroupRulesSql(status, "1", sub_entity_id, type_code, super.getLimitUseAtSelectPage(para));
		List<Map<String, Object>> resultList = super.mysqlSpringJdbcBaseDao.queryForList(ruleSql);
		resultMap.put("rule", resultList);
		resultMap.put("authentication", authenticationList);
		return new Gson().toJson(resultMap);
	}

//	@Override
//	public String getGroupRules(Map<String, Object> para) {
//		Map<String,Object> resultMap = new HashMap<String,Object>();
//		String type_code = (String) para.get("type_code");
//		String sub_entity_id = (String) para.get("sub_entity_id");
//		String status = (String) para.get("status");
//		String sql1 = getGroupRulesSql(status, sub_entity_id, type_code, "");
//		Map<String, Object> countMap = super.mysqlSpringJdbcBaseDao.queryForMap(sql1);
//		String total = String.valueOf(countMap.get("total"));
//		String sql2 = getGroupRulesSql(status, sub_entity_id, type_code,  super.getLimitUseAtSelectPage(para));
//		List<Map<String, Object>> resultList = (List<Map<String, Object>>)  super.mysqlSpringJdbcBaseDao.queryForList(sql2);
//		resultMap.put("total", total );
//		resultMap.put("rows", resultList );
//		return new Gson().toJson(resultMap);
//	}

	private String getGroupRulesSql(String status, String biz_range, String sub_entity_id, String type_code, String limit) {
		StringBuilder sb = new StringBuilder();
		sb.append(" (  ");
		sb.append(" SELECT   ");
		sb.append(" fk_guize.*,   ");
		sb.append(" sys_company_type.`code` as sys_company_type_code    ");
		sb.append(" FROM     ");
		sb.append(" sys_rule_group  ");
		sb.append(" INNER JOIN sys_rule_group__fk_guize ON sys_rule_group.`code` = sys_rule_group__fk_guize.sys_rule_group_code AND sys_rule_group.sys_type_code = '" + type_code + "' AND sys_rule_group.is_able = '1'  and sys_rule_group.biz_range = '" + biz_range + "'  ");
		sb.append(" INNER JOIN fk_guize ON sys_rule_group__fk_guize.fk_guize_code = fk_guize.`code` AND fk_guize.is_able = '1'   ");
		sb.append(" INNER JOIN sys_company_type ON sys_rule_group.sys_type_code = sys_company_type.type_code AND sys_company_type.is_able = '1' AND sys_company_type.sub_entity_id = '"+sub_entity_id+"' AND sys_company_type.type_code = '"+type_code+"'   ");
		sb.append(" ) AS guize1    ");
		sb.append(" LEFT JOIN (     ");
		sb.append(" SELECT ");
		sb.append(" sys_company_type__fk_guize.fk_guize_code,    ");
		sb.append(" sys_company_type.sub_entity_id,   ");
		sb.append(" sys_company_type.type_code   ");
		sb.append(" FROM      ");
		sb.append(" sys_company_type      ");
		sb.append(" INNER JOIN sys_company_type__fk_guize ON sys_company_type.`code` = sys_company_type__fk_guize.sys_company_type_code  and sys_company_type__fk_guize.rule_model='1'  ");
		sb.append(" AND sys_company_type.type_code = '"+type_code+"'   ");
		sb.append(" AND sys_company_type.sub_entity_id = '"+sub_entity_id+"'     ");
		sb.append(" ) AS guize2 ON guize2.fk_guize_code = guize1. CODE     ");
		if("0".equals(status)){
			sb.append(" where fk_guize_code is null ");
		}else if("1".equals(status)){
			sb.append(" where fk_guize_code is not null ");
		}
		StringBuilder sb2 = new StringBuilder();
		if(StringUtils.isEmpty(limit)){
			sb2.append(" select count(*) as total from ");
			sb2.append(sb);
		}else{
			sb2.append(" select * from ");
			sb2.append(sb);
			sb2.append(limit);
		}
		return sb2.toString();
	}

	@Override
	public String modifyAllRules(Map<String, Object> map) {
		String ruleStatus = (String) map.get("ruleStatus");
		String fk_guizeCodes = (String) map.get("fk_guize_codes");
		String sys_company_type_code = (String) map.get("sys_company_type_code");
		String[] fk_guize_codes = fk_guizeCodes.split(",");
		try {
			for (String fk_guize_code : fk_guize_codes) {
				if ("0".equals(ruleStatus)) {//全部关闭
					this.disableRule(fk_guize_code, sys_company_type_code,"1");
				} else if ("1".equals(ruleStatus)) {//全部开启
					this.enableRule(fk_guize_code, sys_company_type_code,"1");
				}
			}
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}
}
