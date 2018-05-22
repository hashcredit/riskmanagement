/**
 *登录页面
 * Created by Administrator on 2017/4/17 0017.
 */
var myLogin = angular.module('myLogin', []);
myLogin.directive('renderFinish', function ($timeout) {      //renderFinish自定义指令
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function() {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});
myLogin.controller('loginCtrl',['$scope','$http',function ($scope,$http) {
	 function DelCookie(name) {
	        var exp = new Date();
	        exp.setTime(exp.getTime() - 1);
	        document.cookie = name + "=; expires=" + exp.toGMTString();
	    }
	 function getCookie(name){
		 var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
		 if(arr=document.cookie.match(reg)){
			 return unescape(arr[2]);
		  }else{ return null;}
	 }
      if(getCookie("username")){
			 $scope.user_name=getCookie("username");
			 $scope.user_password=getCookie("password");
			 $(".extra img").css("display","block");
			 $(".password").attr("type","password");
		 }else{
			 $scope.user_name="";
			 $scope.user_password="";
			 $(".extra img").css("display","none");
		 }
	  $scope.extra=function () {
        if($(".extra img").css("display") == "block"){
            $(".extra img").css("display","none");
        }else if($(".extra img").css("display") == "none"){
            $(".extra img").css("display","block");
        }
    };
$http.get("/dumai_qt/verification.do").success(function(data){
 	$scope.colResults=data;
	$scope.num1=$scope.colResults.num1;
	$scope.num2=$scope.colResults.num2;
	$scope.arr=$scope.colResults.arr;
	$scope.result=$scope.colResults.result;
	$scope.hidden=$scope.result;
	$("#resultJYZSOA").val($scope.result);
}).error(function(error){
	console.log(error)
})
//更换验证码
    $scope.changeYzm=function () {
		$http.get("/dumai_qt/verification.do").success(function(data){
         	$scope.colResults=angular.fromJson(data);
        	$scope.num1=$scope.colResults.num1;
        	$scope.num2=$scope.colResults.num2;
        	$scope.arr=$scope.colResults.arr;
        	$scope.result=$scope.colResults.result;
        	$scope.hidden=$scope.result;
        	$("#resultJYZSOA").val($scope.hidden);
        }).error(function(){
        	alert("获取验证码错误")
        })
    };
//点击enter键
    $(".yam_num").keyup(function(e){
		 var keycode = window.event?e.keyCode:e.which;
	        if(keycode==13){
	        	 login();
	    }
	})

    $(".userEnterBox").focus( function () {
        $(this).attr("placeholder","");
        $(this).css({"text-fill-color":"#d1def5","font-family":"微软雅黑"});
    });
    $(":text").blur( function () {
    	if($(this).val()){
    		$(this).css({"text-fill-color":"#d1def5","font-family":"微软雅黑"});	
    	}else{
    		$(this).attr("placeholder","请输入用户名").css({"text-fill-color":"#42718f","font-family":"微软雅黑"});
    	}
    });
    $(":password").blur( function () {
    	if($(this).val()){
    		$(this).css({"text-fill-color":"#d1def5","font-family":"微软雅黑"});	
    	}else{
    		$(this).attr("placeholder","请输入密码").css({"text-fill-color":"#42718f","font-family":"微软雅黑"});
    	}

   	    
   });
   
    $(".circle_pure").mouseenter(function () {
        $(this).addClass("on");
    });
 //关闭错误提示框
    $scope.close=function(){
		$(".tip_error").css("display","none");
	};
}]);