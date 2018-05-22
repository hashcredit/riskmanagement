package com.newdumai.web.auditing;

import com.newdumai.global.vo.JsonResult;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.ht.auditing.task.AuditingTaskService;
import com.newdumai.sysmgr.UserService;
import com.newdumai.web.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 电核任务列表页
 *
 * @author zgl
 */
@Controller
public class AuditingTaskController {

    @Autowired
    AuditingTaskService auditingTaskService;

    @Autowired
    UserService userService;

    /**
     * 跳到电核任务列表页
     *
     * @param request
     * @param response
     * @zgl 2016年11月23日 下午5:58:14
     */
    @RequestMapping(value = "/auditingTask/toList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String toList(HttpServletRequest request, HttpServletResponse response) {
        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
        if (null == loginVo) {
            return "forward:/htTologin.do";
        }
        String isLeader = loginVo.getIsLeader();
        if ("4".equals(isLeader)) {//电核管理员
            return "forward:/auditingTask/toDistribution.do";
        } else {//普通电核员
            return "auditing/auditingTaskList";
        }
    }

    /**
     * 跳转到分配电核任务页面
     *
     * @param request
     * @param response
     * @return
     * @zgl 2017年5月4日 下午2:18:14
     */
    @RequestMapping(value = "/auditingTask/toDistribution.do")
    public String toDistribution(HttpServletRequest request, HttpServletResponse response) {
        return "auditing/distributionTask";
    }

    /**
     * 跳转到订单查询页面
     *
     * @param request
     * @param response
     * @return
     * @zgl 2017年5月4日 下午2:18:14
     */
    @RequestMapping(value = "/auditingTask/toOrderList.do")
    public String toOrderList(HttpServletRequest request, HttpServletResponse response) {
        return "auditing/distributionTaskList";
    }

    /**
     * 电核管理员分配任务、查询订单页面数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/auditingTask/auditingTaskList.do", method = RequestMethod.POST)
    @ResponseBody
    public void auditingTaskList(HttpServletRequest request, HttpServletResponse response) {
        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = loginVo.getSub_entity_id();
        Map<String, Object> map = WebUtil.request2Map(request);
        map.put("sub_entity_id", sub_entity_id);
        String json = auditingTaskService.getDhTaskList(map);
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter pw = response.getWriter();
            if (json != null && !json.isEmpty()) {
                pw.print(json);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
        }
    }

    /**
     * 普通电核员获取任务列表
     *
     * @param request
     * @param response
     * @zgl Dec 2, 2016 5:43:12 PM
     */
    @RequestMapping(value = "/auditingTask/auditingList.do", method = RequestMethod.POST)
    @ResponseBody
    public void auditingList(HttpServletRequest request, HttpServletResponse response) {
        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
        String userCode = loginVo.getCode();
        Map<String, Object> map = WebUtil.request2Map(request);
        map.put("sys_user_code", userCode);
        map.put("is_finish", "0");
        String json = auditingTaskService.getDhTaskList(map);
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter pw = response.getWriter();
            if (json != null && !json.isEmpty()) {
                pw.print(json);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
        }
    }

    /**
     * 保存电核josn
     *
     * @param request
     * @zgl Dec 2, 2016 5:44:29 PM
     */
    @RequestMapping(value = "/auditingTask/saveJson.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult saveJson(HttpServletRequest request) {
        String result = "";
        // 根据电核结果，更新评分结果，并重新计算评分
        String str = auditingTaskService.updateScoreResult(WebUtil.request2Map(request));
        if ("success".equals(str)) {
            result = str;
        }
        return JsonResult.successResult(null, result);
    }

    /**
     * 跳到电核任务页面
     *
     * @return
     */
    @RequestMapping(value = "/auditingTask/toDhTask.do", method = RequestMethod.GET)
    public String toDhTask(HttpServletRequest request) {
        return "auditing/auditingTask";
    }

    /**
     * 查看电核任务结果页面
     *
     * @return
     */
    @RequestMapping(value = "/auditingTask/toDhTaskResult.do", method = RequestMethod.GET)
    public String toDhTaskResult() {
        return "auditing/viewTaskResult";
    }

    /**
     * 电核项信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/auditingTask/dhTask.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> dhTask(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        Map<String, Object> json = auditingTaskService.getDhItemsByCode(code);
        return json;
    }

    /**
     * 电核增项信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/auditingTask/additional.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> additional(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        List<Map<String, Object>> additionalMapList = auditingTaskService.getAdditional(code);
        return additionalMapList;
    }

    /**
     * 获取电核员信息(只能获取自己商户下的电核员)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/auditingTask/getDhy.do", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public List<Map<String, Object>> getDhy(HttpServletRequest request, HttpServletResponse response) {
        LoginVo loginVo = (LoginVo) request.getSession().getAttribute("login");
        String sub_entity_id = loginVo.getSub_entity_id();
        List<Map<String, Object>> json = userService.getSubUsers("5", sub_entity_id);// 4 电核管理员，5 电核员
        return json;
    }

    /**
     * 更新电核任务对应的电核员
     *
     * @param request
     */
    @RequestMapping(value = "/auditingTask/saveTaskUser.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult saveTaskUser(HttpServletRequest request) {
        Map<String, Object> map = WebUtil.request2Map(request);
        JsonResult json = null;
        // 保存电核任务的电核员
        int result = auditingTaskService.saveTaskUser(map);
        if (result > 0) {
            json = JsonResult.successResult(result);
        } else {
            json = JsonResult.failResult();
        }
        return json;
    }


    /**
     * 电核任务-查看个人详细信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/auditingTask/toReport.do", method = RequestMethod.GET)
    public String toRepot(HttpServletRequest request, HttpServletResponse response) {
        String orderId = request.getParameter("orderId");
        request.setAttribute("orderId", orderId);
        return "auditing/report";
    }
}
