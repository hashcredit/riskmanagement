package com.newdumai.cost.impl;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.cost.CompanyOrderLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("companyOrderLogService")
public class CompanyOrderLogServiceImpl extends BaseServiceImpl implements CompanyOrderLogService {

    @Override
    public Map<String, Object> getCondition(Map<String, Object> map) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Object> list = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();
        String sub_entity_id = (String) map.get("sub_entity_id");
        if (StringUtils.isNotEmpty(sub_entity_id)) {
            sb.append(" AND sub_entity_id = ? ");
            list.add(sub_entity_id);
        }
        String startDate = (String) map.get("start_date");
        if (StringUtils.isNotEmpty(startDate)) {
            sb.append(" AND opttime > ? ");
            list.add(sub_entity_id);
        }
        String end_date = (String) map.get("end_date");
        if (StringUtils.isNotEmpty(end_date)) {
            sb.append(" AND opttime < ? ");
            list.add(end_date);
        }
        sb.append(" order by opttime desc");
        data.put("condition", sb.toString());
        data.put("args", list.toArray());
        return data;
    }

    @Override
    public double getTotalCost(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        List<Object> list = new ArrayList<Object>();
        sb.append("select sum(cost) AS totalCost from company_order_log where 1=1 ");
        String sub_entity_id = (String) map.get("sub_entity_id");
        if (StringUtils.isNotEmpty(sub_entity_id)) {
            sb.append(" AND sub_entity_id = ? ");
            list.add(sub_entity_id);
        }
        String startDate = (String) map.get("start_date");
        if (StringUtils.isNotEmpty(startDate)) {
            sb.append(" AND opttime > ? ");
            list.add(sub_entity_id);
        }
        String end_date = (String) map.get("end_date");
        if (StringUtils.isNotEmpty(end_date)) {
            sb.append(" AND opttime < ? ");
            list.add(end_date);
        }
        Map<String, Object> sumMap = super.mysqlSpringJdbcBaseDao.queryForMap(sb.toString(), list.toArray());
        double totalCost = (Double) sumMap.get("totalCost");
        return totalCost;
    }
}
