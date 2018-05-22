package com.newdumai.loanOverdue;

import java.util.Map;

/**
 * Created by zhang on 2017/3/6.
 */
public interface LoanOverdueService {


    /**
     * 查询逾期信息
     *
     * @param sub_entity_id
     * @param map
     * @return
     */
    String getOverdueInfo(String sub_entity_id, Map<String, Object> map);

    /**
     * 根据订单code查询跟进详情
     *
     * @param fk_orderinfo_code
     * @return
     */
    String getFollowDetail(String fk_orderinfo_code);

    /**
     * 保存跟进情况
     *
     * @param map
     * @return
     */
    void saveFollow(Map<String, Object> map);

    /**
     * 查询升级处理列表数据
     *
     * @param sub_entity_id
     * @param stringObjectMap
     * @return
     */
    String getHandleListInfo(String sub_entity_id, Map<String, Object> stringObjectMap);

    /**
     * 保存审核结果
     *
     * @param userCode
     * @param map
     * @return
     */
    String saveHandle(String userCode, Map<String, Object> map);
}
