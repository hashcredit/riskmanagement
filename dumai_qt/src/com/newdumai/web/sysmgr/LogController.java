package com.newdumai.web.sysmgr;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newdumai.global.vo.LoginVo;
import com.newdumai.log.LogService;
import com.newdumai.web.util.WebUtil;


@Controller
public class LogController {
	
	@Autowired
	private LogService logService;
	
	@RequestMapping("/sysmgr/toLogList.do")
	public String toLogList(HttpServletRequest request){
		return "sysmgr/log/logList";
		
	}
	
	@RequestMapping("/sysmgr/logList.do")
	@ResponseBody
	
	public String logList(HttpServletRequest request){
		
		LoginVo login = (LoginVo) WebUtil.getSession(request).getAttribute("login");
		
		return logService.list(WebUtil.request2Map(request),login.getSub_entity_id());
	}
	
}
