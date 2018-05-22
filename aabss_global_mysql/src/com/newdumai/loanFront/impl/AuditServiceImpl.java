package com.newdumai.loanFront.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.dumai_data.dm_3rd_interface.util.CostUtil;
import com.newdumai.dumai_data.dm_label.DmLabelService;
import com.newdumai.dumai_data.dm_out.Dm_outService;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.global.service.Dumai_sourceBaseService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanFront.AuditResultConstant;
import com.newdumai.loanFront.AuditService;
import com.newdumai.loanFront.OrderInfoService;
import com.newdumai.setting.interface_source.Interface_sourceService;
import com.newdumai.sysmgr.BizFunctionSettingsService;
import com.newdumai.util.Arith;
import com.newdumai.util.DaoHelper;
import com.newdumai.util.Dm_3rd_InterfaceUtils;
import com.newdumai.util.HttpClientUtil;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.MapObjUtil;
import com.newdumai.util.SpringApplicationContextHolder;
import com.newdumai.util.SqlHelper;

@Service("auditService")
public class AuditServiceImpl extends BaseServiceImpl implements AuditService {
	@Autowired
	Interface_sourceService interface_sourceService;

	@Autowired
	Dm_outService dm_outService;

	@Autowired
	Dumai_sourceBaseDao dumai_sourceBaseDao;

	@Autowired
	Dm_3rd_interfaceService dm_3rd_interfaceService;

	@Autowired
	Dumai_sourceBaseService dumai_sourceBaseService;
	
	@Autowired
	DmLabelService dmLabelService;
	
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private BizFunctionSettingsService bizFunctionSettingsService;
    
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public List<Map<String, Object>> findResultsByOrderCode(String code) {
		return mysqlSpringJdbcBaseDao.queryForList("select gzt.*,gz.name guize_name from fk_guize_detail gzt left join fk_guize gz on gzt.guize_code=gz.code  where order_code=?", code);
	}

	@Override
	public float getAuditOneCost(String orderId) {
		Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo WHERE `code`=?", orderId);
		List<Map<String, Object>> ruleList = auditOne_1_getLogic(orderMap.get("thetype").toString());
		return getAuditOneCost(orderId, auditOne_2_getinterfaceCodes(ruleList));
	}

	private float getAuditOneCost(String orderId, Set<String> interfacesCodes) {
		// 准备数据
		float ret = 0;
		for (String code : interfacesCodes) {
			ret += getInterfacesCostInOrder(orderId, code);
		}
		return ret;
	}

	@Override
	public String auditOne(String orderId) {
		Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo WHERE `code`=?", orderId);
		if (orderMap == null || orderMap.isEmpty())
			return orderId + " order Can't find。。。";
		String subEntityId = orderMap.get("sub_entity_id").toString();
		// 查询余额
		Map<String, Object> companyMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM company_order WHERE sub_entity_id =?", subEntityId);
		/** block 查询花费 begin */
		List<Map<String, Object>> ruleList = auditOne_1_getLogic(orderMap.get("thetype").toString());
		Set<String> interfacesCodes = auditOne_2_getinterfaceCodes(ruleList);
		if (interfacesCodes.isEmpty())
			return "interfacesCodes Can't find。。。";
		// float cost=getAuditOneCost(orderId);
		float cost = getAuditOneCost(orderId, interfacesCodes);
		// 余额-cost>50 check
		int status = (Integer) companyMap.get("status");
		float todayBalance = (Float) companyMap.get("todayBalance");
		float totalBalance = (Float) companyMap.get("totalBalance");
		/** block 查询花费 end */
		if (status == 0)
			return "账户已停用！";
		if (totalBalance - cost < 50) {
			return "账户余额不足！";
		} else {
			if (todayBalance - cost < 0) {
				return "今日可用余额不足！";
			} else {
				// 扣钱
				Map<String, Object> para = new HashMap<String, Object>();
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("code", companyMap.get("code"));
				para.put("todayBalance", todayBalance - cost);
				para.put("totalBalance", totalBalance - cost);
				para.put("authorityResult", "正常审核");
				para.put("message", "【扣除：" + cost + "，订单号：" + orderId + "," + orderMap.get("name").toString() + "】");
				para.put("updateTime", new Date());
				super.Update(para, "company_order", where);
				Map<String, Object> save = new HashMap<String, Object>();
				save.put("sub_entity_id", subEntityId);
				save.put("fk_orderinfo_code", orderId);
				save.put("cost", cost);
				save.put("log", "自动审核！【扣除：" + cost + "，订单号：" + orderId + "," + orderMap.get("name").toString() + "】");
				addAndRet(save, "company_order_log");
				// 执行
				executeAuditOne(orderId, orderMap, ruleList, interfacesCodes);
			}
		}
		return "success";
	}

	@SuppressWarnings({ "unchecked" })
	private String executeAuditOne(String orderId, Map<String, Object> orderMap, List<Map<String, Object>> ruleList, Set<String> interfacesCodes) {
		Map<String, Object> interfacesResult = new HashMap<String, Object>();
		List<String> ruleResult = new ArrayList<String>();
		try {
			// 准备数据源结果数据
			Map<String, Object> inPara;
			for (String code : interfacesCodes) {
				inPara = auditOne_3_getInPara(code, orderMap);
				String s = (String) interface_sourceService.testDS(orderMap.get("sub_entity_id").toString(), orderId, code, inPara);
				// s=s.replace("\"", "");
				// s=s.replace("\\", "");
				Map<String, Object> result = JsonToMap.jsonToMap(s);
				interfacesResult.put(code, result);
			}
			// ^^^^准备数据源结果数据
			// 准备逻辑数据
			Map<String, Object> rule;
			StringBuilder sb;// 输出表达式
			StringBuilder sb2;// 输出表达式，供参考
			StringBuilder ruleExpression = new StringBuilder();
			for (int i = 0; i < ruleList.size(); i++) {// 循环规则
				sb = new StringBuilder();
				sb2 = new StringBuilder("[op:0=,1>=,2<,3<=,4<>]");
				rule = new Gson().fromJson(ruleList.get(i).get("logs").toString(), new TypeToken<Map<String, Object>>() {
				}.getType());
				for (int j = 0; j <= Integer.valueOf(rule.get("order").toString()); j++) {// 循环逻辑开始
					// 取逻辑表达式中参数变量对应的数据源结果值
					boolean execute;
					Map<String, Object> dspara = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source_para WHERE `type`='1' and `name` <> '--' AND code=?", rule.get("pa" + j).toString());
					String para_group = dspara.get("para_group").toString();
					sb2.append("[" + dspara.get("name") + "_" + dspara.get("description") + "_");
					String vresult = null;
					try {
						if (para_group == null || "".equals(para_group)) {
							Map<String, Object> mm = (Map<String, Object>) interfacesResult.get(rule.get("ds" + j).toString());
							vresult = mm.get(dspara.get("name")).toString();
							mm = null;
						} else {
							Object obj = interfacesResult.get(rule.get("ds" + j).toString());
							String[] split = para_group.split("_");
							for (String string : split) {
								obj = auditOne_4_getKey(string, obj);
							}
							vresult = ((Map<String, Object>) obj).get(dspara.get("name")).toString();
						}
						vresult = vresult.replace("\"", "");
						execute = auditOne_5_execute(vresult, rule.get("va" + j).toString(), rule.get("op" + j).toString());// true表示命中
					} catch (Exception e) {
						e.printStackTrace();
						execute = false;
					}
					// 生成逻辑的表达式（供参考）
					sb2.append(vresult + "_" + rule.get("op" + j).toString() + "_" + rule.get("va" + j).toString() + "_" + execute + "]");
					// 生成逻辑的表达式开始
					String si = rule.get("si" + j).toString();
					if ("2".equals(si)) {
						sb.append("(");
					} else if ("3".equals(si)) {
						sb.append(")");
					}
					if (j != 0) {
						if ("or".equals(rule.get("sign" + j).toString())) {
							sb.append("||");
						} else if ("and".equals(rule.get("sign" + j).toString())) {
							sb.append("&&");
						}
					}
					if (execute) {
						sb.append(true);
					} else if (execute == false) {
						sb.append(false);
					} else {
						sb.append("#");
					} // 生成逻辑的表达式结束
				} // 循环逻辑结束
				String logicResult = "";
				try {
					logicResult = engine.eval(sb.toString()).toString();
				} catch (ScriptException e) {
					logicResult = "error";
				}
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("expression", sb.toString());
				p.put("expression2", sb2.toString());
				p.put("result", logicResult);
				p.put("guize_code", rule.get("guizeCode").toString());
				p.put("order_code", orderId);
				add_fk_guize_detail(p);
				// rule 结果
				ruleResult.add(logicResult);
				if ("true".equals(logicResult) || "false".equals(logicResult)) {
					ruleExpression.append(logicResult);
					ruleExpression.append("_");
				}
			}
			String s = ruleExpression.deleteCharAt(ruleExpression.length() - 1).toString();
			s = s.replace("_", "||");
			String eval = "";
			try {
				eval = engine.eval(s).toString();
			} catch (ScriptException e) {
				eval = "error";
			}
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("code", orderId);
			if ("true".equals(eval)) {
				p.put("status1", "2");
			} else if ("false".equals(eval)) {
				p.put("status1", "1");
			} else {
				p.put("status1", "3");
			}
			upadte_fk_orderinfo(p);
			return eval;
		} catch (Exception e) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("code", orderId);
			p.put("status1", "3");
			upadte_fk_orderinfo(p);
			e.printStackTrace();
			return "error";
		}
	}

	@Override
	public String auditOne2(String orderId) {
		// 1.执行反欺诈模型评分获取反欺诈
		Float total_score = executeAuditOne2(orderId, "1");

		// 2.根据反欺诈模型评分判断是否执行信用评分
		if (total_score != null) {
			executeAuditOne2(orderId, "2");
		}
		return "success";
	}

	/**
	 * 对订单进行评分
	 * 
	 * @select：[fk_orderinfo,manager_model, manager_model_group,
	 *                                      manager_model_group__manager_model] @param
	 *                                      orderId ： 订单id
	 * @param type
	 *            :1 欺诈评分，2 信用评分
	 * @return
	 */

	/* block executeAuditOne2 begin */
	/**
	 * 对订单进行评分
	 * 
	 * @select：[fk_orderinfo,manager_model, manager_model_group,
	 *                                      manager_model_group__manager_model] @param
	 *                                      orderId ： 订单id
	 * @param type
	 *            :1 欺诈评分，2 信用评分
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Float executeAuditOne2(String orderId, String type) {
		// 1.查询订单信息
		Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo WHERE `code`=?", orderId);
		if (orderMap == null || orderMap.isEmpty()) {
			return null;
		}
		String sys_type_code = (String) orderMap.get("thetype");
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		// 2.查询所有评分项
		String sql = getItems(sub_entity_id, sys_type_code, type);
		List<Map<String, Object>> model_itemList = mysqlSpringJdbcBaseDao.queryForList(sql);
		if (CollectionUtils.isEmpty(model_itemList)) {
			return null;
		}
		// 3.查询并执行所有启用线上数据源等到请求返回结果map
		Set<String> dmSourceCodeSet = new HashSet<String>();
		for (Map<String, Object> model_itemMap : model_itemList) {
			String dmSourceCode = (String) model_itemMap.get("dm_source_code");
			if (StringUtils.isNotEmpty(dmSourceCode)) {
				dmSourceCodeSet.add(dmSourceCode);
			}
		}
		Map<String, Object> dmSourceParaResult = (Map<String, Object>) dm_outService.getAllDmSourceOutParas().get("data");
		Map<String, Object> dmSourceResult = (Map<String, Object>) dm_outService.getAuditOrderResult(orderMap, dmSourceCodeSet).get("data");
		// 4.遍历评分项，根据数据源执行结果打分，并保存
		Float total_score = 0f;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 循环评分项
		for (Map<String, Object> model_itemMap : model_itemList) {
			log.info(model_itemMap.get("item_name") + ":" + model_itemMap.get("item_descripe"));
			String vresult = null;
			String dresult = null;
			Double score = 0.00;
			Float weight = (Float) model_itemMap.get("weight");
			Float weight_score = 0f;
			String seperate_box = (String) model_itemMap.get("seperate_box");
			/** 获取para路径开始 */
			String dm_source_code = (String) model_itemMap.get("dm_source_code");
			String dm_source_para_code = (String) model_itemMap.get("dm_source_para_code");
			if (StringUtils.isNotEmpty(dm_source_code) && StringUtils.isNotEmpty(dm_source_para_code)) {
				// 获取数据源结果
				dresult = (String) dmSourceResult.get(dm_source_code);
				if (dresult == null) {
					vresult = null;
				} else {
					// 获取输出数据
					try {
						Map<String, Object> dm_result = JsonToMap.gson2Map(dresult);
						vresult = (String) dm_result.get(dmSourceParaResult.get(dm_source_para_code));
						vresult = vresult == null ? "no_result" : String.valueOf(vresult);
					} catch (Exception e) {
						vresult = "no_result";
					}
				}
				log.info("dm_result:" + dresult);
				log.info("vresult:" + vresult);
			}
			/** 获取para路径结束 */
			List<Map<String, Object>> ruleList = null;
			try {
				// 获取评分规则
				ruleList = JsonToMap.gson2List(seperate_box);
			} catch (Exception e) {
				log.info("分箱未配置或配置不正确");
			}
			// 根据评分规则评分
			if (CollectionUtils.isNotEmpty(ruleList)) {
				score = getScore(ruleList, vresult);
			}
			// 计算加权分
			weight_score = (float) (score * weight);
			// 保存评分项结果
			Map<String, Object> scoreResult = new LinkedHashMap<String, Object>();
			scoreResult.put("score", score);
			scoreResult.put("result", vresult == null ? "null" : vresult);
			scoreResult.put("dm_result", dresult == null ? "null" : dresult);
			scoreResult.put("seperate_box", ruleList);
			scoreResult.put("manager_item_name", model_itemMap.get("item_name"));
			scoreResult.put("manager_item_code", model_itemMap.get("item_code"));
			scoreResult.put("weight", weight);
			scoreResult.put("weight_score", weight_score);
			result.add(scoreResult);
			// 计算加权总分
			total_score += weight_score;
			log.info("core:" + score);
			log.info("**********************************************");
		}
		Map<String, Object> save = new HashMap<String, Object>();
		save.put("fk_orderinfo_code", orderId);
		save.put("type", type);
		save.put("total_score", total_score);
		save.put("result", new Gson().toJson(result));
		addAndRet(save, "manager_model_result");
		// 返回加权总分
		return total_score;
	}
	/* block executeAuditOne2 end */

	private List<Map<String, Object>> auditOne_1_getLogic(String thetype) {
		return mysqlSpringJdbcBaseDao.queryForList(gen_auditOne_1_getLogic(thetype));
	}

	private Set<String> auditOne_2_getinterfaceCodes(List<Map<String, Object>> ruleList) {
		Set<String> interfacesCode = new HashSet<String>();
		for(Map<String,Object>rule:ruleList){
			String[] split = ((String) rule.get("interfaces")).split(",");
			for(String is:split){
				interfacesCode.add(is);
			}
		}
		return interfacesCode;
	}

	private Map<String, Object> auditOne_3_getInPara(String code, Map<String, Object> map) {
		Map<String, Object> inPara = new HashMap<String, Object>();
		List<Map<String, Object>> list = mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_interface_source_para WHERE `type`= '0' and `name` <> '--' AND Interface_source_code='" + code + "'");
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

	@Override
	public Map<String, Object> getInPara(String orderId, String interfaceCode) {
		Map<String, Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo WHERE `code`=?", orderId);
		return getInPara(interfaceCode, orderMap);
	}

	@Override
	public float getInterfacesCostInOrder(String orderId, String interfaceCode) {
		String inPara = new Gson().toJson(getInPara(orderId, interfaceCode));
		Map<String, Object> map = mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM sys_interface_source_detail a INNER JOIN sys_interface_source_detail_orderinfo b ON a.`code` = b.sys_interface_source_detail_code WHERE a.interface_source_code = '" + interfaceCode + "'  AND b.order_code = '" + orderId + "'");
		if (map != null && inPara.equals((String) map.get("in_para"))) {
			return 0;
		} else {
			return interface_sourceService.getInterfacesCost(interfaceCode);
		}
	}

	private Map<String, Object> getInPara(String interfaceCode, Map<String, Object> orderMap) {
		Map<String, Object> inPara = new HashMap<String, Object>();
		List<Map<String, Object>> list = mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_interface_source_para WHERE `type`= '0' AND Interface_source_code='" + interfaceCode + "'");
		for (Map<String, Object> paras : list) {
			String fk_orderinfo_name = (String) paras.get("fk_orderinfo_name");
			if (!StringUtils.isEmpty(fk_orderinfo_name)) {
				String value = (String) orderMap.get(fk_orderinfo_name);
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

	@SuppressWarnings("rawtypes")
	private Object auditOne_4_getKey(String str, Object o) {
		if (o instanceof List) {
			return ((List) o).get(Integer.valueOf(str));
		} else {
			return ((Map) o).get(str);
		}
	}

	private Boolean auditOne_5_execute(String v1, String v2, String ops) {
		int op = Integer.valueOf(ops);
		Bindings bindings = engine.createBindings();
		bindings.put("value", v1);
		bindings.put("value2", v2);
		boolean b = false;//默认和无结果未命中
		try {
			switch (op) {
			case 1:
				b = (Boolean) engine.eval("value>=value2", bindings);
				break;
			case 2:
				b = (Boolean) engine.eval("value<value2", bindings);
				break;
			case 3:
				b = (Boolean) engine.eval("value<=value2", bindings);
				break;
			case 4:
				b = (Boolean) engine.eval("value!=value2", bindings);
				break;
			// case 5:
			// b= (Boolean) engine.eval("value!=value2");break;
			case 7:
				b = v1.contains(v2);
				break;
			case 8:
				b = (Boolean) engine.eval("value>value2", bindings);
				break;
			default:
				b = v1.equals(v2);
			}
		} catch (ScriptException e) {
			b = false;
		}
		return b;
	}

	private Boolean auditOne2_5_execute(String v1, String v2, String opt) {
		String[] optSign = { ">", ">=", "<", "<=", "=", "!=", "contains", "!contains", "other", "no_result", "null","be_containsed_by"};
		int op = 0;
		for (int i = 0; i < optSign.length; i++) {
			if (opt.equals(optSign[i])) {
				op = i;
				break;
			}
		}
		Bindings bindings = engine.createBindings();
		bindings.put("value", v1);
		bindings.put("value2", v2);
		boolean b;
		try {
			switch (op) {
			case 0:
				b = (Boolean) engine.eval("value>value2", bindings);
				break;
			case 1:
				b = (Boolean) engine.eval("value>=value2", bindings);
				break;
			case 2:
				b = (Boolean) engine.eval("value<value2", bindings);
				break;
			case 3:
				b = (Boolean) engine.eval("value<=value2", bindings);
				break;
			case 4:
				b = (Boolean) engine.eval("value==value2", bindings);
				break;
			case 5:
				b = (Boolean) engine.eval("value!=value2", bindings);
				break;
			case 6:
				b = v1.contains(v2);
				break;
			case 7:
				b = !v1.contains(v2);
				break;
			case 8:
				b = true;
				break;
			case 9:
				b = false;
				break;
			case 10:
				b = false;
				break;
			case 11:
				b = v2.contains(v1);
				break;
			default:
				b = v1.contains(v2);
			}
		} catch (ScriptException e) {
			b = false;
		}
		return b;
	}

	private void upadte_fk_orderinfo(Map<String, Object> para) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("code", para.get("code"));
		para.remove("code");
		Update(para, "fk_orderinfo", where);
	}

	private int add_fk_guize_detail(Map<String, Object> para) {
		return add(para, "fk_guize_detail");
	}

	/**
	 * 
	 */
	ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

	private String gen_auditOne_1_getLogic(String thetype) {
		if (StringUtils.isEmpty(thetype)){
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" fk_guiz_logs.id, ");
		sql.append(" fk_guiz_logs.`code`, ");
		sql.append(" fk_guiz_logs.fk_guize_code, ");
		sql.append(" fk_guiz_logs.interfaces,  ");
		sql.append(" fk_guiz_logs.`logs`,  ");
		sql.append(" fk_guiz_logs.opttime  ");
		sql.append(" FROM sys_type  ");
		sql.append(" INNER JOIN sys_rule_group ON sys_type.`code` = sys_rule_group.sys_type_code AND sys_rule_group.biz_range = '1'  ");
		sql.append(" INNER JOIN sys_rule_group__fk_guize ON sys_rule_group.`code` = sys_rule_group__fk_guize.sys_rule_group_code  ");
		sql.append(" INNER JOIN fk_guize ON sys_rule_group__fk_guize.fk_guize_code = fk_guize.`code`                              ");
		sql.append(" INNER JOIN fk_guiz_logs ON fk_guiz_logs.fk_guize_code = fk_guize.`code`                                      ");
		sql.append(" WHERE  ");
		sql.append(" 1 = 1  ");
		sql.append(" AND sys_type.`code` = '"+thetype+"' ");
		return sql.toString();
	}

	// public Map<String,Map<String, Object>> getInterfacesByLogics(String
	// sub_entity_id,List<Map<String, Object>> logic, Map<String, Object>
	// inPara) {
	// Map<String,Map<String, Object>> interfaces = new
	// HashMap<String,Map<String, Object>>();
	// Set<String> interfacesCode=new HashSet<String>();
	// try {
	// for (int i = 0; i < logic.size(); i++) {
	// String[] split = logic.get(i).get("interfaces").toString().split(",");
	// for (int j = 0; j < split.length; j++) {
	// interfacesCode.add(split[j]);
	// }
	// }
	// for (String str : interfacesCode) {
	// Map<String, Object> map =
	// mysqlSpringJdbcBaseDao.queryForMap(gen_getInterfacesByLogics(str));
	// String ret="";
	// if(map.get("ret")==null||"".equals(map.get("ret"))){
	// ret=interface_sourceService.testDS(sub_entity_id.toString(),str,inPara);
	// }
	// map.put("ret",ret);
	// interfaces.put(map.get("code").toString(),map);
	// }
	// } catch (Exception e) {
	// return null;
	// }
	// return interfaces;
	// }

	// private String gen_getInterfacesByLogics(String code) {
	// return "SELECT "
	// + "sys_interface_source.*,"
	// + "sys_interface_source_detail.result ret"
	// + "FROM"
	// + "sys_interface_source"
	// +
	// "LEFT JOIN sys_interface_source_detail ON sys_interface_source.`code` =
	// sys_interface_source_detail.interface_source_code"
	// + "WHERE"
	// + "sys_interface_source.`code`='"+code+"'";
	// }

	/* block AuditOne_rule begin */
	@Override
	public String auditOne_rule2(Map<String, Object> orderMap ,Map<String, Object> bigLabelMap) {
		// 1.查询规则
		List<Map<String, Object>> ruleList = auditOne_1_getLogic(orderMap.get("thetype").toString());
		if(CollectionUtils.isEmpty(ruleList)){
			return null;
		}
		//2.跑规则
		try {
			executeAuditOne_rule2(orderMap, ruleList, bigLabelMap);
		} catch (Exception e) {
			return null;
		}
		return "success";
	}

	private String executeAuditOne_rule2(Map<String, Object> orderMap, List<Map<String, Object>> ruleList,Map<String, Object> bigLabelMap) {
		String orderId = (String)orderMap.get("code");
		try {
			// 准备逻辑数据
			Map<String, Object> labelNameMap = getLabelNameMap();
			StringBuilder ruleExpression = new StringBuilder();
			for (Map<String,Object>rule:ruleList) {// 循环规则
				String ruleResult = null;
				StringBuilder sb = new StringBuilder();
				StringBuilder sb2 = new StringBuilder("[op:0=,1>=,2<,3<=,4<>]");
				Map<String, Object> logs = null;
				try {
					logs = JsonToMap.gson2Map((String) rule.get("logs"));
				} catch (Exception e1) {
					e1.printStackTrace();
					ruleResult = "error";
				}
				if(org.springframework.util.CollectionUtils.isEmpty(logs)){
					ruleResult = "error";
				}else{
					//ds:dm_label_code,cs:dm_group_code
					for (int j = 0; j <= Integer.valueOf((String)logs.get("order")); j++) {// 循环逻辑开始
						Boolean execute = null;
						String vresult = null;
						String dm_label_code = (String)logs.get("ds" + j);
						if (StringUtils.isNotEmpty(dm_label_code)){
							String labelName = (String) labelNameMap.get(dm_label_code);
							try {
								vresult = String.valueOf(bigLabelMap.get(labelName));
							} catch (Exception e){
								vresult = "null";
							}
							log.info(labelName + ":" + vresult);
							sb2.append("["+labelName+":"+vresult+",op:");
						}
						if(StringUtils.isNotEmpty(vresult)&&!("null".equals(vresult))){
							execute = auditOne_5_execute(vresult,(String)logs.get("va" + j),(String)logs.get("op" + j));// true表示命中
						}
						// 生成逻辑的表达式（供参考）
						sb2.append(logs.get("op" + j).toString() + ",value:" + logs.get("va" + j).toString() + ",result:" + execute + "]");
						// 生成逻辑的表达式开始
						String si = logs.get("si" + j).toString();
						if ("2".equals(si)) {
							sb.append("(");
						} else if ("3".equals(si)) {
							sb.append(")");
						}
						if (j != 0) {
							if ("or".equals(logs.get("sign" + j).toString())) {
								sb.append("||");
							} else if ("and".equals(logs.get("sign" + j).toString())) {
								sb.append("&&");
							}
						}
						String logicResult = String.valueOf(execute);
						if ("true".equals(logicResult)) {
							sb.append(true);
						} else if ("false".endsWith(logicResult)) {
							sb.append(false);
						} else {
							sb.append("#");
						} // 生成逻辑的表达式结束
					} // 循环逻辑结束
				}
				try {
					ruleResult = engine.eval(sb.toString()).toString();
				} catch (ScriptException e) {
					ruleResult = "error";
				}
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("expression", sb.toString());
				p.put("expression2", sb2.toString());
				p.put("result", ruleResult);
				p.put("guize_code", logs.get("guizeCode").toString());
				p.put("order_code", orderId);
				add_fk_guize_detail(p);
				if ("true".equals(ruleResult) || "false".equals(ruleResult)) {
					ruleExpression.append(ruleResult);
					ruleExpression.append("_");
				}
			}
			String s = ruleExpression.deleteCharAt(ruleExpression.length() - 1).toString();
			s = s.replace("_", "||");
			String eval = "";
			try {
				eval = engine.eval(s).toString();
			} catch (ScriptException e) {
				eval = "error";
			}
			Map<String, Object> update_order = new HashMap<String, Object>();
			update_order.put("code", orderId);
			if ("true".equals(eval)) {
				update_order.put("status1", "2");
			} else if ("false".equals(eval)) {
				update_order.put("status1", "1");
			} else {
				update_order.put("status1", "3");
			}
			upadte_fk_orderinfo(update_order);
			return eval;
		} catch (Exception e) {
			Map<String, Object> update_order = new HashMap<String, Object>();
			update_order.put("code", orderId);
			update_order.put("status1", "3");
			upadte_fk_orderinfo(update_order);
			e.printStackTrace();
			return "error";
		}
	}
	/* block AuditOne_rule end */
	
	
	/* block AuditOne3 begin */
	@Override
	public Integer auditOne3(Map<String, Object> orderMap) {
        bizFunctionSettingsService.getFunctionSettingsBySubentityId((String) orderMap.get("sub_entity_id"), (String) orderMap.get("thetype"));
//        if (!"2".equals(sys_company_typeMap.get("rule_model"))) {   
//        	return null;
//        }
        Map<String, Object> bigLabelMap = createLabel(orderMap);
		//执行信用评分
		Float total_score = exeRunItemScore(orderMap, bigLabelMap, "2");
		if (total_score != null) {    
			return Math.round(total_score);
		}
		return null;
	}
	
	private Float exeRunItemScore(Map<String, Object> orderMap, Map<String, Object> labelResultMap, String type) {
		// 1.查询所有评分项
		String orderId = (String) orderMap.get("code");
		String sys_type_code = (String) orderMap.get("thetype");
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		String sql = getItems(sub_entity_id, sys_type_code, type);
		List<Map<String, Object>> model_itemList = mysqlSpringJdbcBaseDao.queryForList(sql);
		if (CollectionUtils.isEmpty(model_itemList)) {
			return null;
		}
		// 2.获取用户标签结果 
//		Map<String, Object> labelResultMap =dmLabelService.getBigLabelMap(dm_label_detail_code);
		Map<String, Object> labelNameMap = getLabelNameMap();
//		Map<String, Object> orderResultMap = getLabelFromOrder(orderMap ,labelNameMap);
//		labelResultMap.putAll(orderResultMap);
		// 4.遍历评分项，根据数据源执行结果打分，并保存
		Float total_score = 0f;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 循环评分项
		for (Map<String, Object> model_itemMap : model_itemList) {
			log.info(model_itemMap.get("item_name") + ":" + model_itemMap.get("item_descripe"));
			String vresult = null;
			Double score = 0.00;
			Float weight = (Float) model_itemMap.get("weight");
			Float weight_score = 0f;
			String seperate_box = (String) model_itemMap.get("seperate_box");
			/** 获取para路径开始 */
			//String dm_label_group_code = (String) model_itemMap.get("dm_source_code");
			String dm_label_code = (String) model_itemMap.get("dm_source_para_code");
			if (StringUtils.isNotEmpty(dm_label_code)) {
				// 获取数据源结果
				String labelName = (String) labelNameMap.get(dm_label_code);
				try {
					vresult = String.valueOf(labelResultMap.get(labelName));
				} catch (Exception e) {
					e.printStackTrace();
					vresult = null;
				}
				log.info(labelName + ":" + vresult);
			}
			/** 获取para路径结束 */
			List<Map<String, Object>> ruleList = null;
			try {
				// 获取评分规则
				ruleList = JsonToMap.gson2List(seperate_box);
			} catch (Exception e) {
				log.info("分箱未配置或配置不正确");
			}
			// 根据评分规则评分
			if (CollectionUtils.isNotEmpty(ruleList)) {
				score = getScore(ruleList, vresult);
			}
			// 计算加权分
			weight_score = (float) (score * (weight==null?0:weight));
			weight_score = Arith.round(weight_score,2);
			// 保存评分项结果
			Map<String, Object> scoreResult = new LinkedHashMap<String, Object>();  
			scoreResult.put("score", score);
			scoreResult.put("result",vresult);
			scoreResult.put("seperate_box", ruleList);
			scoreResult.put("manager_item_name", model_itemMap.get("item_name"));
			scoreResult.put("manager_item_code", model_itemMap.get("item_code"));
			scoreResult.put("weight", weight);
			scoreResult.put("weight_score",Math.round(weight_score/=100));
			result.add(scoreResult);
			// 计算加权总分
			total_score += weight_score;
			log.info("core:" + score);
			log.info("**********************************************");
		}
		Map<String, Object> save = new HashMap<String, Object>();
		save.put("fk_orderinfo_code", orderId);
		save.put("type", type);
		save.put("total_score", Math.round(total_score));
		save.put("result", new GsonBuilder().serializeNulls().create().toJson(result));
		addAndRet(save, "manager_model_result");
		// 返回加权总分
		return total_score;
	}
	
	private Map<String, Object> createLabel(Map<String, Object> orderMap) {
		// 1.保存基本信息到 dm_label_detail
 		String dm_label_detail_code = saveToDm_label_detail(orderMap, "");
		if (StringUtils.isEmpty(dm_label_detail_code)) {
			return null;
		}
		// 2.查询所有标签对应的3rd
		List<Map<String, Object>> dm_label__dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(get3rdSql());
		if (CollectionUtils.isEmpty(dm_label__dm_3rd_interfaceList)) {   
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (Map<String, Object> dm_label__dm_3rd_interfaceMap : dm_label__dm_3rd_interfaceList) {// 循环执行接口数据源
			try {
				String result = null;
				String dm_3rd_interface_code = (String) dm_label__dm_3rd_interfaceMap.get("code");
				// 转换参数（将订单源参数（值）转换为接口数据源参数（值））
				Map<String, Object> dm_3rd_in_para = Dm_3rd_InterfaceUtils.trans_dm_label_inPara((String) dm_label__dm_3rd_interfaceMap.get("in_para"), orderMap);
				result = dm_3rd_interfaceService.testDS(dm_3rd_interface_code, dm_3rd_in_para);
				// 检查结果并存储
				if (StringUtils.isNotEmpty(result)) {
					resultMap.put(dm_3rd_interface_code, result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 3.对3rd结果进行处理
		Map<String, Object> resultMap2 = transResult(resultMap);
		// 4.保存到 dm_label_detail
		Map<String, Object> labelNameMap = getLabelNameMap(); 
		Map<String, Object> orderResultMap = getLabelFromOrder(orderMap, labelNameMap);
		resultMap2.put("group_order", orderResultMap);
		orderMap.putAll(resultMap2); 
		dm_label_detail_code = saveToDm_label_detail(orderMap, dm_label_detail_code);
		// 5.获取用户标签结果 dm_label_detail resultMap
		Map<String, Object> labelResultMap = dmLabelService.getBigLabelMap(dm_label_detail_code);
		//labelResultMap.putAll(orderResultMap);
		return labelResultMap;
	}

	/**
	 * 结果例如：{"msg":"一致"}
	 * @param resultMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> transResult(Map<String, Object> resultMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 查询画像输出参数及对应第三方数据源参数关系
		List<Map<String, Object>> label_3rdList = dumai_sourceBaseDao.queryForList(get_label_group_3rdSql());
		if (CollectionUtils.isEmpty(label_3rdList)) {
			return null;
		}
		for (Map<String, Object> label_3rdMap : label_3rdList) {
			String group_name = (String) label_3rdMap.get("group_name");
			String dm_3rd_interface_code = (String) label_3rdMap.get("dm_3rd_interface_code");
			String dm_3rd_result = (String) resultMap.get(dm_3rd_interface_code);
			try {
				Map<String, Object> dm_3rd_resultMap = JsonToMap.gson2Map(dm_3rd_result);
				String para_group = (String) label_3rdMap.get("para_group");
				Map<String, Object> data = new HashMap<String, Object>();
				if (StringUtils.isNotEmpty(para_group)) {
					String para_path[] = para_group.split("_");// 多层的支持
					Map<String, Object> curData = dm_3rd_resultMap;
					for (String key : para_path) {
						curData = (Map<String, Object>) curData.get(key);
					}
					data = curData;
				} else {
					data = dm_3rd_resultMap;
				}
				if (returnMap.containsKey(group_name)) {
					((Map<String, Object>) returnMap.get(group_name)).put((String) label_3rdMap.get("label_name"), data.get(label_3rdMap.get("3rd_para_name")));
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put((String) label_3rdMap.get("label_name"), data.get(label_3rdMap.get("3rd_para_name")));
					returnMap.put(group_name, map);
				}
			} catch (Exception e) {
				if (returnMap.containsKey(group_name)) {
					((Map<String, Object>) returnMap.get(group_name)).put((String) label_3rdMap.get("label_name"), null);
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put((String) label_3rdMap.get("label_name"), null);
					returnMap.put(group_name, map);
				}
			}
		}
		return returnMap;
	}

	private String get3rdSql() {
		String sql = "  select ";
		sql += " dm_3rd_interface.* , ";
		sql += " dm_label__dm_3rd_interface.in_para ";
		sql += " FROM ";
		sql += " dm_label__dm_3rd_interface ";
		sql += " INNER JOIN dm_3rd_interface ON dm_label__dm_3rd_interface.dm_3rd_interface_code = dm_3rd_interface.`code` ";
		sql += " GROUP BY ";
		sql += " dm_label__dm_3rd_interface.dm_3rd_interface_code ";
		return sql;
	}

	private String get_label_group_3rdSql() {
		String sql = " SELECT ";
		sql += " dm_label_group.group_name,  ";
		sql += " dm_label_group.group_name_zh,  ";
		sql += " dm_label.`name` as 'label_name' ,  ";
		sql += " dm_label.name_zh,  ";
		sql += " dm_label__dm_3rd_interface.dm_3rd_interface_code,  ";
		sql += " dm_3rd_interface_para.`name` as '3rd_para_name',  ";
		sql += " dm_3rd_interface_para.para_group  ";
		sql += " FROM  ";
		sql += " dm_label  ";
		sql += " INNER JOIN dm_label_group ON dm_label.dm_label_group_code = dm_label_group.`code`  ";
		sql += " INNER JOIN dm_label__dm_3rd_interface ON dm_label.`code` = dm_label__dm_3rd_interface.dm_label_code  ";
		sql += " INNER JOIN dm_3rd_interface_para ON dm_label__dm_3rd_interface.dm_3rd_interface_para_code = dm_3rd_interface_para.`code`  ";
		return sql;
	}

	private String saveToDm_label_detail(Map<String, Object> orderMap, String dm_label_detail_code) {
		Map<String, Object> labelMap = new HashMap<String, Object>();
		try {
			// 维护labelMap begin
			labelMap.putAll(orderMap);
			labelMap.remove("code");
			labelMap.remove("ID");
			labelMap.remove("opttime");
			labelMap = transOrderData(labelMap);
			// 维护labelmap end 
			// 查询 dm_label_detail begin
			if (StringUtils.isEmpty(dm_label_detail_code)) {
				String card_num = (String) labelMap.get("card_num");
				if (StringUtils.isEmpty(card_num)) {
					return null;
				}
				String sql = SqlHelper.getSelectSqlByParams("dm_label_detail", orderMap, new String[] {"card_num","mobile","bank_num"});
				Map<String, Object> dm_label_detailMap = dumai_sourceBaseDao.queryForMap(sql);
				// 查询 dm_label_detail end
				// 结果不为空 取标签code
				if (!(org.springframework.util.CollectionUtils.isEmpty(dm_label_detailMap))) {
					dm_label_detail_code = (String) dm_label_detailMap.get("code");
				}
			}
			if (StringUtils.isEmpty(dm_label_detail_code)) {
				// 保存
				dm_label_detail_code = dumai_sourceBaseService.addAndRet(labelMap, "dm_label_detail");
			} else {
				// 更新
				Map<String, Object> where = new HashMap<String, Object>();
				where.put("code", dm_label_detail_code);
				dumai_sourceBaseService.Update(labelMap, "dm_label_detail", where);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dm_label_detail_code = null;
		}
		return dm_label_detail_code;  
	}

	private Map<String, Object> transOrderData(Map<String, Object> orderMap) {
		List<Map<String, Object>> nameList = mysqlSpringJdbcBaseDao.queryForList("select COLUMN_NAME as 'name' from information_schema.COLUMNS where table_name = ?  and table_schema = ? ", new Object[] { "dm_label_detail", "dumai_source" });
		Map<String, Object> returnMap = new HashMap<String, Object>();
		for (Map<String, Object> nameMap : nameList) {
			String name = (String) nameMap.get("name");
			if (orderMap.containsKey(name)) {
				if (orderMap.get(name) instanceof Map) {
					returnMap.put(name, new GsonBuilder().serializeNulls().create().toJson(orderMap.get(name)));
					continue;
				}
				returnMap.put(name, orderMap.get(name));
			}
		}
		return returnMap;
	}

	private Map<String, Object> getLabelFromOrder(Map<String, Object> orderMap, Map<String, Object> labelNameMap) {
		Map<String,Object>returnMap = new HashMap<String,Object>();
		for(Entry<String,Object>entry:labelNameMap.entrySet()){
			String name = (String)entry.getValue();
			if(orderMap.containsKey(name)){
				returnMap.put(name, orderMap.get(name));
			}
		}
		return returnMap;
	}

	/**
	 * 查询  dm_label_code 和 dm_label_name对应关系
	 * @return
	 */
	private Map<String, Object> getLabelNameMap() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> dm_labelList = dumai_sourceBaseDao.queryForList(" select * from dm_label where 1=1 and is_able = '1' ");
		for (Map<String, Object> dm_labelMap : dm_labelList) {
			returnMap.put((String) dm_labelMap.get("code"), dm_labelMap.get("name"));
		}
		return returnMap;
	}

	private Double getScore(List<Map<String, Object>> ruleList, String vresult) {
		Double score = 0.00;
		try {
			if (vresult == null||"null".equals(vresult)) {
				score = getDefaultScore(ruleList, "null");
			} else if ("".equals(vresult)) {
				score = getDefaultScore(ruleList, "no_result");
			} else {
				for (int i = 0; i < ruleList.size(); i++) {
					Map<String, Object> rule = ruleList.get(i);
					String opt = (String) rule.get("opt");
					boolean execute = false;
					try {
						execute = auditOne2_5_execute(vresult, String.valueOf(rule.get("value")), opt);
					} catch (Exception e) {
						score = getDefaultScore(ruleList, "null");
					}
					if (execute) {
						score = (Double) rule.get("score");
						if ("other".equals(opt) && i < ruleList.size() - 1) {
							continue;
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			score = getDefaultScore(ruleList, "null");
		}
		return score;
	}

	private Double getDefaultScore(List<Map<String, Object>> ruleList, String opt) {
		Double score = 0.00;
		try {
			for (Map<String, Object> rule : ruleList) {
				if (opt.equals((String) rule.get("opt"))) {
					score = (Double) rule.get("score");
					break;
				}
			}
		} catch (Exception e) {
			for (Map<String, Object> rule : ruleList) {
				if ("null".equals((String) rule.get("opt"))) {
					score = (Double) rule.get("score");
					break;
				}
			}
		}
		return score;
	}

	// 查询评分项sql
	private String getItems(String sub_entity_id, String sys_type_code, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("  SELECT ");
		sb.append("  * ");
		sb.append("  FROM ");
		sb.append("  ( ");
		sb.append(" SELECT ");
		sb.append(" sys_company_type.`code` AS sys_company_type_code, ");
		sb.append(" manager_model.`code` AS model_code, ");
		sb.append(" manager_model__manager_item.weight, ");
		sb.append(" manager_model__manager_item.seperate_box, ");
		sb.append(" manager_item.dm_source_code, ");
		sb.append(" manager_item.dm_source_para_code, ");
		sb.append(" manager_item.`name` AS item_name, ");
		sb.append(" manager_item.descripe AS item_descripe, ");
		sb.append(" manager_model.`name` AS model_name, ");
		sb.append(" manager_model_group.`name` AS model_group_name, ");
		sb.append(" manager_item.`code` AS item_code ");
		sb.append(" FROM ");
		sb.append(" sys_company_type ");
		sb.append(" INNER JOIN manager_model_group ON sys_company_type.type_code = manager_model_group.sys_type_code ");
		sb.append(" AND manager_model_group.is_able = '1' ");
		sb.append(" and manager_model_group.biz_range='1' ");
		sb.append(" AND sys_company_type.is_able = '1' ");
		sb.append(" AND sys_company_type.sub_entity_id = '"+sub_entity_id+"' ");
		sb.append(" AND sys_company_type.type_code = '"+sys_type_code+"' ");
		sb.append(" INNER JOIN manager_model__manager_model_group ON manager_model_group.`code` = manager_model__manager_model_group.manager_model_group_code ");
		sb.append(" INNER JOIN manager_model ON manager_model.`code` = manager_model__manager_model_group.manager_model_code ");
		sb.append(" AND manager_model.model_type = '2' ");
		sb.append(" AND manager_model.is_able = '1' ");
		sb.append(" INNER JOIN manager_model__manager_item ON manager_model.`code` = manager_model__manager_item.manager_model_code ");
		sb.append(" AND manager_model__manager_item.seperate_box <> '' ");
		sb.append(" AND manager_model__manager_item.weight <> '' ");
		sb.append(" INNER JOIN manager_item ON manager_item.`code` = manager_model__manager_item.manager_item_code ");
		sb.append(" AND manager_item.is_able = '1' ");
		sb.append(" ) AS model ");
		sb.append(" LEFT JOIN ( ");
		sb.append(" SELECT ");
		sb.append(" sys_company_type__manager_model.manager_model_code, ");
		sb.append(" sys_company_type.sub_entity_id, ");
		sb.append(" sys_company_type.type_code ");
		sb.append(" FROM ");
		sb.append(" sys_company_type__manager_model ");
		sb.append(" INNER JOIN sys_company_type ON sys_company_type.`code` = sys_company_type__manager_model.sys_company_type_code ");
		sb.append(" AND sys_company_type.is_able = '1' ");
		sb.append(" AND sys_company_type.rule_model = '2' ");
		sb.append(" AND sys_company_type.sub_entity_id = '"+sub_entity_id+"' ");
		sb.append(" AND sys_company_type.type_code = '"+sys_type_code+"' ");
        sb.append(" ) AS model2 ON model2.manager_model_code = model.model_code ");
        sb.append(" WHERE ");
        sb.append(" manager_model_code IS NOT NULL ");
		return sb.toString();
	}

	/* block runItemScore end */
	/* block AuditOne3 end */
	
	/* run model_grade start */
	//TODO 评级结果
	@Override
	public String getModelGrade(Map<String,Object>orderMap,int score){
		String result = null ;
		try {
			result = runModelGrade(orderMap, score);
		} catch (Exception e) {
			result = "error";
			e.printStackTrace();
		}
		return result;
	}
	
	private String runModelGrade(Map<String,Object>orderMap,int score){
		String [] grade = {"A","B","C","D","E","F"};
		List<Map<String, Object>> data = getData((String)orderMap.get("thetype"));
		if(CollectionUtils.isEmpty(data)){return grade[CollectionUtils.size(data)];}
		Map<String, Object> bigLabelMap = dmLabelService.getBigLabelMap((String)orderMap.get("card_num"),(String)orderMap.get("bank_num"),(String)orderMap.get("mobile"));
	    String model_grade = null;Boolean flag = false;Integer num = 0;
		for(int i =0;i<data.size();i++){
			if(isInsideGrade((float)score, Float.parseFloat((String)data.get(i).get("line")), Float.parseFloat((String)data.get(i).get("proportion")))){
				System.out.println(score+"属于临界区间:"+data.get(i).toString());
				flag = true;num = i ;break;
			}
		}
		if(flag){
			//进行临界运算
			int l = Integer.parseInt((String) data.get(num).get("line"));
			int p = Integer.parseInt((String) data.get(num).get("proportion"));
			System.out.println("区间为：["+l*(1-(float)p/100)+"~"+l*(1+(float)p/100)+"]");
			System.out.println("num:"+num);
			List<Map<String,Object>>xuanxianglist = (List<Map<String, Object>>) data.get(num).get("xuanxianglist");
			if(isAdvance(score,xuanxianglist,bigLabelMap)){num ++;}
			model_grade = grade[data.size()-num];
		}else{
			//进行区间运算
			for(int i =0;i<data.size();i++){
				int line = Integer.parseInt((String)data.get(i).get("line"));num = i ;
				if(score<=line){model_grade = grade[data.size()-i];break;}
				if(i==data.size()-1){model_grade = grade[0];}
			}
			System.out.println(score+"属于常规区间:"+data.get(num).toString());
		}
		return model_grade;
	}
	
	private Boolean isInsideGrade(float i,float l,float p){
		return Math.max(l*(1-p/100),i)==Math.min(i,l*(1+p/100));
	}
	
	@SuppressWarnings("unchecked")
	private Boolean isAdvance(int score ,List<Map<String,Object>>xuanxianglist,Map<String,Object> bigLabelMap){
		if(CollectionUtils.isEmpty(xuanxianglist)||CollectionUtils.sizeIsEmpty(bigLabelMap)){
			return false;
		}
		String result = "true";
		boolean flag = false;
		for(Map<String,Object> item : xuanxianglist){
			if(flag){break;};
			String result2 = "true";
			String item_name = (String) item.get("item_name");
			String v1 = String.valueOf(bigLabelMap.get(item_name));
			List<Map<String,Object>>runList = (List<Map<String, Object>>) item.get("runlist");
			if(CollectionUtils.isEmpty(runList)){continue;}
			for (int  i= 0;i<runList.size();i++) {
				Map<String, Object> run = runList.get(i);
				String opt = (String) run.get("opt");
				String v2 = (String) run.get("value");
				boolean f = grade_execute(v1, v2, opt);
				if(f){
					String result3 = (String) run.get("result");
					if("===true".equals(result3)){
						flag = true;
						break;
					}else if("===false".equals(result3)){
						flag = true;
						result = "false";
						break;
					}else{
						result2 = result3;
					}
				}
				if(i == runList.size()-1){
					result += "&&"+result2 ;
				}
			}
		}
		Boolean flag2 = false;
		 try {
			flag2 = (boolean) engine.eval(result);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return flag2;
	}
	
	private Boolean grade_execute(String v1, String v2, String opt) {
		String[] optSign = { ">", ">=", "<", "<=", "=", "!=", "contains", "!contains", "other", "no_result", "null","be_containsed_by"};
		int op = 0;
		for (int i = 0; i < optSign.length; i++) {
			if (opt.equals(optSign[i])) {
				op = i;
				break;
			}
		}
		Bindings bindings = engine.createBindings();
		bindings.put("value", v1);
		bindings.put("value2", v2);
		boolean b;
		try {
			switch (op) {
			case 0:
				b = (Boolean) engine.eval("value>value2", bindings);
				break;
			case 1:
				b = (Boolean) engine.eval("value>=value2", bindings);
				break;
			case 2:
				b = (Boolean) engine.eval("value<value2", bindings);
				break;
			case 3:
				b = (Boolean) engine.eval("value<=value2", bindings);
				break;
			case 4:
				b = (Boolean) engine.eval("value==value2", bindings);
				break;
			case 5:
				b = (Boolean) engine.eval("value!=value2", bindings);
				break;
			case 6:
				b = v1.contains(v2);
				break;
			case 7:
				b = !v1.contains(v2);
				break;
			case 8:
				b = true;
				break;
			case 9:
				b = false;
				break;
			case 10:
				b = false;
				break;
			case 11:
				b = v2.contains(v1);
				break;
			default:
				b = v1.contains(v2);
			}
		} catch (ScriptException e) {
			b = false;
		}
		return b;
	}
	
	private List<Map<String, Object>> getData(String sys_type_code){
		List<Map<String, Object>> validates = null ;
		try {
			Map<String, Object> queryForMap = DaoHelper.getDumai_newBaseDao().queryForMap(" select * from manager_model_grade where sys_type_code = ? order by opttime desc limit 1 ", sys_type_code);
			validates = JsonToMap.gson2List((String) queryForMap.get("data_config"));
//			validates = JsonToMap.gson2List(IOUtils.toString(this.getClass().getResourceAsStream("data.json"), "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validates;
	}
	
	
	/* run model_grade end */
	
	/* block AuditOne4 start */
	@Override
	public String auditOne4(Map<String,Object>orderMap) {
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		String thetype = (String) orderMap.get("thetype");
		Map<String, Object> sys_company_typeMap = bizFunctionSettingsService.getFunctionSettingsBySubentityId((String) orderMap.get("sub_entity_id"), (String) orderMap.get("thetype"));
        String rule_model = (String) sys_company_typeMap.get("rule_model");
		List<Map<String, Object>> ruleList = mysqlSpringJdbcBaseDao.queryForList(getRuleLogicsSql4(sub_entity_id, thetype,rule_model));
		Set<String> interfacesCodes = getInterfacesCodes4(ruleList,"interfaces");
		if (interfacesCodes.isEmpty()){
			return null;
		}
		String [] interfacesCodesArray = interfacesCodes.toArray(new String[]{});
		if(!CostUtil.isBalanceEnough(sub_entity_id, interfacesCodesArray)){
			return null;
		}
		if(1!=CostUtil.charge(orderMap,"自动审核--"+(String)orderMap.get("name"), interfacesCodesArray)){
			return null;
		}
		return executeAuditOne4(orderMap, ruleList, interfacesCodes);
	}
	
	private String executeAuditOne4(Map<String, Object> orderMap, List<Map<String, Object>> ruleList, Set<String> interfacesCodeSet) {
		String orderId = (String)orderMap.get("code");
		String card_num = (String)orderMap.get("card_num");
		try {
			// 准备数据源结果数据
			Map<String, Object> interfacesResultMap = new HashMap<String, Object>();
			for (String interfaceCode : interfacesCodeSet) {
				Map<String, Object>inPara = getInPara4(interfaceCode, orderMap);
				String is_result = (String) dm_3rd_interfaceService.testDS(interfaceCode,inPara);
				Map<String, Object> result;
				try {
					result = JsonToMap.jsonToMap(is_result);
				} catch (Exception e) {
					result = null ;
					e.printStackTrace();
				}
				interfacesResultMap.put(interfaceCode, result);
			}
			//异步存贮暂时结束
			//AsynchronousThreadHelper.runNewThread(this.getClass().getName()+"#saveToFk_order_bigMap4", new Class[] {Map.class, Map.class, Set.class },orderMap,interfacesResultMap,interfacesCodeSet);
			// ^^^^准备数据源结果数据
			// 准备逻辑数据
			StringBuilder ruleExpression = new StringBuilder();
			Set<Object[]> guize_detailSet= new HashSet<Object[]>();
			for (Map<String,Object>rule:ruleList) {// 循环规则
				String ruleResult = null;
				StringBuilder sb = new StringBuilder();
				StringBuilder sb2 = new StringBuilder("[op:0=,1>=,2<,3<=,4<>,7contains,8>]");
				Map<String, Object> logs = null;
				try {
					logs = JsonToMap.jsonToMap((String) rule.get("logs"));
				} catch (Exception e1) {
					e1.printStackTrace();
					ruleResult = "error";
				}
				if(!"error".equals(ruleResult)){
					for (int j = 0; j <= Integer.valueOf(logs.get("order").toString()); j++) {// 循环逻辑开始
						//取逻辑表达式中参数变量对应的数据源结果值
						Boolean execute = false;
						String vresult = null;
						Map<String, Object> dm_3rd_interface_paraMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface_para WHERE code=?", (String)logs.get("pa" + j));
						Object dm_3rd_interface_result = interfacesResultMap.get((String)logs.get("ds" + j));
						try {
							//获取参数值
							vresult = String.valueOf(auditOne4_transResult(dm_3rd_interface_paraMap,dm_3rd_interface_result));
						} catch (Exception e){
							vresult = "null"; 
						}
						log.info("-------------------------");
						log.info((String)dm_3rd_interface_paraMap.get("description"));
						log.info((String)dm_3rd_interface_paraMap.get("name") + ":" + vresult);
						log.info("-------------------------"); 
						//存在结果则进行比较，不存在结果则无结果
						if(!"null".equals(vresult)){
							String va = (String)logs.get("va" + j);
							if(va.contains("#")){
								va = getVaribleValue(va, orderMap);
							}
							execute = auditOne_5_execute(vresult,va,(String)logs.get("op" + j));// true表示命中
						}
						// 生成逻辑的表达式（供参考）
						sb2.append("[description:"+(String)dm_3rd_interface_paraMap.get("description")+","+(String)dm_3rd_interface_paraMap.get("name")+":"+vresult+",op:");
						sb2.append(logs.get("op" + j).toString() + ",value:" + logs.get("va" + j).toString() + ",result:" + execute + "]");
						// 生成逻辑的表达式开始
						// 括号( 无括号 左括号 右括号）
			            String si = (String) logs.get("si" + j);
			            // 逻辑运算符（且 或 ）
			            String sign = (String) logs.get("sign" + j);
			            if ("2".equals(si)) {// 左括号，运算符在前左括号在后
			                if (StringUtils.isNotEmpty(sign)) {
			                    sb.append(sign);
			                }
			                sb.append(si).append(execute);
			            } else if ("3".equals(si)) {// 右括号，运算符在后右括号在前
			                if (StringUtils.isNotEmpty(sign)) {
			                    sb.append(sign).append(execute);
			                }
			                sb.append(si);
			            } else {// 无括号
			                if (StringUtils.isNotEmpty(sign)) {
			                    sb.append(sign).append(execute);
			                } else {
			                    sb.append(execute);
			                }
			            }
						// 生成逻辑的表达式结束
			            String str = sb.toString();
			            str = str.replaceAll("or", "||");
			            str = str.replaceAll("and", "&&");
			            str = str.replaceAll("2", "(");
			            str = str.replaceAll("3", ")");
			            sb = new StringBuilder(str);
					} // 循环逻辑结束
					try {
						ruleResult = engine.eval(sb.toString()).toString();
					} catch (ScriptException e) {
						ruleResult = "error";
					}
				}else{
					sb.append("#");
					sb2.append("逻辑设置出错！");
				}
				guize_detailSet.add(new Object []{UUID.randomUUID().toString(),logs.get("guizeCode"),orderId,card_num, sb.toString(),sb2.toString(),ruleResult});
				if (AuditResultConstant.TRUE.equals(ruleResult) || AuditResultConstant.FALSE.equals(ruleResult)) {
					ruleExpression.append(ruleResult);
					ruleExpression.append("_");
				}
			}
			add_fk_guize_detail4(guize_detailSet);
			String s = ruleExpression.deleteCharAt(ruleExpression.length() - 1).toString();
			s = s.replace("_", "||");
			String eval = "";
			try {
				eval = engine.eval(s).toString();
			} catch (ScriptException e) {
				eval = "error";
			}
			return eval;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	private String getVaribleValue(String TableAndField,Map<String,Object>order){
		String value = "";
		try{
			String table = TableAndField.split("#")[0];
			String filed = TableAndField.split("#")[1];
			if("fk_orderinfo".equals(table)){
				value = (String) order.get(filed);
			}
		}catch (Exception e) {
			value = "";
			e.printStackTrace();
		}
		return value;
	}
	
	//TODO
	@SuppressWarnings("unused")
	private static void saveToFk_order_bigMap4(Map<String,Object> orderMap,Map<String,Object> interfacesResultMap,Set<?> interfacesCodeSet){
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		String thetype = (String) orderMap.get("thetype");
		String interfaceCodes = null;
		String [] report_interfaces = null;
		Map<String,Object> sys_company_typeMap = DaoHelper.getDumai_newBaseDao().queryForMap("SELECT * FROM sys_company_type WHERE rule_model = '1' and is_able = '1' and sub_entity_id = ? and type_code = ?", new Object[]{sub_entity_id,thetype});
		String report_interfaces_str = (String) sys_company_typeMap.get("report_interfaces");
		if(StringUtils.isNotEmpty(report_interfaces_str)){
			report_interfaces = report_interfaces_str.split(",");
			interfaceCodes = report_interfaces_str+","+StringUtils.join(interfacesCodeSet, ",");
		}else{
			interfaceCodes = StringUtils.join(interfacesCodeSet, ",");
		}
		Map<String, Object> interfacesResultMap_report = new HashMap<String, Object>();
		if(report_interfaces != null){
			for (String interfaceCode : report_interfaces) {
				Map<String, Object> inPara = getInPara4(interfaceCode, orderMap);
				Dm_3rd_interfaceService dm_3rd_interfaceService2 = (Dm_3rd_interfaceService) SpringApplicationContextHolder.getBean("dm_3rd_interfaceService");
				String is_result = (String) dm_3rd_interfaceService2.testDS(interfaceCode,inPara);
				Map<String, Object> result;
				try {
					result = JsonToMap.jsonToMap(is_result);
				} catch (Exception e) {
					result = null ;
					e.printStackTrace();
				}
				interfacesResultMap_report.put(interfaceCode, result);
			}
		}
		DaoHelper.getDumai_newBaseDao().insert("insert into fk_order_bigmap (code,fk_orderinfo_code,interfaceCodes,rule_result,dm_3rd_result) values (?,?,?,?,?) ", new Object[]{UUID.randomUUID().toString(),(String) orderMap.get("code"),interfaceCodes,new GsonBuilder().serializeNulls().create().toJson(interfacesResultMap),new GsonBuilder().serializeNulls().create().toJson(interfacesResultMap_report)});
		System.out.println("异步存储bigMap结束！");
	}
	
	private void add_fk_guize_detail4(Set<Object[]>args) {
		String sql = " insert into fk_guize_detail (code,guize_code,order_code,card_num,expression,expression2,result) values (?,?,?,?,?,?,?)  ";
		mysqlSpringJdbcBaseDao.batchInsert(sql, args.toArray(new Object[][]{}));
	}
	
	private String getRuleLogicsSql4(String sub_entity_id, String type_code ,String rule_model){
		StringBuilder sb = new StringBuilder();    
		sb.append(" select * from (  ");                                                                                                                                                                                                                                                                                                        
		sb.append(" SELECT   ");                                                                                                                                                                                                                                                                                                      
		sb.append(" fk_guize.*,   ");                                                                                                                                                                                                                                                                                                   
		sb.append(" fk_guiz_logs.interfaces,   ");                                                                                                                                                                                                                                                                                      
		sb.append(" fk_guiz_logs.logs,   ");                                                                                                                                                                                                                                                                              
		sb.append(" sys_company_type.`code` as sys_company_type_code    ");                                                                                                                                                                                                                                                               
		sb.append(" FROM     ");                                                                                                                                                                                                                                                                                                          
		sb.append(" sys_rule_group  ");                                                                                                                                                                                                                                                                                                   
		sb.append(" INNER JOIN sys_rule_group__fk_guize ON sys_rule_group.`code` = sys_rule_group__fk_guize.sys_rule_group_code AND sys_rule_group.sys_type_code = '"+type_code+"' AND sys_rule_group.is_able = '1'    ");                                                                                         
		sb.append(" INNER JOIN fk_guize ON sys_rule_group__fk_guize.fk_guize_code = fk_guize.`code` AND fk_guize.is_able = '1'     ");                                                                                                                                                                                                    
		sb.append(" INNER JOIN sys_company_type ON sys_rule_group.sys_type_code = sys_company_type.type_code AND sys_company_type.is_able = '1' AND sys_company_type.sub_entity_id = '"+sub_entity_id+"' AND sys_company_type.type_code = '"+type_code+"'   ");                                                                           
		sb.append(" inner join fk_guiz_logs on fk_guize.code = fk_guiz_logs.fk_guize_code ");                                 
		sb.append(" ) AS guize1    ");                                                                                                                                                                                                                                                                      
		sb.append(" LEFT JOIN (     ");                                                                                                                                                                                                                                                                                                   
		sb.append(" SELECT ");                                                                                                                                                                                                                                                                                                        
		sb.append(" sys_company_type__fk_guize.fk_guize_code,    ");                                                                                                                                                                                                                                                                    
		sb.append(" sys_company_type.sub_entity_id,   ");                                                                                                                                                                                                                                                                               
		sb.append(" sys_company_type.type_code   ");                                                                                                                                                                                                                                                                                   
		sb.append(" FROM      ");                                                                                                                                                                                                                                                                                                        
		sb.append(" sys_company_type      ");                                                                                                                                                                                                                                                                                           
		sb.append(" INNER JOIN sys_company_type__fk_guize ON sys_company_type.`code` = sys_company_type__fk_guize.sys_company_type_code    ");                                                                                                                                                                    
		sb.append(" AND sys_company_type__fk_guize.rule_model = '"+rule_model+"'   ");                                                                                                                                                                                                                                 
		sb.append(" AND sys_company_type.type_code = '"+type_code+"'   ");                                                                                                                                                                                                                                        
		sb.append(" AND sys_company_type.sub_entity_id = '"+sub_entity_id+"'     ");                                                                                                                                                                                                               
		sb.append(" ) AS guize2 ON guize2.fk_guize_code = guize1. CODE   where fk_guize_code is not null   ");   
		return sb.toString();
	}
	
	private Set<String> getInterfacesCodes4(List<Map<String, Object>> ruleList,String columeName) {
		Set<String> interfacesCode = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(ruleList)){
			for(Map<String,Object>rule:ruleList){
				String[] split = ((String) rule.get(columeName)).split(",");
				for(String is:split){
					interfacesCode.add(is);
				}
			}
		}
		return interfacesCode;
	}
	
	/*private Float getAuditOneCost4(Map<String,Object>orderMap,Set<String> interfacesCodes){
		//准备数据
		float ret = 0f ;
		for (String dm_3rd_interface_code : interfacesCodes) {
			float ret2 = 0f;
			String in_para = null;
			try {
				in_para = new Gson().toJson(getInPara4(dm_3rd_interface_code,orderMap));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, Object> map = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface_detail where dm_3rd_interface_code = ? and in_para = ? order by opttime desc limit 1 " ,new Object[]{dm_3rd_interface_code,in_para});
			if(org.springframework.util.CollectionUtils.isEmpty(map)) {
				Map<String, Object> interfaceMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface WHERE `code`=?", dm_3rd_interface_code);
				ret2 = Float.parseFloat(String.valueOf(interfaceMap.get("cost_out")));
			} else {
				ret2 = 0f;
			}
			ret+=ret2;
		}
		return ret;
	}*/
	
	/**
	 * @description 转换订单参数到第三方数据源参数的映射方法，订单参数名->第三方数据源参数（输入参数）名
	 * @param dm_3rd_interface_code
	 * @param orderMap
	 * @return
	 */
	private static Map<String, Object> getInPara4(String dm_3rd_interface_code, Map<String, Object> orderMap) {
		Map<String, Object> returnMap = MapObjUtil.getMapSortedByKey();
		List<Map<String, Object>> nameTransList = DaoHelper.getDumai_sourceBaseDao().queryForList("SELECT * FROM dm_3rd_interface_para WHERE 1=1 and `type`= '0' AND dm_3rd_interface_code= ? " ,new Object[]{dm_3rd_interface_code});
		if(CollectionUtils.isEmpty(nameTransList)){
			return null;
		}
		for (Map<String, Object> nameMap : nameTransList) {
			String or_name = (String) nameMap.get("fk_orderinfo_name");
			String is_name = (String) nameMap.get("name");
			// 根据参数映射转换参数
			if (orderMap.containsKey(or_name)) {
				returnMap.put(is_name, orderMap.get(or_name));
			}
		}
		return returnMap;
		/*Map<String, Object> returnMap = MapObjUtil.getMapSortedByKey();
		// 查询参数映射
		List<Map<String, Object>> nameTransList = dumai_sourceBaseDao.queryForList(getInPara4_sql(dm_3rd_interface_code));
		if(CollectionUtils.isEmpty(nameTransList)){
			return null;
		}
		for (Map<String, Object> nameMap : nameTransList) {
			String ds_name = (String) nameMap.get("ds_name");
			String is_name = (String) nameMap.get("is_name");
			// 根据参数映射转换参数
			if (orderMap.containsKey(ds_name)) {
				returnMap.put(is_name, orderMap.get(ds_name));
			}
		}*/
	}

	/*private String getInPara4_sql(String dm_3rd_interface_code) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");                                                                                                                                                                                                                                                                                       
		sql.append(" dm_source_para.fk_orderinfo_name as ds_name, ");                                                                                                                                                                                                                                                 
		sql.append(" dm_3rd_interface_para.`name` as is_name, ");                                                                                                                                                                                                                                                      
		sql.append(" dm_3rd_interface_para.`value`,  ");                                                                                                                                                                                                                                                               
		sql.append(" dm_3rd_interface_para.para_group,   ");                                                                                                                                                                                                                                                           
		sql.append(" dm_3rd_interface_para.type,   ");                                                                                                                                                                                                                                                                 
		sql.append(" dm_3rd_interface_para.is_able   ");                                                                                                                                                                                                                                                               
		sql.append(" FROM    ");                                                                                                                                                                                                                                                                                       
		sql.append(" dm_3rd_interface_para  ");                                                                                                                                                                                                                                                                        
		sql.append(" INNER JOIN dm_source_para__dm_3rd_interface_para ON dm_3rd_interface_para.`code` = dm_source_para__dm_3rd_interface_para.dm_3rd_interface_para_code   ");                                                                                                                                         
		sql.append(" INNER JOIN dm_source_para ON dm_source_para__dm_3rd_interface_para.dm_source_para_code = dm_source_para.`code`    ");                                                                                                                                                                             
		sql.append(" WHERE  ");                                                                                                                                                                                                                                                                                        
		sql.append(" dm_3rd_interface_para.type = '0' AND   ");                                                                                                                                                                                                                                                        
		sql.append(" dm_3rd_interface_para.is_able = '1' and   ");                                                                                                                                                                                                                                                     
		sql.append(" dm_3rd_interface_para.dm_3rd_interface_code = '"+dm_3rd_interface_code+"'   ");
		return sql.toString();
	}*/
	
	@SuppressWarnings("unchecked")
	private String auditOne4_transResult(Map<String,Object>dm_3rd_interface_paraMap,Object dm_3rd_interface_reuslt){
		String vresult = null;
		String para_group = (String) dm_3rd_interface_paraMap.get("para_group");
		if (StringUtils.isEmpty(para_group)) {
			vresult = String.valueOf(((Map<String, Object>)dm_3rd_interface_reuslt).get((String)dm_3rd_interface_paraMap.get("name")));
		} else {
			Object obj = dm_3rd_interface_reuslt;
			String[] split = para_group.split("_");
			for (String string : split) { 
				obj = auditOne_4_getKey(string, obj);
			}
			vresult = ((Map<String, Object>) obj).get(dm_3rd_interface_paraMap.get("name")).toString();
		}
		return vresult;
	}
	/* block AuditOne4 end */
	
	/* block AuditOne5 start */
	@Override
	public String auditOne5(Map<String,Object>orderMap) {
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		String thetype = (String) orderMap.get("thetype");
		List<Map<String, Object>> ruleList = mysqlSpringJdbcBaseDao.queryForList(getRuleLogicsSql5(sub_entity_id, thetype));
		Set<String> interfacesCodes = getInterfacesCodes5(ruleList,"interfaces");
		if (interfacesCodes.isEmpty()){
			return null;
		}
		/*String [] interfacesCodesArray = interfacesCodes.toArray(new String[]{});
		if(!CostUtil.isBalanceEnough(sub_entity_id, interfacesCodesArray)){
			return null;
		}
		if(1!=CostUtil.charge(orderMap,"自动审核--"+(String)orderMap.get("name"), interfacesCodesArray)){
			return null;
		}*/
		return executeAuditOne5(orderMap, ruleList, interfacesCodes);
	}
	
	private String executeAuditOne5(Map<String, Object> orderMap, List<Map<String, Object>> ruleList, Set<String> interfacesCodeSet) {
		String orderId = (String)orderMap.get("code");
		try {
			// 准备数据源结果数据
			Map<String, Object> interfacesResultMap = new HashMap<String, Object>();
			Map<String, Object> inPara;
			for (String interfaceCode : interfacesCodeSet) {
				inPara = getInPara5(interfaceCode, orderMap);
				String is_result = (String) dm_3rd_interfaceService.testDS(interfaceCode,inPara);
				Map<String, Object> result;
				try {
					result = JsonToMap.jsonToMap(is_result);
				} catch (Exception e) {
					result = null ;
					e.printStackTrace();
				}
				interfacesResultMap.put(interfaceCode, result);
			}
			// ^^^^准备数据源结果数据
			// 准备逻辑数据
			StringBuilder ruleExpression = new StringBuilder();
			Set<Object[]> guize_detailSet= new HashSet<Object[]>();
			for (Map<String,Object>rule:ruleList) {// 循环规则
				String ruleResult = null;
				StringBuilder sb = new StringBuilder();
				StringBuilder sb2 = new StringBuilder("[op:0=,1>=,2<,3<=,4<>]");
				Map<String, Object> logs = null;
				try {
					logs = JsonToMap.jsonToMap((String) rule.get("logs"));
				} catch (Exception e1) {
					e1.printStackTrace();
					ruleResult = "error";  
				}
				if(!"error".equals(ruleResult)){
					for (int j = 0; j <= Integer.valueOf(logs.get("order").toString()); j++) {// 循环逻辑开始
						//取逻辑表达式中参数变量对应的数据源结果值
						Boolean execute = null;
						String vresult = null;
						Map<String, Object> dm_3rd_interface_paraMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface_para WHERE code=?", (String)logs.get("pa" + j));
						Object dm_3rd_interface_result = interfacesResultMap.get((String)logs.get("ds" + j));
						try {
							//获取参数值
							vresult = String.valueOf(auditOne5_transResult(dm_3rd_interface_paraMap,dm_3rd_interface_result));
						} catch (Exception e){
							vresult = "null"; 
						}
						log.info("-------------------------");
						log.info((String)dm_3rd_interface_paraMap.get("description"));
						log.info((String)dm_3rd_interface_paraMap.get("name") + ":" + vresult);
						log.info("-------------------------"); 
						//存在结果则进行比较，不存在结果则无结果
						if(!"null".equals(vresult)){
							execute = auditOne_5_execute(vresult,(String)logs.get("va" + j),(String)logs.get("op" + j));// true表示命中
						}
						// 生成逻辑的表达式（供参考）
						sb2.append("[description:"+(String)dm_3rd_interface_paraMap.get("description")+","+(String)dm_3rd_interface_paraMap.get("name")+":"+vresult+",op:");
						sb2.append(logs.get("op" + j).toString() + ",value:" + logs.get("va" + j).toString() + ",result:" + execute + "]");
						// 生成逻辑的表达式开始
						String si = (String)logs.get("si" + j);
						if ("2".equals(si)) {
							sb.append("(");
						} else if ("3".equals(si)) {
							sb.append(")");
						}
						if(j!=0&&logs.containsKey("sign" + j)){
							sb.append(getExpression5((String)logs.get("sign" + j)));
						}
						String logicResult = String.valueOf(execute);
						if (AuditResultConstant.TRUE.equals(logicResult)) {
							sb.append(true);
						} else if (AuditResultConstant.FALSE.equals(logicResult)) {
							sb.append(false);
						} else {   
							sb.append("#");
						} // 生成逻辑的表达式结束
					} // 循环逻辑结束
					try {
						ruleResult = engine.eval(sb.toString()).toString();
					} catch (ScriptException e) {
						ruleResult = "error";
					}
				}else{
					sb.append("#");
					sb2.append("逻辑设置出错！");
				}
				guize_detailSet.add(new Object []{UUID.randomUUID().toString(),logs.get("guizeCode"),orderId, sb.toString(),sb2.toString(),ruleResult});
				if (AuditResultConstant.TRUE.equals(ruleResult) || AuditResultConstant.FALSE.equals(ruleResult)) {
					ruleExpression.append(ruleResult);
					ruleExpression.append("_");
				}
			}
			add_fk_guize_detail5(guize_detailSet);
			String s = ruleExpression.deleteCharAt(ruleExpression.length() - 1).toString();
			s = s.replace("_", "||");
			String eval = "";
			try {
				eval = engine.eval(s).toString();
			} catch (ScriptException e) {
				eval = "error";
			}
			return eval;
		} catch (Exception e) {
			return "error";
		}
	}
	
	private String getExpression5(String opt){
		switch(opt){
		case "or": return "||";
		case "and": return "&&";
		case "?": return "?";
		case ":": return ":";
		default:return "#";
		}
	}
	
	private void add_fk_guize_detail5(Set<Object[]>args) {
		String sql = " insert into fk_guize_detail (code,guize_code,order_code,expression,expression2,result) values (?,?,?,?,?,?)  ";
		mysqlSpringJdbcBaseDao.batchInsert(sql, args.toArray(new Object[][]{}));
	}
	
	private String getRuleLogicsSql5(String sub_entity_id, String type_code){
		StringBuilder sb = new StringBuilder();    
		sb.append(" select * from (  ");                                                                                                                                                                                                                                                                                                        
		sb.append(" SELECT   ");                                                                                                                                                                                                                                                                                                      
		sb.append(" fk_guize.*,   ");                                                                                                                                                                                                                                                                                                   
		sb.append(" fk_guiz_logs.interfaces,   ");                                                                                                                                                                                                                                                                                      
		sb.append(" fk_guiz_logs.logs,   ");                                                                                                                                                                                                                                                                              
		sb.append(" sys_company_type.`code` as sys_company_type_code    ");                                                                                                                                                                                                                                                               
		sb.append(" FROM     ");                                                                                                                                                                                                                                                                                                          
		sb.append(" sys_rule_group  ");                                                                                                                                                                                                                                                                                                   
		sb.append(" INNER JOIN sys_rule_group__fk_guize ON sys_rule_group.`code` = sys_rule_group__fk_guize.sys_rule_group_code AND sys_rule_group.sys_type_code = '"+type_code+"' AND sys_rule_group.is_able = '1'    ");                                                                                         
		sb.append(" INNER JOIN fk_guize ON sys_rule_group__fk_guize.fk_guize_code = fk_guize.`code` AND fk_guize.is_able = '1'     ");                                                                                                                                                                                                    
		sb.append(" INNER JOIN sys_company_type ON sys_rule_group.sys_type_code = sys_company_type.type_code AND sys_company_type.is_able = '1' AND sys_company_type.sub_entity_id = '"+sub_entity_id+"' AND sys_company_type.type_code = '"+type_code+"'   ");                                                                           
		sb.append(" inner join fk_guiz_logs on fk_guize.code = fk_guiz_logs.fk_guize_code ");                                 
		sb.append(" ) AS guize1    ");                                                                                                                                                                                                                                                                      
		sb.append(" LEFT JOIN (     ");                                                                                                                                                                                                                                                                                                   
		sb.append(" SELECT ");                                                                                                                                                                                                                                                                                                        
		sb.append(" sys_company_type__fk_guize.fk_guize_code,    ");                                                                                                                                                                                                                                                                    
		sb.append(" sys_company_type.sub_entity_id,   ");                                                                                                                                                                                                                                                                               
		sb.append(" sys_company_type.type_code   ");                                                                                                                                                                                                                                                                                   
		sb.append(" FROM      ");                                                                                                                                                                                                                                                                                                        
		sb.append(" sys_company_type      ");                                                                                                                                                                                                                                                                                           
		sb.append(" INNER JOIN sys_company_type__fk_guize ON sys_company_type.`code` = sys_company_type__fk_guize.sys_company_type_code    ");                                                                                                                                                                    
		sb.append(" AND sys_company_type.type_code = '"+type_code+"'   ");                                                                                                                                                                                                                                        
		sb.append(" AND sys_company_type.sub_entity_id = '"+sub_entity_id+"'     ");                                                                                                                                                                                                               
		sb.append(" ) AS guize2 ON guize2.fk_guize_code = guize1. CODE   where fk_guize_code is not null   ");   
		return sb.toString();
	}
	
	private Set<String> getInterfacesCodes5(List<Map<String, Object>> ruleList,String columeName) {
		Set<String> interfacesCode = new HashSet<String>();
		for(Map<String,Object>rule:ruleList){
			String[] split = ((String) rule.get(columeName)).split(",");
			for(String is:split){
				interfacesCode.add(is);
			}
		}
		return interfacesCode;
	}
	
	/*private Float getAuditOneCost5(Map<String,Object>orderMap,Set<String> interfacesCodes){
		//准备数据
		float ret = 0f ;
		for (String dm_3rd_interface_code : interfacesCodes) {
			float ret2 = 0f;
			String in_para = null;
			try {
				in_para = new Gson().toJson(getInPara5(dm_3rd_interface_code,orderMap));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, Object> map = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface_detail where dm_3rd_interface_code = ? and in_para = ? order by opttime desc limit 1 " ,new Object[]{dm_3rd_interface_code,in_para});
			if(org.springframework.util.CollectionUtils.isEmpty(map)) {
				Map<String, Object> interfaceMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface WHERE `code`=?", dm_3rd_interface_code);
				ret2 = Float.parseFloat(String.valueOf(interfaceMap.get("cost_out")));
			} else {
				ret2 = 0f;
			}
			ret+=ret2;
		}
		return ret;
	}*/
	
	/**
	 * @description 转换订单参数到第三方数据源参数的映射方法，订单参数名->第三方数据源参数（输入参数）名
	 * @param dm_3rd_interface_code
	 * @param orderMap
	 * @return
	 */
	private Map<String, Object> getInPara5(String dm_3rd_interface_code, Map<String, Object> orderMap) {
		Map<String, Object> returnMap = MapObjUtil.getMapSortedByKey();
		List<Map<String, Object>> nameTransList = dumai_sourceBaseDao.queryForList("SELECT * FROM dm_3rd_interface_para WHERE 1=1 and `type`= '0' AND dm_3rd_interface_code= ? " ,new Object[]{dm_3rd_interface_code});
		if(CollectionUtils.isEmpty(nameTransList)){
			return null;
		}
		for (Map<String, Object> nameMap : nameTransList) {
			String or_name = (String) nameMap.get("fk_orderinfo_name");
			String is_name = (String) nameMap.get("name");
			// 根据参数映射转换参数
			if (orderMap.containsKey(or_name)) {
				returnMap.put(is_name, orderMap.get(or_name));
			}
		}
		return returnMap;
		/*Map<String, Object> returnMap = MapObjUtil.getMapSortedByKey();
		// 查询参数映射
		List<Map<String, Object>> nameTransList = dumai_sourceBaseDao.queryForList(getInPara5_sql(dm_3rd_interface_code));
		if(CollectionUtils.isEmpty(nameTransList)){
			return null;
		}
		for (Map<String, Object> nameMap : nameTransList) {
			String ds_name = (String) nameMap.get("ds_name");
			String is_name = (String) nameMap.get("is_name");
			// 根据参数映射转换参数
			if (orderMap.containsKey(ds_name)) {
				returnMap.put(is_name, orderMap.get(ds_name));
			}
		}*/
	}

	/*private String getInPara5_sql(String dm_3rd_interface_code) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");                                                                                                                                                                                                                                                                                       
		sql.append(" dm_source_para.fk_orderinfo_name as ds_name, ");                                                                                                                                                                                                                                                 
		sql.append(" dm_3rd_interface_para.`name` as is_name, ");                                                                                                                                                                                                                                                      
		sql.append(" dm_3rd_interface_para.`value`,  ");                                                                                                                                                                                                                                                               
		sql.append(" dm_3rd_interface_para.para_group,   ");                                                                                                                                                                                                                                                           
		sql.append(" dm_3rd_interface_para.type,   ");                                                                                                                                                                                                                                                                 
		sql.append(" dm_3rd_interface_para.is_able   ");                                                                                                                                                                                                                                                               
		sql.append(" FROM    ");                                                                                                                                                                                                                                                                                       
		sql.append(" dm_3rd_interface_para  ");                                                                                                                                                                                                                                                                        
		sql.append(" INNER JOIN dm_source_para__dm_3rd_interface_para ON dm_3rd_interface_para.`code` = dm_source_para__dm_3rd_interface_para.dm_3rd_interface_para_code   ");                                                                                                                                         
		sql.append(" INNER JOIN dm_source_para ON dm_source_para__dm_3rd_interface_para.dm_source_para_code = dm_source_para.`code`    ");                                                                                                                                                                             
		sql.append(" WHERE  ");                                                                                                                                                                                                                                                                                        
		sql.append(" dm_3rd_interface_para.type = '0' AND   ");                                                                                                                                                                                                                                                        
		sql.append(" dm_3rd_interface_para.is_able = '1' and   ");                                                                                                                                                                                                                                                     
		sql.append(" dm_3rd_interface_para.dm_3rd_interface_code = '"+dm_3rd_interface_code+"'   ");
		return sql.toString();
	}*/
	
	@SuppressWarnings("unchecked")
	private String auditOne5_transResult(Map<String,Object>dm_3rd_interface_paraMap,Object dm_3rd_interface_reuslt){
		String vresult = null;
		String para_group = (String) dm_3rd_interface_paraMap.get("para_group");
		if (StringUtils.isEmpty(para_group)) {
			vresult = String.valueOf(((Map<String, Object>)dm_3rd_interface_reuslt).get((String)dm_3rd_interface_paraMap.get("name")));
		} else {
			Object obj = dm_3rd_interface_reuslt;
			String[] split = para_group.split("_");
			for (String string : split) { 
				obj = auditOne_5_getKey(string, obj);
			}
			vresult = ((Map<String, Object>) obj).get(dm_3rd_interface_paraMap.get("name")).toString();
		}
		return vresult;
	}
	
	private Object auditOne_5_getKey(String str, Object o) {
		if (o instanceof List) {
			return ((List<?>) o).get(Integer.valueOf(str));
		} else {
			return ((Map<?, ?>) o).get(str);
		}
	}
	/* block AuditOne5 end */
	
	
	/* block Audit_DH begin */
	@Override
	public void Audit_DH(String task_code){
		//查询电核task结果并转换成list
		Map<String, Object> dh_task_detailMap = mysqlSpringJdbcBaseDao.queryForMap(" SELECT dh_task.*, dh_task_detail.other_exception, dh_task_detail.dh_content FROM dh_task INNER JOIN dh_task_detail ON dh_task.`code` = dh_task_detail.task_code WHERE dh_task.`code` = '"+task_code+"'");
		List<Map<String,Object>>resultList = null; 
		try{
			resultList = JsonToMap.gson2List((String)dh_task_detailMap.get("dh_content"));
		}catch(Exception e){
			e.printStackTrace();   
		}
		if(CollectionUtils.isNotEmpty(resultList)){
			//处理过程
			String auditDHResult = AuditResultConstant.FALSE ;
			String orderId = (String) dh_task_detailMap.get("order_code");
			Set<Object[]> guizeSet= new HashSet<Object[]>();
			Set<Object[]> itemSet= new HashSet<Object[]>();
			for (Map<String, Object> result : resultList) {
				String dh_source_type = (String) result.get("dh_source_type");
				String dh_result = (String) result.get("dh_content");
				String code = (String) result.get("manager_item_code");
				if("1".equals(dh_source_type)){
					guizeSet.add(new String[]{dh_result,code,orderId});
				}
				if("2".equals(dh_source_type)){
					itemSet.add(new String[]{dh_result,code,orderId});
				}
				if(AuditResultConstant.TRUE.equals(dh_result)){
					auditDHResult = AuditResultConstant.TRUE;
				}
			}
			String other_exception = (String) dh_task_detailMap.get("other_exception");
			if(StringUtils.isNotEmpty(StringUtils.trim(other_exception))){
				auditDHResult = AuditResultConstant.TRUE;
			}
			if(CollectionUtils.isNotEmpty(guizeSet)){
				update_guize_detail(guizeSet);
			}
			if(CollectionUtils.isNotEmpty(itemSet)){
				update_model_result(itemSet);
			}
			updateOrderAuditResult(orderId, auditDHResult,false);//不需要走电核通道
			sendAuditResult(orderId, auditDHResult,true);//需要走电核通道
		}
	} 
	
	private void update_guize_detail(Set<Object[]>args){
		String sql = " update fk_guize_detail set dh_result = ? where guize_code = ? and order_code = ? ";
		mysqlSpringJdbcBaseDao.batchInsert(sql, args.toArray(new Object[][]{}));
	}
	
	private void update_model_result(Set<Object[]>args){
		//String sql = " update manager_model_result set ... = ? where ... ";
		//mysqlSpringJdbcBaseDao.batchInsert(sql, args.toArray(new Object[]{}));
	}
	
	/* block Audit_DH end */
	
	/* block CreateDh_task start */
	@Override
	public void CreateDh_task(Map<String,Object>orderMap){
		if(orderMap.containsKey("repeat")){
			String sql = " update dh_task set customer_name = ? , mobile = ? , thetype = ? where order_code = ? ";
			mysqlSpringJdbcBaseDao.update(sql, new Object[]{orderMap.get("name"),orderMap.get("mobile"),orderMap.get("thetype"),orderMap.get("code")});
		}else{
			String sql = " insert into dh_task (code,order_code,customer_name,mobile,thetype,create_time) values (?,?,?,?,?,?)  ";
			mysqlSpringJdbcBaseDao.insert(sql, new Object[]{UUID.randomUUID().toString(),orderMap.get("code"),orderMap.get("name"),orderMap.get("mobile"),orderMap.get("thetype"),new Date()});
		}
	}
	/* block CreateDh_task end */
	
	
	/* block CreateDh_task_3rd_interface start */
	//线下数据源
	@Override
	public void CreateDh_task_3rd_interface(Map<String,Object>orderMap){
		String sql = " select * from sys_company_type where sub_entity_id = ? and type_code = ?   ";
		Map<String, Object> sys_company_typeMap = mysqlSpringJdbcBaseDao.queryForMap(sql, new Object[]{orderMap.get("sub_entity_id"),orderMap.get("thetype")});
		String report_interfaces = (String)sys_company_typeMap.get("report_interfaces");
		String sql2 = " select * from dm_3rd_interface where code in('"+StringUtils.replace(report_interfaces, ",", "','")+"') and is_able = '1' and is_online = '0' ";
		List<Map<String, Object>> dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(sql2);
		if(CollectionUtils.isNotEmpty(dm_3rd_interfaceList)){
			ArrayList<String> interfaceCodes = new ArrayList<String>();
			Set<Object[]> args= new HashSet<Object[]>();
			for (Map<String, Object> dm_3rd_interfaceMap : dm_3rd_interfaceList) {
				String interfaceCode = (String) dm_3rd_interfaceMap.get("code");
				interfaceCodes.add(interfaceCode);
				args.add(new Object[]{UUID.randomUUID().toString(),orderMap.get("code"),interfaceCode,new Gson().toJson(getCreateDh_task_3rd_interfaceInPara(interfaceCode, orderMap)),new Gson().toJson(getCreateDh_task_3rd_interfaceOutPara(interfaceCode)),dm_3rd_interfaceMap.get("description"),new Date()});
			}
			String sql3 =" insert into dh_task_3rd_interface (code,order_code,dm_3rd_interface_code,in_para,out_para,description,create_time) values (?,?,?,?,?,?,?)  ";
			mysqlSpringJdbcBaseDao.batchInsert(sql3, args.toArray(new Object[][]{}));
		}
	}
	
	private List<String[]> getCreateDh_task_3rd_interfaceInPara(String dm_3rd_interface_code, Map<String, Object> orderMap) {
		List<String[]>returnList = new ArrayList<String[]>();
		List<Map<String, Object>> dm_3rd_interface_paraList = dumai_sourceBaseDao.queryForList("SELECT * FROM dm_3rd_interface_para WHERE 1=1 and `type`= '0' AND dm_3rd_interface_code= '"+dm_3rd_interface_code+"' order by id asc " );
		if(CollectionUtils.isNotEmpty(dm_3rd_interface_paraList)){
			for (Map<String, Object> dm_3rd_interface_paraMap : dm_3rd_interface_paraList) {
				if (orderMap.containsKey((String) dm_3rd_interface_paraMap.get("fk_orderinfo_name"))) {
					returnList.add(new String[]{(String) dm_3rd_interface_paraMap.get("name"),(String) dm_3rd_interface_paraMap.get("name_zh"),(String) orderMap.get((String) dm_3rd_interface_paraMap.get("fk_orderinfo_name"))});
				}
			}
		}
		return returnList;
	}
	
	private List<String[]> getCreateDh_task_3rd_interfaceOutPara(String dm_3rd_interface_code) {
		List<String[]>returnList = new ArrayList<String[]>();
		List<Map<String, Object>> dm_3rd_interface_paraList = dumai_sourceBaseDao.queryForList("SELECT * FROM dm_3rd_interface_para WHERE 1=1 and `type`= '1' AND dm_3rd_interface_code= '"+dm_3rd_interface_code+"' order by id asc " );
		if(CollectionUtils.isNotEmpty(dm_3rd_interface_paraList)){
			for (Map<String, Object> dm_3rd_interface_paraMap : dm_3rd_interface_paraList) {
				returnList.add(new String[]{(String) dm_3rd_interface_paraMap.get("name"),(String) dm_3rd_interface_paraMap.get("name_zh")});
			}
		}
		return returnList;
	}
	/* block CreateDh_task_3rd_interface end */
	
	/* block checkCompanyDHFunction start   */
	@Override
	public boolean checkCompanyDHFunction(String sub_entity_id){
		Map<String,Object> companyMap = mysqlSpringJdbcBaseDao.queryForMap("select * from company_order where status = '1' and is_audit = '1' and sub_entity_id = '"+sub_entity_id+"'");
		if(org.springframework.util.CollectionUtils.isEmpty(companyMap)){
			return false;
		}
		return true;
	}
	/* block checkCompanyDHFunction end */
	
	/* block sendAuditResult begin*/
	/**
	 * 花花文档1.4
	 * 回传审核结果
	 */
	@Override
	public void sendAuditResult(String orderId,String auditResult ,boolean flag){
		String sql = "select * from fk_orderinfo where code = '"+orderId+"'";
		Map<String,Object> orderMap = mysqlSpringJdbcBaseDao.queryForMap(sql);
		if(!org.springframework.util.CollectionUtils.isEmpty(orderMap)){
			String url = (String) orderMap.get("noticeUrl");
			String orderid = (String) orderMap.get("orderid");
			Map<String,Object>inParams = new HashMap<String,Object>();
			inParams.put("orderId", orderid);
			if(AuditResultConstant.FALSE.equals(auditResult)){
				inParams.put("riskStatus", 1);
				inParams.put("message", AuditResultConstant.PASS);
				inParams.put("messageInfo", "");
				int score = auditOne3(orderMap);
				inParams.put("model_score", String.valueOf(score));
				inParams.put("model_grade", getModelGrade(orderMap,score));
			}else if(AuditResultConstant.TRUE.equals(auditResult)){ 
				inParams.put("riskStatus", 2);
				inParams.put("message", AuditResultConstant.REFUSE);
				inParams.put("messageInfo", getDetailMessage(orderId, flag));
			}else{
				inParams.put("riskStatus", 3);
				inParams.put("message", AuditResultConstant.WRONG);
				inParams.put("messageInfo","");
			}
			String result = null;
			try {
				if(StringUtils.isNotEmpty(url)){
					result = HttpClientUtil.exec(url,"1","1",inParams);
				}else{
					result = "url,不存在,未能发送回调结果！";
				}
			} catch (Exception e) {     
				e.printStackTrace();
				result = "发送或接受回传结果出错！";
			}
			//保存发送记录结束！
			mysqlSpringJdbcBaseDao.insert(" insert into send_message_log (code,fk_orderinfo_code,send_message,recieve_message) values (?,?,?,?) ", new Object[]{UUID.randomUUID().toString(),orderId,new GsonBuilder().serializeNulls().create().toJson(inParams),result});
			System.out.println("保存发送记录结束！");
		}
	}
	
	private String getDetailMessage(String orderId ,boolean flag){
		String detailMessage = "";
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" fk_guize.`name`, ");
		sql.append(" fk_guize.description, ");
		sql.append(" fk_guize_detail.result, ");
		sql.append(" fk_guize_detail.dh_result, ");
		sql.append(" fk_guize_detail.opttime ");
		sql.append(" FROM ");
		sql.append(" fk_guize_detail ");
		sql.append(" INNER JOIN fk_guize ON fk_guize_detail.guize_code = fk_guize.`code` ");
		sql.append(" where  ");
		sql.append( flag==true?" dh_result ":" result "); 
		sql.append(" = 'true' ");    
		sql.append(" and fk_guize_detail.order_code = '"+orderId+"' ");
		sql.append(" group BY  fk_guize_detail.guize_code ");
				
		List<Map<String, Object>> queryForList = mysqlSpringJdbcBaseDao.queryForList(sql.toString());		
		if(CollectionUtils.isNotEmpty(queryForList)){
			List<String> message = new ArrayList<String>();
			for(Map<String,Object> map :queryForList){
				String msg = (String)map.get("name")+"-不通过("+(String)map.get("description")+")";
				message.add(msg);
			}
			detailMessage = message.toString();
		}
		Map<String, Object> dh_task_detailMap = mysqlSpringJdbcBaseDao.queryForMap(" SELECT dh_task.*, dh_task_detail.other_exception, dh_task_detail.dh_content FROM dh_task INNER JOIN dh_task_detail ON dh_task.`code` = dh_task_detail.task_code WHERE dh_task.`order_code` = '"+orderId+"'");
		String other_exception = "";
		try {
			other_exception = (String) dh_task_detailMap.get("other_exception");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotEmpty(StringUtils.trim(other_exception))){
			detailMessage += "["+other_exception+"--规则之外的其他风险情况]";
		}
		return detailMessage;
	}
	
	/* block sendAuditResult end*/
	
	@Override
	public void updateOrderAuditResult(String orderId ,String result,boolean flag){
		if(AuditResultConstant.FALSE.equals(result)){
			orderInfoService.updateOrderStatus(orderId,"1",flag?"1":"2");
    	}else if(AuditResultConstant.TRUE.equals(result)){
    		orderInfoService.updateOrderStatus(orderId,"2","1");
    	}else if(AuditResultConstant.ERROR.equals(result)){
    		orderInfoService.updateOrderStatus(orderId,"3","1");
    	}
	}
	

	/* block jianquan begin */
	//TODO
	public Map<String,Object> auditOne_jianquan(Map<String, Object> orderMap) {
		Map<String,Object> returnMap =  new HashMap<String,Object>();
		orderMap.put("jianquan_key", UUID.randomUUID().toString());
		returnMap.put("identifier", "message_jianquan");
		String sub_entity_id = (String) orderMap.get("sub_entity_id");
		String thetype = (String) orderMap.get("thetype");
		List<Map<String, Object>> ruleList = mysqlSpringJdbcBaseDao.queryForList(getRuleLogicsSql_jianquan(sub_entity_id, thetype));
		Set<String> interfacesCodes = AuditServiceUtil.getInterfacesCodes(ruleList, "interfaces");
		if (interfacesCodes.isEmpty()) {
			returnMap.put("success", true);
			returnMap.put("invalidMsg", "没有查询到启用的鉴权规则逻辑,默认放行！");
			saveFailedValidateOrder(orderMap,returnMap);
			orderMap.remove("jianquan_key");
			return returnMap;
		}
		String [] interfacesCodesArray = interfacesCodes.toArray(new String[]{});
		if (!CostUtil.isBalanceEnough(sub_entity_id, interfacesCodesArray)) {
			returnMap.put("success", false);
			returnMap.put("invalidMsg", "费用不足!请联系客服");
			saveFailedValidateOrder(orderMap,returnMap);
			orderMap.remove("jianquan_key");
			return returnMap;
		}
		CostUtil.charge(orderMap,"鉴权审核--"+(String)orderMap.get("name"), interfacesCodesArray);
		String jianquan_result =  executeAuditOne_jianquan(orderMap,ruleList,interfacesCodes);
		if(AuditResultConstant.TRUE.equals(jianquan_result)){
			returnMap.put("success", false); 
			returnMap.put("invalidMsg", getDetailMessage_jianquan((String)orderMap.get("jianquan_key")));
		}else if(AuditResultConstant.FALSE.equals(jianquan_result)){
			returnMap.put("success", true);
			returnMap.put("invalidMsg", "鉴权通过");
		}else{
			returnMap.put("success", true);
			returnMap.put("invalidMsg", "鉴权出现异常，默认放行");
		}
		saveFailedValidateOrder(orderMap,returnMap);
		orderMap.remove("jianquan_key");
		return returnMap;
	}

	private String executeAuditOne_jianquan(Map<String, Object> orderMap,List<Map<String, Object>> ruleList,Set<String> interfacesCodes) {
		String orderId = (String) orderMap.get("jianquan_key");
		String card_num = (String) orderMap.get("card_num");
		try {
			// 准备数据源结果数据
			Map<String, Object> interfacesResultMap = AuditServiceUtil.getDm_3rd_interfacesResult_jianquan(dumai_sourceBaseDao, dm_3rd_interfaceService, orderMap, interfacesCodes);
			AuditServiceUtil.callTemporaryInterface(dumai_sourceBaseDao, dm_3rd_interfaceService, orderMap);// 百度金融和小爱有信调用
			StringBuilder ruleExpression = new StringBuilder();
			Set<Object[]> guize_detailSet = new HashSet<Object[]>();
			for (Map<String, Object> rule : ruleList) {// 循环规则开始
				String ruleResult = null;
				StringBuilder sb = new StringBuilder();
				StringBuilder sb2 = new StringBuilder("[op:0=,1>=,2<,3<=,4<>,7contains,8>]");
				Map<String, Object> logs = null;
				try {
					logs = JsonToMap.jsonToMap((String) rule.get("logs"));
				} catch (Exception e1) {
					e1.printStackTrace();
					ruleResult = "error";
				}
				if (!"error".equals(ruleResult)) {
					for (int j = 0; j <= Integer.valueOf(logs.get("order").toString()); j++) {// 循环逻辑开始
						// 取逻辑表达式中参数变量对应的数据源结果值
						Boolean execute = false;
						String vresult = null;
						Map<String, Object> dm_3rd_interface_paraMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_3rd_interface_para WHERE code=?", (String) logs.get("pa" + j));
						Object dm_3rd_interface_result = interfacesResultMap.get((String)logs.get("ds" + j));
						try {
							// 获取参数值
							vresult = String.valueOf(auditOne_jianquan_transResult(dm_3rd_interface_paraMap,dm_3rd_interface_result));
						} catch (Exception e) {
							vresult = "null";
						}
						log.info("-------------------------");
						log.info((String)dm_3rd_interface_paraMap.get("description"));
						log.info((String)dm_3rd_interface_paraMap.get("name") + ":" + vresult);
						log.info("-------------------------"); 
						//存在结果则进行比较，不存在结果则无结果
						if (!"null".equals(vresult)) {
							execute = AuditServiceUtil.auditOne_execute(vresult, (String) logs.get("va" + j),(String) logs.get("op" + j));// true表示命中
						}
						//生成逻辑的表达式开始,生成逻辑的表达式（供参考）
						sb2.append("[description:"+(String)dm_3rd_interface_paraMap.get("description")+","+(String)dm_3rd_interface_paraMap.get("name")+":"+vresult+",op:");
						sb2.append(logs.get("op" + j).toString() + ",value:" + logs.get("va" + j).toString() + ",result:" + execute + "]");
						// 括号( 无括号 左括号 右括号）
			            String si = (String) logs.get("si" + j);
			            // 逻辑运算符（且 或 ）
			            String sign = (String) logs.get("sign" + j);
			            if ("2".equals(si)) {// 左括号，运算符在前左括号在后
			                if (StringUtils.isNotEmpty(sign)) {
			                    sb.append(sign);
			                }
			                sb.append(si).append(execute);
			            } else if ("3".equals(si)) {// 右括号，运算符在后右括号在前
			                if (StringUtils.isNotEmpty(sign)) {
			                    sb.append(sign).append(execute);
			                }
			                sb.append(si);
			            } else {// 无括号
			                if (StringUtils.isNotEmpty(sign)) {
			                    sb.append(sign).append(execute);
			                } else {
			                    sb.append(execute);
			                }
			            }
						// 生成逻辑的表达式结束
						String str = sb.toString();
						str = str.replaceAll("or", "||");
						str = str.replaceAll("and", "&&");
						str = str.replaceAll("2", "(");
						str = str.replaceAll("3", ")");
						sb = new StringBuilder(str);
					} // 循环逻辑结束
					try {// 计算逻辑结果
						ruleResult = engine.eval(sb.toString()).toString();
					} catch (ScriptException e) {
						ruleResult = "error";
					}
				} else {
					sb.append("#");
					sb2.append("逻辑设置出错！");
				}
				guize_detailSet.add(new Object []{UUID.randomUUID().toString(),logs.get("guizeCode"),orderId,card_num, sb.toString(),sb2.toString(),ruleResult});
				if (AuditResultConstant.TRUE.equals(ruleResult) || AuditResultConstant.FALSE.equals(ruleResult)) {
					ruleExpression.append(ruleResult);
					ruleExpression.append("_");
				}
			} //循环规则结束
			add_fk_guize_detail_jianquan(guize_detailSet);
			String s = ruleExpression.deleteCharAt(ruleExpression.length() - 1).toString();
			s = s.replace("_", "||");
			String eval = "";
			try {// 计算规则结果
				eval = engine.eval(s).toString();
			} catch (ScriptException e) {
				eval = "error";
			}
			return eval;
		} catch (Exception e) {
			return "error";
		}
	}

	private String getRuleLogicsSql_jianquan(String sub_entity_id, String type_code) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" fk_guize.*, ");
		sb.append(" fk_guiz_logs.interfaces, ");
		sb.append(" fk_guiz_logs.`logs` ");
		sb.append(" FROM ");
		sb.append(" sys_company_type ");
		sb.append(" INNER JOIN sys_type ON sys_type.`code` = sys_company_type.type_code ");
		sb.append(" INNER JOIN sys_rule_group ON sys_company_type.type_code = sys_rule_group.sys_type_code ");
		sb.append(" INNER JOIN sys_rule_group__fk_guize ON sys_rule_group.`code` = sys_rule_group__fk_guize.sys_rule_group_code ");
		sb.append(" INNER JOIN fk_guize ON sys_rule_group__fk_guize.fk_guize_code = fk_guize.`code` ");
		sb.append(" INNER JOIN fk_guiz_logs ON fk_guize.`code` = fk_guiz_logs.fk_guize_code ");
		sb.append(" WHERE ");
		sb.append(" 1 = 1 ");
		sb.append(" AND sys_type.is_able = '1' ");
		sb.append(" AND sys_rule_group.is_able = '1' ");
		sb.append(" AND fk_guize.is_able = '1' ");
		sb.append(" AND sys_company_type.is_able = '1' ");
		sb.append(" AND sys_rule_group.biz_range = '0' ");
		sb.append(" AND sys_company_type.sub_entity_id = '"+sub_entity_id+"' ");
		sb.append(" AND sys_company_type.type_code = '"+type_code+"' ");
		return sb.toString();
	}

	private void add_fk_guize_detail_jianquan(Set<Object[]> args) {
		String sql = " insert into fk_guize_detail (code,guize_code,order_code,card_num,expression,expression2,result) values (?,?,?,?,?,?,?)  ";
		mysqlSpringJdbcBaseDao.batchInsert(sql, args.toArray(new Object[][] {}));
	}
	
	private void saveFailedValidateOrder(Map<String,Object>orderMap,Map<String,Object>validateResultMap){
		List<Map<String, Object>> nameList = mysqlSpringJdbcBaseDao.queryForList("select COLUMN_NAME as 'name' from information_schema.COLUMNS where table_name = 'failed_validate_order'  and table_schema = 'new_dumai' ");
		Map<String,Object> failed_validate_order = new HashMap<String,Object>();
		orderMap.remove("id");
		if(!orderMap.containsKey("jsonData")){
			String jsonData = new GsonBuilder().serializeNulls().create().toJson(orderMap);
			orderMap.put("jsonData", jsonData);
		}/*if(!orderMap.containsKey("orderid")){
			orderMap.put("orderid", "");
		}*/
		for (Map<String, Object> nameMap : nameList) {
			String name = (String) nameMap.get("name");
			if (orderMap.containsKey(name)) {
				Object value = String.valueOf(orderMap.get(name));
				failed_validate_order.put(name, value);
			}
		}
		failed_validate_order.put("validate_result",validateResultMap.get("success"));
		failed_validate_order.put("validate_message",validateResultMap.get("invalidMsg"));
		super.add(failed_validate_order, "failed_validate_order");
	}
	
	private String getDetailMessage_jianquan(String orderId){
		String detailMessage = "";
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" fk_guize.`name`, ");
		sql.append(" fk_guize.description, ");
		sql.append(" fk_guize_detail.result, ");
		sql.append(" fk_guize_detail.opttime ");
		sql.append(" FROM ");
		sql.append(" fk_guize_detail ");
		sql.append(" INNER JOIN fk_guize ON fk_guize_detail.guize_code = fk_guize.`code` ");
		sql.append(" where  ");
		sql.append(" result "); 
		sql.append(" = 'true' ");    
		sql.append(" and fk_guize_detail.order_code = '"+orderId+"' ");
		sql.append(" group BY  fk_guize_detail.guize_code ");
				
		List<Map<String, Object>> queryForList = mysqlSpringJdbcBaseDao.queryForList(sql.toString());		
		if(CollectionUtils.isNotEmpty(queryForList)){
			List<String> message = new ArrayList<String>();
			for(Map<String,Object> map :queryForList){
				String msg = (String)map.get("name")+"-不通过("+(String)map.get("description")+")";
				message.add(msg);
			}
			detailMessage = message.toString();
		}
		return detailMessage;
	}
	
	@SuppressWarnings("unchecked")
	private String auditOne_jianquan_transResult(Map<String,Object>dm_3rd_interface_paraMap,Object dm_3rd_interface_reuslt){
		String vresult = null;
		String para_group = (String) dm_3rd_interface_paraMap.get("para_group");
		if (StringUtils.isEmpty(para_group)) {
			vresult = String.valueOf(((Map<String, Object>)dm_3rd_interface_reuslt).get((String)dm_3rd_interface_paraMap.get("name")));
		} else {
			Object obj = dm_3rd_interface_reuslt;
			String[] split = para_group.split("_");
			for (String string : split) { 
				obj = auditOne_4_getKey(string, obj);
			}
			vresult = ((Map<String, Object>) obj).get(dm_3rd_interface_paraMap.get("name")).toString();
		}
		return vresult;
	}
	/* block jianquan end */
}
