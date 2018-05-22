package com.newdumai.loanFront;

import com.newdumai.global.service.BaseService;

import java.util.List;
import java.util.Map;

public interface OrderInfoService extends BaseService {
	
	/**
	 * 获取订单信息，关联fk_personinfo sys_type company_order
	 * @param code
	 * @return
	 */
	public Map<String,Object> findByCode(String code);
	
	/**
	 * 获取订单信息，仅订单信息
	 * @param code
	 * @return
	 */
	public Map<String,Object> getByCode(String code);
	
	public List<Map<String, Object>> findTheSamePersonOrderIdsByCode(String orderId);
	
	
	/**
	 * 人工审核
	 * @param data 包含code,status2,dqspyj,dqshr字段
	 * @return
	 */
	public boolean manualAudit(Map<String,Object> data);
	
	public void updateOrderStatus(String orderId, String status1, String biz_range);
	
}