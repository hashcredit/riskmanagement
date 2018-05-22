package com.newdumai.ht.auditing.task;

import com.newdumai.global.service.BaseService;
import com.newdumai.util.excel.vo.AuditResultVo;

import java.util.List;
import java.util.Map;

/**
 * 电核任务Service
 *
 * @author zgl
 * @datetime Nov 30, 2016 4:52:07 PM
 */
public interface AuditingTaskService extends BaseService {

    /**
     * 根据用户code查询任务
     *
     * @param logincode 登录用户code
     * @param flag      是否完成
     * @return
     * @zgl Dec 1, 2016 2:21:18 PM
     */
    public Map<String, Object> getByUserCode(String logincode, String flag);

    /**
     * 根据用户code先判断是走规则还是模型，然后获取电核任务的电核项信息
     *
     * @param logincode
     * @param flag      1 欺诈评分，2 信用评分
     * @return
     * @zgl Dec 1, 2016 3:26:44 PM
     */
    public String getConfigByUserCode(String logincode, String flag);

    /**
     * 根据电核结果，更新评分结果，并重新计算评分
     *
     * @return
     * @zgl Dec 30, 2016 3:19:49 PM
     */
    public String updateScoreResult(Map<String, Object> map);

    /**
     * 查询线下电核任务
     *
     * @param flag：0 未完成，1 已完成，2 处理中，3 未完成和处理中
     * @return
     */
    List<Map<String, Object>> getDmTask(String flag);

    /**
     * 保存线下数据源电核任务
     *
     * @param map
     * @param user_code
     * @return
     */
    int saveDmDhDetail(Map<String, Object> map, String user_code);

    /**
     * 更新线下电核任务为处理中状态
     *
     * @param code
     * @param user_code
     * @return
     */
    int updateDmDhTaskState(String code, String user_code);

    /**
     * 根据用户code查询用户下的电核任务
     *
     * @param map
     * @return
     */
    String getDhTaskList(Map<String, Object> map);

    /**
     * 根据电核任务code查询电核项信息
     *
     * @param code
     * @return
     */
    Map<String, Object> getDhItemsByCode(String code);

    /**
     * 分配电核任务给电核员
     *
     * @param map
     * @return
     */
    int saveTaskUser(Map<String, Object> map);

    /**
     * 查询所有非待审核的电核结果(导出excel专用)
     *
     * @return
     */
    List<AuditResultVo> getDhResultList();


    /**
     * 获取所有规则
     *
     * @return
     */
    List<Map<String, Object>> getAllRules();

    /**
     * 电核增项信息
     *
     * @param code 电核任务code
     * @return
     */
    List<Map<String, Object>> getAdditional(String code);

    /**
     * 根据订单code查询电核结果
     *
     * @param orderCode
     * @return
     */
    Map<String, Object> getDhResultByOrderCode(String orderCode);
}
