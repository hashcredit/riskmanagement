<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>

<!DOCTYPE HTML>
<html lang="en" ng-app="myLogin">
<head>
<meta charset="UTF-8">
<title>风控管理系统</title>
<meta name="viewport"
	content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,initial-scale=1.0" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<link rel="stylesheet" href="${ctx}/static/css/load.css">
<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/static/dumai/global/css/waitMe.css">
<script src="${ctx}/static/script/lib/jquery.min.js"></script>
<script src="${ctx}/static/script/lib/angular.min.js"></script>
<script src="${ctx}/static/dumai/global/js/waitMe.js"></script>
<script src="${ctx}/static/script/controller/loginController.js"></script>

<script language="Javascript">
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
		function login(){
		    var user= document.loginForm.name.value;
			var pass= document.loginForm.pass.value;
			if(user==null || user.length==0)
			{
				$(".tip_error").css("display","block");
				$(".tipText").html('请输入您的用户名！')
				document.loginForm.name.focus();
				return false;
			}
			if(pass==null || pass.length==0)
			{
				$(".tip_error").css("display","block");
				$(".tipText").html('请输入您的密码！');
				document.loginForm.pass.focus();
				return false;
			}
			
			var inputNum= document.loginForm.inputNum.value;
			if(inputNum==null || inputNum.length==0)
			{
				$(".tip_error").css("display","block");
				$(".tipText").html('请输入验证码！');
				document.loginForm.inputNum.focus();
				return false;
			}
			/* loading效果*/
			function run_waitMe(id,effect){
		        $(id).waitMe({
		            effect: effect,
		            text: 'Please wait...',
		            bg: 'rgba(255,255,255,0.2)',
		            color:'white',
		            sizeW:'',
		            sizeH:'',
		            source: 'img.svg'
		        });
		    }
	         run_waitMe("#form-post",'roundBounce');
			 $(".circle").css({"transform":"rotate(360deg)","transition":"all 3s cubic-bezier(.11,.81,.92,.21) 0s "}); 
			 sessionStorage.username=user; 
			 document.cookie="userId="+user;
             sessionStorage.username=user;
			 if($(".extra img").css("display") == "block"){
				 document.cookie="username="+user;
			     document.cookie="password="+pass;
			 }else if($(".extra img").css("display") == "none"){
				 DelCookie("username");
			     DelCookie("password");
			 }
			 document.loginForm.submit(); 
		};
		
</script>
</head>
<body>
<div class="contain">
 <div class="allContent"  ng-controller="loginCtrl" id="allContent">
    <div class="contain">
        <div class="logo" id="logo">
        	<span class="loginLogo"></span>
        	<ul>
        		<li>风控管理平台</li>
        		<li>DuMai Risk Management System </li>
        	</ul>
        </div>
	    <form class="form-post" id="form-post" name="loginForm" action="/dumai_qt/login.do" method=post>
        <div class="circle_pure">
            <ul>
                <li class="user_load"><i class="icon_user"></i><input type="text" class="user userEnterBox" placeholder="请输入用户名" name="name" ng-model="user_name"></li>
                <li class="password_load" ><i class="icon_password"></i><input class="password userEnterBox" name="pass" placeholder="请输入密码" ng-model="user_password" onfocus="this.type='password'"></li>
                <li style="margin-top: 20px" class="yzm">
                    <div>
                    	<span ng-bind="num1" renderFinish></span><span ng-bind="arr" renderFinish></span><span ng-bind="num2" renderFinish></span>=
                    </div>
                    <input type="number" placeholder="整数" ng-model="value" name="inputNum" class="yam_num" >
                    <a href="javascript:;" ng-click="changeYzm()">换一张</a>
                </li>
                <li style="margin-top: 20px;padding-left: 0px">
                	<input type="button"  value="登录"  onclick="javascrpt:login();" class="sub" id="waitMe_ex" ng-click="loginCookie()" >
                </li>
                <li class="extra" style="margin-top: 10px" ng-click="extra()">
                	<label ng-click="remember()" class="load_label spriteIcon">
                		<img src="${ctx}/static/images/duigou.png" alt="选择" class="selectFlag">
                	</label> 
					<input type="checkbox">记住密码
				 </li>	
            </ul>
        </div>
        	<input type="hidden" name="resultJYZSOA"  ng-model="hidden" id="resultJYZSOA"/>
        </form>
        <div class="circle" id="circle"></div>
        <div class="tip_error">
          <span class="tipText"></span>
          <i class="close" ng-click="close();"></i>
        </div>
    </div>
</div>
</div>
</body>
<!--[if IE 6]>
<script type="text/javascript">
alert('浏览器版本低，请升级后打开')
</script>
<![endif]-->
<!--[if IE 7]>
<script type="text/javascript">
alert('浏览器版本低，请升级后打开')
</script>
<![endif]--> 
<!--[if IE 8]>
<script type="text/javascript">
alert('浏览器版本低，请升级后打开')
</script>
<![endif]--> 
<!--[if IE 9]>
<script type="text/javascript">
alert('浏览器版本低，请升级后打开')
</script>
<![endif]--> 
</html>
