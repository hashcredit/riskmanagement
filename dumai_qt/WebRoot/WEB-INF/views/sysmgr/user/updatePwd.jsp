<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en" ng-app="mySystem">
<head>
<meta charset="UTF-8">
<title>修改密码</title>
<link rel="stylesheet/less" href="${ctx}/static/css/system.less"
	type="text/less">
<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/static/css/style.css">
<link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ng-grid.js"></script>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/controller/updatePwdController.js"></script>
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
				<a href="/dumai_qt/sysmgr/toLogList.do"><li><span class="shezhi shezhi_3 spriteIcon"></span> <span>日志</span></li></a>
				<a href="/dumai_qt/sysmgr/toUpatePwd.do"><li class="current1"><span class="shezhi shezhi_4 spriteIcon"></span> <span>修改密码</span></a>
				</li>
			</ul>
			<div class="system_Box">
				<div class="system_content system_content4">
					<div class="contain" ng-controller="myPassCtrl">
						<div class="bottom">
							<form name="myForm" class="form-horizontal userForm"  style="width: 60%">
								<div class="form-group">
									<label class="control-label col-md-3">新密码：</label>
									<div class="col-md-5">
										<input type="password" name="user_pass" ng-model="pass"
											ng-maxlength="16" ng-minlength="6"
											class="form-control col-md-2" required id="pass"
											ng-blur="CheckWord1('{{pass}}')"> <i class="Eye1 spriteIcon"
											ng-click="changeClass1()"
											ng-class="{true:'openEye',false:'closeEye'}[classFlag1]"></i>
										<div ng-show="myForm.user_pass.$invalid&&myForm.user_pass.$dirty">
											<div class="alert alert-danger">
												<p ng-show="myForm.user_pass.$error.required">该项为必填项，请输入内容</p>
												<p ng-show="myForm.user_pass.$error.maxlength">字符不可以超过16位</p>
												<p ng-show="myForm.user_pass.$error.minlength">字符不可以少于6位</p>
											</div>
										</div>
									</div>
									<span style="display: inline;">6-16
										个字符，需使用字母、数字或符号组合，不能使用纯数字、纯字母、纯符号</span>
								</div>
								<div class="form-group">
									<label class="control-label col-md-3">确认密码：</label>
									<div class="col-md-5">
										<input type="password" ng-model="newPass" class="form-control" required id="newPass"> 
										<i class="Eye2 spriteIcon" ng-class="{true:'openEye',false:'closeEye'}[classFlag2]" ng-click="changeClass2()"></i>
									</div>
									<span style="display: inline-block; margin-top: 8px">请再次输入密码</span>
								</div>
								<div class="form-group tijiaoButton">
									<div ng-click="InputCheckWord()" id="tijiao">保存</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>