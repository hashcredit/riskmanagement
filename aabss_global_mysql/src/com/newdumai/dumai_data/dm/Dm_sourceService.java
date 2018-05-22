package com.newdumai.dumai_data.dm;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.Dumai_sourceBaseService;

public interface Dm_sourceService extends Dumai_sourceBaseService {
	public String list(Map<String, Object> para);

	public int add_Interface_sourceService(Map<String, Object> para);

	public String para_toUpdate(String interface_source_code);

	public int upadte_Interface_sourceService(Map<String, Object> para);

	public String toTestDM(String code);

	public String testDM(String orderId,String sub_entity_id ,String dm_source_code, Map<String, Object> map);

	public List<Map<String, Object>> getSourceList(String code);

	public List<Map<String, Object>> interfaceSourceList(Map<String, Object> para);

	public boolean addSourceInterfaces(String dumaiCode, String[] sourceCodes);

	public void delSource(String code);

	public void updateSwitch(String code, String mode);
	
	public List<Map<String, Object>> getDmSources();

	/**
	 * 更新启用状态：如果已启用，则调用该方法停用；如果已停用，则调用该方法启用
	 * 
	 * @param parameter
	 * @zgl Dec 15, 2016 2:17:46 PM
	 */
	public void updateIsable(String parameter);
	
	/**
	 * 
	 * @param orderInfo
	 * @return
	 */
	public Map<String, Object> validateInputOrderAll(Map<String, Object> orderInfo);
	
	/**
	 * 
	 * @param orderInfo
	 * @param validateIndentifies
	 * @return
	 */
	public Map<String, Object> validateInputOrder(Map<String, Object> orderInfo,String... validateIndentifies);
}
