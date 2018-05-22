package com.newdumai.web.sysmgr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * echarts图表
 *
 */
@Controller
public class echartsController {
	
	@RequestMapping("sysmgr/echarts/toDataForm.do")
	public String toDataForm(){
		return "sysmgr/echarts/data_display";
	}
}
