package com.newdumai.order.control.impl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newdumai.loanFront.AuditService;
import com.newdumai.loanFront.OrderInfoService;
import com.newdumai.order.control.AuditAsynchronousThread;

@Component("auditAsynchronousThread")
public class AuditAsynchronousThreadImpl implements AuditAsynchronousThread {

	@Autowired
	HttpSession session;
	@Autowired
	private AuditService auditService;
	@Autowired
	private OrderInfoService orderInfoService;

	@Override
	public void doAuditByOrderId(final String orderinfoCode) {
		Map<String, Object> order = orderInfoService.getByCode(orderinfoCode);
		doAuditByOrder(order);
	}

	/**
	 * 通过订单code自动审核<br/>
	 * 内部判断该业务类型是否启用规则决定是否运行AuditOne
	 *
	 * @param orderMap
	 *            订单map
	 */
	@Override
	public void doAuditByOrder(final Map<String, Object> orderMap) {
		session.setAttribute("fk_orderinfo_code", orderMap.get("code"));
		Thread thread = new Thread() {
			public void run() {
				String auditResult = "";
				String orderId = (String) orderMap.get("code");
				String sub_entity_id = (String) orderMap.get("sub_entity_id");
				try {
					System.out.println("************************************");
					System.out.println("开始自动审核:" + orderMap.get("name"));
					auditResult = auditService.auditOne4(orderMap);
					System.out.println("自动审核结束");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getCause() + e.getMessage());
					System.out.println("自动审核异常");
				}
				System.out.println("************************************");
				boolean flag = auditService.checkCompanyDHFunction(sub_entity_id);
				auditService.updateOrderAuditResult(orderId, auditResult, flag);
				if (flag) {
					try {
						auditService.CreateDh_task(orderMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						// 暂时不用到电核线下数据源
						// auditService.CreateDh_task_3rd_interface(orderMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					auditService.sendAuditResult(orderId, auditResult, flag);
				}
			}
		};
		thread.start();
	}

}
