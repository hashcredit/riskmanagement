package com.newdumai.setting.interface_source.para;

import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface Interface_sourceService_para extends BaseService {
	public String list(Map<String, Object> para);

	public int add_Interface_sourceService_para(Map<String, Object> para);

	public String para_toUpdate(String interface_source_code);

	public int upadte_Interface_sourceService_para(Map<String, Object> para);
}
