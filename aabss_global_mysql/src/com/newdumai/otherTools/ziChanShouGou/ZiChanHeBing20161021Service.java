package com.newdumai.otherTools.ziChanShouGou;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface ZiChanHeBing20161021Service extends BaseService {
	
	public List<Map<String, Object>> listInterface_source();

	List<Map<String, Object>> queryForList(String sql);
	Map<String, Object> queryForMap(String sql);
}
