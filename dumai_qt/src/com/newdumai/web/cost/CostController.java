package com.newdumai.web.cost;

import com.newdumai.cost.CompanyOrderLogService;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.web.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 对帐信息Controller
 * <p>
 * Created by zhang on 2017/2/16.
 */
@Controller
@RequestMapping(value = "/cost/")
public class CostController {

    @Autowired
    CompanyOrderLogService companyOrderLogService;

    /**
     * 跳到对帐明细页面
     */
    @RequestMapping(value = "toList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toList(HttpServletRequest request) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = login.getSub_entity_id();
        request.setAttribute("sub_entity_id", sub_entity_id);
        return "cost/list";
    }

    /**
     * 对帐明细
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "list.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String list(HttpServletRequest request) {
        return companyOrderLogService.list("company_order_log", WebUtil.request2Map(request));
    }

    /**
     * 总消费金额
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "totalCost.do", method = RequestMethod.POST)
    @ResponseBody
    public double totalCost(HttpServletRequest request) {
        return companyOrderLogService.getTotalCost(WebUtil.request2Map(request));
    }

    /**
     * 跳到测试页面
     */
    @RequestMapping(value = "test.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String test(HttpServletRequest request) {
        return "cost/test";
    }
}
