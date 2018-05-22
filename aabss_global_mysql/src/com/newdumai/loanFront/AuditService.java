package com.newdumai.loanFront;

import java.util.List;
import java.util.Map;

public interface AuditService {
	List<Map<String, Object>> findResultsByOrderCode(String orderId);
	
	String auditOne(String orderId);
	
	float getAuditOneCost(String orderId);
	
	float getInterfacesCostInOrder(String orderId,String interfacesCodes);
	
	Map<String, Object> getInPara(String orderId,String interfacesCodes);
	
	String auditOne2(String orderId);

	String auditOne_rule2(Map<String, Object> orderMap ,Map<String, Object> bigLabelMap);

	Integer auditOne3(Map<String, Object> orderMap);
	
	String auditOne4(Map<String, Object> orderMap);

	String auditOne5(Map<String, Object> orderMap);
	
	void Audit_DH(String task_code);

	void CreateDh_task(Map<String, Object> orderMap);

	void CreateDh_task_3rd_interface(Map<String, Object> orderMap);

	boolean checkCompanyDHFunction(String sub_entity_id);

	void sendAuditResult(String orderId, String auditResult, boolean flag);

	void updateOrderAuditResult(String orderId, String result, boolean flag);

	Map<String, Object> auditOne_jianquan(Map<String, Object> orderMap);

	String getModelGrade(Map<String, Object> orderMap, int score);
	
	
}
