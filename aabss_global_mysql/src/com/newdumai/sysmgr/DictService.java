package com.newdumai.sysmgr;

import com.newdumai.global.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/2/15.
 */
public interface DictService extends BaseService {
    List<Map<String, Object>> findAllList(String type);
}
