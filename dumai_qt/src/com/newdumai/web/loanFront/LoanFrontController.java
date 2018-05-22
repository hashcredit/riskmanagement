package com.newdumai.web.loanFront;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.newdumai.setting.type.TypeService;
import com.newdumai.util.JsonToMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newdumai.global.vo.JsonResult;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.loanFront.LoanFrontService;
import com.newdumai.web.util.WebUtil;

@Controller
public class LoanFrontController {
    @Autowired
    private LoanFrontService loanFrontService;
    @Autowired
    private TypeService typeService;

    @RequestMapping(value = "/loanFront/toLoanFront.do", method ={ RequestMethod.POST, RequestMethod.GET})
    public String toList(HttpServletRequest request, HttpServletResponse response) {
        return "loanFront/list";
    }

    /**
     * 业务类型数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/loan/headtype.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> headtype(HttpServletRequest request, HttpServletResponse response) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String subEntityId = login.getSub_entity_id();
        String typesString = (String) request.getSession().getAttribute(subEntityId + "Types");
        List<Map<String, Object>> types = null;
        if (StringUtils.isNotEmpty(typesString)) {
            types = JsonToMap.gson2List(typesString);
        } else {
            types = typeService.getTypesBySubEntityId(subEntityId);
            request.getSession().setAttribute(subEntityId + "Types", new Gson().toJson(types));
        }
        return types;
    }

    /**
     * 列表页面数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/loanFront/list.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String subEntityId = login.getSub_entity_id();
        Map<String, Object> param = WebUtil.request2Map(request);
        param.put("sub_entity_id", subEntityId);
        String json = loanFrontService.list(param);
        return json;
    }

    /**
     * 删除订单(逻辑删除)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/loanFront/delete.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public JsonResult delete(HttpServletRequest request, HttpServletResponse response) {
        try {
            loanFrontService.deleteByCode(request.getParameter("code"));
            return JsonResult.successResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failResult();
        }
    }

}
