package com.newdumai.commonDataChannel;

import java.util.List;
import java.util.Map;

/**
 * 
 * @Description 公共数据通道
 * @Params
 * @author MH
 * @date 2016年9月28日 上午11:06:03
 */
public interface CommonDataChannelService {

	public List<Map<String, Object>> queryListBySql(Map<String, Object> params);

	public Map<String, Object> queryMapBySql(Map<String, Object> params);

}
