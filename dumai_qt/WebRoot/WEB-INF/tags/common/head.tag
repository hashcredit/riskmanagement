<%@tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@attribute name="back" required="false" type="java.lang.String"  rtexprvalue="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	request.setAttribute("BASE_PATH",basePath );
	String uri = request.getRequestURI();
	request.setAttribute("URI",uri );
%>
<style type="text/css">
	.back-header+.below{
		margin-top: 30px;
	}
</style>
<c:url var="baseUrl" value="${BASE_PATH}" ></c:url>
<div class="back-header" style="height:29px;position: fixed;width: 58px;z-index: 1000;top:0;left:0;right:0">
	<header data-am-widget="header" style="height: 29px;line-height: 29px;font-size:16px;text-align: center;background: rgba(189, 184, 184,0.8)"  class="am-header am-header-default">
	    <div >
    		<a href="javascript:history.go(-1)" style="color:#FFF">
	        	&lt;返回
	        </a>	
	    </div>
	</header>
</div>
