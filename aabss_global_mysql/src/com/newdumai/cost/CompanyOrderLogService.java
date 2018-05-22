package com.newdumai.cost;

import com.newdumai.global.service.BaseService;

import java.util.Map;

public interface CompanyOrderLogService extends BaseService {

    /**
     * 根据条件查询消费总金额
     *
     * @param map
     * @return
     */
    double getTotalCost(Map<String, Object> map);
}
