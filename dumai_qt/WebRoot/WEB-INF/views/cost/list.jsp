<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>对帐信息</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/global.js" data-ctx="${ctx}" id="globalJs"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">

	<script type="text/javascript">
        var page_url = '${ctx}/cost/list.do';

        $(function(){
            var sub_entity_id = '${sub_entity_id}';
            if('' == sub_entity_id || null == sub_entity_id){
                alert("您的登录已失效，请重新登录");
                window.location.href =  '${ctx}/tologin.do';
            }
            //读脉数据源列表
            $('#pageList').datagrid({
                url  :page_url,
                loadMsg:'数据载入中，请稍后……',
                method:"post",
                nowrap: false,
                striped: true,
                fit:true,
                fitColumns: true,
                singleSelect:true,
                rownumbers:true,
                remoteSort: false,
                pageList:[5,10,20,50],
                pageSize:20,
                pagination:true,//分页属性
                queryParams: {sub_entity_id:sub_entity_id}, //查询条件
                width:fixWidth(0.99),
                columns:[[
                    {field:'code',title:'id',hidden:true},
                    {field:'description',title:'描述',width:40},
                    {field:'cost',title:'消费金额',width:20},
                    {field:'opttime',title:'操作时间',width:20,formatter:$.getFormatter("yyyy-MM-dd HH:mm:ss")}
                ]], //要展示的数据结束
                onLoadSuccess:function(){
                    getTotalCost('${sub_entity_id}')
                    $('#pageList').datagrid('clearSelections');
                },
                onDblClickRow:function()//双击如果复选框选中则取消
                {
                    var objs = $('#pageList').datagrid('getSelected');
                    if (objs != null || objs != '') {
                        $('#pageList').datagrid('clearSelections');
                    }
                }
            });
        });

        //计算页面宽度方法,easyui调用
        function fixWidth(percent){
            return document.body.clientWidth * percent ;
        }

        //计算总消费
        function getTotalCost(sub_entity_id){
            $.ajax({
                url:"${ctx}/cost/totalCost.do",
                data:{sub_entity_id:sub_entity_id},
                type:"post",
                success:function(result){
                   $("#totalCost").html(result);
                }
            });
        }
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
<!-- 搜索框-->
<div data-options="region:'center',border:false" >
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" style="height:50px;line-height:50px;overflow: hidden">
			总消费：<span id="totalCost"></span>
		</div>
		<div data-options="region:'center',border:false" >
			<table id="pageList"></table>
		</div>
	</div>
</div>

</body>
</html>