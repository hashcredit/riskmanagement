<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myPass">
<head>
	<meta charset="UTF-8">
	<title>修改密码</title>
	<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
	<style>
		body,html{
			width: 100%;
			height: 100%;
		}
		*{
			margin: 0;
			padding: 0;
		}
		.contain{
			width: 100%;
			height: 100%;
		}
		.top{
			width:100%;
			height: 44%;
			background: url("${ctx}/static/images/banner_01.png") no-repeat;
		}
		.bottom{
			width: 100%;
			height: 56%;
			padding-top: 60px;
			color: #499DD4;
			font-size: 12px;

		}
		.bottom input{
			outline: none;
			border: 1px solid #499DD4;
			background: none;
		}
		.bottom label{
			font-weight: 200;
		}
		.userForm{
			width: 40%;
			margin: 0 auto;
		}
		.alert-danger{
			background: none;
			border: none;
			padding: 0;
		}
		.form-group i{
			display: inline-block;
		}
		.form-group{
			margin-bottom: 0;
		}
		.openEye,.closeEye{
			position: relative;
			width: 22px;
			height: 16px;
			left:88%;
			top:-25px;
			background: url("${ctx}/static/images/icon_01.png") no-repeat;
		}
		.closeEye{
			top:-22px;
			background: url("${ctx}/static/images/icon_02.png") no-repeat;
		}
		.tijiaoButton{
			text-align: left;
			padding-left: 28%;
		}
		#tijiao{
			width: 50%;
			height: 30px;
			line-height: 30px;
			text-align: center;
			background: #105380;
			cursor: pointer;
			color: white;
		}
		.userForm input.ng-pristine{
			border: 1px solid #499DD4;
		}
		.userForm input.ng-dirty.ng-invalid{
			border-color:red
		}
		.userForm input.ng-valid{
			border: 1px solid #499DD4;
		}

	</style>
</head>
<body>
<div class="contain" ng-controller="myPassCtrl">
	<div class="top"></div>
	<div class="bottom">
		<form name="myForm" class="form-horizontal userForm" method="post" action="${ctx}/sysmgr/upatePwd.do">
			<div class="form-group">
				<label class="control-label col-md-3">新密码：</label>
				<div class="col-md-5">
					<input type="password" name="pwd" ng-model="name" ng-maxlength="16" ng-minlength="6" class="form-control col-md-2" required id="pass" ng-blur="CheckWord1('{{name}}')">
					<i class="Eye1" ng-click="changeClass1()" ng-class="{true:'openEye',false:'closeEye'}[classFlag1]"></i>
					<div ng-show="myForm.pwd.$invalid&&myForm.pwd.$dirty">
						<div class="alert alert-danger">
							<p ng-show="myForm.pwd.$error.required">
								该项为必填项，请输入内容
							</p>
							<p ng-show="myForm.pwd.$error.maxlength">
								字符不可以超过16位
							</p>
							<p ng-show="myForm.pwd.$error.minlength">
								字符不可以少于6位
							</p>
						</div>
					</div>
				</div>
				<span>6-16 个字符，需使用字母、数字或符号组合，不能使用纯数字、纯字母、纯符号</span>
			</div>
			<div class="form-group">
				<label class="control-label col-md-3">确认密码：</label>
				<div class="col-md-5">
					<input type="password" ng-model="newPass" class="form-control" required id="newPass" >
					<i class="Eye2" ng-class="{true:'openEye',false:'closeEye'}[classFlag2]" ng-click="changeClass2()"></i>
				</div>
			</div>
			<div class="form-group tijiaoButton">
				<div ng-click="InputCheckWord()" id="tijiao">保存</div>
			</div>
		</form>
	</div>
</div>
</body>
<%-- <script src="${ctx}/static/script/lib/jquery.min.js"></script> --%>
<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
<script src="${ctx}/static/script/lib/angular.min.js"></script>
<script>
    var  myPass= angular.module('myPass', []);
    myPass.controller("myPassCtrl",["$scope",function ($scope) {
        $scope.classFlag1=false; $scope.classFlag2=false;
        $scope.changeClass1=function() {
            $scope.classFlag1=!$scope.classFlag1;
            if($scope.classFlag1){
                $(".Eye1").prev().attr("type","text")
            }else{
                $(".Eye1").prev().attr("type","password")
            }
        };
        $scope.changeClass2=function() {
            $scope.classFlag2=!$scope.classFlag2;
            if($scope.classFlag2){
                $(".Eye2").prev().attr("type","text")
            }else{
                $(".Eye2").prev().attr("type","password")
            }
        };
        $scope.InputCheckWord=function() {
            console.log(!$scope.name || !$scope.newPass)
            if($scope.name || $scope.newPass){
                if($scope.name == $scope.newPass){
//                    alert("输入一致")
                    document.myForm.submit();
                }else{
                    alert("密码输入不一致")
                }
            }
        };
//        $scope.CheckWord1=function (n) {
//            if(n.replace(/[\w]+/g,"")!=""){
//                alert("输入非法！应该填写数字,字母或下划线");
//            }
//        };
    }])
</script>

</html>