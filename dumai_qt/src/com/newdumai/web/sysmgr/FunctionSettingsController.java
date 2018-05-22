package com.newdumai.web.sysmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.newdumai.setting.type.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.newdumai.global.vo.JsonResult;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.jinjian.InputOrderService;
import com.newdumai.sysmgr.BizFunctionSettingsService;
import com.newdumai.sysmgr.FunctionSettingsService;
import com.newdumai.web.util.WebUtil;

/**
 * 一般用户-系统管理-功能设定
 */
@Controller
public class FunctionSettingsController {
	
	@Autowired
	private FunctionSettingsService functionSettingsService;
	
	@Autowired
	private BizFunctionSettingsService bizFunctionSettingsService;
	@Autowired
	private TypeService typeService;

	@RequestMapping("sysmgr/funsettings/toPage.do")
	public String toPage(){
		return "sysmgr/funsettings/page";
	}
	
	@RequestMapping("sysmgr/funsettings/toIpConfig.do")
	public String toIpConfig(){
		return "sysmgr/funsettings/IpConfig";
	}
	
	@Autowired
	private InputOrderService inputOrderService;
	
	@RequestMapping("sysmgr/funsettings/load.do")
	@ResponseBody
	public JsonResult load(HttpSession session){
		LoginVo loginVo = (LoginVo) session.getAttribute("login");
		try{
			return JsonResult.successResult(functionSettingsService.getFunctionSettingsBySubentityId(loginVo.getSub_entity_id()));
		}catch(Exception e){
			e.printStackTrace();
			return JsonResult.failResult(e.getMessage());
		}
	}
	
	@RequestMapping("sysmgr/funsettings/save.do")
	@ResponseBody
	public JsonResult save(HttpServletRequest request,HttpSession session){
		Map<String,Object> param = WebUtil.request2Map(request);
		String whiteIps = (String) param.remove("white_ips");
		
		String funtionSettings = new Gson().toJson(param);
		LoginVo loginVo = (LoginVo) session.getAttribute("login");
		Map<String,Object> updateParam = new HashMap<String, Object>();
		updateParam.put("Function_settings", funtionSettings);
		updateParam.put("white_ips", whiteIps);
		
		try {
			boolean flag = functionSettingsService.updateBySubentityId(updateParam, loginVo.getSub_entity_id());
			return JsonResult.successResult(flag);
			
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "sysmgr/funsettings/type.do",method =RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getTypeData(HttpServletRequest request,
			HttpServletResponse response) {
		LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
		return typeService.getTypesBySubEntityId(loginVo.getSub_entity_id());
	}
	
	@RequestMapping("sysmgr/funsettings/bizLoad.do")
	@ResponseBody
	public JsonResult bizLoad(HttpServletRequest request,HttpSession session){
		LoginVo loginVo = (LoginVo) session.getAttribute("login");
		try{
			String typeCode = request.getParameter("type_code");
			return JsonResult.successResult(bizFunctionSettingsService.getFunctionSettingsBySubentityId(loginVo.getSub_entity_id(),typeCode));
		}catch(Exception e){
			e.printStackTrace();
			return JsonResult.failResult(e.getMessage());
		}
	}

	@RequestMapping("sysmgr/funsettings/bizSave.do")
	@ResponseBody
	public JsonResult bizSave(HttpServletRequest request) {
		Map<String, Object> param = WebUtil.request2Map(request);
		String code = (String) param.remove("code");
		String loanfront_rule = (String) param.remove("loanfront_rule");
		String report_para = new Gson().toJson(param);
		Map<String, Object> updateParam = new HashMap<String, Object>();
		updateParam.put("rule_model", loanfront_rule);
		updateParam.put("report_para", report_para);
		try {
			boolean flag = bizFunctionSettingsService.update(updateParam, code);
			return JsonResult.successResult(flag);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failResult(e.getMessage());
		}
	}
	
	@RequestMapping("sysmgr/funsettings/dataViewLoad.do")
	@ResponseBody
	public List<Map<String,Object>> dataViewLoad(HttpServletRequest request,HttpSession session){
		LoginVo loginVo = (LoginVo) session.getAttribute("login");
		Map<String, Object> param = WebUtil.request2Map(request);
		String typeCode = (String) param.get("type_code");
		List<Map<String,Object>> list = bizFunctionSettingsService.getViewDataSettings(loginVo.getSub_entity_id(),typeCode);
		return list;
	}
	@RequestMapping(value="sysmgr/funsettings/dataViewSave.do",method =RequestMethod.POST)
	@ResponseBody
	public boolean dataViewSave(HttpServletRequest request,HttpSession session){
		LoginVo loginVo = (LoginVo) session.getAttribute("login");
		Map<String, Object> param = WebUtil.request2Map(request);
		String typeCode = (String) param.get("type_code");
		String codes = (String) param.get("code");
		return bizFunctionSettingsService.saveViewDataSettings(loginVo.getSub_entity_id(),typeCode,codes);
	}

	/**
	 * 新版业务类型规则模型选择
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("sysmgr/funsettings/ruleOrModel.do")
	@ResponseBody
	public String ruleOrModel(HttpServletRequest request) {
		String result = "0";
		LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
		String subEntityId = loginVo.getSub_entity_id();
		String type_code =  request.getParameter("type_code");
		String rule_model =  request.getParameter("rule_model");
		try {
			int flag = bizFunctionSettingsService.updateRuleOrModel(subEntityId, type_code, rule_model);
			if (1 == flag) {
				result = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
