package com.newdumai.web.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.newdumai.global.vo.PageConfig;

public class WebUtil {
	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}
	
	public static Map<String, Object> request2Map(HttpServletRequest request) {
	    // 参数Map  
	    Map<String, String[]> properties = request.getParameterMap();  

	    // 返回值Map  
	    Map<String, Object> returnMap = new HashMap<String, Object>();  
		Iterator<Entry<String, String[]>>  entries = properties.entrySet().iterator(); 
		Map.Entry<String, String[]> entry;  
	    //String name = "";  
	    while (entries.hasNext()) {
	    	String value = "";
	        entry = entries.next();  
	        String name = (String) entry.getKey();  
	        Object valueObj = entry.getValue();  
	        if(null == valueObj){  
	            value = "";  
	        }else if(valueObj instanceof String[]){  
	            String[] values = (String[])valueObj;  
	            for(int i=0;i<values.length;i++){  
	                value += values[i] + ",";  
	            }  
	            value = value.substring(0, value.length()-1);  
	        }else{  
	            value = valueObj.toString();  
	        }  
	        returnMap.put(name, value);  
	    }  
	    return returnMap;  
	} 
	
	public static PageConfig getPageConfig(HttpServletRequest request){
		PageConfig pageConfig = new PageConfig();
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		int currPage;
		int pageSize;
		try {
			currPage = Integer.valueOf(page);
		} catch (NumberFormatException e) {
			currPage = 1;
		}
		try {
			pageSize = Integer.valueOf(rows);
		} catch (NumberFormatException e) {
			pageSize = 15;
		}
		pageConfig.setCurrPage(currPage);
		pageConfig.setPageSize(pageSize);
		return pageConfig;
	}
}
