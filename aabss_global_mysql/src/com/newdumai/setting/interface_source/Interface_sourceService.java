package com.newdumai.setting.interface_source;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface Interface_sourceService extends BaseService {
	public String list(Map<String, Object> para);
	public int add_Interface_sourceService(Map<String, Object> para);
	public String para_toUpdate(String interface_source_code);
	public int upadte_Interface_sourceService(Map<String, Object> para);
	public String toTestDS(String code);
	public String testDS(String sub_entity_id,String orderId,String interface_source_code, Map<String, Object> map);
	public String testDSOnly(String sub_entity_id,String interface_source_code, Map<String, Object> map);
	public int validateMobile(String sub_entity_id,String name,String card_num,String mobile);
	public int validateBankCard(String sub_entity_id,String name,String card_num, String bank_num);
	public String getXiaoShiPhoto(String sub_entity_id,String name,String card_num);
	public float getInterfacesCost(String interfacesCodes);
}
