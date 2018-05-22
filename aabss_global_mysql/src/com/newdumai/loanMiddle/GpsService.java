package com.newdumai.loanMiddle;

import com.newdumai.global.service.BaseService;
import com.newdumai.util.excel.vo.GpsDeviceVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/4/10.
 */
public interface GpsService extends BaseService {

    /**
     * 查询报警信息
     *
     * @param deviceNumber
     * @param alarmTypeId
     * @param dateTime
     * @return
     */
    Map<String, Object> getAlarmInfo(String deviceNumber, String alarmTypeId, Date dateTime);

    /**
     * 查询两天内的报警信息
     *
     * @return
     */
    List<Map<String, Object>> getTwoDaysAlarmInfo();

    /**
     * 保存或更新报告信息
     */
    void saveAlarm(Map<String, Object> map);

    /**
     * 根据设备编号获取订单
     *
     * @param deviceNumber
     * @return
     */
    Map<String, Object> getOrderInfo(String deviceNumber);

    /**
     * 根据订单code查询报警信息
     *
     * @param orderCode
     * @return
     */
    List<Map<String, Object>> getGpsAlarm(String orderCode);

    /**
     * 更新订单报警状态
     */
    void updateOrderAlarm(String orderCode);


    /**
     * 查询所有未设置完成gps设置编号的订单，并通过excel表数据进行更新设备id
     *
     * @return
     */
    Map<String, Object> getHaveNotDeviceOrder(List<GpsDeviceVo> list);

    /**
     * 查询GPS报警推送短信手机号及公司
     *
     * @return
     */
    List<Map<String, Object>> getAlarmDefine();
}
