package com.newdumai.loanFront.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.dumai_data.dm_3rd_interface.util.CostUtil;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanFront.AuditService;
import com.newdumai.loanFront.GenReportService;
import com.newdumai.util.JsonToMap;

@Service("genReportService")
public class GenReportServiceServiceImpl extends BaseServiceImpl implements GenReportService {
	// 以下注释掉的是老的报告数据详情，暂勿删除@2017-02-28
	// @Autowired
	// Interface_sourceService interface_sourceService;
	// @Autowired
	// AuditService auditService;
	//
	// @Override
	// public String Report(String type, String orderId) {
	// String ret = "";
	// String Interface_source_code = "";
	// switch (Integer.valueOf(type)) {
	// case 2:
	// Interface_source_code = "897e2877-1030-48a2-a696-c81abdcdaab8";
	// break;//通讯运营商数据-凭安染黑度
	// case 3:
	// Interface_source_code = "914f0a9e-7e72-455a-b40c-f47f4aa6bd74";
	// break;//涉诉数据-汇法
	// case 4:
	// Interface_source_code = "5687256d-ee6b-486c-8741-fc05af7533df";
	// break;//个人基本信息数据 -银行卡验证:
	// case 5:
	// Interface_source_code = "c2f2edc1-f5cb-40ab-8f43-5c2bbfceb98a";
	// break;//借款信息--宜信逾期
	// case 6:
	// Interface_source_code = "9bb3afce-deab-4276-a027-1f001e2e09ae";
	// break;//同住人信息
	// case 7:
	// Interface_source_code = "1b500978-2eee-47a5-b937-6d3dc97a8db3";
	// break;//单卡号用户画像
	// case 8:
	// Interface_source_code = "ac187116-d5d3-4a32-8b8e-8946d51d6d88";
	// break;//车辆基本信息
	// case 9:
	// Interface_source_code = "ed27affc-5047-41b3-a9e8-414b7dd28935";
	// break;//车辆维修报告
	// case 10:
	// Interface_source_code = "897e2877-1030-48a2-a696-c81abdcdaab8";
	// break;//凭安染黑度
	// case 11:
	// Interface_source_code = "f9e68a53-126f-4c58-bc73-6431f744b4fa";
	// break;//逾期信息 3.1
	// case 12:
	// Interface_source_code = "12e7e5fe-eec0-4f52-b147-2bf2db528fe3";
	// break;//贷款信息 3.2
	// case 13:
	// Interface_source_code = "197e2871-1030-48a2-a696-c81abdcdaab1";
	// break;//凭安黑名单 3.3
	// case 14:
	// Interface_source_code = "381897d9-3855-4992-af0c-521c92a7be49";
	// break;//天创公积金
	// case 15:
	// Interface_source_code = "f9726895-242d-46f9-b276-624d1b4f6376";
	// break;//优分涉案信息
	// default:
	// Interface_source_code = "93a69a72-d867-4f7d-a587-64ab3b8a3378";
	// break;//照片
	// }
	// type = "report" + type;
	// ret = execReport(type, orderId, Interface_source_code);
	// return ret;
	// }
	//
	// public String execReport(String type, String orderId) {
	// Map<String, Object> map = mysqlSpringJdbcBaseDao.queryForMap("SELECT *
	// FROM fk_orderinfo_report WHERE `fk_orderinfo_code`=?", orderId);
	// return (String) map.get(type);
	// }
	//
	// private boolean cost(String orderId, Map<String, Object> orderMap, String
	// Interface_source_code, String type) {
	// boolean ret = false;
	// //查询余额
	// Map<String, Object> companyMap =
	// mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order WHERE
	// sub_entity_id =?", orderMap.get("sub_entity_id").toString());
	// //查询花费
	// float cost = auditService.getInterfacesCostInOrder(orderId,
	// Interface_source_code);
	// //余额-cost>50 check
	// int status = (Integer) companyMap.get("status");
	// float todayBalance = (Float) companyMap.get("todayBalance");
	// float totalBalance = (Float) companyMap.get("totalBalance");
	// if (status == 0) return ret;//"账户已停用！";
	// if (totalBalance - cost < 50) {
	// return ret;//"账户余额不足！";
	// } else {
	// if (todayBalance - cost < 0) {
	// return ret;//"今日可用余额不足！";
	// } else {
	// //扣钱
	// Map<String, Object> para = new HashMap<String, Object>();
	// Map<String, Object> where = new HashMap<String, Object>();
	// where.put("code", companyMap.get("code"));
	// para.put("todayBalance", todayBalance - cost);
	// para.put("totalBalance", totalBalance - cost);
	// para.put("updateTime", new Date());
	// super.Update(para, "company_order", where);
	// Map<String, Object> save = new HashMap<String, Object>();
	// save.put("sub_entity_id", orderMap.get("sub_entity_id").toString());
	// save.put("fk_orderinfo_code", orderId);
	// save.put("cost", cost);
	// save.put("report_type", type);
	// save.put("log", "报告/查看数据！【扣除：" + cost + "，订单号：" + orderId + "," +
	// orderMap.get("name").toString() + "】");
	// addAndRet(save, "company_order_log");
	// //执行
	// ret = true;
	// }
	// }
	// return ret;
	// }
	//
	// public String execReport(String type, String orderId, String
	// Interface_source_code) {
	// Map<String, Object> orderReportMap =
	// mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo_report
	// WHERE `fk_orderinfo_code`=?", orderId);
	// if (orderReportMap != null && !orderReportMap.isEmpty() &&
	// !StringUtils.isEmpty((String) orderReportMap.get(type))) {
	// return (String) orderReportMap.get(type);
	// } else {
	// Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT
	// * FROM fk_orderinfo WHERE `code`=?", orderId);
	// if (!cost(orderId, orderMap, Interface_source_code, type)) {
	// return "余额不足";
	// } else {
	// String ret =
	// interface_sourceService.testDS(orderMap.get("sub_entity_id").toString(),
	// orderId, Interface_source_code, getInPara(Interface_source_code,
	// orderMap));
	// Map<String, Object> save = new HashMap<String, Object>();
	// if (orderReportMap != null && !orderReportMap.isEmpty()) {
	// save.put(type, ret);
	// Map<String, Object> where = new HashMap<String, Object>();
	// where.put("fk_orderinfo_code", orderId);
	// Update(save, "fk_orderinfo_report", where);
	// } else {
	// save.put(type, ret);
	// save.put("fk_orderinfo_code", orderId);
	// addAndRet(save, "fk_orderinfo_report");
	// }
	// return ret;
	// }
	// }
	// }
	//
	//
	// private Map<String, Object> getInPara(String code, Map<String, Object>
	// map) {
	// Map<String, Object> inPara = new HashMap<String, Object>();
	// List<Map<String, Object>> list =
	// mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM
	// sys_interface_source_para WHERE `type`= '0' AND Interface_source_code='"
	// + code + "'");
	// for (Map<String, Object> paras : list) {
	// String fk_orderinfo_name = (String) paras.get("fk_orderinfo_name");
	//
	// if (!StringUtils.isEmpty(fk_orderinfo_name)) {
	// String value = (String) map.get(fk_orderinfo_name);
	// if (value != null) {
	// inPara.put((String) paras.get("name"), value);
	// }
	// } else {
	// String value = (String) paras.get("value");
	// inPara.put((String) paras.get("name"), value);
	// }
	// }
	// return inPara;
	// }
	// @SuppressWarnings("unused")
	// private String execReport(String type, String orderId, String
	// dm_3rd_interface_code) {
	//
	// Map<String, Object> orderReportMap =
	// mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo_report
	// WHERE `fk_orderinfo_code`=?", orderId);
	// if (orderReportMap != null && !orderReportMap.isEmpty() &&
	// !StringUtils.isEmpty((String) orderReportMap.get(type))) {
	// return (String) orderReportMap.get(type);
	// } else {
	// Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT
	// * FROM fk_orderinfo WHERE `code`=?", orderId);
	//// String sub_entity_id = (String) orderMap.get("sub_entity_id");
	//// boolean isBalanceEnough = CostUtil.isBalanceEnough(sub_entity_id,
	// dm_3rd_interface_code);
	//// if (!isBalanceEnough) {
	//// return "余额不足";
	//// }
	// int chargeResult = CostUtil.charge(orderMap,"报告数据:" + type +"--" +
	// orderMap.get("name"), dm_3rd_interface_code);
	// if(chargeResult==0){
	// return "扣费失败";
	// }
	// String ret = dm_3rd_interfaceService.testDSRaw(dm_3rd_interface_code,
	// getInPara(dm_3rd_interface_code, orderMap));
	// Map<String, Object> save = new HashMap<String, Object>();
	// if (orderReportMap != null && !orderReportMap.isEmpty()) {
	// save.put(type, ret);
	// Map<String, Object> where = new HashMap<String, Object>();
	// where.put("fk_orderinfo_code", orderId);
	// Update(save, "fk_orderinfo_report", where);
	// } else {
	// save.put(type, ret);
	// save.put("fk_orderinfo_code", orderId);
	// addAndRet(save, "fk_orderinfo_report");
	// }
	// return ret;
	// }
	// }

	private static final String String = null;

	@Autowired
	private Dm_3rd_interfaceService dm_3rd_interfaceService;

	@Autowired
	private Dumai_sourceBaseDao dumai_sourceBaseDao;

	@Autowired
	private AuditService auditService;

	@Override
	public String Report(String type, String orderId) {
		String ret = "";
		String dm_3rd_interface_code = "";
		switch (Integer.valueOf(type)) {
		case 2:
			// dm_interface_code = "897e2877-1030-48a2-a696-c81abdcdaab8";
			dm_3rd_interface_code = "897e2877-1030-48a2-a696-c81abdcdaab8";
			break;// 通讯运营商数据-凭安染黑度
		case 3:
			// dm_interface_code = "914f0a9e-7e72-455a-b40c-f47f4aa6bd74";
			dm_3rd_interface_code = "914f0a9e-7e72-455a-b40c-f47f4aa6bd74";
			break;// 涉诉数据-汇法
		case 4:
			dm_3rd_interface_code = "5687256d-ee6b-486c-8741-fc05af7533df";
			break;// 个人基本信息数据 -银行卡验证:
		case 5:
			dm_3rd_interface_code = "c2f2edc1-f5cb-40ab-8f43-5c2bbfceb98a";
			break;// 借款信息--宜信逾期
		case 6:
			dm_3rd_interface_code = "9bb3afce-deab-4276-a027-1f001e2e09ae";
			break;// 同住人信息
		case 7:
			dm_3rd_interface_code = "1b500978-2eee-47a5-b937-6d3dc97a8db3";
			break;// 单卡号用户画像
		case 8:
			dm_3rd_interface_code = "ac187116-d5d3-4a32-8b8e-8946d51d6d88";
			break;// 车辆基本信息
		case 9:
			dm_3rd_interface_code = "ed27affc-5047-41b3-a9e8-414b7dd28935";
			break;// 车辆维修报告
		case 10:
			dm_3rd_interface_code = "897e2877-1030-48a2-a696-c81abdcdaab8";
			break;// 凭安染黑度
		case 11:
			dm_3rd_interface_code = "f9e68a53-126f-4c58-bc73-6431f744b4fa";
			break;// 逾期信息 3.1
		case 12:
			dm_3rd_interface_code = "12e7e5fe-eec0-4f52-b147-2bf2db528fe3";
			break;// 贷款信息 3.2
		case 13:
			dm_3rd_interface_code = "197e2871-1030-48a2-a696-c81abdcdaab1";
			break;// 凭安黑名单 3.3
		case 14:
			dm_3rd_interface_code = "ca2c08e2-1633-4990-835d-10c8f20ce2f7";
			break;// 天创公积金
		case 15:
			dm_3rd_interface_code = "9b4cc12d-ff09-4de3-944a-3101c9df688a";
			break;// 优分涉案信息
		default:
			dm_3rd_interface_code = "93a69a72-d867-4f7d-a587-64ab3b8a3378";
			break;// 照片
		}
		// type = "report" + type;
		ret = dataDetail(dm_3rd_interface_code, orderId);
		return ret;
	}

	private Map<String, Object> getInPara(String code, Map<String, Object> map) {
		Map<String, Object> inPara = new HashMap<String, Object>();
		List<Map<String, Object>> list = dumai_sourceBaseDao.queryForList(
				"SELECT * FROM dm_3rd_interface_para WHERE `type`= '0' AND dm_3rd_interface_code='" + code + "'");
		for (Map<String, Object> paras : list) {
			String fk_orderinfo_name = (String) paras.get("fk_orderinfo_name");
			if (!StringUtils.isEmpty(fk_orderinfo_name)) {
				String value = (String) map.get(fk_orderinfo_name);
				if (value != null) {
					inPara.put((String) paras.get("name"), value);
				}
			} else {
				String value = (String) paras.get("value");
				inPara.put((String) paras.get("name"), value);
			}
		}
		return inPara;
	}

	/**
	 * 数据详情
	 */
	@Override
	public String dataDetailRaw(String dm_3rd_interface_code, String orderId) {
		Map<String, Object> dm_3rd_interfaceMap = dumai_sourceBaseDao
				.queryForMap("select * from dm_3rd_interface where code=?", dm_3rd_interface_code);
		String report = (String) dm_3rd_interfaceMap.get("report");
		String type = "report" + report;
		Map<String, Object> orderReportMap = mysqlSpringJdbcBaseDao
				.queryForMap("SELECT * FROM fk_orderinfo_report WHERE `fk_orderinfo_code`=?", orderId);
		// List<Map<String, Object>> outParas =
		// JsonToMap.gson2List(dm_3rd_interfaceService.getDm_3rd_interface_para(dm_3rd_interface_code,
		// "1"));

		if (orderReportMap != null && !orderReportMap.isEmpty()
				&& !StringUtils.isEmpty((String) orderReportMap.get(type))) {
			String ret = (String) orderReportMap.get(type);
			return ret;

		} else {
			Map<String, Object> orderMap = mysqlSpringJdbcBaseDao
					.queryForMap("SELECT * FROM fk_orderinfo WHERE `code`=?", orderId);
			// String sub_entity_id = (String) orderMap.get("sub_entity_id");
			// boolean isBalanceEnough = CostUtil.isBalanceEnough(sub_entity_id,
			// dm_3rd_interface_code);
			// if (!isBalanceEnough) {
			// return "余额不足";
			// }
			int chargeResult = CostUtil.charge(orderMap, "报告数据:" + dm_3rd_interface_code + "--" + orderMap.get("name"),
					dm_3rd_interface_code);
			if (chargeResult == 0) {
				return "扣费失败";
			}

			String ret = dm_3rd_interfaceService.testDS(dm_3rd_interface_code,
					getInPara(dm_3rd_interface_code, orderMap));
			if (ret == null) {
				return null;
			}

			// List<List<Map<String, Object>>> result =
			// getOutParaResults2(JsonToMap.gson2Map(ret), outParas);
			Map<String, Object> save = new HashMap<String, Object>();
			if (orderReportMap != null && !orderReportMap.isEmpty()) {
				save.put(type, ret);
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("fk_orderinfo_code", orderId);
				Update(save, "fk_orderinfo_report", where);
			} else {
				save.put(type, ret);
				save.put("fk_orderinfo_code", orderId);
				addAndRet(save, "fk_orderinfo_report");
			}
			return ret;
			// return retParsed;
		}

	}

	/**
	 * 数据详情
	 */
	@Override
	public String dataDetail(String dm_3rd_interface_code, String orderId) {
		System.out.println(dm_3rd_interface_code + "|" + orderId);
		Map<String, Object> dm_3rd_interfaceMap = dumai_sourceBaseDao
				.queryForMap("select * from dm_3rd_interface where code=?", dm_3rd_interface_code);
		String report = (String) dm_3rd_interfaceMap.get("report");
		String type = "report" + report;
		Map<String, Object> orderReportMap = mysqlSpringJdbcBaseDao
				.queryForMap("SELECT * FROM fk_orderinfo_report WHERE `fk_orderinfo_code`=?", orderId);
		List<Map<String, Object>> outParas = JsonToMap
				.gson2List(dm_3rd_interfaceService.getDm_3rd_interface_para(dm_3rd_interface_code, "1"));
		if (orderReportMap != null && !orderReportMap.isEmpty()
				&& !StringUtils.isEmpty((String) orderReportMap.get(type))) {
			String ret = (String) orderReportMap.get(type);
			List<List<Map<String, Object>>> result = getOutParaResults2(JsonToMap.gson2Map(ret), outParas);
			String retParsed = new GsonBuilder().serializeNulls().create().toJson(result);
			return retParsed;

		} else {
			Map<String, Object> orderMap = mysqlSpringJdbcBaseDao
					.queryForMap("SELECT * FROM fk_orderinfo WHERE `code`=?", orderId);
			// String sub_entity_id = (String) orderMap.get("sub_entity_id");
			// boolean isBalanceEnough = CostUtil.isBalanceEnough(sub_entity_id,
			// dm_3rd_interface_code);
			// if (!isBalanceEnough) {
			// return "余额不足";
			// }
			int chargeResult = CostUtil.charge(orderMap, "报告数据:" + dm_3rd_interface_code + "--" + orderMap.get("name"),
					dm_3rd_interface_code);
			if (chargeResult == 0) {
				return "扣费失败";
			}

			String ret = dm_3rd_interfaceService.testDS(dm_3rd_interface_code,
					getInPara(dm_3rd_interface_code, orderMap));
			if (ret == null) {
				return null;
			}

			List<List<Map<String, Object>>> result = getOutParaResults2(JsonToMap.gson2Map(ret), outParas);
			String retParsed = new GsonBuilder().serializeNulls().create().toJson(result);
			Map<String, Object> save = new HashMap<String, Object>();
			if (orderReportMap != null && !orderReportMap.isEmpty()) {
				save.put(type, ret);
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("fk_orderinfo_code", orderId);
				Update(save, "fk_orderinfo_report", where);
			} else {
				save.put(type, ret);
				save.put("fk_orderinfo_code", orderId);
				addAndRet(save, "fk_orderinfo_report");
			}
			return retParsed;
		}
	}

	/**
	 * 数据详情新 time:2017-04-14
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public List<Map<String, Object>> dataDetail2(String dm_3rd_interface_code, String orderId) {
		// 取得大map
		String sql = "SELECT dm_3rd_result,rule_result FROM fk_order_bigmap WHERE fk_order_bigmap.fk_orderinfo_code ='"
				+ orderId + "'";
		Map<String, Object> dm_3rd_resultMap = mysqlSpringJdbcBaseDao.queryForMap(sql);

		String str = (String) dm_3rd_resultMap.get("dm_3rd_result");
		Map<String, Object> dm_3rdmap = JsonToMap.jsonToMap(str);
		str = (String) dm_3rd_resultMap.get("rule_result");
		Map<String, Object> dm_3rd_map = JsonToMap.jsonToMap(str);
		// 重组大map
		if (dm_3rd_map != null) {
			dm_3rdmap.putAll(dm_3rd_map);
		} else {
			return null;
		}
		Map<String, Object> m = JsonToMap.jsonToMap(dm_3rd_map.get(dm_3rd_interface_code).toString());
		// 取得参数定义
		String sql2 = "SELECT dm_3rd_interface_para.name_zh, dm_3rd_interface_para.`name` "
				+ "FROM dm_3rd_interface_para INNER JOIN dm_3rd_interface ON dm_3rd_interface.`code` = dm_3rd_interface_para.dm_3rd_interface_code "
				+ "WHERE dm_3rd_interface_para.type = '1' AND dm_3rd_interface_para.is_report = '1' "
				+ "AND dm_3rd_interface.is_able = '1' AND dm_3rd_interface_para.dm_3rd_interface_code ='"
				+ dm_3rd_interface_code + "'";
		List<Map<String, Object>> dm_3rd_paraList = dumai_sourceBaseDao.queryForList(sql2);
		if (dm_3rd_paraList == null) {
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> ret;
		// list.add(dm_3rdMap);
		for (Map<String, Object> map : dm_3rd_paraList) {
			// 迭代dm_3rd_paraMap的值，存入
			ret = new HashMap<String, Object>();
			ret.put("name_zh", map.get("name_zh"));
			ret.put("name", map.get("name"));
			ret.put("value", m.get(map.get("name")));
			list.add(ret);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getOutParaResults(Map<String, Object> result,
			List<Map<String, Object>> out_paras) {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < out_paras.size(); i++) {
			Map<String, Object> outPara = out_paras.get(i);
			if (!"1".equals(outPara.get("is_report"))) {
				continue;
			}
			String name = (String) outPara.get("name");
			Map<String, Object> currentMap = result;
			String para_group = (String) outPara.get("para_group");
			if (!StringUtils.isEmpty(para_group)) {
				String paths[] = para_group.split("_");
				for (int j = 0; j < paths.length; j++) {
					String path = paths[j];
					currentMap = (Map<String, Object>) currentMap.get(path);
					if (currentMap == null)
						break;
				}
			}
			String name_zh = (String) outPara.get("name_zh");
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("name", name);
			row.put("name_zh", name_zh);
			Object value = currentMap.get(name);
			row.put("value", value);
			results.add(row);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private List<List<Map<String, Object>>> getOutParaResults2(Map<String, Object> result,
			List<Map<String, Object>> out_paras) {
		Map<String, Map<String, Object>> results = new HashMap<String, Map<String, Object>>();
		int length = 0;

		for (int i = 0; i < out_paras.size(); i++) {
			Map<String, Object> outPara = out_paras.get(i);
			if (!"1".equals(outPara.get("is_report"))) {
				continue;
			}
			String name = (String) outPara.get("name");
			Object current = result;
			String para_group = (String) outPara.get("para_group");
			if (!StringUtils.isEmpty(para_group)) {
				String paths[] = para_group.split("_");
				for (int j = 0; j < paths.length; j++) {
					String path = paths[j];
					if (current instanceof Map) {
						current = ((Map<String, Object>) current).get(path);
					} else {
						break;
					}
				}
			}
			String name_zh = (String) outPara.get("name_zh");
			Map<String, Object> row = new HashMap<String, Object>();
			// row.put("name", name);
			row.put("name_zh", name_zh);
			List<Object> values = new ArrayList<Object>();
			if (current instanceof List) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) current;
				length = list.size();
				for (Map<String, Object> map : list) {
					values.add(map.get(name));
				}
			} else {
				length = 1;
				values.add(((Map<String, Object>) current).get(name));
			}

			row.put("values", values);
			results.put(name, row);
		}

		List<List<Map<String, Object>>> retList = new ArrayList<List<Map<String, Object>>>();
		Set<String> keySet = results.keySet();
		for (int i = 0; i < length; i++) {
			List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
			for (String key : keySet) {
				Map<String, Object> temp = results.get(key);
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("name_zh", temp.get("name_zh"));
				row.put("name", key);
				List<Object> values = (List<Object>) temp.get("values");
				row.put("value", values.get(i));
				table.add(row);
			}
			retList.add(table);
		}
		return retList;
	}

}
