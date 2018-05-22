package com.newdumai.setting.score;

import java.util.List;
import java.util.Map;

public interface ScoreService {

	String list(Map<String, Object> map);

	boolean add(Map<String, Object> map);

	boolean delete(Map<String, Object> map);

	boolean update(Map<String, Object> request2Map);

	List<Map<String,Object>> getDmSources();
	
	List<Map<String,Object>> getDmSourceOutParas(String dm_source_code);

}
