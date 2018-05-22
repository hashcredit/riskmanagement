package com.newdumai.loanFront;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface GenReportService extends BaseService{
	/**
	 *<p>2:讯运营商数据-凭安染黑度
     *<p>3:诉数据-汇法
     *<p>4:人基本信息数据 -银行卡验证:
     *<p>5:款信息--宜信逾期
     *<p>6:住人信息
     *<p>7:卡号用户画像
     *<p>8:辆基本信息
     *<p>9:辆维修报告
     *<p>10安染黑度
     *<p>11逾期信息 3.1
     *<p>12贷款信息 3.2
     *<p>13凭安黑名单 3.3
     *<p>14天创公积金
     *<p>15优分涉案信息
     *<p>default 照片
	 * @param type
	 * @param orderId
	 * @return
	 */
	public String Report(String type,String orderId);
	
	/**
	 * 获取报告详情数据[{name_zh:'XXXX':value:"xxxx"},...]
	 * @param code 数据源
	 * @param orderId 订单code
	 * @return
	 */
	public String dataDetail(String dm_3rd_interface_code, String orderId);
	
	/**
	 * 获取报告详情数据[{name_zh:'XXXX':value:"xxxx"},...]NEW
	 * @param code 数据源
	 * @param orderId 订单code
	 * @return
	 */
	public List<Map<String, Object>> dataDetail2(String dm_3rd_interface_code, String orderId);
	
	
	/**
	 * 
	 * @param dm_3rd_interface_code
	 * @param orderId
	 * @return
	 */
	public String dataDetailRaw(String dm_3rd_interface_code, String orderId);
}
