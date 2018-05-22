package com.newdumai.jinjian.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.jinjian.ValidateOrderService;

@Service
public class ValidateOrderServiceImpl extends BaseServiceImpl implements ValidateOrderService {

    @Override
    public String getList(Map<String, Object> request2Map) {
        return new GsonBuilder().serializeNulls().create().toJson(super.mysqlSpringJdbcBaseDao.queryForList("select * from failed_validate_order where sub_entity_id=? order by opttime desc limit 0,200", request2Map.get("sub_entity_id")));
    }

    @Override
    public Map<String, Object> getOrderInfoByCode(String code) {
        String sql = "select * from fk_orderinfo where code = ?";
        return super.mysqlSpringJdbcBaseDao.queryForMap(sql, code);
    }
}
