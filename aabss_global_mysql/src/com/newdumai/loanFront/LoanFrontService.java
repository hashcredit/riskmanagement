package com.newdumai.loanFront;

import java.util.List;
import java.util.Map;

import com.newdumai.global.service.BaseService;

public interface LoanFrontService extends BaseService{
	
	public String list(Map<String, Object> map);
	
	/**
	 * 获取全部业务类型
	 * @return
	 */
	public String getHeadtype();
	
	public boolean deleteByCode(String code);
}
