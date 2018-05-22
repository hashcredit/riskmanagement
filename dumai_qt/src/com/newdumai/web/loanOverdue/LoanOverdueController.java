package com.newdumai.web.loanOverdue;

import com.google.gson.Gson;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.loanOverdue.LoanOverdueService;
import com.newdumai.sysmgr.DictService;
import com.newdumai.web.util.WebUtil;
import com.newdumai.zhangwu.ZhangWuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 逾期Controoler
 */
@Controller
@RequestMapping(value = "/loanOverdue")
public class LoanOverdueController {

    @Autowired
    ZhangWuService zhangWuService;
    @Autowired
    LoanOverdueService loanOverdueService;
    @Autowired
    DictService dictService;

    /**
     * 跳转到逾期列表页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.GET)
    public String toList(HttpServletRequest request, HttpServletResponse response) {
        return "loanOverdue/list";
    }

    /**
     * 获取逾期列表详细数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = login.getSub_entity_id();
        String json = loanOverdueService.getOverdueInfo(sub_entity_id, WebUtil.request2Map(request));
        return json;
    }

    /**
     * 跳到分期明细列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toStages.do")
    public String toStages() throws Exception {
        return "zhangwu/list";
    }

    /**
     * 分期明细详细数据
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "stagesList.do", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> list(HttpServletRequest request) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = login.getSub_entity_id();
        //仅爱钱帮进行帐务查询
        Map<String, Object> json = zhangWuService.getStagesByOrderId(WebUtil.request2Map(request));
        return json;
        //TODO 仅爱钱帮有帐务查询，目前测试都从花花查
//        if ("38b562a1-020c-4e3c-883b-95cbe680cce9".equals(sub_entity_id)) {
//        } else {
//            return null;
//        }
    }

    /**
     * 跳转到催收跟进列表页面
     *
     * @return
     */
    @RequestMapping(value = "toFollowList.do", method = RequestMethod.GET)
    public String toFollowList(HttpServletRequest request) {
        String flag = request.getParameter("flag");
        if ("1".equals(flag)) {
            return "loanOverdue/follow";//跳转到催收记录页面
        }
        return "loanOverdue/followList";
    }

    /**
     * 跟进详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "followList.do", method = {RequestMethod.POST})
    @ResponseBody
    public String followList(HttpServletRequest request) {
        String fk_orderinfo_code = request.getParameter("code");
        String json = loanOverdueService.getFollowDetail(fk_orderinfo_code);
        return json;
    }

    /**
     * 保存跟进结果
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "saveFollow.do", method = {RequestMethod.POST})
    @ResponseBody
    public String saveFollow(HttpServletRequest request) {
        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
        String userCode = loginVo.getCode();
        Map<String, Object> map = WebUtil.request2Map(request);
        map.put("charger", userCode);
        loanOverdueService.saveFollow(map);
        return "1";
    }

    /**
     * 跳转到升级处理列表页面
     *
     * @return
     */
    @RequestMapping(value = "toHandleList.do", method = RequestMethod.GET)
    public String toHandleList(HttpServletRequest request) {
        String flag = request.getParameter("flag");
        if ("1".equals(flag)) {
            return "loanOverdue/handle";//跳到升级处理页面
        }
        return "loanOverdue/handleList";
    }


    /**
     * 升级处理列表数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "handleList.do", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String handleList(HttpServletRequest request, HttpServletResponse response) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = login.getSub_entity_id();
        String json = loanOverdueService.getHandleListInfo(sub_entity_id, WebUtil.request2Map(request));
        return json;
    }

    /**
     * 保存审核结果
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "saveHandle.do", method = RequestMethod.POST)
    @ResponseBody
    public String saveHandle(HttpServletRequest request, HttpServletResponse response) {
        LoginVo login = (LoginVo) request.getSession().getAttribute("login");
        String userCode = login.getCode();
        String json = loanOverdueService.saveHandle(userCode, WebUtil.request2Map(request));
        return json;
    }

    /**
     * 跟进时的客户标签数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "labels.do", method = RequestMethod.GET)
    @ResponseBody
    public String getLabel(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> json = dictService.findAllList("overdue_handle");
        return new Gson().toJson(json);
    }

}
