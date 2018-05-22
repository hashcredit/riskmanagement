package com.newdumai.dumai_data.dm.para;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.Dumai_sourceBaseService;

public interface Dm_source_paraService extends Dumai_sourceBaseService {
	public String list(Map<String, Object> para);

	public int add_Interface_sourceService_para(Map<String, Object> para);

	public String para_toUpdate(String interface_source_code);

	public int upadte_Interface_sourceService_para(Map<String, Object> para);

	public List<Map<String, Object>> getAllParams(String interface_source_code, String type);

	public void deleteParamsMatch(String paramCode);

	public void insertParamsMatch(List<Map<String, Object>> dataParams);
	
	/**
	 * 保存参数匹配，删除旧的数据，插入新数据
	 * @param paramCode dm_source_para_code
	 * @param dataParams 读脉参数code和第三方数据源参数code对应列表
	 */
	public void saveParamsMatch(String paramCode, List<Map<String, Object>> dataParams);

	public List<Map<String, Object>> getSourceParamList(String paramCode);

	/**
	 * 根据code删除数据
	 * 
	 * @param code
	 * @return
	 * @zgl Dec 13, 2016 5:16:42 PM
	 */
	public int delByCode(String code);

	/**
	 * 对外接口：获取某个读脉源的所有输出参数
	 * @param dm_source_code
	 * @return
	 */
	public List<Map<String, Object>> getOutParasByDmSourceCode(String dm_source_code);

	/**
	 * 对外接口：获取所有读脉源的所有输出参数
	 * @param 
	 * @return
	 */
	public Map<String, Object> getAllDmSourceOutParas();
}
