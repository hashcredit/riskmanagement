<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DocType html>
<html lang="en" ng-app="mySystem">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IP设定</title>
<link rel="stylesheet/less" href="${ctx}/static/css/system.less" type="text/less">
<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/static/css/style.css">
<link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
<link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ng-grid.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/loading-bar.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ajaxHelper.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/controller/pageController.js"></script>

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
				<a href="/dumai_qt/sysmgr/funsettings/toPage.do"><li><span class="shezhi shezhi_1 spriteIcon"></span><span>功能设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toUserList.do"><li><span class="shezhi shezhi_2 spriteIcon"></span> <span>用户管理</span></li></a>
				<a href="/dumai_qt/sysmgr/funsettings/toIpConfig.do"><li class="current1"><span class="shezhi shezhi_1 spriteIcon"></span><span>IP设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toLogList.do"><li><span class="shezhi shezhi_3 spriteIcon"></span> <span>日志</span></li></a>
				<a href="/dumai_qt/sysmgr/toUpatePwd.do"><li><span class="shezhi shezhi_4 spriteIcon"></span> <span>修改密码</span></li></a>
			</ul>
			<div class="system_Box">
				<div class="system_content system_content1">
					<div class="system_left">
						<h5>安全设定</h5>
						<div class="system_left_bottom">
							<p>通用</p>
							<label ng-click="addIP()" ng-class="{true:'addSel',false:'removeSel'}[addFlag]" class="spriteIcon"></label> 
							<input type="checkbox">IP访问控制
							<div ng-class="{true:'show_2',false:'hide_2'}[true]">
								<textarea name='多个ip用","分隔' ng-model="white_ips" value="white_ips"></textarea><p>注：多个ip用","分隔</p>
								<div class="yingyong">
									<input type="button" value="应用" ng-click="saveIp()">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	   <div class="selectError" ng-class="{true:'show_2',false:'hide_2'}[tipSelectFlag]">
          <div>
             <p>保存成功</p>
             <span class="sure" ng-click="closeTip()"></span>
           </div>
        </div>
	</div>
</body>
</html>