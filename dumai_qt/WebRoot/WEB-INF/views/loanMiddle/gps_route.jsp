<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html ng-app="myMod">
	<head>
	<title>GPS报警</title>
	<meta http-equiv="content-type" content="text/html;charset=UTF-8">
	<link rel="stylesheet" href="${ctx}/static/css/gps/gps2.css">
	 <link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
	</head>
	<script src="http://api.map.baidu.com/api?v=2.0&ak=zmAWIlHnn8qQ4qMKkBGtypeoZio5q86s" type="text/javascript"></script>
	<body>

<div ng-controller="myCtrl">
    <h4 class="header">GPS</h4>
    <ul class="con_list">
        <li class="current"><a href="#/gpsAlarm" >GPS报警</a></li>
        <li><a href="#/gpsInfo">GPS信息</a></li>
    </ul>
    <div class="con_info">
        <div ng-view></div>
    </div>
</div>
    <script src="${ctx}/static/script/lib/jquery.min.js"></script>
	<script src="${ctx}/static/script/lib/angular.min.js"></script>
	<script src="${ctx}/static/script/lib/angular-route.js"></script>
	<script src="${ctx}/static/script/lib/loading-bar.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/static/script/lib/moment.js" type="text/javascript" charset="utf-8"></script>
   <script src="${ctx}/static/script/GPS/GpsApp.js" type="text/javascript" charset="utf-8"></script>
   <script src="${ctx}/static/script/GPS/gpsController.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
