package com.newdumai.interfaces.iqianbang.huahua;

import java.util.Map;

/**
 * 花花接口的Service
 */
public interface RiskService {

	Map<String, Object> doService(String command, Map<String, Object> param);
}
