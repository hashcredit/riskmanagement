package com.newdumai.ht.auditing.task.impl;

import com.google.gson.GsonBuilder;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.vo.SqlSavePatrsVo;
import com.newdumai.ht.auditing.task.AuditingTaskService;
import com.newdumai.loanFront.AuditService;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.TimeHelper;
import com.newdumai.util.excel.vo.AuditResultVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service("auditingTaskService")
public class AuditingTaskServiceImpl extends BaseServiceImpl implements AuditingTaskService {

    @Autowired
    AuditService auditService;
    @Autowired
    private Dumai_sourceBaseDao sourceBaseDao;

    @Override
    public Map<String, Object> getByUserCode(String logincode, String flag) {
        String sql = "select * from dh_task where user_code='" + logincode + "' and is_finish='" + flag + "' limit 1";
        Map<String, Object> map = mysqlSpringJdbcBaseDao.queryForMap(sql);
        // 如果为空，则分配一条未完成任务
        if (null == map && "0".equals(flag)) {
            String unDistribution = "select * from dh_task where user_code='' and is_finish='0' limit 1";
            Map<String, Object> unDistributionMap = mysqlSpringJdbcBaseDao.queryForMap(unDistribution);
            if (null != unDistributionMap) {
                String taskcode = (String) unDistributionMap.get("code");
                // 把电核员code更新到该任务中
                String distribution = "update dh_task set user_code='" + logincode + "' where code ='" + taskcode + "'";
                this.update(distribution);
                unDistributionMap.put("user_code", logincode);
            }
            return unDistributionMap;
        }
        return map;
    }

    // 根据用户code先判断是走规则还是模型，然后获取电核任务的电核项信息(由于逻辑修改，该方法已废弃20170309)
    @Deprecated
    @Override
    public String getConfigByUserCode(String logincode, String flag) {
        String sys_company_typeSql = "SELECT a.* FROM sys_company_type AS a INNER JOIN dh_task AS b ON b.thetype = a.type_code INNER JOIN fk_orderinfo AS c ON b.order_code = c.`code` AND c.sub_entity_id = a.sub_entity_id WHERE b.is_finish = '0' AND b.user_code =?";
        Map<String, Object> sys_company_typeMap = super.mysqlSpringJdbcBaseDao.queryForMap(sys_company_typeSql, logincode);
        if (CollectionUtils.isEmpty(sys_company_typeMap)) {
            return "";
        }
        String sql = "";
        String rule_model = (String) sys_company_typeMap.get("rule_model");//1 规则，2模型
        if ("1".equals(rule_model)) {
            String sys_company_type_code = (String) sys_company_typeMap.get("code");
            sql = "select a.* from dh_item a where a.dh_source_type='1' and a.manager_item_code in (select fk_guize_code from sys_company_type__fk_guize where sys_company_type_code = '" + sys_company_type_code + "')";
        } else {
            String sys_type_code = (String) sys_company_typeMap.get("type_code");
            sql = "SELECT a.*,e.model_type FROM dh_item AS a,manager_model_group AS b,manager_model__manager_model_group AS c,manager_model__manager_item AS d,manager_model e where 1=1 and b.`code` = c.manager_model_group_code " +
                    " and c.manager_model_code = d.manager_model_code AND a.manager_item_code = d.manager_item_code and e.code = c.manager_model_code and a.dh_source_type='2' and b.is_able='1' and e.is_able='1' and b.sys_type_code = '" + sys_type_code + "' ORDER BY model_type";
        }
        return super.mysqlSpringJdbcBaseDao.executeSelectSql(sql);
    }


    @Override
    public String updateScoreResult(Map<String, Object> map) {
        Date enddate = new Date();
        String data1 = (String) map.get("data1");
        String data2 = (String) map.get("data2");
        // 电核任务code
        final String taskCode = (String) map.get("taskCode");
        String description = (String) map.get("description");
        String other_exception = (String) map.get("other_exception");
        String flag = (String) map.get("flag");

        // 保存电核结果
        Map<String, Object> detailMap = super.mysqlSpringJdbcBaseDao.queryForMap("select * from dh_task_detail where task_code = ?", taskCode);
        if (CollectionUtils.isEmpty(detailMap)) {
            detailMap = new HashMap<String, Object>();
            detailMap.put("task_code", taskCode);
        }
        detailMap.put("dh_content", data1);
        detailMap.put("additional_content", data2);
        detailMap.put("description", description);
        detailMap.put("other_exception", other_exception);
        super.saveOrUpdate("dh_task_detail", detailMap);

        List<Map<String, Object>> mapList = JsonToMap.gson2List(data1);
        String status = "1";
        for (Map<String, Object> dh_contentMap : mapList) {
            String dh_content = (String) dh_contentMap.get("dh_content");
            if ("true".equals(dh_content)) {
                status = "2";
                break;
            }
        }
        String opentime = (String) map.get("opentime");
        Map<String, Object> dhTaskMap = super.mysqlSpringJdbcBaseDao.queryForMap("select * from dh_task where code = ?", taskCode);
        Date distribution_time = (Date) dhTaskMap.get("distribution_time");
        int response_time = (int) dhTaskMap.get("response_time");
        int handle_time = (int) dhTaskMap.get("handle_time");
        if (StringUtils.isNotEmpty(opentime) && null != distribution_time) {
            if (response_time == 0) {
                response_time = TimeHelper.fistanceOfTwoDate(distribution_time, TimeHelper.parseDate(opentime));
            }
            handle_time += TimeHelper.fistanceOfTwoDate(TimeHelper.parseDate(opentime), enddate);
        }

        // 更新电核任务状态
        Map<String, String> taskMap = new HashMap<String, String>();
        if ("1".equals(flag)) {//1 提交，2 暂停
            taskMap.put("is_finish", "1");// 1 已完成
            // 更新规则或模型的电核结果
            Thread thread = new Thread() {
                public void run() {
                    auditService.Audit_DH(taskCode);
                }
            };
            thread.start();
        } else {
            status = "3";
        }
        if (StringUtils.isNotEmpty(StringUtils.trim(other_exception))) {//如果其它异常不为空，则状态直接拒单
            status = "2";
        }
        taskMap.put("status", status);// 审核状态：0 待审核，1 通过，2 拒绝,3 退回
        taskMap.put("handle_time", String.valueOf(handle_time));
        taskMap.put("response_time", String.valueOf(response_time));
        super.update("dh_task", taskMap, taskCode);
        return "success";
    }

    // 电核项评分
    private float calScore(String manager_item_code, String dh_content) {
        float score = 0;
        // 评分项配置与模型集关联关系
        String result = super.findByPara("manager_model__manager_item", "manager_item_code='" + manager_item_code + "'");
        Map<String, Object> resultMap = JsonToMap.gson2Map(result.replace("[", "").replace("]", ""));
        String seperate_box = (String) resultMap.get("seperate_box");
        if (StringUtils.isNotEmpty(seperate_box)) {
            List<Map<String, Object>> listMap = JsonToMap.gson2List(seperate_box);
            Map<String, String> optMap = new HashMap<String, String>();
            optMap.put("=", "等于");
            optMap.put(">", "大于");
            optMap.put("<", "小于");
            optMap.put(">=", "大于等于");
            optMap.put("<=", "小于等于");
            optMap.put("other", "其它");
            for (Map<String, Object> map : listMap) {
                String opt = (String) map.get("opt");
                String value = (String) map.get("value");
                if ((opt + value).equals(dh_content)) {
                    return (Float) map.get("score");
                }
            }
        }
        return score;
    }

    @Override
    public List<Map<String, Object>> getDmTask(String flag) {
        String sql = "select * from dh_task_3rd_interface where 1=1 ";
        if ("3".equals(flag)) {
            sql += " and (is_finish = '0' or is_finish = '2')";
        } else {
            sql += " and is_finish = '" + flag + "'";
        }
        return super.mysqlSpringJdbcBaseDao.queryForList(sql);
    }

    @Override
    public int saveDmDhDetail(Map<String, Object> map, String user_code) {
        //更新线下数据源电核任务状态为完成
        String dh_task_3rd_interface_code = (String) map.remove("code");
        String updateSql = "update dh_task_3rd_interface set is_finish = '1',user_code=? where code = ?";
        super.mysqlSpringJdbcBaseDao.update(updateSql, user_code, dh_task_3rd_interface_code);
        //保存线下数据源电核结果
        String code = UUID.randomUUID().toString();
        map.put("code", code);
        map.put("result", map.remove("out_para"));
        map.put("succ_status", "1");
        SqlSavePatrsVo vo = gen_add(map, "dm_3rd_interface_detail");
        return sourceBaseDao.update(vo.getSql(), vo.getVal().toArray());
    }

    @Override
    public int updateDmDhTaskState(String code, String user_code) {
        String updateSql = "update dh_task_3rd_interface set is_finish = '2',user_code = ? where code = ?";
        return super.mysqlSpringJdbcBaseDao.update(updateSql, user_code, code);
    }

    @Override
    public String getDhTaskList(Map<String, Object> map) {//flag:1 未分配的电核任务，2 未完成的电核任务
        Map<String, Object> conditionMap = getCondition(map);
        String condition = (String) conditionMap.get("condition");
        return listPageBase(conditionMap, genCountSql(condition), genListSql(condition, this.getLimitUseAtSelectPage(map)));
    }

    public String listPageBase(Map<String, Object> condition, String sql1, String sql2) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", mysqlSpringJdbcBaseDao.executeSelectSqlInt(sql1, (Object[]) condition.get("args")));
        data.put("rows", mysqlSpringJdbcBaseDao.queryForList(sql2, (Object[]) condition.get("args")));
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create().toJson(data);
    }

    public String getLimitUseAtSelectPage(Map<String, Object> map) {
        try {
            String page = (String) map.get("page");
            String rows = (String) map.get("rows");
            String limit = "";
            if ((page != null && !"".equals(page)) && (rows != null && !"".equals(rows))) {
                int strart = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);
                limit = " limit " + strart + " , " + rows;
            }
            return limit;
        } catch (Exception e) {
            return "";
        }
    }

    public String genCountSql(String condition) {
    	StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(*) FROM dh_task");
        sb.append(" INNER JOIN fk_orderinfo ON fk_orderinfo.`code` = dh_task.order_code");
        sb.append(" LEFT JOIN sys_user ON dh_task.user_code = sys_user.`code` ");
        sb.append(" where 1=1 ");
        sb.append(" " + condition + " ");
        return sb.toString();
    }

    public String genListSql(String condition, String limit) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT");
        sb.append(" 	dh_task.`code`,");
        sb.append(" 	dh_task.create_time,");
        sb.append(" 	dh_task.customer_name,");
        sb.append(" 	fk_orderinfo.organization,");
        sb.append(" 	dh_task.`status`,");
        sb.append(" 	dh_task.handle_time,");
        sb.append(" 	dh_task.response_time,");
        sb.append(" 	sys_user.surname,");
        sb.append(" 	fk_orderinfo.sqje");
        sb.append(" FROM dh_task ");
        sb.append(" INNER JOIN fk_orderinfo ON fk_orderinfo.`code` = dh_task.order_code");
        sb.append(" LEFT JOIN sys_user ON dh_task.user_code = sys_user.`code` ");
        sb.append(" where 1=1 ");
        sb.append(" " + condition + " ");
        sb.append(" " + limit + " ");
        return sb.toString();
    }

    public Map<String, Object> getCondition(Map<String, Object> map) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Object> list = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();
        String sys_user_code = (String) map.get("sys_user_code");
        if (StringUtils.isNotEmpty(sys_user_code)) {
            sb.append(" AND dh_task.user_code = ? ");
            list.add(sys_user_code);
        }
        String is_finish = (String) map.get("is_finish");
        if (StringUtils.isNotEmpty(is_finish)) {
            sb.append(" AND dh_task.is_finish = ? ");
            list.add(is_finish);
        }
        /*String surname = (String) map.get("surname");
        if (StringUtils.isNotEmpty(surname)) {
            sb.append(" AND sys_user.surname like ? ");
            list.add("%" + surname + "%");
        }*/
        String customerName = (String) map.get("customerName");
        if (StringUtils.isNotEmpty(customerName)) {
            sb.append(" AND dh_task.customer_name like ? ");
            list.add("%" + customerName + "%");
        }
//        String thetype = (String) map.get("thetype");
//        if (StringUtils.isNotEmpty(thetype)) {
//            sb.append(" AND dh_task.thetype = ? ");
//            list.add(thetype);
//        }
        String status = (String) map.get("status");
        if (StringUtils.isNotEmpty(status)) {
            sb.append(" AND dh_task.status = ? ");
            list.add(status);
        }
        String thetype = (String) map.get("thetype");
        if (StringUtils.isNotEmpty(thetype)) {
        	sb.append(" AND dh_task.thetype = ? ");
        	list.add(thetype);
        }
        String startTime = (String) map.get("startTime");
        if (StringUtils.isNotEmpty(startTime)) {
            sb.append(" AND dh_task.create_time > ? ");
            list.add(startTime);
        }
        String endTime = (String) map.get("endTime");
        if (StringUtils.isNotEmpty(endTime)) {
            sb.append(" AND dh_task.create_time < ? ");
            list.add(endTime);
        }
        String dist_status = (String) map.get("dist_status");//分配状态 0 待分配，1 已分配
        if (StringUtils.isNotEmpty(dist_status)) {
            if ("0".equals(dist_status)) {
                sb.append(" AND dh_task.user_code = '' ");
            } else {
                sb.append(" AND dh_task.user_code <> '' ");
            }
        }

        //查询进商户过滤
        String sub_entity_id = (String) map.get("sub_entity_id");
        if (StringUtils.isNotEmpty(sub_entity_id)) {
            sb.append(" AND fk_orderinfo.sub_entity_id = ?");
            list.add(sub_entity_id);
        }

        sb.append(" ORDER BY dh_task.create_time desc ");
        data.put("condition", sb.toString());
        data.put("args", list.toArray());
        return data;
    }

    @Override
    public Map<String, Object> getDhItemsByCode(String code) {
        String sys_company_typeSql = "SELECT a.* FROM sys_company_type AS a INNER JOIN fk_orderinfo AS b ON b.sub_entity_id = a.sub_entity_id AND b.thetype = a.type_code INNER JOIN dh_task AS c ON b.`code` = c.order_code WHERE c.`code` = ?";
        Map<String, Object> sys_company_typeMap = super.mysqlSpringJdbcBaseDao.queryForMap(sys_company_typeSql, code);
        String sql = "";
        String rule_model = (String) sys_company_typeMap.get("rule_model");//1 规则，2模型
        String sys_type_code = (String) sys_company_typeMap.get("type_code");
        //TODO 电核暂时先走规则20170427 以后需要再进行修改,模型暂不电核
        if ("".equals(rule_model)) {//模型
            sql = "SELECT a.*,e.model_type FROM dh_item AS a,manager_model_group AS b,manager_model__manager_model_group AS c,manager_model__manager_item AS d,manager_model e where 1=1 and b.`code` = c.manager_model_group_code " +
                    " and c.manager_model_code = d.manager_model_code AND a.manager_item_code = d.manager_item_code and e.code = c.manager_model_code and a.dh_source_type='2' and b.is_able='1' and e.is_able='1' and b.sys_type_code = '" + sys_type_code + "' ORDER BY model_type";
        } else {//规则
            sql = "SELECT a.* FROM dh_item AS a,sys_rule_group AS b,sys_rule_group__fk_guize AS c,fk_guize d " +
                    " WHERE a.manager_item_code = c.fk_guize_code AND b.biz_range='1' " +
                    " AND d.code=c.fk_guize_code AND d.is_able = '1' " +
                    " AND b.code =c.sys_rule_group_code and a.dh_source_type = '1' and b.sys_type_code ='" + sys_type_code + "'";
        }
        String orderInfoSql = "SELECT a.*,c.name AS typeName FROM fk_orderinfo a,dh_task b,sys_type c WHERE a.code = b.order_code and a.thetype = c.code and b.code = ?";

        List<Map<String, Object>> list = super.mysqlSpringJdbcBaseDao.queryForList(sql);
        String description = "";
        String other_exception = "";
        Map<String, Object> detailMap = super.mysqlSpringJdbcBaseDao.queryForMap("select * from dh_task_detail where task_code = ?", code);
        if (!CollectionUtils.isEmpty(detailMap)) {
            description = (String) detailMap.get("description");
            other_exception = (String) detailMap.get("other_exception");
            String dh_content = (String) detailMap.get("dh_content");
            if(StringUtils.isNotEmpty(dh_content) && !"undefined".equals(dh_content)){
                List<Map<String, Object>> detailMapList = JsonToMap.gson2List(dh_content);
                for (Map<String, Object> map : list) {
                    String dh_item_code = (String) map.get("code");
                    for (Map<String, Object> dhContentMap : detailMapList) {
                        String dhContent_dh_item_code = (String) dhContentMap.get("dh_terms");
                        if (dh_item_code.equals(dhContent_dh_item_code)) {
                            map.put("result", dhContentMap.get("dh_content"));
                            map.put("remark", dhContentMap.get("remark"));
                        }
                    }
                }
            }
        }
//        if (!"2".equals(rule_model)) {//非模型时执行（规则或默认）
//        }
        for (Map<String, Object> map : list) {
            String fk_guize_code = (String) map.get("manager_item_code");
            String fk_guize_detailSql = "SELECT * FROM fk_guize_detail AS a INNER JOIN dh_task AS b ON b.order_code = a.order_code WHERE a.guize_code = ? and b.code = ? order by a.opttime desc limit 1 ";
            Map<String, Object> fk_guize_detaiMap = super.mysqlSpringJdbcBaseDao.queryForMap(fk_guize_detailSql, fk_guize_code, code);
            if (!CollectionUtils.isEmpty(fk_guize_detaiMap)) {
                map.put("auto_result", fk_guize_detaiMap.get("result"));
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", 0);//未启用
        data.put("rows", list);
        data.put("orderInfo", super.mysqlSpringJdbcBaseDao.queryForMap(orderInfoSql, code));

        if (StringUtils.isNotEmpty(description)) {
            data.put("description", description);
        }
        if (StringUtils.isNotEmpty(other_exception)) {
            data.put("other_exception", other_exception);
        }
        return data;
    }

    @Override
    public int saveTaskUser(Map<String, Object> map) {
        String sys_user_code = (String) map.get("sys_user_code");
        String taskDatas = (String) map.get("taskDatas");
        String taskCodes[] = taskDatas.split(",");
        for (int i = 0; i < taskCodes.length; i++) {
            String sql = "update dh_task set user_code = ?,distribution_time = ? where code = ?";
            super.mysqlSpringJdbcBaseDao.update(sql, sys_user_code, new Date(), taskCodes[i]);
        }
        return 1;
    }

    @Override
    public List<AuditResultVo> getDhResultList() {
        String resultInfoSql = "SELECT c.`code`,c.`name`,c.card_num,c.bank_num,c.mobile,b.other_exception,b.dh_content,b.description FROM dh_task AS a " +
                " INNER JOIN dh_task_detail AS b ON a.`code` = b.task_code " +
                " INNER JOIN fk_orderinfo AS c ON c.`code` = a.order_code WHERE b.createtime < ?";
        List<Map<String, Object>> resultInfoMapList = super.mysqlSpringJdbcBaseDao.queryForList(resultInfoSql, TimeHelper.getCurrentDateZero());
        List<AuditResultVo> taskVoList = new ArrayList<>();
        for (Map<String, Object> resultInfoMap : resultInfoMapList) {
            AuditResultVo taskVo = new AuditResultVo();
            taskVo.setCustomer_name((String) resultInfoMap.get("name"));
            taskVo.setMobile((String) resultInfoMap.get("mobile"));
            taskVo.setCard_num((String) resultInfoMap.get("card_num"));
            taskVo.setBank_num((String) resultInfoMap.get("bank_num"));
            taskVo.setDescription((String) resultInfoMap.get("description"));
            taskVo.setOther_exception((String) resultInfoMap.get("other_exception"));
            String dh_content = (String) resultInfoMap.get("dh_content");
            String orderCode = (String) resultInfoMap.get("code");
            List<Map<String, Object>> resultDhResultList = this.handleDhResult(dh_content, orderCode);
            taskVo.setRuleList(resultDhResultList);
            taskVoList.add(taskVo);
        }
        return taskVoList;
    }

    private List<Map<String, Object>> handleDhResult(String dh_content, String orderCode) {
        String ruleDetailSql = "select * from fk_guize_detail where guize_code = ? and order_code = ? order by opttime desc limit 1";
        List<Map<String, Object>> ruleMapList = getAllRules();
        List<Map<String, Object>> dh_contentMapList = JsonToMap.gson2List(dh_content);
        List<Map<String, Object>> dhResultInfoMapList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> ruleMap : ruleMapList) {
            String ruleCode = (String) ruleMap.get("code");
            Map<String, Object> dhMap = new HashMap<String, Object>();
            for (Map<String, Object> dhItemMap : dh_contentMapList) {
                String manager_item_code = (String) dhItemMap.get("manager_item_code");
                if (ruleCode.equals(manager_item_code)) {
                    Map<String, Object> fk_guize_detailMap = super.mysqlSpringJdbcBaseDao.queryForMap(ruleDetailSql, ruleCode, orderCode);
                    dhMap.put("code", ruleCode);
                    dhMap.put("name", ruleMap.get("name"));
                    String autoResult = "";
                    if (!CollectionUtils.isEmpty(fk_guize_detailMap)) {
                        autoResult = (String) fk_guize_detailMap.get("result");
                    }
                    dhMap.put("autoResult", autoResult);
                    dhMap.put("dhResult", dhItemMap.get("dh_content"));
                    dhMap.put("remark", dhItemMap.get("remark"));
                    dhResultInfoMapList.add(dhMap);
                    break;
                }
            }
        }
        return dhResultInfoMapList;
    }

    @Override
    public List<Map<String, Object>> getAllRules() {
        String ruleSql = "select * from fk_guize";
        List<Map<String, Object>> ruleMapList = super.mysqlSpringJdbcBaseDao.queryForList(ruleSql);
        return ruleMapList;
    }

    @Override
    public List<Map<String, Object>> getAdditional(String code) {
        String taskDetailSql = "select * from dh_task_detail where task_code = ?";
        Map<String, Object> taskDetailMap = super.mysqlSpringJdbcBaseDao.queryForMap(taskDetailSql, code);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> additionalMap = null;
        if (null != taskDetailMap) {
            String additioinalContent = (String) taskDetailMap.get("additional_content");
            if (StringUtils.isNotEmpty(additioinalContent) && !"undefined".equals(additioinalContent)) {
                List<Map<String, Object>> additionalContentMapList = JsonToMap.gson2List(additioinalContent);
                for (Map<String, Object> map : additionalContentMapList) {
                    additionalMap = new HashMap<>();
                    additionalMap.put("item_name", map.get("item_name"));
                    additionalMap.put("item_value", map.get("item_value"));
                    additionalMap.put("dh_content", map.get("dh_content"));
                    additionalMap.put("remark", map.get("remark"));
                    list.add(additionalMap);
                }
            } else {
                list = this.additional(code);
            }
        } else {
            list = this.additional(code);
        }
        return list;
    }

    @Override
    public Map<String, Object> getDhResultByOrderCode(String orderCode) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        String taskDetailSql = "SELECT b.* FROM dh_task AS a INNER JOIN dh_task_detail AS b ON a.`code` = b.task_code WHERE a.order_code = ?";
        String dhTermsSql = "SELECT * FROM dh_item WHERE code = ?";
        Map<String, Object> taskDetailMap = super.mysqlSpringJdbcBaseDao.queryForMap(taskDetailSql, orderCode);
        if (null != taskDetailMap) {
            Map<String, Object> dhMap = null;
            String dh_content = (String) taskDetailMap.get("dh_content");
            List<Map<String, Object>> dhContentList = JsonToMap.gson2List(dh_content);
            for (Map<String, Object> map : dhContentList) {
                dhMap = new HashMap<String, Object>();
                String dh_terms = (String) map.get("dh_terms");
                String result = (String) map.get("dh_content");
                String remark = (String) map.get("remark");
                Map<String, Object> dhTermsMap = super.mysqlSpringJdbcBaseDao.queryForMap(dhTermsSql, dh_terms);
                String name = (String) dhTermsMap.get("manager_item_name");
                String value = (String) dhTermsMap.get("dh_description");
                dhMap.put("name", name);
                dhMap.put("value", value);
                dhMap.put("result", result);
                dhMap.put("remark", remark);
                resultList.add(dhMap);
            }
            String additional_content = (String) taskDetailMap.get("additional_content");
            if (StringUtils.isNotEmpty(additional_content)) {
                List<Map<String, Object>> additionalList = JsonToMap.gson2List(additional_content);
                for (Map<String, Object> map : additionalList) {
                    dhMap = new HashMap<String, Object>();
                    dhMap.put("name", map.get("item_name"));
                    dhMap.put("value", map.get("item_value"));
                    dhMap.put("result", map.get("dh_content"));
                    dhMap.put("remark", map.get("remark"));
                    resultList.add(dhMap);
                }
            }
            String other_exception = (String) taskDetailMap.get("other_exception");
            String description = (String) taskDetailMap.get("description");
            resultMap.put("other_exception", other_exception);
            resultMap.put("description", description);
            resultMap.put("dhResult", resultList);

            return resultMap;
        } else {
            return null;
        }
    }

    /**
     * 新配电核增项
     *
     * @param code
     * @return
     */
    private List<Map<String, Object>> additional(String code) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> additionalMap = new HashMap<String, Object>();

        String basicSql = "select a.company,a.company_address,a.company_phone,a.colleague_name1,a.colleague_phone1,a.colleague_name2,a.colleague_phone2,b.thetype " +
                "from fk_personinfo a " +
                " inner join fk_orderinfo b on a.code = b.personinfo_code " +
                " inner join dh_task c on c.order_code = b.`code` " +
                "where c.`code` = ? ";
        Map<String, Object> personInfoMap = super.mysqlSpringJdbcBaseDao.queryForMap(basicSql, code);

        if (!CollectionUtils.isEmpty(personInfoMap) && "73c89a5a-643a-4254-b045-d8e831d2c33a".equals(personInfoMap.get("thetype"))) {
            String company = (String) personInfoMap.get("company");
            String company_address = (String) personInfoMap.get("company_address");
            String company_phone = (String) personInfoMap.get("company_phone");
            String colleague_name1 = (String) personInfoMap.get("colleague_name1");
            String colleague_phone1 = (String) personInfoMap.get("colleague_phone1");
            String colleague_name2 = (String) personInfoMap.get("colleague_name2");
            String colleague_phone2 = (String) personInfoMap.get("colleague_phone2");

            additionalMap = new HashMap<String, Object>();
            additionalMap.put("item_name", "所属行业");
            additionalMap.put("item_value", "是否在公安、检察院、法院、监狱、戒毒所、军人行业工作");
            additionalMap.put("dh_content", "");
            additionalMap.put("remark", "");
            list.add(additionalMap);

            if (StringUtils.isNotEmpty(company)) {
                additionalMap = new HashMap<String, Object>();
                additionalMap.put("item_name", "公司名称");
                additionalMap.put("item_value", company);
                additionalMap.put("dh_content", "");
                additionalMap.put("remark", "");
                list.add(additionalMap);
            }

            if (StringUtils.isNotEmpty(company_address)) {
                additionalMap = new HashMap<String, Object>();
                additionalMap.put("item_name", "公司地址");
                additionalMap.put("item_value", company_address);
                additionalMap.put("dh_content", "");
                additionalMap.put("remark", "");
                list.add(additionalMap);
            }

            if (StringUtils.isNotEmpty(company_phone)) {
                additionalMap = new HashMap<String, Object>();
                additionalMap.put("item_name", "公司电话");
                additionalMap.put("item_value", company_phone);
                additionalMap.put("dh_content", "");
                additionalMap.put("remark", "");
                list.add(additionalMap);
            }

            if (StringUtils.isNotEmpty(colleague_name1)) {
                additionalMap = new HashMap<String, Object>();
                additionalMap.put("item_name", "同事姓名电话1");
                additionalMap.put("item_value", colleague_name1 + ":" + colleague_phone1);
                additionalMap.put("dh_content", "");
                additionalMap.put("remark", "");
                list.add(additionalMap);
            }

            if (StringUtils.isNotEmpty(colleague_name2)) {
                additionalMap = new HashMap<String, Object>();
                additionalMap.put("item_name", "同事姓名电话2");
                additionalMap.put("item_value", colleague_name2 + ":" + colleague_phone2);
                additionalMap.put("dh_content", "");
                additionalMap.put("remark", "");
                list.add(additionalMap);
            }
        } else {
            //TODO 以下常住地址等字段获取位置待定
            additionalMap.put("item_name", "常住地址");
            String residence_address = "";//常住地址
            additionalMap.put("item_value", residence_address);
            additionalMap.put("dh_content", "");
            additionalMap.put("remark", "");
            if (StringUtils.isNotEmpty(residence_address)) {
                list.add(additionalMap);
            }

            additionalMap = new HashMap<String, Object>();
            additionalMap.put("item_name", "借款人公司全称");
            String company = "";//借款人公司全称
            additionalMap.put("item_value", company);
            additionalMap.put("dh_content", "");
            additionalMap.put("remark", "");
            if (StringUtils.isNotEmpty(company)) {
                list.add(additionalMap);
            }

            additionalMap = new HashMap<String, Object>();
            additionalMap.put("item_name", "借款人公司地址");
            String company_address = "";//借款人公司地址
            additionalMap.put("item_value", company_address);
            additionalMap.put("dh_content", "");
            additionalMap.put("remark", "");
            if (StringUtils.isNotEmpty(company_address)) {
                list.add(additionalMap);
            }
            additionalMap = new HashMap<String, Object>();
            additionalMap.put("item_name", "借款人公司电话");
            String company_telphone = "";//借款人公司电话
            additionalMap.put("item_value", company_telphone);
            additionalMap.put("dh_content", "");
            additionalMap.put("remark", "");
            if (StringUtils.isNotEmpty(company_telphone)) {
                list.add(additionalMap);
            }
        }

        String orderSql = "SELECT dh_task.`code`,fk_orderinfo.sub_entity_id,fk_orderinfo.organization,fk_orderinfo.`name` FROM dh_task INNER JOIN fk_orderinfo ON dh_task.order_code = fk_orderinfo.`code` WHERE dh_task.`code`= ?";
        Map<String, Object> orderInfoMap = super.mysqlSpringJdbcBaseDao.queryForMap(orderSql, code);
        additionalMap = new HashMap<String, Object>();
        additionalMap.put("item_name", "服务机构名称 ");
        String servicesOrganization = (String) orderInfoMap.get("organization");
        additionalMap.put("item_value", servicesOrganization);
        additionalMap.put("dh_content", "");
        additionalMap.put("remark", "");
        if (StringUtils.isNotEmpty(servicesOrganization)) {
            list.add(additionalMap);
        }

        additionalMap = new HashMap<String, Object>();
        additionalMap.put("item_name", "对购买车型是否熟悉");
        String familiar = "对购买车型的熟悉程度";
        additionalMap.put("item_value", familiar);
        additionalMap.put("dh_content", "");
        additionalMap.put("remark", "");
        if (StringUtils.isNotEmpty(familiar)) {
            list.add(additionalMap);
        }
        return list;
    }


}
