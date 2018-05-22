//package com.newdumai.web.loanFront;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.ObjectUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.shiro.util.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.google.gson.Gson;
//import com.newdumai.global.vo.JsonResult;
//import com.newdumai.global.vo.LoginVo;
//import com.newdumai.ht.manager.model.report.ReportService;
//import com.newdumai.loanFront.AuditService;
//import com.newdumai.loanFront.GenReportService;
//import com.newdumai.loanFront.OrderInfoService;
//import com.newdumai.sysmgr.BizFunctionSettingsService;
//import com.newdumai.util.DictUtils;
//import com.newdumai.util.JsonToMap;
//import com.newdumai.web.util.WebUtil;
//
///**
// * 报告controller<br/>
// * <li>报告首页</li>
// * <li>报告详细数据</li>
// *
// * @author 岳晓
// */
//@Controller
//public class ReportController {
//
//    @Autowired
//    private OrderInfoService orderInfoService;
//
//    @Autowired
//    private AuditService auditService;
//
//    @Autowired
//    private GenReportService genReportService;
//
//    @Autowired
//    ReportService reportService;
//
//    @Autowired
//    private BizFunctionSettingsService bizFunctionSettingsService;
//
//    @RequestMapping(value = "/report/toReport.do", method = RequestMethod.GET)
//    public String toRepot(HttpServletRequest request, HttpServletResponse response) {
//        String orderId = request.getParameter("orderId");
//        request.setAttribute("orderId", orderId);
//        return "loanFront/report";
//    }
//
//    /**
//     * 获取报告详细数据
//     * 1 订单 人员表
//     * 2 小视图片
//     * 3 同住人信息
//     * 4 同一人其他订单列表
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "/report/report.do", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResult report(HttpServletRequest request, HttpServletResponse response) {
//
//    	//LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//
//        String code = request.getParameter("code");
//
//        Map<String, Object> data = orderInfoService.findByCode(code);
//
//        if (CollectionUtils.isEmpty(data)) {
//        	return JsonResult.failResult("数据不存在");
//       	}
//
//        //String type_code = (String) data.get("thetype");
//        String xiaoShiPohto = genReportService.Report("1", code);
//        if (!StringUtils.isEmpty(xiaoShiPohto)) {
//
//			List<Object> photoDatas = new Gson().fromJson(xiaoShiPohto, List.class);
//        	if(photoDatas.size()>0){
//        		List<Map<String, Object>> photoDatas0 = (List<Map<String, Object>>) photoDatas.get(0);
//	        	for(Map<String, Object> photoData : photoDatas0){
//	        		if("PHOTO".equals(photoData.get("name"))){
//	        			data.put("xiaoshi-picture", photoData.get("value"));
//	        		}
//	        	}
//        	}
//        }
//
//        data.put("dictionary",DictUtils.getDictList("order"));
//        //反欺诈评分
//        Map<String, Object> cheatMap = reportService.getResult(code, "1");
//        data.put("cheat",cheatMap);
//        //信用评分
//        Map<String, Object> creditMap = reportService.getResult(code, "2");
//        data.put("credit",creditMap);
//        //反欺诈规则
//        data.put("audit_result", auditService.findResultsByOrderCode(code));
//        //右上角下拉框
//        data.put("orders", orderInfoService.findTheSamePersonOrderIdsByCode(code));
//
//        //详细数据列表
//        data.put("detailInterfaces", reportService.getDetailInterfaces(data));
//
//        //基本信息
//        //Map<String,Object> basicinfoMap = new HashMap<String, Object>();
//        String basicinfo = genReportService.dataDetailRaw("9bb3afce-deab-4276-a027-1f001e2e09ae", code);
//        if (!StringUtils.isEmpty(basicinfo)) {
//        	Map<String, Object> basicinfoData = JsonToMap.gson2Map(basicinfo);
//        	Map<String, Object> dataMap = (Map<String, Object>) basicinfoData.get("data");
//        	data.put("basicinfo", dataMap.get("result"));
//        }
//        JsonResult result = JsonResult.successResult(data);
//        return result;
//    }
//
//    /**
//     * 人工审核
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/report/manaulAudit.do", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResult manaulAudit(HttpServletRequest request, HttpServletResponse response) {
//
//    	LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//        Map<String, Object> param = WebUtil.request2Map(request);
//        param.put("dqshr", login.getCode());
//        try {
//            return JsonResult.successResult(orderInfoService.manualAudit(param));
//        } catch (Exception e) {
//            return JsonResult.failResult(e.getMessage());
//        }
//    }
//
//    /**
//     * 自动审核
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/report/audit.do", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResult audit(HttpServletRequest request, HttpServletResponse response) {
//
//        String orderCode = request.getParameter("code");
//        try {
//            return JsonResult.successResult(auditService.auditOne(orderCode));
//        } catch (Exception e) {
//            return JsonResult.failResult(e.getMessage());
//        }
//    }
//
//    /**
//     * 跳转到详细数据页面
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/report/toDataDetail.do", method = RequestMethod.GET)
//    public String toDataDetail(HttpServletRequest request, HttpServletResponse response) {
//        //String report = request.getParameter("report");
//    	String dm_3rd_interface_code = request.getParameter("dm_3rd_interface_code");
//    	if("9bb3afce-deab-4276-a027-1f001e2e09ae".equals(dm_3rd_interface_code)){
//    		return "loanFront/reportDataDetail-6";
//    	}
//    	else if("914f0a9e-7e72-455a-b40c-f47f4aa6bd74".equals(dm_3rd_interface_code)){
//    		return "loanFront/reportDataDetail-3";
//    	}
//    	else return "/loanFront/litigation";
//    }
//
//    /**
//     * 报告详细数据 request参数report[]报告详细数据编号列表,code订单code
//     *
//     * @param request
//     * @param response
//     * @return 多个报告详细数据(顺序与请求参数report[]指定的一致)
//     */
//    @RequestMapping(value = "/report/dataDetail.do", method = {RequestMethod.POST,RequestMethod.GET})
//    @ResponseBody
//    public JsonResult dataDetail(HttpServletRequest request, HttpServletResponse response) {
//
//        String orderCode = request.getParameter("orderCode");
//        String dm_3rd_interface_code = request.getParameter("dm_3rd_interface_code");
//        try {
//        	if("9bb3afce-deab-4276-a027-1f001e2e09ae".equals(dm_3rd_interface_code) || "914f0a9e-7e72-455a-b40c-f47f4aa6bd74".equals(dm_3rd_interface_code)){
//        		return JsonResult.successResult(genReportService.dataDetailRaw(dm_3rd_interface_code,orderCode));
//        	}
//        	else{
//        		return JsonResult.successResult(genReportService.dataDetail(dm_3rd_interface_code,orderCode));
//        	}
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JsonResult.failResult();
//        }
//    }
//
//    /**
//     * 从文件读取的假数据,用于调试页面<br/>
//     * 报告详细数据 request参数report[]报告详细数据编号列表,code订单code
//     *
//     * @param request
//     * @param response
//     * @return 多个报告详细数据(顺序与请求参数report[]指定的一致)
//     */
//    @RequestMapping(value = "/report/fakeDataDetail.do", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResult fakeDataDetailFile(HttpServletRequest request, HttpServletResponse response) {
//
//        String[] reports = request.getParameterValues("report");
//        String code = request.getParameter("code");
//        try {
//            return JsonResult.successResult(getReports(code, true, reports));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JsonResult.failResult();
//        }
//    }
//
//    /**
//     * 获取报告多个详细数据
//     *
//     * @param code
//     * @param fake    是否为假数据
//     * @param reports
//     * @return
//     */
//    private List<Object> getReports(String code, boolean fake, String... reports) {
//        List<Object> datas = new ArrayList<Object>();
//        for (String report : reports) {
//            try {
//                String json = null;
//                if (fake) {
//                    //从json文件读取数据，用于调试，reportN.json请勿删除
//                    json = FileUtils.readFileToString(new File(ReportController.class.getResource("report" + report + ".json").getFile()), "UTF-8");
//                } else {
//                    //真实数据
//                    json = genReportService.Report(report, code);
//                }
//                datas.add(new Gson().fromJson(json, Object.class));
//            } catch (Exception e) {
//                datas.add(null);//异常时null占位
//                System.out.println(e.getMessage());
//            }
//        }
//        return datas;
//    }
//}
