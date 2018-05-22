package com.newdumai.web.setting.model;

import com.newdumai.global.vo.LoginVo;
import com.newdumai.ht.manager.rule.rule.CustomModelService;
import com.newdumai.web.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 前台模型管理
 * <p>
 * Created by zhang on 2017/4/24.
 */
@Controller
public class CustomModelController {

    @Autowired
    CustomModelService customModelService;

    @RequestMapping(value = "/model/toLoanFrontModelList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toLoanFrontList(HttpServletRequest request) {
        return "setting/model/loanFrontModelList";
    }


    /**
     * 获取贷前鉴权规则，模型规则，模型
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/model/getModelList.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String loanFrontList(HttpServletRequest request) {
        String result = "";
        try {
            LoginVo login = (LoginVo) request.getSession().getAttribute("login");
            String subEntityId = login.getSub_entity_id();
            Map<String, Object> map = WebUtil.request2Map(request);
            map.put("sub_entity_id", subEntityId);
            result = customModelService.getModels(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 前台贷中启用模型：一个业务类型仅有一个启用模型，启用时会自动删除其它模型
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/model/enableModel.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String enableModel(HttpServletRequest request) {
        String result = "0";
        try {
            String manager_model_code = request.getParameter("manager_model_code");
            String sys_company_type_code = request.getParameter("sys_company_type_code");
            int rs = customModelService.enableModel(sys_company_type_code, manager_model_code);
            if (1 == rs) {
                result = "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/model/disableModel.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String disableModel(HttpServletRequest request) {
        String result = "0";
        try {
            String manager_model_code = request.getParameter("manager_model_code");
            String sys_company_type_code = request.getParameter("sys_company_type_code");
            int rs = customModelService.disableModel(sys_company_type_code, manager_model_code);
            if (1 == rs) {
                result = "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
