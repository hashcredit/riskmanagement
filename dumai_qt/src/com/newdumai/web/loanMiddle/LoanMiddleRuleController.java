package com.newdumai.web.loanMiddle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.newdumai.ht.manager.rule.rule.CustomRuleService;

/**
 * 一般用户-贷中 - 规则设定
 */
@Controller
public class LoanMiddleRuleController {
    @Autowired
    private CustomRuleService customRuleService;

    @RequestMapping(value = "/loanMiddle/toRuleList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toList(HttpServletRequest request) {
        return "loanMiddle/ruleList";
    }
    
//    @RequestMapping(value = "/loanMiddle/ruleList.do", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public String customList(HttpServletRequest request) {
//        String result = "";
//        try {
//            LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//            String subEntityId = login.getSub_entity_id();
//            Map<String, Object> map = WebUtil.request2Map(request);
//            map.put("sub_entity_id", subEntityId);
//            map.put("biz_range", "2");
//            result = customRuleService.getGroupRules(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//		return result;
//    }
//
//    @RequestMapping(value = "/rule/types.do", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public List<Map<String, Object>> ruleTypeInit(HttpServletRequest request, HttpServletResponse response) {
//        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//        String subEntityId = login.getSub_entity_id();
//        List<Map<String, Object>> types = customRuleService.getRuleEnabledTypesBySubEntityId(subEntityId);
//        return types;
//    }
//
//    @RequestMapping(value = "/rule/ruleGroups.do", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public List<Map<String, Object>> ruleGroupInit(HttpServletRequest request, HttpServletResponse response) {
//        String sysTypeCode = request.getParameter("sys_type_code");
//        return customRuleService.getRuleGroupsByTypeCode(sysTypeCode);
//    }
//
//    @RequestMapping(value = "/rule/customDisabeRule.do", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public JsonResult disabeRule(HttpServletRequest request) {
//        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//        String subEntityId = login.getSub_entity_id();
//        String fk_guize_code = request.getParameter("fk_guize_code");
//        String sys_company_type_code = request.getParameter("sys_company_type_code");
//        try {
//            return JsonResult.successResult(customRuleService.disableRule(fk_guize_code, sys_company_type_code));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JsonResult.failResult();
//        }
//    }
//
//    @RequestMapping(value = "/rule/customEnabeRule.do", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public JsonResult enabeRule(HttpServletRequest request) {
//        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//        String subEntityId = login.getSub_entity_id();
//
//        String fk_guize_code = request.getParameter("fk_guize_code");
//        String sys_company_type_code = request.getParameter("sys_company_type_code");
//        try {
//            return JsonResult.successResult(customRuleService.enableRule(fk_guize_code, sys_company_type_code));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JsonResult.failResult();
//        }
//    }
}
