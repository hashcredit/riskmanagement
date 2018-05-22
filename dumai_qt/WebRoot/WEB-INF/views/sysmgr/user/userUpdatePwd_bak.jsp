<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common/"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%
	String root=request.getContextPath();//获取项目根目录名称
%>
<!DocType html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改密码</title>
		<script type="text/javascript" src="<%=root%>/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script type="text/javascript" src="<%=root%>/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=root%>/js/global.js"></script>
		<link rel="stylesheet" href="<%=root%>/css/amazeui.css" />
		<link rel="stylesheet" type="text/css" href="<%=root%>/js/lib/jquery-easyui-1.5/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=root%>/js/lib/jquery-easyui-1.5/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=root%>/css/app.css">
		<script type="text/javascript">
		
			function updatePwd(){
				var pass = $("#form-updatePwd").form("validate");
				if(!pass) return;
				
				$.ajax({
					url:"${ctx}/sysmgr/upateUserPwd.do",
					dataType:"json",
					type:"post",
					data:{
						pwd:$("#form-updatePwd [textboxname='pwd']").textbox("getValue")
					},
					success:function(result){
						if(result.code==0 && result.body==true){
							alert("修改成功");
						}
						else{
							alert("修改失败");
						}
					},
					error:function(){
						alert("修改失败");
					}
				});
			}
		</script>
		<style type="text/css">
			.add-input td{
				border: none;
			}
		</style>
	</head>
	<body class="easyui-layout" data-options="fit:true">
		<common:head-tabs tabs="[
			{href:'sysmgr/toUpateUserPwd.do',name:'修改密码'},
			{href:'sysmgr/funsettings/toPage.do',name:'功能设定',isLeader:['1','2']},
			{href:'sysmgr/toUserList.do',name:'用户管理',isLeader:['1','2']},
 			//{href:'cost/toList.do',name:'对帐信息',isLeader:['1','2']}, 
			{href:'sysmgr/toLogList.do',name:'日志',isLeader:['1','2']}
		]"/>
		<div data-options="region:'center',border:false">
			<!-- <div id="updatePwdWin" style="width:300px;height:200px;" title="修改用户"> -->
			<form class="form-input" id="form-updatePwd" style="height: 100%;width: 100%">
				<div style="margin: 0 auto;width:30%;padding-top: 100px;max-width: 400px">
				
				<table class="add-input" style="width: 100%;height:150px" >
					
					<tr>
						<td class="label">新密码</td>
						<td><input name="pwd" id="pwd" class="easyui-passwordbox" style="width: 100%;height: 30px" prompt="密码为6位数字" validType="numberLength[6,'密码必须是六位数字']" required="required"/></td>
					</tr>
					<tr>
						<td class="label">确认密码</td>
						<td><input id="pwd_confirm" class="easyui-passwordbox" width="100%" prompt="密码为6位数字,且须与上方密码一致"  style="width: 100%;height: 30px"  validType="equalTo['#pwd','与新密码不一致']" class="easyui-textbox" required="required"/></td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: right;">
							<a href="#" class="easyui-linkbutton" style="padding:3px 50px" data-options="iconCls:'icon-save',onClick:updatePwd">保存</a>
						</td>
					</tr>
				</table>
				</div>
			</form>
			<!-- </div> -->
		</div>
	</body>
</html>