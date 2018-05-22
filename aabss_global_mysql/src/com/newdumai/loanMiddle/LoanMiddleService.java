package com.newdumai.loanMiddle;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;
import com.newdumai.global.vo.LoginVo;

public interface LoanMiddleService extends BaseService {

	public String list(Map<String, Object> map);

	/**
	 * 贷中获取报告详细数据 1 订单 人员表 2 小视图片 3 同住人信息 4 同一人其他订单列表
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, Object> getReportInfo(LoginVo login, String code, String typeCode);
	
	/**
	 * 获取GPS报警信息
	 * 
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getGpsAlarm(String orderCode);
	
	/**
	 * 获取设备GPS轨迹
	 * @param orderCode
	 * @param queryParam 
	 * @return
	 */
	List<Map<String, Object>> getDeviceHisTrack(String orderCode, Map<String, Object> queryParam);

	public boolean upateGpsDevice(Map<String, Object> map, String code);
}
