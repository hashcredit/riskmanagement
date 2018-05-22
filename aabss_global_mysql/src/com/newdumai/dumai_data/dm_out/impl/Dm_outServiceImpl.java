package com.newdumai.dumai_data.dm_out.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.newdumai.dumai_data.dm.Dm_sourceService;
import com.newdumai.dumai_data.dm.para.Dm_source_paraService;
import com.newdumai.dumai_data.dm_external_interface.auditOrder.AuditOrderService;
import com.newdumai.dumai_data.dm_out.Dm_outService;
import com.newdumai.global.service.impl.Dumai_sourceBaseServiceImpl;

@Service(value="dm_outService")
public class Dm_outServiceImpl extends Dumai_sourceBaseServiceImpl implements Dm_outService{
	
	@Autowired
	private Dm_source_paraService dm_source_paraService;
	@Autowired
	private Dm_sourceService dm_sourceService;
	@Autowired
	private AuditOrderService auditOrderService;
	
	/**
	 * 获取读脉数据源列表数据
	 * @select:[dm_source]
	 * @param request
	 * @param response
	 */
	@Override
	public Map<String,Object> getDmSources() {
		List<Map<String, Object>> dmSources = null ;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			dmSources = dm_sourceService.getDmSources();
			returnMap.put("message", "success");
		} catch (Exception e) {
			returnMap.put("message", e.getCause()+e.getMessage());
		}
		returnMap.put("data", dmSources);
		return returnMap;
	}
	
	/**
	 * 获取读脉数据源输出参数
	 * @select:[dm_source_para]
	 * @param request
	 * @param response
	 */
	@Override
	public Map<String,Object> getOutParasByDmSourceCode(@RequestBody Map<String,Object>params) {
		List<Map<String, Object>> dmSourceOutParas = null ;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			String dm_source_code = (String) params.get("dm_source_code");
			dmSourceOutParas = dm_source_paraService.getOutParasByDmSourceCode(dm_source_code);
			returnMap.put("message", "success");
		} catch (Exception e) {
			returnMap.put("message", e.getCause()+e.getMessage());
		}
		returnMap.put("data", dmSourceOutParas);
		return returnMap;	
	}
	
	/**
	 * 获取读脉数据源所有输出参数
	 * @select:[dm_source_para]
	 * @param request
	 * @param response
	 */
	@Override
	public Map<String,Object> getAllDmSourceOutParas() {
		Map<String, Object> dmSourceOutParas = null ;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			dmSourceOutParas = dm_source_paraService.getAllDmSourceOutParas();
			returnMap.put("message", "success");
		} catch (Exception e) {
			returnMap.put("message", e.getCause()+e.getMessage());
		}
		returnMap.put("data", dmSourceOutParas);
		return returnMap;	
	}
	
	/**
	 * 获取读脉数据源订单audit结果
	 * @select:[dm_source_para]
	 * @param request
	 * @param response
	 */
	@Override
	public Map<String,Object> getAuditOrderResult(Map<String,Object>params) {
		Map<String, Object> orderResult = null ;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			orderResult = auditOrderService.getAuditOrderResult(params);
			returnMap.put("message", "success");
		} catch (Exception e) {
			returnMap.put("message", e.getCause()+e.getMessage());
		}
		returnMap.put("data", orderResult);
		return returnMap;	
	}
	
	/**
	 * 获取读脉数据源订单audit结果
	 * @select:[dm_source_para]
	 * @param request
	 * @param response
	 */
	@Override
	public Map<String,Object> getAuditOrderResult(Map<String,Object>orderMap,Set<String>dm_source_codeSet) {
		Map<String, Object> orderResult = null ;
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			orderResult = auditOrderService.getAuditOrderResult(orderMap,dm_source_codeSet);
			returnMap.put("message", "success");
		} catch (Exception e) {
			returnMap.put("message", e.getCause()+e.getMessage());
		}
		returnMap.put("data", orderResult);
		return returnMap;	
	}

}
