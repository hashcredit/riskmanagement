package com.newdumai.loanFront.impl;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanFront.LoanFrontService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("loanFrontService")
public class LoanFrontServiceImpl extends BaseServiceImpl implements LoanFrontService {
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
		
	}

	@Override
	public String getHeadtype() {
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(gen_getHeadtype());
	}

	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM fk_orderinfo WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT fk_orderinfo.*,sys_type.name type_name,sys_user.surname audit_person,"
				+ "(select count(*) from fk_guize_detail where fk_guize_detail.order_code=fk_orderinfo.code and result='true') hit_counts FROM fk_orderinfo "
				+ "left join sys_type on fk_orderinfo.thetype = sys_type.code "
				+ "left join sys_user on fk_orderinfo.dqshr = sys_user.code "
				+ "WHERE 1=1 and fk_orderinfo.deleted=0 and fk_orderinfo.biz_range='1' "+condition+" order by fk_orderinfo.createtime desc "+limit;
	}

	public Map<String, Object> getCondition_list(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();

		String sub_entity_id = (String) map.get("sub_entity_id");
		sb.append(" AND fk_orderinfo.sub_entity_id=? ");
		list.add(sub_entity_id);

		String filter_headtype = (String) map.get("filter_headtype");
		if (StringUtils.isNotEmpty(filter_headtype)) {
			sb.append(" AND fk_orderinfo.thetype=? ");
			list.add(filter_headtype);
		}

		String status1 = (String) map.get("status1");
		if (StringUtils.isNotEmpty(status1)) {
			if ("0".equals(status1)) {
				sb.append(" AND fk_orderinfo.biz_range = '1' AND (fk_orderinfo.status1 = '0' or fk_orderinfo.status1 = '1')  ");
			} else if ("2".equals(status1)) {
				sb.append(" AND fk_orderinfo.status1 = '2' ");
			}
		}

		String filter_keyword = (String) map.get("filter_keyword");
		if (StringUtils.isNotEmpty(filter_keyword)) {
			sb.append(" AND (fk_orderinfo.name like ? or fk_orderinfo.card_num like ?)");
			list.add("%" + filter_keyword + "%");
			list.add("%" + filter_keyword + "%");
		}

		String filter_checkStatus = (String) map.get("filter_checkStatus");
		if (StringUtils.isNotEmpty(filter_checkStatus)) {
			sb.append(" AND fk_orderinfo.status1=? ");
			list.add(filter_checkStatus);
		}

		String filter_dateTime = (String) map.get("filter_dateTime");
		if (StringUtils.isNotEmpty(filter_dateTime)) {
			sb.append(" AND DATE_FORMAT(fk_orderinfo.createtime,'%Y-%m-%d') = ? ");
			list.add(filter_dateTime);
		}

		String filter_startTime = (String) map.get("filter_startTime");
		if (StringUtils.isNotEmpty(filter_startTime)) {
			sb.append(" AND fk_orderinfo.createtime > ? ");
			list.add(filter_startTime);
		}

		String filter_endTime = (String) map.get("filter_endTime");
		if (StringUtils.isNotEmpty(filter_endTime)) {
			sb.append(" AND fk_orderinfo.createtime < ? ");
			list.add(filter_endTime);
		}

		data.put("condition", sb.toString());
		data.put("args", list.toArray());

		return data;
	}

	private String gen_getHeadtype() {
		return "SELECT * FROM sys_type ";
	}

	@Override
	public boolean deleteByCode(String code) {
		mysqlSpringJdbcBaseDao.update("update fk_orderinfo set deleted=? where code=?",1,code);
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
