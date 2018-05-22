package com.newdumai.zhangwu;

import com.newdumai.global.service.Zichan_BaseService;

import java.util.Map;

/**
 * @Author zgl.
 * @Date 2017/3/3 11:16
 */
public interface ZhangWuService extends Zichan_BaseService {

    /**
     * 根据订单编号查询帐物系统各分期明细
     *
     * @param map
     * @return
     */
    Map<String, Object> getStagesByOrderId(Map<String, Object> map);
}
