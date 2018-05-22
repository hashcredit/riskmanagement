//package com.newdumai.web.loanFront;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.newdumai.global.vo.JsonResult;
//import com.newdumai.global.vo.LoginVo;
//import com.newdumai.loanFront.NewReportService;
//import com.newdumai.web.util.WebUtil;
//
///**
// * 演示用
// */
//@Controller
//public class NewReportController {
//
//	@Autowired
//	private NewReportService newReportService;
//
//	@RequestMapping(value = "/newreport/toReport.do", method = RequestMethod.GET)
//	public String toRepot(HttpServletRequest request, HttpServletResponse response) {
//		String orderId = request.getParameter("orderId");
//		request.setAttribute("orderId", orderId);
//		return "loanFront/newreport";
//	}
//
//	@RequestMapping(value = "/newreport/report.do", method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult report(HttpServletRequest request){
//		LoginVo login = (LoginVo) request.getSession().getAttribute("login");
//
//		Map<String,Object> param = WebUtil.request2Map(request);
//		param.put("sub_entity_id", login.getSub_entity_id());
//		Map<String,Object> report = newReportService.genReport(param);
//		if(report==null){
//			return JsonResult.failResult("数据不存在");
//		}
//		else{
//			return JsonResult.successResult(report);
//		}
//	}
//
//	@RequestMapping(value ="newreport/toXypfResult.do",method = RequestMethod.GET)
//	public String toXypfDetail(HttpServletRequest request){
//		return "loanFront/xypfResult";
//	}
//
//	@RequestMapping(value ="newreport/xypfResult.do",method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult xypfDetail(HttpServletRequest request){
//		String code = request.getParameter("code");
//		try {
//			return JsonResult.successResult(newReportService.getScoreResult(code, "2"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failResult();
//		}
//	}
//}
