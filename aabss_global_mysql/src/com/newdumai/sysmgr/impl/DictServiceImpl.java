package com.newdumai.sysmgr.impl;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.sysmgr.DictService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/2/15.
 */
@Service("dictService")
public class DictServiceImpl extends BaseServiceImpl implements DictService {
    @Override
    public List<Map<String, Object>> findAllList(String type) {
        String sql = "select * from sys_dict where type = ?";
        return super.mysqlSpringJdbcBaseDao.queryForList(sql, type);
    }
}
