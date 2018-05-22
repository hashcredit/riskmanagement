package com.newdumai.jinjian;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface ValidateOrderService extends BaseService {

	public String getList(Map<String, Object> request2Map);

	/**
	 * 根据code查询订单信息
	 *
	 * @param code
	 * @return
	 */
	Map<String ,Object>	 getOrderInfoByCode(String code);

}
