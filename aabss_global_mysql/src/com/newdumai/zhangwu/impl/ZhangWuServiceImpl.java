package com.newdumai.zhangwu.impl;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.Zichan_BaseServiceImpl;
import com.newdumai.zhangwu.ZhangWuService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zgl.
 * @Date 2017/3/3 11:16
 */
@Service("zhangWuService")
public class ZhangWuServiceImpl extends Zichan_BaseServiceImpl implements ZhangWuService {
    @Override
    public Map<String, Object> getStagesByOrderId(Map<String, Object> map) {
        String orderId = (String) map.get("orderId");
        String sql = "select * from inst_billinfo where status != 4 and orderId = '" + orderId + "' order by instorder,repayno " + getLimitUseAtSelectPage(map);
        String sqlTotal = "SELECT count(*) FROM inst_billinfo where status != 4 and orderId = '" + orderId + "'";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", mysqlSpringJdbcBaseDao.executeSelectSqlInt(sqlTotal));
        data.put("rows", mysqlSpringJdbcBaseDao.queryForList(sql));
//        data.put("tt", map);
        return data;
    }
}
