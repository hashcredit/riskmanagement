<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%
	String root=request.getContextPath();//获取项目根目录名称
%>
<!DOCTYPE html>
<html lang="en" >
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="<%=root%>/css/bootstrap.css">
    <link rel="stylesheet" href="<%=root%>/css/reCss.css">
</head>
<body>
<a href="http://localhost:8082/dumai_qt/cost/toList.do?sub_entity_id=5a538c26-3e0d-464e-b16c-2220e8642693">对帐明细</a>
</body>
</html>