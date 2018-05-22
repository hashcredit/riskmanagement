<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html lang="en" ng-app="myOverAcc">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>分期列表</title>
	<link rel="stylesheet/less" href="${ctx}/static/css/overdue.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
    <link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script>
   	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ng-grid.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/controller/dueController1.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true">
	 <div class="overDuePage" ng-class="{true:'show_1',false:'hide_1'}[overdue_accound_Flag]" ng-controller="gridCtrl_account">
    <h5 ng-click="backMain_account()"><span>返回到逾期催收主页面</span><span class="over_back"></span></h5>
    <div class="overCon">
        <ul class="over_title">
            <li><span class="icon_peop"></span><span>李莹莹</span></li>
            <li><span>借款金额：</span><span>123456</span></li>
            <li><span>借款期数：</span><span>12</span></li>
            <li><span>放款日期：</span><span>2016-04-18</span></li>
            <li ng-click="baseReport2()" class="dzReport"><span>贷中报表</span></li>
        </ul>
        <div class="over_box">
            <div class="over_right over_right_account" >
                <div class="gridStyle" ng-grid="gridOptions"  ></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>