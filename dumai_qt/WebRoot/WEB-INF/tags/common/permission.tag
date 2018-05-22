<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ tag import="com.newdumai.util.JsonToMap" %>
<%@ tag import="java.util.Map"%>
<%@ tag import="com.newdumai.global.vo.LoginVo"%>
<%@ attribute name="permission" required="true" type="java.lang.String"  rtexprvalue="true"%>
<%@ attribute name="isLeader" required="false" type="java.lang.String"  rtexprvalue="true"%>
<%@ attribute name="function_settings" required="false" type="java.lang.String"  rtexprvalue="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%

	boolean flag = false;
	LoginVo login = (LoginVo)session.getAttribute("login");
	if(login!=null){
		//System.out.println(function_settings+"---" + permission);
		if(function_settings!=null){
			String fucntionSettingsString = login.getFunction_settings();
			//System.out.println(fucntionSettingsString+"---" + permission);
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
		
		if(!flag){
			return;
		}
		
		String permissionString = login.getUser_permission();
		if(permission!=null){
			String[] permissions = permissionString.split(":");
			
			if(permission.equals("view-report")){
				if(permissions.length>0){
					flag = permissions[0].equals("1");
				}
			}
			else if(permission.equals("delete-order")){
				if(permissions.length>1){
					flag = permissions[1].equals("1");
				}
			}
			else if(permission.equals("export-report")){
				if(permissions.length>2){
					flag = permissions[2].equals("1");
				}
			}
		}
		if(flag){
			getJspBody().invoke(out);
		}
	}
%>
