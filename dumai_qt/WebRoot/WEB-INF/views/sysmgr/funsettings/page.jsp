<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DocType html>
<html lang="en" ng-app="mySystem">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>功能设定</title>
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
<body background="#000000">
	<div class="system" id="system" ng-controller="mySystem">
		<div class="system_header">
			<h5>
				读脉风控管理平台
			</h5>
		</div>
		<div class="system_bodies">
			<ul>
				<a href="/dumai_qt/sysmgr/funsettings/toPage.do"><li class="current1"><span class="shezhi shezhi_1 spriteIcon"></span><span>功能设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toUserList.do"><li><span class="shezhi shezhi_2 spriteIcon"></span> <span>用户管理</span></li></a>
				<a href="/dumai_qt/sysmgr/funsettings/toIpConfig.do"><li><span class="shezhi shezhi_1 spriteIcon"></span><span>IP设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toLogList.do"><li><span class="shezhi shezhi_3 spriteIcon"></span> <span>日志</span></li></a>
				<a href="/dumai_qt/sysmgr/toUpatePwd.do"><li><span class="shezhi shezhi_4 spriteIcon"></span> <span>修改密码</span></li></a>
			</ul>
			<div class="system_Box">
				<div class="system_content system_content1">
					<!-- <div class="system_left">
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
					</div> -->
					<!-- <div class="system_right">
						<h5>审核方式</h5>
						<div class="system_right_bottom" >
							<label>业务类型：
								  <select ng-Model="bValue" ng-change="changeData()" >
									<option style="background-color:#223746;" ng-repeat="typeList in typeLists"  ng-bind="typeList.name" value="{{typeList.code}}" ng-model="typeList.code"></option>
								</select>
							</label> 
							<label style="display: inline-block"> <span style="letter-spacing: 24px">贷</span>前：</label> 
							<span class="daizhong box1" ng-click="review_way(1)"><input type="radio" name="dai" >规则 </span> 
							<span class="daizhong box1" ng-click="review_way(2)"><input type="radio" name="dai">模型 </span>
						</div>
					</div> -->
					<div class="system_grid" ng-controller="systemGridCtrl"> 
						<h5>数据查询设定</h5>
						<div class="system_right_bottom"  >
							<label>业务类型： 
								<select ng-Model="typeCode" ng-change="changeDatas()" ><!--  -->
									<option style="background-color:#223746;" ng-repeat="type in types"  ng-bind="type.name" value="{{type.code}}"></option>
								</select>
							<input type="button" value="保存配置"  style="text-align:center;display:inline-block;background:red;padding: 5px 20px 5px 28px;letter-spacing:10px;border-radius:4px;"ng-click="dataViewSave()">
							</label>
						</div>
						<div class="system_gridStyle" ng-grid="gridOptions"></div>
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
<script type="text/javascript">
	$(function(){
		$("#system").css("background-color","#191917");
	})
</script>
</html>