package com.newdumai.web.loanMiddle;

import com.newdumai.global.vo.JsonResult;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.loanMiddle.GpsService;
import com.newdumai.loanMiddle.LoanMiddleService;
import com.newdumai.setting.type.TypeService;
import com.newdumai.web.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class LoanMiddleController {
	
	@Autowired
	private LoanMiddleService loanMiddleService;
	@Autowired
	private GpsService gpsService;
	@Autowired
	private TypeService typeService;

	@RequestMapping(value = "/loanMiddle/toLoanMiddle.do", method = RequestMethod.GET)
	public String toList(HttpServletRequest request, HttpServletResponse response) {
		return "loanMiddle/list";
	}

	/**
	 * 列表页面数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/list.do", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response) {
		LoginVo login = (LoginVo) request.getSession().getAttribute("login");
		String subEntityId = login.getSub_entity_id();
		Map<String, Object> param = WebUtil.request2Map(request);
		param.put("sub_entity_id", subEntityId);
		String json = loanMiddleService.list(param);
		return json;
	}
	
	/**
	 * 跳转到贷中GPS报警页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/gps_route.do", method = RequestMethod.GET)
	public String gps_route(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("code");
		request.setAttribute("orderId", orderId);
		return "loanMiddle/gps_route";
	}
	
	@RequestMapping(value = "/loanMiddle/toGps_alarm.do", method = RequestMethod.GET)
	public String toGps_alram(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		request.setAttribute("orderId", orderId);
		return "loanMiddle/gps_alarm";
	}
	/**
	 * 维护GPS设备
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/gps_device_upate.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult gps_device_upate(HttpServletRequest request, HttpServletResponse response) {
		String code  = request.getParameter("code");
		Map<String,Object> map = WebUtil.request2Map(request);
		try {
			loanMiddleService.upateGpsDevice(map, code);
			return JsonResult.successResult(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResult.failResult(e.getMessage());
		}
	}
	
	/**
	 * 贷中GPS报警数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/gps_alarm.do")
	@ResponseBody
	public List<Map<String, Object>> gps_alram(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
//		return loanMiddleService.getGpsAlarm(code);
		return gpsService.getGpsAlarm(code);
	}
	
	/**
	 * 跳转到贷中GPS信息页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/toGps_info.do", method = RequestMethod.GET)
	public String toGps_info(HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		request.setAttribute("orderId", orderId);
		return "loanMiddle/gps_info";
	}
	
	/**
	 * 贷中GPS信息数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/gps_info.do")
	@ResponseBody
	public List<Map<String, Object>> gps_info(HttpServletRequest request, HttpServletResponse response) {
		/*
		//假数据
		String json = "[                                           "+
				"{                                                 "+
				"	\"Time\": \"2016/12/28 3:04:01\",              "+
				"	\"Lng\": 118.53893133,                         "+
				"	\"Lat\": 31.70303012,                          "+
				"	\"Speed\": 100,                          "+
				"	\"Location\": \"安徽省马鞍山市花山区蓬莱路\"     "+
				"  },                                              "+
				"{	\"Time\": \"2016/12/28 3:04:01\",              "+
				"	\"Lng\": 118.43893133,                         "+
				"	\"Lat\": 31.71303012,                          "+
				"	\"Speed\": 100,                          "+
				"	\"Location\": \"安徽省马鞍山市花山区东四十条\"   "+
				"  }                                               "+
				"]";
		return JsonToMap.gson2List(json);*/
		
		String code = request.getParameter("code");
		Map<String,Object> queryParam = WebUtil.request2Map(request);
		return loanMiddleService.getDeviceHisTrack(code, queryParam);
	}
	
	
	/**
	 * 跳转到贷中报告页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/toReport.do", method = RequestMethod.GET)
	public String toRepot(HttpServletRequest request, HttpServletResponse response) {
		return "loanMiddle/report";
	}

	/**
	 * 获取报告详细数据 1 订单 人员表 2 小视图片 3 同住人信息 4 同一人其他订单列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loanMiddle/report.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult report(HttpServletRequest request, HttpServletResponse response) {
		LoginVo login = (LoginVo) request.getSession().getAttribute("login");
		String code = (String) request.getParameter("code");
		String typeCode = (String) request.getParameter("type_code");

		Map<String, Object> data = loanMiddleService.getReportInfo(login, code, typeCode);

		if (data == null) {
			return JsonResult.failResult("数据不存在");
		}

		JsonResult result = JsonResult.successResult(data);
		return result;
	}

}
