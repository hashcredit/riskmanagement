package com.newdumai.loanMiddle.impl;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanMiddle.GpsService;
import com.newdumai.util.DictUtils;
import com.newdumai.util.TimeHelper;
import com.newdumai.util.excel.vo.GpsDeviceVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhang on 2017/4/10.
 */
@Service("gpsService")
public class GpsServiceImpl extends BaseServiceImpl implements GpsService {
    @Override
    public Map<String, Object> getAlarmInfo(String deviceNumber, String alarmTypeId, Date dateTime) {
        String sql = "select * from gps_alarm where device_number = ? and alarm_type = ? and alarm_time = ?";
        return super.mysqlSpringJdbcBaseDao.queryForMap(sql, deviceNumber, alarmTypeId, dateTime);
    }

    @Override
    public void saveAlarm(Map<String, Object> map) {
        add(map, "gps_alarm");
    }

    @Override
    public Map<String, Object> getOrderInfo(String deviceNumber) {
        String sql = "select * from gps_device where device_number = ?  limit 1";
        return super.mysqlSpringJdbcBaseDao.queryForMap(sql, deviceNumber);
    }

    @Override
    public List<Map<String, Object>> getGpsAlarm(String orderCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "select * from gps_alarm where fk_orderinfo_code = ?";
        List<Map<String, Object>> list = super.mysqlSpringJdbcBaseDao.queryForList(sql, orderCode);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            Map<String, Object> newMap = new HashMap<String, Object>();
            Date alarmTime = (Date) map.get("alarm_time");
            String alarm_time = sdf.format(alarmTime);
            String alarmType = (String) map.get("alarm_type");
            newMap.put("AlarmTypeId", alarmType);
            newMap.put("AlarmType", DictUtils.getDictLabel(alarmType, "gps_alarm", ""));
            newMap.put("Time", alarm_time);
            newMap.put("Lng", map.get("longitude"));
            newMap.put("Lat", map.get("latitude"));
            newMap.put("Location", map.get("alarm_location"));
            resultList.add(newMap);
        }
        return resultList;
    }

    @Override
    public void updateOrderAlarm(String orderCode) {
        String sql = "update fk_orderinfo set is_alarm = '1' where code = ?";
        super.mysqlSpringJdbcBaseDao.update(sql, orderCode);
    }

    @Override
    public Map<String, Object> getHaveNotDeviceOrder(List<GpsDeviceVo> list) {
        String sql = "select a.code,a.name,a.card_num,b.service_organization from fk_orderinfo a left join fk_personinfo b on a.personinfo_code = b.code " +
                " where a.code not in (select distinct fk_orderinfo_code from gps_device) ";
//                " where a.code ='a9825fdd-2f03-464b-bb4d-77a58bd405b9' ";
        List<Map<String, Object>> orderList = super.mysqlSpringJdbcBaseDao.queryForList(sql);
        int successNum = 0;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            for (Map<String, Object> map : orderList) {
                String code = (String) map.get("code");
                String name = (String) map.get("name");
                String card_num = (String) map.get("card_num");
                String service_organization = (String) map.get("service_organization");
                for (GpsDeviceVo gpsDevice : list) {
                    if (gpsDevice.getLender().equals(name) && StringUtils.isNotEmpty(service_organization) && gpsDevice.getCompany().equals(service_organization)) {
                        String deviceId = gpsDevice.getDeviceId();
                        String deviceSql = "select * from gps_device where fk_orderinfo_code = ? and card_num = ?";
                        List<Map<String, Object>> gpsDeviceList = super.mysqlSpringJdbcBaseDao.queryForList(deviceSql, code, card_num);
                        if (CollectionUtils.isEmpty(gpsDeviceList)) {
                            successNum = setDeviceId(successNum, code, card_num, gpsDevice, deviceId);
                        } else {
                            for (Map<String, Object> gpsDeviceMap : gpsDeviceList) {
                                if (deviceId.equals(gpsDeviceMap.get("device_number"))) {
                                    continue;
                                } else {
                                    successNum = setDeviceId(successNum, code, card_num, gpsDevice, deviceId);
                                }
                            }
                        }
                    }
                }
            }
            resultMap.put("num", successNum);
            resultMap.put("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", "error");
        }

        return resultMap;
    }

    private int setDeviceId(int successNum, String code, String card_num, GpsDeviceVo gpsDevice, String deviceId) {
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("fk_orderinfo_code", code);
        columnMap.put("card_num", card_num);
        columnMap.put("device_number", deviceId);
        columnMap.put("company", gpsDevice.getCompany());
        columnMap.put("car_vin", gpsDevice.getVin());
        columnMap.put("car_type", gpsDevice.getCarType());
        columnMap.put("borrower_name", gpsDevice.getLender());
        super.add(columnMap, "gps_device");
        successNum += 1;
        return successNum;
    }

    @Override
    public List<Map<String, Object>> getAlarmDefine() {
        String alarmMobileSql = "select company,mobiles from dba_gps_alarm_define";
        return super.mysqlSpringJdbcBaseDao.queryForList(alarmMobileSql);
    }

    @Override
    public List<Map<String, Object>> getTwoDaysAlarmInfo() {
        String sql = "select * from gps_alarm where DATE_FORMAT(alarm_time,'%Y-%m-%d') > DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 day),'%Y-%m-%d')";
        return super.mysqlSpringJdbcBaseDao.queryForList(sql);
    }
}
