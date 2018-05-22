package com.newdumai.ht.manager.rule.rule.impl;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.ht.manager.rule.rule.CustomModelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/4/26.
 */
@Service("customModelService")
public class CustomModelServiceImpl extends BaseServiceImpl implements CustomModelService {

    @Override
    public String getModels(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String type_code = (String) map.get("type_code");
        String sub_entity_id = (String) map.get("sub_entity_id");

        resultMap.put("model", this.getModels(sub_entity_id, type_code));
        resultMap.put("authentication", this.getAuthentication(type_code));
        resultMap.put("rule", this.getRules(sub_entity_id, type_code));
        return new Gson().toJson(resultMap);
    }

    //模型
    private List<Map<String, Object>> getModels(String sub_entity_id, String type_code) {
        //贷前业务类型对应的模型
        String modelSql = "SELECT a.`code`,a.`name`,a.model_type,a.is_able,a.description,a.opttime,c.`name` AS modelGroupName FROM manager_model AS a " +
                " INNER JOIN manager_model__manager_model_group AS b ON b.manager_model_code = a.`code` " +
                " INNER JOIN manager_model_group AS c ON c.`code` = b.manager_model_group_code  WHERE " +
                " c.sys_type_code = ? and c.biz_range = '1' AND c.is_able = '1' AND a.is_able = '1' ";


        //商户业务类型
        String sql = "select a.* from sys_company_type a where a.sub_entity_id = ? AND a.type_code = ? ";
        Map<String, Object> sys_company_typeMap = super.mysqlSpringJdbcBaseDao.queryForMap(sql, sub_entity_id, type_code);
        String sys_company_type_code = (String) sys_company_typeMap.get("code");
        //商户关联业务类型对应的模型code
        String models = "SELECT manager_model_code FROM  sys_company_type__manager_model where sys_company_type_code = ? ";
        List<Map<String, Object>> modelsList = super.mysqlSpringJdbcBaseDao.queryForList(models, sys_company_type_code);

        List<Map<String, Object>> managerModelList = super.mysqlSpringJdbcBaseDao.queryForList(modelSql, type_code);
        for (Map<String, Object> modelMap : managerModelList) {
            modelMap.put("status", "0");
            modelMap.put("sys_company_type_code", sys_company_type_code);
            String code = (String) modelMap.get("code");
            for (Map<String, Object> modelCodeMap : modelsList) {

                if (code.equals(modelCodeMap.get("manager_model_code"))) {
                    modelMap.put("status", "1");
                    break;
                }
            }
        }
        return managerModelList;
    }

    //模型规则
    private List<Map<String, Object>> getRules(String sub_entity_id, String type_code) {
        //规则sql
        String ruleSql = getGroupRulesSql("2", "1", sub_entity_id, type_code, " ");
        List<Map<String, Object>> resultList = super.mysqlSpringJdbcBaseDao.queryForList(ruleSql);
        return resultList;
    }

    //鉴权规则
    private List<Map<String, Object>> getAuthentication(String type_code) {
        String sql = "SELECT * FROM fk_guize where code in (SELECT b.fk_guize_code FROM sys_rule_group AS a INNER JOIN sys_rule_group__fk_guize AS b ON a.`code` = b.sys_rule_group_code WHERE a.biz_range='0' AND a.sys_type_code = ?)";
        return super.mysqlSpringJdbcBaseDao.queryForList(sql, type_code);
    }

    @Override
    public int enableModel(String sys_company_type_code, String manager_model_code) {
        String delSql = "delete from sys_company_type__manager_model where sys_company_type_code= ? ";
        super.mysqlSpringJdbcBaseDao.delete(delSql, sys_company_type_code);
        String addSql = "insert into sys_company_type__manager_model (sys_company_type_code,manager_model_code) values (?,?)";
        return super.mysqlSpringJdbcBaseDao.update(addSql, sys_company_type_code, manager_model_code);
    }

    @Override
    public int disableModel(String sys_company_type_code, String manager_model_code) {
        String delSql = "delete from sys_company_type__manager_model where sys_company_type_code= ? and manager_model_code = ?";
        return super.mysqlSpringJdbcBaseDao.delete(delSql, sys_company_type_code, manager_model_code);
    }

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
        sb.append(" INNER JOIN sys_company_type ON sys_rule_group.sys_type_code = sys_company_type.type_code AND sys_company_type.is_able = '1' AND sys_company_type.sub_entity_id = '" + sub_entity_id + "' AND sys_company_type.type_code = '" + type_code + "'   ");
        sb.append(" ) AS guize1    ");
        sb.append(" LEFT JOIN (     ");
        sb.append(" SELECT ");
        sb.append(" sys_company_type__fk_guize.fk_guize_code,    ");
        sb.append(" sys_company_type.sub_entity_id,   ");
        sb.append(" sys_company_type.type_code   ");
        sb.append(" FROM      ");
        sb.append(" sys_company_type      ");
        sb.append(" INNER JOIN sys_company_type__fk_guize ON sys_company_type.`code` = sys_company_type__fk_guize.sys_company_type_code  and sys_company_type__fk_guize.rule_model='2'  ");
        sb.append(" AND sys_company_type.type_code = '" + type_code + "'   ");
        sb.append(" AND sys_company_type.sub_entity_id = '" + sub_entity_id + "'     ");
        sb.append(" ) AS guize2 ON guize2.fk_guize_code = guize1. CODE     ");
        if ("0".equals(status)) {
            sb.append(" where fk_guize_code is null ");
        } else if ("1".equals(status)) {
            sb.append(" where fk_guize_code is not null ");
        }
        StringBuilder sb2 = new StringBuilder();
        if (StringUtils.isEmpty(limit)) {
            sb2.append(" select count(*) as total from ");
            sb2.append(sb);
        } else {
            sb2.append(" select * from ");
            sb2.append(sb);
            sb2.append(limit);
        }
        return sb2.toString();
    }
}
