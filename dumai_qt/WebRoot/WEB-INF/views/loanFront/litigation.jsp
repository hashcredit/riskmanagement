<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html ng-app="mylitigation">
<head>
<meta charset="UTF-8">
<title>详情页</title>
<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
<link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
<style>
* {
	margin: 0;
	padding: 0;
}

.loading {
	position: absolute;
	top: 50%;
	left: 50%;
	margin-left: -100px;
	margin-top: -4rem;
	display: inline-block;
	overflow: hidden;
	height: 30px;
	line-height: 30px;
	vertical-align: text-bottom;
	font-size: 30px;
}

#list li {
	list-style: none;
	border-bottom: 1px solid lightgrey;
	height: 35px;
	line-height: 35px;
}

#list {
	width: 80%;
	padding: 10px;
	border: 1px solid;
	margin: 10px auto;
}

#list li span {
	display: inline-block;
	width: 49%;
	padding-left: 10px;
}

#list li span:nth-child(1) {
	border-right: 1px solid #7d7d7d;
}

.loading::after {
	display: inline-table;
	white-space: pre;
	text-align: left;
}

.loading.rhomb::after {
	content: "◇◇◇◇◇\A◈◇◇◇◇\A◇◈◇◇◇\A◇◇◈◇◇\A◇◇◇◈◇\A◇◇◇◇◈\A◇◇◇◇◇";
	color:blue;
	animation: spin6 2s steps(6) infinite;
}

@keyframes spin6 {to { transform:translateY(-180px);}}

</style>
</head>
<body>
	<common:head/>
	<div id="loading-bar-container"></div>
	<div ng-controller="myCtrl" class="content">
		<ul ng-repeat="ary in users" id="list">
			<li style="background: lightgrey"><span>类别</span><span>数值</span></li>
			<li ng-repeat="aa in ary"><span ng-bind="aa.name_zh"></span><span ng-bind="aa.value"></span></li>
		</ul>
	</div>
	<script src="${ctx}/static/script/lib/jquery.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/static/script/lib/angular.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/script/lib/angular-animate.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/static/script/lib/loading-bar.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/static/script/lib/ng-grid.js" type="text/javascript"
		charset="utf-8"></script>
	<script type="text/javascript">
		var myLitigation = angular.module('mylitigation', [ 'ngGrid',
				'chieffancypants.loadingBar' ]);
		myLitigation.config(['cfpLoadingBarProvider',function(cfpLoadingBarProvider) {
							cfpLoadingBarProvider.includeSpinner = true;
							cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
						} ]);
		var params = window.location.search;
		myLitigation.controller("myCtrl", [
				"$scope",
				"$http",
				"cfpLoadingBar",
				function($scope, $http, cfpLoadingBar) {
					$scope.start = function() {
						cfpLoadingBar.start();
					};
					$scope.complete = function() {
						cfpLoadingBar.complete();
					};
					cfpLoadingBar.start();
					$http.post("${ctx}/report/dataDetail.do" + params).success(function(data) {
								if (angular.fromJson(data.body)) {
									$scope.users = angular.fromJson(data.body);
									console.log($scope.users)
									$.each($scope.users,function(index,data){
										$.each(data,function(index2,data2){
											if(data2["value"] ===false){
												data2["value"] = "否";
											}else if(data2["value"] === true){
												data2["value"] = "是";
											}else if(data2["value"] === "" ){
												data2["value"] = "-";
											}else if(data2["value"] === "null" ||data2["value"] === null ){
												data2["value"] = "无";
											}else if(data2["value"] == "[object Object]" ){
												data2["value"]=data2["value"].min+"-"+data2["value"].max;
											}
										});	
									});
								} else {
									$(".content").html("暂未查询到数据").css("text-align","center");
								}
							});

				} ]);
	</script>
</body>
</html>