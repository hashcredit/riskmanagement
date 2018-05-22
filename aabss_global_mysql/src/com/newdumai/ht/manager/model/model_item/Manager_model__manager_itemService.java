package com.newdumai.ht.manager.model.model_item;

import java.util.Map;

public interface Manager_model__manager_itemService{

	String listAll(String string, Map<String, Object> request2Map);
	
	boolean delete(String tableName, Map<String, Object> map) ;
	
	boolean addModels(Map<String, Object> params);

	String get_item_listAll(Map<String, Object> params);

	boolean update_seperate_box(String tableName, Map<String, Object> map);
	
}
