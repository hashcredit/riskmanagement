package com.newdumai.loanOverdue.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanOverdue.LoanOverdueService;
import com.newdumai.util.MapObjUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhang on 2017/3/6.
 */
@Service("loanOverdueService")
public class LoanOverdueServiceImpl extends BaseServiceImpl implements LoanOverdueService {
    @Override
    public String getOverdueInfo(String sub_entity_id, Map<String, Object> map) {
        Map<String, Object> conditionMap = getCondition(map);
        String condition = (String) conditionMap.get("condition");
        return listPageBase(conditionMap, genCountSql(sub_entity_id, condition), genListSql(sub_entity_id, condition, getLimitUseAtSelectPage(map)));
    }

    public String genCountSql(String sub_entity_id, String condition) {
        return "SELECT count(*) FROM fk_orderinfo a,sys_type b where 1=1 and a.sub_entity_id = '" + sub_entity_id + "'and a.thetype=b.code and a.status3 = '1' " + condition;
    }

    public String genListSql(String sub_entity_id, String condition, String limit) {
        return "SELECT a.*,b.name AS typeName FROM fk_orderinfo a,sys_type b where 1=1 and a.sub_entity_id = '" + sub_entity_id + "' and a.thetype=b.code and a.status3 = '1' " + condition + limit;
    }

    public Map<String, Object> getCondition(Map<String, Object> map) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Object> list = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();
        String code = (String) map.get("code");
        if (StringUtils.isNotEmpty(code)) {
            sb.append(" AND a.code = ? ");
            list.add(code);
        }
        String filter_headtype = (String) map.get("filter_headtype");
        if (StringUtils.isNotEmpty(filter_headtype)) {
            sb.append(" AND a.thetype = ? ");
            list.add(filter_headtype);
        }
        String filter_startTime = (String) map.get("filter_startTime");
        if (StringUtils.isNotEmpty(filter_startTime)) {
            filter_startTime = filter_startTime.substring(0, filter_startTime.indexOf(" "));
            sb.append(" AND a.createtime > ? ");
            list.add(filter_startTime);
        }
        String filter_endTime = (String) map.get("filter_endTime");
        if (StringUtils.isNotEmpty(filter_endTime)) {
            filter_endTime = filter_endTime.substring(0, filter_endTime.indexOf(" "));
            sb.append(" AND a.createtime < ? ");
            list.add(filter_endTime);
        }
        String filter_keyword = (String) map.get("filter_keyword");
        if (StringUtils.isNotEmpty(filter_keyword)) {
            sb.append(" AND (a.name like ? or a.card_num like ?)  ");
            list.add("%" + filter_keyword + "%");
            list.add("%" + filter_keyword + "%");
        }
        data.put("condition", sb.toString());
        data.put("args", list.toArray());
        return data;
    }

    @Override
    public String getFollowDetail(String fk_orderinfo_code) {
        String sql = "select a.*,b.surname from fk_orderinfo_follow a,sys_user b where a.charger=b.code and fk_orderinfo_code= ? order by a.opttime desc";
        List<Map<String, Object>> list = super.mysqlSpringJdbcBaseDao.queryForList(sql, fk_orderinfo_code);
        for (Map<String, Object> map : list) {
            map.put("follow_date", String.valueOf(map.get("follow_date")));
            map.put("next_date", String.valueOf(map.get("next_date")));
        }
        return new Gson().toJson(list);
    }

    @Override
    public void saveFollow(Map<String, Object> map) {
        map.put("code", UUID.randomUUID());
        map.put("validate_status", "0");
        map.put("follow_date", new Date());
        add(map, "fk_orderinfo_follow");
    }

    @Override
    public String getHandleListInfo(String sub_entity_id, Map<String, Object> map) {
        Map<String, Object> conditionMap = getCondition(map);
        String fk_orderinfo_code = (String) map.get("code");
        return listPageBase(conditionMap, genHandleCountSql(fk_orderinfo_code), genHandleCountListSql(fk_orderinfo_code, getLimitUseAtSelectPage(map)));
    }

    public String genHandleCountSql(String fk_orderinfo_code) {
        return "select count(*) from fk_orderinfo_follow as a left join sys_user as b on a.charger = b.`CODE` where a.opt_request <> '' and a.fk_orderinfo_code = ? ";
    }

    public String genHandleCountListSql(String fk_orderinfo_code, String limit) {
        return "select a.*,b.surname from fk_orderinfo_follow as a left join sys_user as b on a.charger = b.`CODE` where a.opt_request <> '' and a.fk_orderinfo_code = ? " + limit;
    }

    @Override
    public String saveHandle(String userCode, Map<String, Object> map) {
        String fk_orderinfo_follow_code = (String) map.remove("fk_orderinfo_follow_code");
        map.put("validate_person", userCode);
        Map<String, String> stringMap = MapObjUtil.mapObjToString(map);
        super.update("fk_orderinfo_follow", stringMap, fk_orderinfo_follow_code);
        return "1";
    }

    public String listPageBase(Map<String, Object> condition, String sql1, String sql2) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", mysqlSpringJdbcBaseDao.executeSelectSqlInt(sql1, (Object[]) condition.get("args")));
        data.put("rows", mysqlSpringJdbcBaseDao.queryForList(sql2, (Object[]) condition.get("args")));
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(data);
    }
}
