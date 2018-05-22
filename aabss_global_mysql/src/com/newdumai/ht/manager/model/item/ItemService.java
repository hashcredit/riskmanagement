package com.newdumai.ht.manager.model.item;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

/**
 * 模型service
 * @author txy
 *
 */
public interface ItemService extends BaseService {

	/**
	 * 获取读脉源列表(下拉列表)
	 * @return
	 */
	List<Map<String, Object>> getDmSources();
	
	/**
	 * 获取指定读脉源输出参数列表(下拉列表)
	 * @param dm_source_code
	 * @return
	 */
	List<Map<String, Object>> getDmSourceOutParas(String dm_source_code);
	
	/**
	 * 获取类别列表(下拉列表)
	 * @return
	 */
	List<Map<String, Object>> getModel_classList();
}
