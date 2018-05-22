package com.newdumai.web.auditing;

import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.dumai_data.dm_3rd_interface.util.CommonUtil;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.jinjian.ValidateOrderService;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.TimeHelper;
import com.newdumai.web.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 个人身份验证和银行流水、鉴权不通过查询
 * <p>
 * Created by zhang on 17-7-3.
 */
@Controller
public class AuthenticationController {

    @Autowired
    Dm_3rd_interfaceService dm_3rd_interfaceService;

    @Autowired
    ValidateOrderService validateOrderService;

    @RequestMapping(value = "/auth/toAuth.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toAuthenticationAndBandFlow(HttpServletRequest request) {
        return "/auditing/authenticationAndBankFlow";
    }


    /**
     * 身份验证及银行流水
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/auth/validate.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> authenticationAndBandFlow(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> map = WebUtil.request2Map(request);
        String result = dm_3rd_interfaceService.testDS("5687256d-ee6b-486c-8741-fc05af7533df", map);

        if (StringUtils.isNotEmpty(result)) {
            Map<String, Object> strMap = JsonToMap.gson2Map(result);
            Map<String, Object> data = (Map<String, Object>) strMap.get("data");
            String statusCode = (String) data.get("statusCode");
            String statusMsg = (String) data.get("statusMsg");
            resultMap.put("status", statusMsg);
            if ("2005".equals(statusCode)) {//身份一致，查询银行流水
                resultMap.put("bankFlow", getBankFlow(map));
            }
        } else {
            resultMap.put("status", "数据库中未查得");
        }

        return resultMap;
    }

    /**
     * 获取银行流水
     *
     * @param map
     * @return
     */
    private List<Map<String, Object>> getBankFlow(Map<String, Object> map) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date startDate = TimeHelper.getFrontDate(date, 183);
        //补全输入参数
        map.put("beginDate", sdf.format(startDate));
        map.put("endDate", sdf.format(date));

        String resultFlow = dm_3rd_interfaceService.testDS("d534a49f-deea-4b71-8e22-e3f0a715b00b", map);
        boolean successFlow = CommonUtil.checkBaseCondition(resultFlow, "resCode=='0000' && data.statusCode=='2012'");
        if (successFlow && StringUtils.isNotEmpty(resultFlow)) {
            Map<String, Object> strMap = JsonToMap.gson2Map(resultFlow);
            Map<String, Object> flowData = (Map<String, Object>) strMap.get("data");
            List<Map<String, Object>> flowDataResult = (List<Map<String, Object>>) flowData.get("result");
            Collections.sort(flowDataResult, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    if (o1.get("transTime") == null && o2.get("transTime") == null)
                        return 0;
                    if (o1.get("transTime") == null)
                        return -1;
                    if (o2.get("transTime") == null)
                        return 1;
                    return (o2.get("transTime").toString()).compareTo(o1.get("transTime").toString());
                }
            });
            return flowDataResult;
        } else {
            return null;
        }
    }


    /**
     * 跳转鉴权结果页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/auth/toList.do", method = { RequestMethod.POST,RequestMethod.GET })
    public String toRule(HttpServletRequest request) {
        return "auditing/authenticationResult";
    }

    /**
     * 获取鉴权结果数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/auth/list.do", method = { RequestMethod.POST,RequestMethod.GET })
    public @ResponseBody String list(HttpServletRequest request) {
        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = loginVo.getSub_entity_id();
        Map<String, Object> map = WebUtil.request2Map(request);
        map.put("sub_entity_id", sub_entity_id);
        return validateOrderService.getList(map);
    }

}
