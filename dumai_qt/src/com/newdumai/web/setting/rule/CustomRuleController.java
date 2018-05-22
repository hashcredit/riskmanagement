package com.newdumai.web.setting.rule;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newdumai.global.vo.JsonResult;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.ht.manager.rule.rule.CustomRuleService;
import com.newdumai.web.util.WebUtil;

/**
 * 一般用户-贷前审核-规则设定
 */
@Controller
public class CustomRuleController {
    @Autowired
    private CustomRuleService customRuleService;
    
    /**
     * 跳转贷前规则集合
     * @param request
     * @return
     */
    @RequestMapping(value = "/rule/toLoanFrontRuleList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toLoanFrontList(HttpServletRequest request) {
        return "setting/rule/loanFrontRuleList";
    }
    
    /**
     * 获取贷前规则集合
     * @param request
     * @return
     */
    @RequestMapping(value = "/rule/loanFrontRuleList.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String loanFrontList(HttpServletRequest request) {
        String result = "";
        try {
            LoginVo login = (LoginVo) request.getSession().getAttribute("login");
            String subEntityId = login.getSub_entity_id();
            Map<String, Object> map = WebUtil.request2Map(request);
            map.put("sub_entity_id", subEntityId);
            result = customRuleService.getGroupRules(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
    }
    
    /**
     * 跳转贷中规则集合
     * @param request
     * @return
     */
    @RequestMapping(value = "/rule/toLoanMiddleRuleList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toLoanMiddleRuleList(HttpServletRequest request) {
        return "setting/rule/loanMiddleRuleList";
    }
    
    /**
     * 获取贷中规则集合
     * @param request
     * @return
     */
    @RequestMapping(value = "/rule/loanMiddleRuleList.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String loanMiddleList(HttpServletRequest request) {
        String result = "";
        try {
            LoginVo login = (LoginVo) request.getSession().getAttribute("login");
            String subEntityId = login.getSub_entity_id();
            Map<String, Object> map = WebUtil.request2Map(request);
            map.put("sub_entity_id", subEntityId);
            result = customRuleService.getGroupRules(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
    }
    
    /**
     * 获取业务类型
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "/rule/types.do", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public List<Map<String, Object>> ruleTypeInit(HttpServletRequest request, HttpServletResponse response) {
//        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//        String subEntityId = login.getSub_entity_id();
//        List<Map<String, Object>> types = customRuleService.getRuleEnabledTypesBySubEntityId(subEntityId);
//        return types;
//    }
    
    @RequestMapping(value = "/rule/ruleGroups.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> ruleGroupInit(HttpServletRequest request, HttpServletResponse response) {
        String sysTypeCode = request.getParameter("sys_type_code");
        return customRuleService.getRuleGroupsByTypeCode(sysTypeCode);
    }

    @RequestMapping(value = "/rule/customDisabeRule.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult disabeRule(HttpServletRequest request) {
        String fk_guize_code = request.getParameter("fk_guize_code");
        String sys_company_type_code = request.getParameter("sys_company_type_code");
        String rule_model = request.getParameter("rule_model");
        try {
            return JsonResult.successResult(customRuleService.disableRule(fk_guize_code, sys_company_type_code, rule_model));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failResult();
        }
    }

    @RequestMapping(value = "/rule/customEnabeRule.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult enabeRule(HttpServletRequest request) {
        String fk_guize_code = request.getParameter("fk_guize_code");
        String sys_company_type_code = request.getParameter("sys_company_type_code");
        String rule_model = request.getParameter("rule_model");
        try {
            return JsonResult.successResult(customRuleService.enableRule(fk_guize_code, sys_company_type_code, rule_model));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failResult();
        }
    }

    /**
     * 前台贷前规则全部启停用
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/rule/modifyAllRules.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String modifyAllRules(HttpServletRequest request) {
        String flag = customRuleService.modifyAllRules(WebUtil.request2Map(request));
        return flag;
    }

}
