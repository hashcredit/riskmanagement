package com.newdumai.loanFront.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanFront.AuditService;
import com.newdumai.loanFront.GenReportService;
import com.newdumai.loanFront.NewReportService;
import com.newdumai.loanFront.OrderInfoService;
import com.newdumai.sysmgr.BizFunctionSettingsService;
import com.newdumai.util.JsonToMap;

@Service("newReportService")
public class NewReportServiceImpl extends BaseServiceImpl implements NewReportService {
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private GenReportService genReportService;
	
	@Autowired
	private BizFunctionSettingsService bizFunctionSettingsService;
	
	@Override
	public Map<String, Object> genReport(Map<String, Object> param) {
		
		String code = (String) param.get("code");
		
		Map<String,Object> data = orderInfoService.findByCode(code);
		
		if(data==null){
			return null;
		}

		String xiaoShiPohto = genReportService.Report("1", code);
		if(!StringUtils.isEmpty(xiaoShiPohto)){
			Map<String,Object> photoData = JsonToMap.jsonToMap(xiaoShiPohto);
			data.put("xiaoshi-picture",photoData.get("PHOTO"));
		}
		
		String subEntityId = (String) param.get("sub_entity_id");
		String type_code = (String) param.get("type_code");
		
		if(bizFunctionSettingsService.hasFunctions(subEntityId, "loanfront_report4",type_code)){
			String bankValidation = genReportService.Report("4", code);
			if(!StringUtils.isEmpty(bankValidation)){
				Map<String,Object> result = JsonToMap.jsonToMap(bankValidation);
				data.put("bankValidation", result.get("data"));
			}
		}
		
		data.put("audit_result", auditService.findResultsByOrderCode(code));
		data.put("orders", orderInfoService.findTheSamePersonOrderIdsByCode(code));
		data.put("basicinfo", new Gson().fromJson(genReportService.Report("6", code),Object.class));
		data.put("fqzpf", getScoreResult(code, "1"));
		return data;
	}
	
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public Map<String,Object> getScoreResult(String orderCode,String type) {
		String sql = "select * from sys_score_result where fk_orderinfo_code=? and type=?";
		
		Map<String,Object> scoreResult = mysqlSpringJdbcBaseDao.queryForMap(sql, orderCode,type);
		if (scoreResult != null) {
			String result = (String) scoreResult.get("result");
			try {
				scoreResult.put("result", JsonToMap.gson2List(result));
			} catch (Exception e) {
				//异常忽略
				e.printStackTrace();
			}
			return scoreResult;
		}
		else return null;
	}
}
