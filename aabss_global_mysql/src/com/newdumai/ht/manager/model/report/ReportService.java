package com.newdumai.ht.manager.model.report;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

/**
 * 模型报告Service
 */
public interface ReportService extends BaseService {

	Map<String, Object> findOrderByCode(String code);

	public Map<String, Object> getResult(String orderCode,String type);

	Map<String, Object> getPersonInfo(Map<String,Object>orderMap);

	/**
	 * 获取此订单的报告需要展现的详细数据对应的数据源列表
	 * @param orderMap 订单Map
	 * @return
	 */
	List<Map<String, Object>> getDetailInterfaces(Map<String,Object> orderMap);

	List<Map<String, Object>> getDetailInterfaceMap(String orderCode);

    /**
     * 根据订单code查询分期情况
     *
     * @param code
     * @return
     */
    Map<String, Object> getOrderBill(String code);
}
