package com.newdumai.web.login.qt;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.newdumai.global.vo.LoginVo;
import com.newdumai.login.qt.LoginQTService;
import com.newdumai.order.company.CompanyOrderService;
import com.newdumai.sysmgr.BizFunctionSettingsService;
import com.newdumai.sysmgr.FunctionSettingsService;
import com.newdumai.util.TimeHelper;
import com.newdumai.util.IpAddress;
import com.newdumai.web.util.SystemConst;

/**
 * 登录的controller
 * 
 * @author 岳晓
 *
 */
@Controller
public class LoginController {
	@Autowired
	private LoginQTService loginQTService;
	@Autowired
	private FunctionSettingsService functionSettingsService;
	@Autowired
	private CompanyOrderService companyOrderService;
	@Autowired
	private BizFunctionSettingsService bizFunctionSettingsService;
	@Autowired  
	private HttpSession session;
	/**
	 * 跳转到登录页面<br/>
	 * 生成验证码和验证码结果
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/tologin.do", method = RequestMethod.GET)
	public String tologin(HttpServletRequest request) {
		return "login";
	}

	/**
	 * 重新生成验证码和验证码结果
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verification.do", method = { RequestMethod.POST, RequestMethod.GET })
	public String verification(HttpServletRequest request) {
		Random random = new java.util.Random();
		// char[] arr = { '+', '*' };
		int num1 = random.nextInt(10);
		int operate = random.nextInt(2);
		int num2 = random.nextInt(10);
		// 运算结果
		int result = 0;

		// 假定position值0/1/2/3分别代表”+”,”-”,”*”,”/”，计算前面操作数的运算结果
		String arra = null;
		switch (operate) {
		case 0:
			arra = "+";
			result = num1 + num2;
			break;
		case 1:
			arra = "*";
			result = num1 * num2;
			break;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num1", num1);
		map.put("num2", num2);
		map.put("arr", arra);
		map.put("result", result);
		System.out.println(new Gson().toJson(map));
		return new Gson().toJson(map);
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response) {

		// 用户名
		String username = request.getParameter("name");
		// 密码
		String userpass = request.getParameter("pass");

		//String topageSuccess = "redirect:/loanFront/toLoanFront.do"; // OA登录成功页面
		String topageSuccess = "forward:/loanFront/toLoanFront.do"; // OA登录成功页面
		String toDhTaskSuccess="forward:/auditingTask/toList.do"; //电核任务页面
		// String toPageErrory="redirect:/";//登录失败

		String resultJYZSOA = (String) request.getParameter("resultJYZSOA");

		String inputNum = (String) request.getParameter("inputNum");
		if (inputNum == null) {
			inputNum = "";
		}
		if (resultJYZSOA != null) {
			resultJYZSOA = resultJYZSOA.trim();
			if (resultJYZSOA.equals(inputNum) == false) {
				request.setAttribute("message", "验证码错误");
				return this.tologin(request);
			}
		}

		if (username != null && userpass != null) {
			LoginVo login = new LoginVo();
			Map<String, Object> userMap;
			try {
				userMap = loginQTService.getByUserName(username);
				if (userMap == null || !userpass.equals(userMap.get("PWD"))) {
					request.setAttribute("message", "用户名或密码错误");
					return this.tologin(request);
				} else if (SystemConst.SUPER_CODE.equals(userMap.get("sub_entity_id"))) {
					request.setAttribute("message", "非法用户");
					return this.tologin(request);
				} else {
					String subEntityId = (String) userMap.get("sub_entity_id");

					if (functionSettingsService.hasFunctions(subEntityId, "ip_access_ctrl")) {
						String ip = IpAddress.GetRemoteIpAddress(request);
						boolean accessable = loginQTService.inWhiteIps(ip, subEntityId);
						if (!accessable) {
							request.setAttribute("message", "非法IP地址登录!");
							return this.tologin(request);
						}
					}
					if (!companyOrderService.isEnable(subEntityId)) {
						request.setAttribute("message", "贵公司账户可能被禁用，请联系管理员!");
						return this.tologin(request);
					}
					login.setUsername((String) userMap.get("USER_NAME"));
					login.setSub_entity_id(subEntityId);
					login.setUser_permission((String) userMap.get("user_permission"));
					login.setIsLeader((String) userMap.get("ISLEADER"));
					login.setCode((String) userMap.get("code"));
					request.getSession().setAttribute("login", login);
					session.setAttribute("sys_user_code",userMap.get("code"));
					Map<String, Object> functionSettings = functionSettingsService.getFunctionSettingsBySubentityId(subEntityId);
					login.setWhite_ips((String) functionSettings.get("white_ips"));
					login.setFunction_settings((String) functionSettings.get("function_settings"));
					login.setBizFunction_settings(bizFunctionSettingsService.getAsMapBySubentityId(subEntityId));
				}
			} catch (Exception ex) {
				request.setAttribute("message", "系统异常!");
				ex.printStackTrace();
				return this.tologin(request);
			}

			// 在登录时保留登陆名,不保留登陆密码
			try {
				Cookie cookiename = new Cookie("OAUserName", username);
				cookiename.setMaxAge(365 * 24 * 60 * 60);
				response.addCookie(cookiename);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

			try {
				Map<String, Object> log = new HashMap<String, Object>();
				log.put("user_code", userMap.get("code"));
				log.put("user_name", userMap.get("USER_NAME"));
				log.put("user_surname", userMap.get("SURNAME"));
				log.put("deptname", userMap.get("user_dept"));
				log.put("logtime", TimeHelper.getCurrentTime());
				log.put("ipadress", request.getRemoteAddr());
				log.put("content", "登录成功");
				log.put("functionname", "登录");
				log.put("sub_entity_id", userMap.get("sub_entity_id"));
				loginQTService.addLog(log);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("0".equals(userMap.get("status"))) {
				return "redirect:/sysmgr/toUpateUserPwd.do";
			}
			// 用户角色
			String isLeader = (String) userMap.get("isleader");
			// 如果是电核员或电核管理员,跳到电核相应页面
			if ("4".equals(isLeader) || "5".equals(isLeader)) {
				return toDhTaskSuccess;
			} else {// 非电核员
				return topageSuccess;
			}
		} else {
			request.setAttribute("message", "用户名或密码为空");
			return this.tologin(request);
		}

	}

	/**
	 * 登录成功跳转到首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/toPortal.do", method = RequestMethod.GET)
	public String toPortal(HttpServletRequest request, HttpServletResponse response) {
		return "portal";
	}

	/**
	 * 注销登录,销毁session
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			session.invalidate();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return "forward:/";
	}
}
