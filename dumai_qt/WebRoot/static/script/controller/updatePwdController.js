/**
 * Created by Administrator on 2017/4/18 0018.
 */
var mySystem = angular.module('mySystem', ["ngGrid"]);

mySystem.controller('mySystem',['$scope','$http',function ($scope,$http) {
    $scope.selectedIndex="setup";$scope.addFlag=false;$scope.addUserFlag=false;
    $scope.cutoverTab=function (type) {
        $scope.selectedIndex=type;
    };
    $scope.addIP=function(){
        $scope.addFlag=!$scope.addFlag;
    };
    $scope.addGrid=function () {
        $scope.addUserFlag=true;
    };
    $scope.close=function () {
        $scope.addUserFlag=false;
    };
    $('.box1').click(function(){
        $(this).addClass('checked').siblings().removeClass("checked");
    });
    $scope.checkFlag1=true;$scope.checkFlag2=false;$scope.checkFlag3=false;
    $scope.change1=function (){
      console.log($scope.checkFlag1)
      $scope.checkFlag1=!$scope.checkFlag1;
  }
    $scope.change2=function (){
     console.log($scope.checkFlag2)
        $scope.checkFlag2=!$scope.checkFlag2;
    }
    $scope.change3=function (){
        console.log($scope.checkFlag3)
        $scope.checkFlag3=!$scope.checkFlag3;
    }
}]);
mySystem.controller("myPassCtrl",["$scope",function ($scope) {
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
        if($scope.pass && $scope.newPass){
            if($scope.pass == $scope.newPass){
                $.ajax({
                	url: "/dumai_qt/sysmgr/upateUserPwd.do",
                	method: "post",
                	traditional: true,
                	data: {
                	    pwd: $scope.newPass                	    
                	},
                	success: function(result) {
                	    if (result.code == '0') {
                	    	alert("修改成功");
                	    	window.location.href="/dumai_qt/sysmgr/funsettings/toPage.do";
                	    } else {
                	        alert("修改失败");
                	    }
                	},
                	error:function() {
                	    alert("修改失败");
                	}
                });
            }else{
                alert("密码输入不一致")
            }
        }else if(!$scope.pass || !$scope.newPass){
        	alert("请输入完整")
        }
    };
}]);