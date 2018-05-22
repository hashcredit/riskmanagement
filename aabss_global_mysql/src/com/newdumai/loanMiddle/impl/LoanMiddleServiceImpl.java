package com.newdumai.loanMiddle.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.loanFront.AuditService;
import com.newdumai.loanFront.GenReportService;
import com.newdumai.loanFront.OrderInfoService;
import com.newdumai.loanMiddle.LoanMiddleService;
import com.newdumai.sysmgr.BizFunctionSettingsService;
import com.newdumai.util.JsonToMap;

@Service("loanMiddleService")
public class LoanMiddleServiceImpl extends BaseServiceImpl implements LoanMiddleService {
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private GenReportService genReportService;
	
	@Autowired
	private BizFunctionSettingsService bizFunctionSettingsService;
	
	@Autowired
	private Dm_3rd_interfaceService dm_3rd_interfaceService;
	
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition, gen_list_1(condition.get("condition").toString()), gen_list_2(condition.get("condition").toString(), getLimitUseAtSelectPage(map)));
		
	}

	@Override
	public Map<String, Object> getReportInfo(LoginVo login, String code, String typeCode) {
		
		String sql = "select o.*,c.name typename,p.sex,p.age,p.married,p.address,p.income,p.banknum," + " p.otherincome,p.profession,p.linkname1,p.linkname2,p.linkphone1,"
				+ " p.linkphone2,p.insuranceid,p.insurancepwd,p.fundid,p.fundpwd,co.name as companyName " + " from fk_orderInfo o " + " left join fk_personinfo p on o.personinfo_code=p.code"
				+ " left join sys_type c on o.thetype=c.code " + " left join company_order co on o.sub_entity_id=co.sub_entity_id " + " where o.code=?";
		Map<String, Object> data = mysqlSpringJdbcBaseDao.queryForMap(sql, code);
		
		if (null != data) {
			String xiaoShiPohto = genReportService.Report("1", code);
			if (StringUtils.isNotEmpty(xiaoShiPohto)) {
				Map<String, Object> photoData = JsonToMap.jsonToMap(xiaoShiPohto);
				data.put("xiaoshi-picture", photoData.get("PHOTO"));
			}
			
			if (bizFunctionSettingsService.hasFunctions(login, "loanfront_report4", typeCode)) {
				String bankValidation = genReportService.Report("4", code);
				if (!StringUtils.isEmpty(bankValidation)) {
					Map<String, Object> result = JsonToMap.jsonToMap(bankValidation);
					data.put("bankValidation", result.get("data"));
				}
			}
			
			data.put("audit_result", auditService.findResultsByOrderCode(code));
			data.put("orders", orderInfoService.findTheSamePersonOrderIdsByCode(code));
			data.put("basicinfo", new Gson().fromJson(genReportService.Report("6", code), Object.class));
		}
		
		return data;
	}
	
	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM fk_orderinfo WHERE 1=1 " + condition;
	}
	
	private String gen_list_2(String condition, String limit) {
		
		return "SELECT fk_orderinfo.*,sys_type.name type_name,sys_user.surname audit_person,"
				+ "(select count(*) from fk_guize_detail where fk_guize_detail.order_code=fk_orderinfo.code and result='true') hit_counts FROM fk_orderinfo "
				+ "left join sys_type on fk_orderinfo.thetype = sys_type.code " + "left join sys_user on fk_orderinfo.dqshr = sys_user.code " + "WHERE 1=1 and fk_orderinfo.deleted=0 "
				+ " and fk_orderinfo.biz_range = '2' " + condition + " order by fk_orderinfo.createtime desc " + limit;
	}
	
	private Map<String, Object> getCondition_list(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		
		String sub_entity_id = (String) map.get("sub_entity_id");
		if (StringUtils.isNotEmpty(sub_entity_id)) {
			sb.append(" AND fk_orderinfo.sub_entity_id=? ");
			list.add(sub_entity_id);
		}

		String filter_dateTime = (String) map.get("filter_dateTime");
		if(StringUtils.isNotEmpty(filter_dateTime)){
			sb.append(" AND DATE_FORMAT(fk_orderinfo.createtime,'%Y-%m-%d') = ? ");
			list.add(filter_dateTime);
		}

		String filter_headtype = (String) map.get("filter_headtype");
		if (!StringUtils.isEmpty(filter_headtype)) {
			sb.append(" AND fk_orderinfo.thetype=? ");
			list.add(filter_headtype);
		}

		String status3 = (String) map.get("status3");
		if (StringUtils.isNotEmpty(status3)) {
			sb.append(" AND fk_orderinfo.status3=? ");
			list.add(status3);
		}
		
		String filter_keyword = (String) map.get("filter_keyword");
		if (!StringUtils.isEmpty(filter_keyword)) {
			sb.append(" AND (fk_orderinfo.name like ? or fk_orderinfo.card_num like ?)");
			list.add("%" + filter_keyword + "%");
			list.add("%" + filter_keyword + "%");
		}
		
		String filter_startTime = (String) map.get("filter_startTime");
		if (!StringUtils.isEmpty(filter_startTime)) {
			sb.append(" AND fk_orderinfo.createtime > ? ");
			list.add(filter_startTime);
		}
		
		String filter_endTime = (String) map.get("filter_endTime");
		if (!StringUtils.isEmpty(filter_endTime)) {
			sb.append(" AND fk_orderinfo.createtime < ? ");
			list.add(filter_endTime);
		}
		
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		
		return data;
	}
	
	private static final Map<Integer, Object> ARLARM_TYPEID_TEXT_MAP = new HashMap<Integer, Object>() {
		private static final long serialVersionUID = 1L;
		{	
			put(1001, "低电报警");
			put(1002, "越界报警");
			put(1003, "有线异常报警");
			put(1004, "拆除报警");
			put(2001, "断电报警");
			put(2002, "震动报警");
			put(2003, "离线报警");
			put(3001, "进围栏报警");
			put(3002, "出围栏报警");
			put(3003, "超速报警");
			put(3004, "分离报警");
			put(3005, "风险点报警");
		}
	};
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getGpsAlarm(String orderCode) {
		
		String sql = "select * from fk_orderinfo where code=? and deleted=0";
		Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap(sql, orderCode);
		
		Map<String, Object> inPara = new HashMap<String, Object>();
		inPara.put("DeviceNumber", orderMap.get("DeviceNumber"));
		inPara.put("PageIndex", "1");
		inPara.put("PageCount", "500");
		inPara.put("BeginDate", "1970-01-01");
		inPara.put("EndDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		String result = dm_3rd_interfaceService.testDS("b2fdc8b4-a1f8-4efa-906c-b03fdc52a90a", inPara);
		
		if (StringUtils.isNotEmpty(result)) {
			try {
				Map<String, Object> resultMap = JsonToMap.gson2Map(result);
				if (resultMap != null) {
					Map<String, Object> resultDataMap = (Map<String, Object>) resultMap.get("Result");
					if (resultDataMap != null) {
						
						List<Map<String, Object>> list = (List<Map<String, Object>>) resultDataMap.get("AlarmList");
						for(Map<String, Object> map : list){
							Integer alarmTypeId = ((Number) map.get("AlarmTypeId")).intValue();
							map.put("AlarmType", ARLARM_TYPEID_TEXT_MAP.get(alarmTypeId));
						}
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDeviceHisTrack(String orderCode, Map<String, Object> queryMap) {
		
		String sql = "select * from fk_orderinfo where code=? and deleted=0";
		Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap(sql, orderCode);
		Map<String, Object> inPara = new HashMap<String, Object>();
		inPara.put("DeviceNumber", orderMap.get("DeviceNumber"));
		inPara.put("PageIndex", "1");
		inPara.put("PageCount", "500");
		
		String startDate = (String) queryMap.get("startDate");
		String endDate = (String) queryMap.get("EndDate");
		
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		inPara.put("BeginDate", startDate == null ? today : startDate);
		inPara.put("EndDate", endDate == null ? today : endDate);
		
		String result = dm_3rd_interfaceService.testDS("94a2ad81-4aa0-4814-8d6d-21a33e92456c", inPara);
		if (StringUtils.isNotEmpty(result)) {
			try {
				Map<String, Object> resultMap = JsonToMap.gson2Map(result);
				if (resultMap != null) {
					Map<String, Object> resultDataMap = (Map<String, Object>) resultMap.get("Result");
					if (resultDataMap != null) {
						return (List<Map<String, Object>>) resultDataMap.get("TrackList");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean upateGpsDevice(Map<String, Object> map, String code) {
		mysqlSpringJdbcBaseDao.update("update fk_orderinfo set DeviceNumber=?,DeviceNumber_wireless=? where code=?", map.get("DeviceNumber"), map.get("DeviceNumber_wireless"), code);
		return true;
	}

	public String getLimitUseAtSelectPage(Map<String, Object> map) {
		try {
			String page = (String) map.get("page");
			String rows = (String) map.get("rows");
			String limit = "";
			if ((page != null && !"".equals(page)) && (rows != null && !"".equals(rows))) {
				int strart = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);
				// int end=(Integer.parseInt(page))*Integer.parseInt(rows)-1;
				limit = " limit " + strart + " , " + rows;
			}
			return limit;
		} catch (Exception e) {
			return "";
		}
	}
	
}
