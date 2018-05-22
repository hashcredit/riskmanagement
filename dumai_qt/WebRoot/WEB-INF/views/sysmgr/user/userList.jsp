<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DocType html>
<html lang="en" ng-app="mySystem">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet/less" href="${ctx}/static/css/system.less" type="text/less">
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
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/controller/userController.js"></script>


</head>
<body style="background-color: #000000">
	<div class="system" id="system" ng-controller="mySystemUserCtrl">
		<div class="system_header">
			<h5>
				读脉风控管理平台
			</h5>
		</div>
		<div class="system_bodies">
			<ul>
				<a href="/dumai_qt/sysmgr/funsettings/toPage.do"><li><span class="shezhi shezhi_1 spriteIcon"></span> <span>功能设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toUserList.do"><li class="current1"><span class="shezhi shezhi_2 spriteIcon"></span><span>用户管理</span></li></a>
				<a href="/dumai_qt/sysmgr/funsettings/toIpConfig.do"><li><span class="shezhi shezhi_1 spriteIcon"></span><span>IP设定</span></li></a>
				<a href="/dumai_qt/sysmgr/toLogList.do"><li><span class="shezhi shezhi_3 spriteIcon"></span> <span>日志</span></li></a>
				<a href="/dumai_qt/sysmgr/toUpatePwd.do"><li><span class="shezhi shezhi_4 spriteIcon"></span> <span>修改密码</span></li></a>
			</ul>
			<div class="system_Box">
				<div class="system_content system_content2">
					<div class="addUser_title">
						用户列表<span ng-click="addGrid()"><i></i>增加</span>
					</div>
					<div ng-controller="userGridCtrl">
						<div class="user_gridStyle" ng-grid="gridOptions"></div>
					</div>
					<div class="system_addUser" ng-class="{true:'show_2',false:'hide_2'}[addUserFlag]">
                     <ul>
                        <li><span>用户名：</span>
                            <label><input type="text" ng-model="user_name" name="user_name" ng-blur="checkUserExist()"></label>
                        </li>
                        <li><span>姓名：</span>
                            <label><input type="text" ng-model="surname" name="surname"></label>
                        </li>
                        <li class="dan"><span>性别：</span>
                            <label class="checked box1 spriteIcon" ><input type="radio" name="sex" ng-model="sex" value="女">女</label>
                            <label class="box1 spriteIcon"><input type="radio" name="sex" ng-model="sex" value="男">男</label>
                        </li>
                        <li class="dan"><span>是否为管理员：</span>
                            <label class="checked box1 spriteIcon"><input type="radio" name="isLeader" ng-model="isLeader" value="2">是</label>
                            <label class="box1 spriteIcon"><input type="radio" name="isLeader" ng-model="isLeader" value="3">否</label>
                        </li>
                        <li><span>部门：</span>
                            <label><input type="text" ng-model="user_dept" name="user_dept"></label>
                        </li>
                        <li><span>角色：</span>
                            <label><input type="text" ng-model="user_role" name="user_role"></label>
                        </li>
                        <li class="dan dan3"><span>权限：</span>
                            <span class="checkBoxButton spriteIcon" ><input type="checkbox" name="user_permission" ng-model="user_permission" >查看</span>
                            <span class="checkBoxButton spriteIcon" ><input type="checkbox" name="user_permission" ng-model="user_permission" >删除</span>
                            <span class="checkBoxButton spriteIcon" ><input type="checkbox" name="user_permission" ng-model="user_permission" >导出</span>
                        </li>
                        <li><span>邮箱：</span>
                            <label><input type="email" ng-model="email" name="email"></label>
                        </li>
                        <li><span>工作电话：</span>
                            <label><input type="number" ng-model="office_tel" name="office_tel"></label>
                        </li>
                        <li><span>手机号：</span>
                            <label><input type="telephone" ng-model="mobile" name="mobile"></label>
                        </li>
                        <li class="dan"><span>账号启用：</span>
                            <label  class="checked box1 spriteIcon"><input type="radio" name="isvalid" ng-model="isvalid" value="1">是</label>
                            <label class="box1 spriteIcon"><input type="radio" name="isvalid" ng-model="isvalid" value="0">否</label>
                        </li>
                    </ul>
                    <div class="system_butt">
                        <input type="button" value="保存" class="system_save" ng-click="addUser()">
                        <input type="button" value="取消" class="system_cancle" ng-click="close()">
                    </div>
                </div>
				</div>
			</div>
		</div>
	</div>
</html>