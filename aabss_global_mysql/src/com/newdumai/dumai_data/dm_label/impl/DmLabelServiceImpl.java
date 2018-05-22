package com.newdumai.dumai_data.dm_label.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.newdumai.util.Dm_3rd_InterfaceUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.dumai_data.dm_label.DmLabelService;
import com.newdumai.global.service.impl.Dumai_sourceBaseServiceImpl;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.MapObjUtil;
import com.newdumai.util.SqlHelper;

@Service("dmLabelService")
public class DmLabelServiceImpl extends Dumai_sourceBaseServiceImpl implements DmLabelService {

	@Autowired
	Dm_3rd_interfaceService dm_3rd_interfaceService;

	/* block list begin */
	@Override
	public String list(String table, Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition, gen_list_1(condition.get("condition").toString()), gen_list_2(condition.get("condition").toString(), getLimitUseAtSelectPage(map)));
	}

	private String gen_list_1(String condition) {
		String sql = "SELECT count(*) FROM dm_label AS a LEFT JOIN dm_label_group AS b ON a.dm_label_group_code = b.`code` where 1=1 " + condition;
		return sql;
	}

	private String gen_list_2(String condition, String limit) {
		return "SELECT a.*,b.group_name_zh FROM dm_label AS a LEFT JOIN dm_label_group AS b ON a.dm_label_group_code = b.`code` where 1=1 " + condition + limit;
	}

	private Map<String, Object> getCondition_list(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String is_able = (String) map.get("is_able");
		if (StringUtils.isNotEmpty(is_able)) {
			sb.append(" AND a.is_able = ? ");
			list.add(is_able);
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
	/* block list end */

	@Override
	public String getThirdSource(String code) {
		String sql = "select c.*,d.`name` AS paraName,d.description AS paraDescription from  (SELECT a.*,b.in_para,b.dm_3rd_interface_para_code AS paraCode,b.code AS label3rdCode from dm_3rd_interface AS a,dm_label__dm_3rd_interface b "
				+ " WHERE a.`code` = b.dm_3rd_interface_code AND b.dm_label_code ='" + code + "') c LEFT JOIN dm_3rd_interface_para d on c.paraCode=d.`code` ";
		return super.dumai_sourceBaseDao.executeSelectSql(sql);
	}

	@Override
	public String getUnRelateThirdSource(String code) {
		String sql = "SELECT a.* FROM dm_3rd_interface AS a WHERE a.is_able='1' and a.code not in (select dm_3rd_interface_code from dm_label__dm_3rd_interface b where b.dm_label_code = '" + code + "')";
		return super.dumai_sourceBaseDao.executeSelectSql(sql);
	}

	@Override
	public Map<String, Object> getBycode(String tablename, String code) {
		return super.dumai_sourceBaseDao.queryForMap("select * from " + tablename + " where code = ?", code);
	}

	@Override
	public void delRelation(String dm_label_code, String dm_3rd_interface_code) {
		String sql = "delete from dm_label__dm_3rd_interface where dm_label_code='" + dm_label_code + "' and dm_3rd_interface_code='" + dm_3rd_interface_code + "'";
		super.dumai_sourceBaseDao.executeSql(sql);
	}

	@Override
	public boolean addSources(String userImgCode, String[] sourceCodes) {
		Object[][] args = new Object[sourceCodes.length][];
		for (int i = 0; i < args.length; i++) {
			Object[] arg = new Object[3];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = userImgCode;
			arg[2] = sourceCodes[i];
			args[i] = arg;
		}
		dumai_sourceBaseDao.batchInsert("insert into dm_label__dm_3rd_interface(code,dm_label_code,dm_3rd_interface_code) values(?,?,?)", args);
		return true;
	}

	@Override
	public List<Map<String, Object>> getSourcePara(String dm_3rd_interface_code, String flag) {
		String sql = "select * from dm_3rd_interface_para where type='" + flag + "' and dm_3rd_interface_code='" + dm_3rd_interface_code + "'";
		return super.dumai_sourceBaseDao.executeSelectSql2(sql);
	}

	@Override
	public Integer updateSourcePara(String dmLabelCode, String sourceCode, String paraCode) {
		String sql = "update dm_label__dm_3rd_interface set dm_3rd_interface_para_code = ? where dm_label_code = ? and dm_3rd_interface_code = ? ";
		return super.dumai_sourceBaseDao.update(sql, paraCode, dmLabelCode, sourceCode);
	}

	@Override
	public Map<String, Object> getImg3rdRelationInfo(String dm_label_code, String dm_3rd_interface_code) {
		String sql = "select * from dm_label__dm_3rd_interface where dm_label_code = ? and dm_3rd_interface_code = ?";
		return super.dumai_sourceBaseDao.queryForMap(sql, dm_label_code, dm_3rd_interface_code);
	}

	@Override
	public String toTestPage(String dm_label_code) {
		String sql = "SELECT b.* FROM dm_label__dm_3rd_interface AS a INNER JOIN dm_3rd_interface_para AS b ON a.dm_3rd_interface_code = b.dm_3rd_interface_code WHERE b.type = '0' AND a.dm_label_code = '"
				+ dm_label_code + "' ";
		return super.dumai_sourceBaseDao.executeSelectSql(sql);
	}

	@Override
	public void updateOutInterface(Map<String, Object> request2Map) {
		String sql = "update dm_label__dm_3rd_interface set out_interface = ? WHERE dm_label_code = ? and dm_3rd_interface_code = ?";
		super.dumai_sourceBaseDao.update(sql, request2Map.get("out_interface"), request2Map.get("code"), request2Map.get("dm_3rd_interface_code"));
	}

	@Override
	public String testImg(Map<String, Object> params) {
		String dm_label_code = (String) params.remove("dm_label_code");
		String dm_3rd_interface_code = (String) params.remove("dm_3rd_interface_code");
		//标签项目表与数据源关联关系Map
		Map<String, Object> label3rdMap = getImg3rdRelationInfo(dm_label_code, dm_3rd_interface_code);
		String inPara = (String) label3rdMap.get("in_para");
		Map<String, Object> params3rd = Dm_3rd_InterfaceUtils.transInParaToColumn(inPara, params, dumai_sourceBaseDao);
		// 先查询dm_label_detail（手机号码,身份证号码，银行卡号）
		String mobile = (String) params3rd.get("mobile");
		String card_num = (String) params3rd.get("card_num");
		String bank_num = (String) params3rd.get("bank_num");
		String sql = "select * from dm_label_detail where 1=1 ";
		if (StringUtils.isNotEmpty(card_num)) {
			sql += " and card_num='" + card_num + "'";
		} else {
			sql += " and (card_num='' or card_num is null)";
		}
		if (StringUtils.isNotEmpty(mobile)) {
			sql += " and mobile='" + mobile + "'";
		} else {
			sql += " and (mobile='' or mobile is null)";
		}
		if (StringUtils.isNotEmpty(bank_num)) {
			sql += " and bank_num='" + bank_num + "'";
		} else {
			sql += " and (bank_num='' or bank_num is null)";
		}
		sql += " order by opttime desc limit 1";
		Map<String, Object> detailMap = super.dumai_sourceBaseDao.queryForMap(sql);
		// 如果dm_label_detail结果不存在先保存
		if (CollectionUtils.isEmpty(detailMap)) {
			String code = addAndRet(params3rd,"dm_label_detail");
			params3rd.put("code", code);
			detailMap = new HashMap<String,Object>();
			detailMap.putAll(params3rd);
		}
		return execTestImg(dm_label_code, params, detailMap);
	}
	
	/**
	 * test begin
	 */
	/**
	 * 
	 * @param dm_label_code
	 * @param params
	 * @return
	 */
	private String execTestImg(String dm_label_code, Map<String, Object> params, Map<String, Object> detailMap) {
		Map<String, Object> dm_labelMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_label WHERE `code` = '" + dm_label_code + "'");
		String result = null;
		String dm_3rd_interface_code = null;
		try {
//			String switcher = (String) dm_labelMap.get("switcher");
			List<Map<String, Object>> dm_3rd_interfaceList = null;
			dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(gen_testDs_1(dm_label_code, "cost"));
//			if ("1".equals(switcher)) {
//			} else if ("2".equals(switcher)) {
//				dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(gen_testDs_1(dm_label_code, "error"));
//			} else {
//			}
			if (CollectionUtils.isEmpty(dm_3rd_interfaceList)) {
				return null;
			}
			/**
			 * 取3rd  list结束
			 */
			/**
			 * 取3rd结果
			 */
			for (Map<String, Object> dm_3rd_interfaceMap : dm_3rd_interfaceList) {
				dm_3rd_interface_code = (String) dm_3rd_interfaceMap.get("code");
				try {
					result = dm_3rd_interfaceService.testDS(dm_3rd_interface_code, params);
					if (StringUtils.isNotEmpty(result)) {
						//解析结果
						Map<String, Object> resultMap = transResult(dm_label_code, dm_3rd_interface_code, result);
						if (!CollectionUtils.isEmpty(resultMap)) {
							String dm_label_group_code = (String) dm_labelMap.get("dm_label_group_code");
							if (StringUtils.isNotEmpty(dm_label_group_code) && null != resultMap.get(dm_labelMap.get("name"))) {
								this.updateDetailColumn(resultMap, detailMap, dm_label_group_code);
								result = new Gson().toJson(resultMap);
							} else {
								result = null;
							}
						} else {
							dumai_sourceBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
						}
					} else {
						dumai_sourceBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
					}
				} catch (Exception e) {
					e.printStackTrace();
					dumai_sourceBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			return result;
		}
	}

	/**
	 * 将解析的结果更新到dm_label_detail表
	 *
	 * @param resultMap
	 * @param detailMap
	 * @param dm_label_group_code
	 */
	private void updateDetailColumn(Map<String, Object> resultMap, Map<String, Object> detailMap, String dm_label_group_code) {
		Map<String, Object> dm_label_groupMap = dumai_sourceBaseDao.queryForMap("SELECT * FROM dm_label_group WHERE `code` = '" + dm_label_group_code + "'");
		String column = (String) dm_label_groupMap.get("group_name");
		String columnStr = (String) detailMap.get(column);
		Map<String, Object> columnMap = null;
		if (StringUtils.isNotEmpty(columnStr)) {
            columnMap = JsonToMap.gson2Map(columnStr);
        } else {
            columnMap = new HashMap<String, Object>();
        }
		columnMap.putAll(resultMap);
		this.updateByCode((String) detailMap.get("code"), column, new Gson().toJson(columnMap));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> transResult(String dm_label_code, String dm_3rd_interface_code, String result) {
		// 查询画像输出参数及对应第三方数据源参数关系
		List<Map<String, Object>> dm_label__dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(gen_testDs_3(dm_label_code, dm_3rd_interface_code));
		Map<String, Object> jsonData = JsonToMap.gson2Map(result);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (Map<String, Object> dm_label__dm_3rd_interfaceMap : dm_label__dm_3rd_interfaceList) {
			String para_group = (String) dm_label__dm_3rd_interfaceMap.get("para_group");
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
			resultMap.put((String) dm_label__dm_3rd_interfaceMap.get("label_name"), data.get(dm_label__dm_3rd_interfaceMap.get("3rd_name")));
		}
		return resultMap;
	}
	
	private String gen_testDs_3(String dm_label_code, String dm_3rd_interface_code) {
		String sql = " SELECT ";
		sql += " dm_3rd_interface_para.para_group, ";
		sql += " dm_3rd_interface_para.`name` as 3rd_name, ";
		sql += " dm_label.`name` as label_name ";
		sql += " FROM ";
		sql += " dm_label__dm_3rd_interface ";
		sql += " INNER JOIN dm_3rd_interface_para ON dm_3rd_interface_para.`code` = dm_label__dm_3rd_interface.dm_3rd_interface_para_code ";
		sql += " INNER JOIN dm_label ON dm_label__dm_3rd_interface.dm_label_code = dm_label.`code` ";
		sql += " WHERE ";
		sql += " 1 = 1 ";
		sql += " and  ";
		sql += " dm_label__dm_3rd_interface.dm_label_code = '"+dm_label_code+"' ";
		sql += " AND ";
		sql += " dm_label__dm_3rd_interface.dm_3rd_interface_code = '"+dm_3rd_interface_code+"' ";
		sql += "  AND ";
		sql += " dm_3rd_interface_para.type = '1' ";
		return sql;
	}

	private String gen_testDs_1(String dm_label_code, String order) {
		String sql = " SELECT ";
		sql += " dm_3rd_interface.* ";
		sql += " FROM ";
		sql += " dm_label ";
		sql += " INNER JOIN dm_label__dm_3rd_interface ON dm_label.`code` = dm_label__dm_3rd_interface.dm_label_code ";
		sql += " INNER JOIN dm_3rd_interface ON dm_label__dm_3rd_interface.dm_3rd_interface_code = dm_3rd_interface.`code` ";
		sql += " WHERE ";
		sql += " dm_3rd_interface.is_able='1' ";
		sql += " and dm_label.`code`= '" + dm_label_code + "'";
		sql += " order by " + order;
		return sql;
	}
	/**
	 * test end
	 */

	@Override
	public Map<String, Object> getDetailByIdCard(String idCard) {
		String sql = "select * from dm_label_detail where id_card = ?";
		return super.dumai_sourceBaseDao.queryForMap(sql, idCard);
	}

	@Override
	public void updateByCode(String code, String column, String detailMapStr) {
		String sql = "update dm_label_detail set " + column + "= ? where code = ?";
		super.dumai_sourceBaseDao.update(sql, detailMapStr, code);
	}

	@Override
	public List<Map<String, Object>> getGroupName() {
		String sql = "select * from dm_label_group";
		return super.dumai_sourceBaseDao.executeSelectSql2(sql);
	}

	@Override
	public String findByCode(String dm_label_code) {
		String sql = "SELECT a.*,b.code AS dm_label_group_code,b.group_name_zh FROM dm_label AS a LEFT JOIN dm_label_group AS b ON a.dm_label_group_code = b.`code` where a.`code`='" + dm_label_code + "'";
		return super.dumai_sourceBaseDao.executeSelectSql(sql);
	}
	
	@Override
	public List<Map<String, Object>> findByGroupCode(String dm_label_group_code) {
		String sql = " SELECT ";
		sql += " dm_label.`code`,  ";
		sql += " dm_label.`name`,  ";
		sql += " dm_label.name_zh,  ";
		sql += " dm_label.description  ";
		sql += " FROM  ";
		sql += " dm_label  ";
		sql += " INNER JOIN dm_label_group ON dm_label_group.`code` = dm_label.dm_label_group_code ";
		sql += "WHERE dm_label.dm_label_group_code = '"+dm_label_group_code+"'";
		return super.dumai_sourceBaseDao.queryForList(sql);
	}

	@Override
	public Integer updateInPara(String code, String inPara)	{
		String sql = "update dm_label__dm_3rd_interface set in_para = ? where code = ? ";
		return super.dumai_sourceBaseDao.update(sql, inPara, code);
	}
	
	
	
	@Override
	public Map<String, Object> getBigLabelMap(String card_num,String bank_num,String mobile) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(StringUtils.isEmpty(card_num)){
			return null;
		}
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("bank_num", bank_num);
		params.put("card_num", card_num);
		params.put("mobile", mobile);
		String sql = SqlHelper.getSelectSqlByParams("dm_label_detail",params, new String[]{"card_num","bank_num","mobile"});
		Map<String, Object> dm_label_detailMap = dumai_sourceBaseDao.queryForMap(sql);
		List<Map<String, Object>> dm_label_groupList = dumai_sourceBaseDao.queryForList(" select * from dm_label_group  ");
		Map<String, Object> jsonDataMap = new HashMap<String, Object>();
		for (Map<String, Object> dm_label_groupMap : dm_label_groupList) {
			String group_name = (String) dm_label_groupMap.get("group_name");
			if (dm_label_detailMap.containsKey(group_name)){
				String jsonData = (String) dm_label_detailMap.get(group_name);
				try {
					jsonDataMap = JsonToMap.gson2Map(jsonData);
					returnMap.putAll(jsonDataMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getBigLabelMap(String dm_label_detail_code) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> dm_label_detailMap = dumai_sourceBaseDao.queryForMap(" select * from dm_label_detail where code = ? ", new Object[] { dm_label_detail_code });
		List<Map<String, Object>> dm_label_groupList = dumai_sourceBaseDao.queryForList(" select * from dm_label_group  ");
		Map<String, Object> jsonDataMap = new HashMap<String, Object>(); 
		for (Map<String, Object> dm_label_groupMap : dm_label_groupList) {
			String group_name = (String) dm_label_groupMap.get("group_name");
			if (dm_label_detailMap.containsKey(group_name)) {
				String jsonData = (String) dm_label_detailMap.get(group_name);
				try {
					jsonDataMap = JsonToMap.gson2Map(jsonData);
					returnMap.putAll(jsonDataMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return returnMap;
	}
	
	@Override
	public Map<String,Object>getListWithGroup(Map<String,Object>params){
		try {
			params.put("data", super.dumai_sourceBaseDao.queryForList(" SELECT dm_label_group.group_name_zh, dm_label.name, dm_label.name_zh, dm_label.`code`, dm_label.description FROM dm_label_group INNER JOIN dm_label ON dm_label.dm_label_group_code = dm_label_group.`code` where  dm_label.is_able = '1' order by group_name_zh "));
			params.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			params.put("result", "failure");
		}
		return params;
	}
	
	
}
