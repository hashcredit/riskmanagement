<%
	String root=request.getContextPath();//获取项目根目录名称
%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <link id="mylink" rel="stylesheet" type="text/css" href="<%=root%>/css/index_1.css">
	<title>风控管理平台</title>
	<style type="text/css">
		a{
			cursor: pointer;
		}
		.disabled{
			/* background: #cccbcb; */
			cursor: pointer;
		}
		#userinfo{
			position: absolute;bottom:20px;right:0px;color:#fff;font-weight: bold;
		}
	</style>
</head>

<body>
	<div class="box">
			 <div class="bg_img">    
		        <img src="<%=root%>/images/total_bg.png" class="stretch" alt="" />    
		    </div>
		    <div style="width:90%;margin:auto;height:23%">
			<div class="div_nav">
				<div>
					<img src="<%=root%>/images/nav_bg.png" class="title_bg" alt="" />
				</div>
				<div id="userinfo">
					${sessionScope.login.username} [${sessionScope.login.username}]&nbsp;<a onclick="window.location.href='<%=root%>/logout.do'">退出</a>
				</div>
			</div>
			</div>
			<div class="div_section">
				<a href="<%=root%>/loanFront/toLoanFront.do" class="btn">
				<div class="section_one">
					<p class="section_bold">贷前审核</p>
					<span class="section_padding"></span><br>
					<img src="<%=root%>/images/after_login_one.png" alt="" class="inner_img">
				</div>
				</a>
				
				<a href="<%=root%>/loanMiddle/toLoanMiddle.do" class="btn">
				<div class="section_two disabled">
					<p class="section_bold">贷中跟踪</p>
					<span class="section_padding"></span><br>
					<img src="<%=root%>/images/after_login_two.png" alt="" class="inner_img">
				</div>
				</a>
				
				<div class="divide_line_first"></div>
				
				<div class="section_four disabled">
					<p class="section_bold">数据管理</p>
					<img src="<%=root%>/images/after_login_four.png" alt="" class="inner_img">
				</div>
				
				<div class="section_three disabled">
					<p class="section_bold">参数设置</p>
					<img src="<%=root%>/images/after_login_three.png" alt="" class="inner_img">
				</div>
				
				<div class="clear"></div>
				<div class="divide_line"></div>
				<a href="<%=root%>/sysmgr/toUpateUserPwd.do" class="btn">
				<div class="section_five">
					<p class="section_bold">系统管理</p>
					<img src="<%=root%>/images/after_login_five.png" alt="" class="inner_img">
				</div>
				</a>
				
				<div class="section_six disabled">
					<p class="section_bold">趋势分析</p>
					<img src="<%=root%>/images/after_login_six.png" alt="" class="inner_img">
				</div>

				<a href="<%=root%>/loanOverdue/toList.do" class="btn">
				<div class="section_seven disabled">
					<p class="section_bold">逾期催款</p>
					<span class="section_padding"></span><br>
					<img src="<%=root%>/images/after_login_seven.png" alt="" class="inner_img">
				</div>
				</a>
				
				<div class="divide_line_last"></div>
				
				<div class="section_eight disabled">
					<p class="section_bold">行业资讯</p>
					<img src="<%=root%>/images/after_login_eight.png" alt="" class="inner_img">
				</div>
				
				<a href="<%=root%>/docs/" target="_blank" class="btn">
				<div class="section_nine">
					<p class="section_bold">产品说明</p>
					<img src="<%=root%>/images/after_login_nine.png" alt="" class="inner_img">
				</div>
				</a>
				
			</div>
			<div class="clear"></div>
			<br>
			<div class="div_footer"><span class="footer_font">
			 北京读脉科技有限公司
			</span></div>
		</div>
</body>
</html>
