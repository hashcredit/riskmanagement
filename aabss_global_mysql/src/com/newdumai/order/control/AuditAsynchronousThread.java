package com.newdumai.order.control;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("auditAsynchronousThread")
public interface AuditAsynchronousThread {

	
	/**
	 * 通过订单code自动审核<br/>
	 * 内部判断该订单的业务类型是否启用规则决定是否运行AuditOne
	 * @param orderinfoCode 订单code
	 */
	public void doAuditByOrderId(final String orderinfoCode);

	void doAuditByOrder(Map<String, Object> orderMap);	
}
