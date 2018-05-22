<%@tag import="com.newdumai.util.JsonToMap" trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Map"%>
<%@tag import="com.newdumai.global.vo.LoginVo"%>
<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="function_settings" required="true" type="java.lang.String"  rtexprvalue="true"%>
<%@attribute name="type_code" required="true" type="java.lang.String"  rtexprvalue="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	/* boolean flag = false;
	LoginVo login = (LoginVo)session.getAttribute("login");
	if(login!=null){
		if(function_settings!=null){
	String fucntionSettingsString = login.getFunction_settings();

	try{
		Map<String,Object> fucntionSettings = JsonToMap.gson2Map(fucntionSettingsString);
		flag = "1".equals(fucntionSettings.get(function_settings));
	}catch(Exception e){
		//异常时忽略，可能未配置任何功能
	}
		}
		else{
	flag = true;
		}
		
		if(flag){
	getJspBody().invoke(out);
		}
	} */
	boolean flag = false;
	LoginVo login = (LoginVo)session.getAttribute("login");
	if (login != null) {
		Map<String, Map<String, Object>> bizfucntionSettingsMap = login.getBizFunction_settings();

		Map<String, Object> map = bizfucntionSettingsMap.get(type_code);
		try {
			flag = "1".equals(map.get(function_settings));
		} catch (Exception e) {
			//异常时忽略，可能未配置任何功能
		}

		if (flag) {
			getJspBody().invoke(out);
		}
	}
%>
