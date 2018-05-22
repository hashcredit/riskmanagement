package com.newdumai.dumai_data.dm.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm.Dm_sourceService;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.dumai_data.dm_3rd_interface.util.CommonUtil;
import com.newdumai.global.service.impl.Dumai_sourceBaseServiceImpl;
import com.newdumai.util.JsonToMap;

@Service("dm_sourceService")
public class Dm_sourceServiceImpl extends Dumai_sourceBaseServiceImpl implements Dm_sourceService {

	@Autowired
	Dm_3rd_interfaceService dm_3rd_interfaceService;

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * 对外接口 获取所有可用读脉源
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDmSources() {
		return dumai_sourceBaseDao.queryForList("select * from dm_source where is_able='1'");
	}

	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition, gen_list_1(condition.get("condition").toString()), gen_list_2(condition.get("condition").toString(), getLimitUseAtSelectPage(map)));

	}

	@Override
	public int add_Interface_sourceService(Map<String, Object> para) {
		return add(para, "dm_source");
	}

	@Override
	public String para_toUpdate(String interface_source_code) {
		return super.dumai_sourceBaseDao.executeSelectSql(gen_para_toUpdate(interface_source_code));
	}

	@Override
	public int upadte_Interface_sourceService(Map<String, Object> para) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("code", para.get("code"));
		para.remove("code");
		return Update(para, "dm_source", where);
	}

	@Override
	public String toTestDM(String code) {
		return super.dumai_sourceBaseDao.executeSelectSql("SELECT * FROM dm_source_para WHERE `type`='0' AND `dm_source_code`='" + code + "'");
	}

	// private String formatOutPara(String is, String result) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("result", result);
	// try {
	// return InvokeMethod.invoke(is, map);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	// @SuppressWarnings("unchecked")
	// private Map<String, Object> getIn_para(String interface_source_code,
	// Map<String, Object> map) {
	// String split = "_";
	// List<Map<String, Object>> params =
	// dumai_sourceBaseDao.queryForList("SELECT * FROM dm_3rd_interface_para WHERE Interface_source_code = '"
	// + interface_source_code + "' and type=0");
	// Map<String, Object> resultMap = new HashMap<String, Object>();
	// int size = params.size();
	// for (int i = 0; i < size; i++) {
	// Map<String, Object> param = params.get(i);
	// String paraGroup = (String) param.get("para_group");
	// String name = (String) param.get("name");
	// if (StringUtils.isEmpty((String) map.get(name)))
	// break;
	// paraGroup = StringUtils.isEmpty(paraGroup) ? name : (paraGroup + "_" +
	// name);
	// String paramPaths[] = paraGroup.split(split);
	// Map<String, Object> current = resultMap;
	// for (int j = 0; j < paramPaths.length; j++) {
	// String key = paramPaths[j];
	// if (j == paramPaths.length - 1) {
	// String value = (String) param.get("value");
	// if (!StringUtils.isEmpty(value)) {
	// current.put(key, value);
	// } else {
	// current.put(key, map.get(key));
	// }
	// } else {
	//
	// Object o = current.get(key);
	// if (o != null) {
	// if (o instanceof Map) {
	// current = (Map<String, Object>) o;
	// } else {
	// HashMap<String, Object> m = new HashMap<String, Object>();
	// current.put(key, m);
	// current = m;
	// }
	// } else {
	// HashMap<String, Object> m = new HashMap<String, Object>();
	// current.put(key, m);
	// current = m;
	// }
	// }
	// }
	// }
	// return resultMap;
	// }

	/**
	 * 获取单个数据源的费用
	 * 
	 * @param code
	 *            数据源code
	 */
	protected Integer getDsCost(String code) {
		String sql = "select cost_tuomin from dm_source where code=?";
		try {
			return dumai_sourceBaseDao.executeSelectSqlInt(sql, code);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM dm_source WHERE 1=1 " + condition;
	}

	private String gen_list_2(String condition, String limit) {

		return "SELECT * FROM dm_source WHERE 1=1 " + condition + limit;
	}

	public Map<String, Object> getCondition_list(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String companyName = (String) map.get("companyName");
		if (!StringUtils.isEmpty(companyName)) {
			sb.append(" AND company_name LIKE ? ");
			list.add("%" + companyName + "%");
		}
		String name = (String) map.get("name");
		if (!StringUtils.isEmpty(name)) {
			sb.append(" AND name LIKE ? ");
			list.add("%" + name + "%");
		}
		String is_able = (String) map.get("is_able");
		if (StringUtils.isNotEmpty(is_able)) {
			sb.append(" AND is_able = ? ");
			list.add(is_able);
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	private String gen_para_toUpdate(String dm_source_code) {
		return "SELECT * FROM dm_source WHERE code='" + dm_source_code + "'";
	}

	// private String gen_testDS_1(String dm_3rd_interface_code, String in_para)
	// {
	// return
	// "SELECT * FROM dm_3rd_interface_detail WHERE `dm_3rd_interface_code`='" +
	// dm_3rd_interface_code + "' and `in_para`='" + in_para + "' ";
	// }

	@Override
	public List<Map<String, Object>> getSourceList(String code) {
		String sql = "select i.code,i.version,i.name,i.cost,i.is_able,i.description,di.code dataInterCode from dm_3rd_interface i inner join dm_source__dm_3rd_interface di on i.code=di.dm_3rd_interface_code and di.dm_source_code=?";
		return super.dumai_sourceBaseDao.queryForList(sql, code);
	}

	@Override
	public List<Map<String, Object>> interfaceSourceList(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		String sqlAll = "SELECT * FROM dm_3rd_interface WHERE 1=1 and is_able='1' " + condition.get("condition").toString();
		List<Map<String, Object>> dm_3rd_interfaceList = super.dumai_sourceBaseDao.queryForList(sqlAll, (Object[]) condition.get("args"));
		String sql = "SELECT * FROM dm_source__dm_3rd_interface WHERE dm_source_code=?";
		List<Map<String, Object>> dm_source__dm_3rd_interfaceList = super.dumai_sourceBaseDao.queryForList(sql, map.get("code"));
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		boolean flag;
		for (Map<String, Object> dm_3rd_interfaceMap : dm_3rd_interfaceList) {
			flag = true;
			for (Map<String, Object> dm_source__dm_3rd_interfaceMap : dm_source__dm_3rd_interfaceList) {
				if (dm_source__dm_3rd_interfaceMap.get("dm_3rd_interface_code").equals(dm_3rd_interfaceMap.get("code"))) {
					flag = false;
				}
			}
			if (flag) {
				result.add(dm_3rd_interfaceMap);
			}
		}
		return result;
	}

	@Override
	public boolean addSourceInterfaces(String dataSourceCode, String[] sourceCodes) {
		Object[][] args = new Object[sourceCodes.length][];
		for (int i = 0; i < args.length; i++) {
			Object[] arg = new Object[3];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = dataSourceCode;
			arg[2] = sourceCodes[i];
			args[i] = arg;
		}
		dumai_sourceBaseDao.batchInsert("insert into dm_source__dm_3rd_interface(code,dm_source_code,dm_3rd_interface_code) values(?,?,?)", args);
		return true;
	}

	@Override
	public void delSource(String code) {
		dumai_sourceBaseDao.delete("delete from dm_source__dm_3rd_interface where code=?", code);
	}

	@Override
	public void updateSwitch(String code, String mode) {
		dumai_sourceBaseDao.update("update dm_source set switch=? where code=?", mode, code);
	}

	/**
	 * testDM begin
	 */

	@Override
	public String testDM(String fk_orderinfo_code, String sub_entity_id, String dm_source_code, Map<String, Object> params) {
		Map<String, Object> dm_sourceMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_source WHERE `code` = '" + dm_source_code + "'");
		String dm_inpara = new Gson().toJson(params);
		String result = null;
		String dm_3rd_interface_code = null;
		String is_db = "0";
		try {
			// 扣费
			// execCost(orderId,sub_entity_id, map,dm_sourceMap);
			// 查询当前数据库内是否有数据
			result = findDB((String) dm_sourceMap.get("code"), dm_inpara);
			if (StringUtils.isNotEmpty(result)) {
				is_db = "1";
				return result;
			}
			String switcher = dm_sourceMap.get("switch").toString();
			List<Map<String, Object>> dm_3rd_interfaceList = null;
			// 按照模式排序
			if ("1".equals(switcher)) {// 按成本升序
				dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(gen_testDs_1(dm_source_code, "cost"));
			} else if ("2".equals(switcher)) {// 按故障升序
				dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(gen_testDs_1(dm_source_code, "error"));
			} else {
				// TODO 手动排序，未实现
			}
			if (CollectionUtils.isEmpty(dm_3rd_interfaceList)) {
				return null;
			}
			for (Map<String, Object> dm_3rd_interfaceMap : dm_3rd_interfaceList) {// 循环执行接口数据源
				dm_3rd_interface_code = (String) dm_3rd_interfaceMap.get("code");
				try {
					// 转换参数（将读脉源参数（值）转换为接口数据源参数（值））
					Map<String, Object> dm_3rd_in_para = transInParams(dm_source_code, dm_3rd_interface_code, params);
					// 执行接口数据源
					// result =
					// "{\"resCode\":\"0000\",\"resMsg\":\"提交成功\",\"data\":{\"statusCode\":\"2005\",\"statusMsg\":\"一致\"}}";
					result = dm_3rd_interfaceService.testDS(dm_3rd_interface_code, dm_3rd_in_para);
					if (StringUtils.isEmpty(result)) {
						continue;
					}
					// 检查结果
					result = transResult(dm_source_code, dm_3rd_interface_code, dm_inpara, result);
					if (StringUtils.isNotEmpty(result)) {
						return result;
					} else {
						dumai_sourceBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
					}
				} catch (Exception e) {
					e.printStackTrace();
					dumai_sourceBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
					result = null;
				}
			}
			return result;
		} catch (Exception e) {
			return result;
		} finally {
			// 存对账log
			log.debug("**************************************************************");
			log.debug("dm_source_name:" + dm_sourceMap.get("name") + "--" + dm_sourceMap.get("description") + "\t");
			log.debug("result:" + result + "\t");
			log.debug("**************************************************************");
			save_to_dm_source_log(fk_orderinfo_code, sub_entity_id, dm_inpara, dm_sourceMap, result, dm_3rd_interface_code, is_db);
		}

	}

	/**
	 * 根据读脉源code和输入参数从数据库中查询读脉源请求结果
	 * 
	 * @param dm_source_code
	 *            读脉源code
	 * @param dm_inpara
	 *            输入参数JSON字符串
	 * @return
	 */
	private String findDB(String dm_source_code, String dm_inpara) {
		String sql = "select result from dm_source_detail where dm_source_code=? and in_para=? order by optime desc limit 1";
		String result = null;
		try {
			Map<String, Object> dm_source_detailMap = dumai_sourceBaseDao.queryForMap(sql, dm_source_code, dm_inpara);
			if (dm_source_detailMap != null) {
				result = (String) dm_source_detailMap.get("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Do LOG
		}
		return result;
	}

	/**
	 * 保存读脉源访问记录
	 * 
	 * @param fk_orderinfo_code
	 *            订单code
	 * @param sub_entity_id
	 * @param in_para
	 *            输入参数
	 * @param dm_sourceMap
	 *            读脉源记录Map
	 * @param result
	 *            访问结果
	 * @param dm_3rd_interface_code
	 *            此次访问的第三方数据源code
	 */
	private void save_to_dm_source_log(String fk_orderinfo_code, String sub_entity_id, String in_para, Map<String, Object> dm_sourceMap, String result, String dm_3rd_interface_code, String is_db) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sub_entity_id", sub_entity_id);
		params.put("dm_source_code", dm_sourceMap.get("code"));
		params.put("dm_3rd_interface_code", dm_3rd_interface_code);
		params.put("is_db", is_db);
		params.put("fk_orderinfo_code", fk_orderinfo_code);
		params.put("in_para", in_para);
		params.put("result", result);
		addAndRet(params, "dm_source_log");
	}

	private boolean execCost(String orderId, String sub_entity_id, Map<String, Object> map, Map<String, Object> dm_sourceMap) {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressWarnings("unchecked")
	private String transResult(String dm_source_code, String dm_3rd_interface_code, String in_para, String result) {
		// 查询读脉源输出参数及对应第三方数据源关系
		List<Map<String, Object>> dm_source_para__dm_3rd_interface_paraList = dumai_sourceBaseDao.queryForList(gen_testDs_3(dm_source_code, dm_3rd_interface_code));
		Map<String, Object> jsonData = JsonToMap.gson2Map(result);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (Map<String, Object> dm_source_para__dm_3rd_interface_paraMap : dm_source_para__dm_3rd_interface_paraList) {
			String para_group = (String) dm_source_para__dm_3rd_interface_paraMap.get("para_group");
			Map<String, Object> data = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty(para_group)) {
				String para_path[] = para_group.split("_");// 多层的支持
				Map<String, Object> curJsonData = jsonData;
				for (String key : para_path) {
					curJsonData = (Map<String, Object>) curJsonData.get(key);
				}
				data = curJsonData;
			} else {
				data = jsonData;
			}
			resultMap.put((String) dm_source_para__dm_3rd_interface_paraMap.get("dm_source_para_name"), data.get(dm_source_para__dm_3rd_interface_paraMap.get("dm_3rd_interface_para_name")));
		}
		Map<String, Object> save_to_dm_source_detail = new HashMap<String, Object>();
		result = new Gson().toJson(resultMap);
		save_to_dm_source_detail.put("dm_source_code", dm_source_code);
		save_to_dm_source_detail.put("dm_3rd_interface_code", dm_3rd_interface_code);
		save_to_dm_source_detail.put("in_para", in_para);
		save_to_dm_source_detail.put("result", result);
		addAndRet(save_to_dm_source_detail, "dm_source_detail");
		return result;
	}

	private Map<String, Object> transInParams(String dm_source_code, String dm_3rd_interface_code, Map<String, Object> map) {
		// 查询参数映射
		List<Map<String, Object>> nameTransList = dumai_sourceBaseDao.queryForList(gen_testDs_2(dm_source_code, dm_3rd_interface_code));
		for (Map<String, Object> nameMap : nameTransList) {
			for (String key : map.keySet()) {
				String ds_name = (String) nameMap.get("ds_name");
				String is_name = (String) nameMap.get("is_name");
				// 根据参数映射转换参数
				if (key.equals(ds_name)) {
					if (("--").equals(is_name)) {
						map.remove(key);
					} else {
						map.put(is_name, map.remove(key));
					}
					break;
				}
			}
		}
		return map;
	}

	private String gen_testDs_2(String dm_source_code, String dm_3rd_interface_code) {
		String sql = " SELECT ";
		sql += " dm_source_para.`name` AS ds_name,";
		sql += " dm_3rd_interface_para.`name` as is_name,";
		sql += " dm_source_para.type ";
		sql += " FROM ";
		sql += " dm_source_para ";
		sql += " INNER JOIN dm_source_para__dm_3rd_interface_para ON dm_source_para__dm_3rd_interface_para.dm_source_para_code = dm_source_para.`code`";
		sql += " INNER JOIN dm_3rd_interface_para ON dm_source_para__dm_3rd_interface_para.dm_3rd_interface_para_code = dm_3rd_interface_para.`code`";
		sql += " WHERE ";
		sql += " dm_source_para__dm_3rd_interface_para.dm_source_code = '" + dm_source_code + "'";
		sql += " AND dm_source_para__dm_3rd_interface_para.dm_3rd_interface_code = '" + dm_3rd_interface_code + "'";
		sql += " AND dm_source_para.type = '0' ";
		return sql;
	}

	private String gen_testDs_3(String dm_source_code, String dm_3rd_interface_code) {
		String sql = " SELECT ";
		sql += " dm_source_para.`name` AS dm_source_para_name,";
		sql += " dm_3rd_interface_para.`name` as dm_3rd_interface_para_name,";
		// sql += " dm_source_para__dm_3rd_interface_para.base_condition,";
		sql += " dm_3rd_interface_para.para_group,";
		sql += " dm_source_para.type ";
		sql += " FROM ";
		sql += " dm_source_para ";
		sql += " INNER JOIN dm_source_para__dm_3rd_interface_para ON dm_source_para__dm_3rd_interface_para.dm_source_para_code = dm_source_para.`code`";
		sql += " INNER JOIN dm_3rd_interface_para ON dm_source_para__dm_3rd_interface_para.dm_3rd_interface_para_code = dm_3rd_interface_para.`code`";
		sql += " WHERE ";
		sql += " dm_source_para__dm_3rd_interface_para.dm_source_code = '" + dm_source_code + "'";
		sql += " AND dm_source_para__dm_3rd_interface_para.dm_3rd_interface_code = '" + dm_3rd_interface_code + "'";
		sql += " AND dm_source_para.type = '1' ";
		return sql;
	}

	private String gen_testDs_1(String dm_source_code, String order) {
		String sql = " SELECT ";
		sql += " dm_3rd_interface.* ";
		sql += " FROM ";
		sql += " dm_source ";
		sql += " INNER JOIN dm_source__dm_3rd_interface ON dm_source.`code` = dm_source__dm_3rd_interface.dm_source_code ";
		sql += " INNER JOIN dm_3rd_interface ON dm_source__dm_3rd_interface.dm_3rd_interface_code = dm_3rd_interface.`code` ";
		sql += " WHERE ";
		sql += " dm_3rd_interface.is_able='1' ";
		sql += " and dm_source.`code`= '" + dm_source_code + "'";
		sql += " order by " + order;
		return sql;
	}

	/**
	 * testDM end
	 */

	@Override
	public void updateIsable(String code) {
		String sql = "select * from dm_source where code = '" + code + "'";
		String dm_sourceStr = dumai_sourceBaseDao.executeSelectSql(sql);
		String dm_sourceJsonStr = dm_sourceStr.substring(1, dm_sourceStr.length() - 1);
		Map<String, Object> map = JsonToMap.gson2Map(dm_sourceJsonStr);
		String is_able = (String) map.get("is_able");

		String new_is_able = "0";
		if ("0".equals(is_able)) {
			new_is_able = "1";
		}
		String updateSql = "update dm_source set is_able = '" + new_is_able + "' where code = '" + code + "'";
		dumai_sourceBaseDao.update(updateSql);
	}

	private List<Map<String, Object>> getValidates(String... validateIndentifiers) {
		List<Map<String, Object>> validates = null;

		try {
			validates = JsonToMap.gson2List(IOUtils.toString(Dm_sourceServiceImpl.class.getResourceAsStream("validates.json"), "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return validates;
		}
		if (validateIndentifiers != null) {
			List<Map<String, Object>> validatesReturn = new ArrayList<Map<String, Object>>();
			for (String validateIndentifier : validateIndentifiers) {
				for (Map<String, Object> validate : validates) {
					if (validateIndentifier.equals(validate.get("identifier"))) {
						validatesReturn.add(validate);
						break;
					}
				}
			}
			return validatesReturn;
		} else {
			return validates;
		}

	}

	private Map<String, Object> validateInputOrder(Map<String, Object> orderInfo, List<Map<String, Object>> validates) {
		Map<String, Object> validateResult = new HashMap<String, Object>();
		validateResult.put("success", true);
		if (validates == null) {
			validateResult.put("success", false);
		}
		for (Map<String, Object> validate : validates) {
			String dm_source_code = (String) validate.get("dm_source_code");
			List<Map<String, Object>> inParams = dumai_sourceBaseDao.queryForList("select * from dm_source_para where type='0' and dm_source_code=?", dm_source_code);
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			boolean valid = true;// 参数是否有效
			for (Map<String, Object> inPara : inParams) {
				String name = (String) inPara.get("name");
				String fk_orderinfo_name = (String) inPara.get("fk_orderinfo_name");
				Object value = orderInfo.get(fk_orderinfo_name);
				if (value == null || value.equals("")) {
					valid = false;
					break;
				}
				inParamMap.put(name, value);
			}
			boolean flag = false;
			if (valid) {
				String expression = (String) validate.get("expression");
				String result = testDM((String) orderInfo.get("code"), (String) orderInfo.get("sub_entity_id"), dm_source_code, inParamMap);

				flag = CommonUtil.evalExpression(result, expression, orderInfo);
			}
			if (!flag) {
				validateResult.put("success", false);
				validateResult.put("identifier", validate.get("identifier"));
				validateResult.put("invalidMsg", validate.get("invalidMsg"));
				break;
			}
		}
		return validateResult;
	}

	public Map<String, Object> validateInputOrderAll(Map<String, Object> orderInfo) {
		return validateInputOrder(orderInfo, getValidates((String[]) null));
	}

	@Override
	public Map<String, Object> validateInputOrder(Map<String, Object> orderInfo, String... validateIndentifies) {

		if (validateIndentifies == null || validateIndentifies.length == 0) {
			Map<String, Object> validateResult = new HashMap<String, Object>();
			validateResult.put("success", false);
			return validateResult;
		}

		return validateInputOrder(orderInfo, getValidates(validateIndentifies));
	}
}
