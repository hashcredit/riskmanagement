package com.newdumai.dumai_data.task.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.task.DmTaskService;
import com.newdumai.global.service.impl.BaseServiceImpl;

@Service("dmTaskService")
public class DmTaskServiceImpl extends BaseServiceImpl implements DmTaskService {

	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition, gen_list_1(condition.get("condition").toString()), gen_list_2(condition.get("condition").toString(), getLimitUseAtSelectPage(map)));
	}

	@Override
	public Map<String, Object> getByUserCode(String logincode, String flag) {
		String sql = "select * from dm_task where user_code='" + logincode + "' and is_finish='" + flag + "' limit 1";
		Map<String, Object> map = mysqlSpringJdbcBaseDao.queryForMap(sql);
		// 如果为空，则分配一条未完成任务
		if (null == map && "0".equals(flag)) {
			String unDistribution = "select * from dm_task where user_code='' and is_finish='0' limit 1";
			Map<String, Object> unDistributionMap = mysqlSpringJdbcBaseDao.queryForMap(unDistribution);
			if (null != unDistributionMap) {
				String taskcode = (String) unDistributionMap.get("code");
				// 把电核员code更新到该任务中
				String distribution = "update dm_task set user_code='" + logincode + "' where code ='" + taskcode + "'";
				this.update(distribution);
				unDistributionMap.put("user_code", logincode);
			}
			return unDistributionMap;
		}
		return map;
	}

	// 根据用户code获取电核任务的电核项信息
	@Override
	public String getConfigByUserCode(String logincode) {
		String sql = "select a.*,b.dh_type,b.dh_content from sys_score a,sys_score__sys_type b where a.code=b.sys_score_code " 
				+ "and b.sys_type_code=(select thetype from dm_task where user_code ='"
				+ logincode + "' and is_finish='0')";
		return super.mysqlSpringJdbcBaseDao.executeSelectSql(sql);
	}

	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM dm_task WHERE 1=1 " + condition;
	}

	private String gen_list_2(String condition, String limit) {
		return "SELECT * FROM dm_task WHERE 1=1 ORDER BY is_finish " + condition + limit;
	}

	private Map<String, Object> getCondition_list(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String code = (String) map.get("code");
		if (StringUtils.isNotBlank(code)) {
			sb.append(" and code=?");
			list.add(code);
		}
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}
}
