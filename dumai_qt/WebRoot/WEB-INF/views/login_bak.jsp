<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>风控管理系统</title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,initial-scale=1.0" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
	<link rel="stylesheet" href="<%=path%>/css/login.css">
	<script language="Javascript">
  	
		function login()
		{
		    var user= document.loginForm.name.value;
			var pass= document.loginForm.pass.value;
			if(user==null || user.length==0)
			{
				alert("请输入用户名");
				document.loginForm.name.focus();
				return false;
			}
			if(pass==null || pass.length==0)
			{
				alert("请输入密码");
				document.loginForm.pass.focus();
				return false;
			}
			
			var inputNum= document.loginForm.inputNum.value;
			if(inputNum==null || inputNum.length==0)
			{
				alert("请输入验证码");
				document.loginForm.inputNum.focus();
				return false;
			}
			document.loginForm.submit();
		}
		
		function f_enter(){
		    if (window.event.keyCode==13){
		        login();
		    }
		}
		
	</script>
</head>
<body>
	 <div class="background" >
	 	<img src="<%=path%>/images/login-bg.png">
	 </div>
	 <div class="box">
		<div class="logo">
			<img src="<%=path%>/images/logo.png" alt="读脉">
		</div>
		<div class="login_win">
			<p>风控管理平台</p>
			<form class="form-post" name="loginForm" action="<%=path%>/login.do" method=post>
				<div class="u_name" id="user_name">
					<input class="name" type="text" name="name" value="${userName }" placeholder="&nbsp;&nbsp;&nbsp;&nbsp;请输入用户名/手机号/邮箱" onfocus="onfocus_usename()" onblur="onblur_usename()" /> 
				</div>
				<div class="u_pwd" id="user_pwd">
					<input class="pwd" type="password" name="pass" value="" placeholder="&nbsp;&nbsp;&nbsp;&nbsp;请输入密码" onfocus="onfocus_pwd()" onblur="onblur_pwd()"/>
				</div>
				<div class="div_verify_code">
					<div class="verify_code_2">
						<h3 class="number">${num1}&nbsp;${arr}&nbsp;${num2}=&nbsp;</h3>
					</div>
					<div class="code_div">
						<input class="verify_code_1" type="number" name="inputNum" placeholder="整数" onKeyPress="javascript:f_enter()" />
					</div>
					<div class="change_one">
						<span>
							<a href="javascript:window.location.reload()"  id="mblMsg" >换一张</a>
							<br><font color="#ff0000">${message}</font>
						</span>
					</div>
				</div>
				<div class="submit-btn">
					<input type="button" name="bsearch" value="登  录" onclick="javascrpt:login();"  class="js-submit" />
				</div>
				<div class="login_bottom">
					<div class="div_remember_use">
						<input type="checkbox"  class="remember_use" name="rememberme" id="rememberme" value="yes"  checked="checked" >
						<span class="checkbox_font">记住我</span>
					</div>
					<span class="forget_password"><a href="#">忘记密码 ?</a></span>
				</div>
         		<input type="hidden" name="resultJYZSOA" value="${result}" /> 
			</form>
		</div>
	</div>	
</body>
</html>