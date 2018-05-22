<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DocType html>
<html lang="en" ng-app="mySystem">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志管理</title>
<link rel="stylesheet/less" href="${ctx}/static/css/system.less"
	type="text/less">
<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/static/css/style.css">
<link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
<link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
<%-- <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script> --%>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ng-grid.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/loading-bar.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/controller/logController.js"></script>

</head>
<body style="background-color: #000000">

	<div class="system" id="system" ng-controller="mySystem">
		<div class="system_header">
			<h5>
				读脉风控管理平台
			</h5>
		</div>
		<div class="system_bodies">
			<ul>
				<a href="/dumai_qt/sysmgr/funsettings/toPage.do"><li><span class="shezhi shezhi_1 spriteIcon"></span> <span>功能设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toUserList.do"><li><span class="shezhi shezhi_2 spriteIcon"></span> <span>用户管理</span></li></a>
				<a href="/dumai_qt/sysmgr/funsettings/toIpConfig.do"><li><span class="shezhi shezhi_1 spriteIcon"></span><span>IP设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toLogList.do"><li class='current1'><span class="shezhi shezhi_3 spriteIcon"></span> <span>日志</span></li></a>
				<a href="/dumai_qt/sysmgr/toUpatePwd.do"><li><span class="shezhi shezhi_4 spriteIcon"></span> <span>修改密码</span></li></a>
			</ul>
			<div class="system_Box">
				<div class="system_content system_content3">
					<div ng-controller="logGridCtrl">
						<div class="log_gridStyle" ng-grid="gridOptions"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>