<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %> 
<!DocType html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>日志管理</title>
		<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/global.js"></script>
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<%-- <link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/default/easyui.css"> --%>
		<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	   
		<script type="text/javascript">
			$(function(){
				$('#pageList').datagrid({
					title:'日志列表', //标题
					url  :"${ctx}/sysmgr/logList.do",
					loadMsg:'数据载入中，请稍后', 
					method:"post",
					nowrap: false,
					striped: true,
					fitColumns: true,
					singleSelect:true,
					fit:true,
					rownumbers:true,
					border:false,
					remoteSort: false,
					pageList:[1,5,10,20,50],
					pageSize:10,
					pagination:true,//分页属性
					queryParams: {}, //查询条件
					columns:[[
						{field:'code',title:'id',hidden:true},
						{field:'user_name',title:'用户名',width:20},
						{field:'user_surname',title:'姓名',width:20},
						{field:'logtime',title:'记录时间',width:15,formatter:$.getFormatter("yyyy-MM-dd HH:mm:ss")},
						{field:'ipadress',title:'IP地址',width:20},
						{field:'content',title:'操作内容',width:25}
					]]
			    });
			});
		</script>
	</head>
	<body class="easyui-layout" data-options="fit:true,border:false">
		<common:head-tabs tabs="[
			{href:'sysmgr/toUpateUserPwd.do',name:'修改密码'},
			{href:'sysmgr/funsettings/toPage.do',name:'功能设定',isLeader:['1','2']},
			{href:'sysmgr/toUserList.do',name:'用户管理',isLeader:['1','2']},
			//{href:'cost/toList.do',name:'对帐信息',isLeader:['1','2']}, 
			{href:'sysmgr/toLogList.do',name:'日志',isLeader:['1','2']}
		]"/>
		<div data-options="region:'center',border:false">
			<table id="pageList"></table>
		</div>
	</body>
</html>