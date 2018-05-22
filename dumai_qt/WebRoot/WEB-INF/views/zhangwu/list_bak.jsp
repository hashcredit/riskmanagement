<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>分期列表</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/global.js"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
   	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
<script>

var page_url = '${ctx}/loanOverdue/stagesList.do?orderId=${param.orderId}&fk_orderinfo_code=${param.fk_orderinfo_code}';
console.log('${param.name}');




$(function(){


	$('#pageList').datagrid({
		title:'分期情况', //标题
		url  :page_url,
		loadMsg:'数据载入中，请稍后……',
		method:"post",
		nowrap: false,
		striped: true,
		fit:true,
		borrder:false,
		fitColumns: true,
		singleSelect:true,
		rownumbers:false,//序号
		remoteSort: false,
		pageList:[5,10,20,50],
		pageSize:20,
// 		idField:'id',
		pagination:true,//分页属性
		queryParams: {}, //查询条件

		columns:[[
// 			{field:'code',title:'code',width:80,hidden:true},
			{field:'REPAYNO',title:'还款序号',width:10,align:'center'},
			{field:'LASTREPAYDATE',title:'最后还款日',width:20,align:'center'},
			{field:'CUR_REPAY_AMT',title:'本期应还总额',width:20,align:'center',
                formatter:function(value){
                    return value / 100;
                }
            },
			{field:'CUR_REPAY_PRINCIPAL',title:'本期应还本金',width:20,align:'center',
                formatter:function(value){
                    return value / 100;
                }
			},
			{field:'CUR_REPAY_OVERDUE_INTEREST',title:'本期应还逾期利息',width:20,align:'center',
                formatter:function(value){
                    return value / 100;
                }
			},
            {field:'PRE_OVERDUE_INTEREST',title:'每日滞纳金',width:20,align:'center',
                formatter:function(value){
                    return value / 100;
                }
            },
//			{field:'FIXED_OVERDUE_AMT',title:'违约金',width:20,align:'center',
//                formatter:function(value){
//                    return value / 100;
//                }
//			},
			{field:'OVERDUE_DAYS',title:'逾期天数',width:20,align:'center'},
			{field:'CUR_REPAYDATE',title:'还款时间',width:20,align:'center'}
		]], //要展示的数据结束
        onLoadSuccess:function(){
          $('#pageList').datagrid('clearSelections');
            $("#name").val('${param.name}');
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
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	 <%--<common:head-tabs />--%>
	 <div region="center" data-options="border:false">
		<table id="pageList"></table>
	 </div>
	 <%--<div class="easyui-layout" data-options="fit:true,border:false">--%>
		 <%--<div data-options="region:'north',border:false" style="height:140px;">--%>
			 <%--<form class="search-form">--%>
				 <%--<table >--%>
					 <%--<tr>--%>
						 <%--<td class="label">姓名:</td>--%>
						 <%--<td id="name" >--%>
						 <%--</td>--%>
					 <%--</tr>--%>
					 <%--<tr>--%>
						 <%--<td class="label">关键字:</td>--%>
						 <%--<td id="rule-group-code">--%>
							 <%--<input type="text" class="easyui-textbox" prompt="姓名/身份证号码" id="keyword" />--%>
						 <%--</td>--%>
					 <%--</tr>--%>
					 <%--<tr>--%>
						 <%--<td class="label">提交日期:</td>--%>
						 <%--<td>--%>
							 <%--<input type="text" class="easyui-datetimebox" id="filter_startTime" name="filter_startTime" size="20"/>--%>
							 <%--至--%>
							 <%--<input type="text" class="easyui-datetimebox" id="filter_endTime" name="filter_endTime" size="20"/>--%>

							 <%--<a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-cancel">清空</a>--%>
							 <%--<a href="javascript:void(0)" onclick="searchList();" class="easyui-linkbutton" iconCls="icon-search">查询</a>--%>
						 <%--</td>--%>
					 <%--</tr>--%>
				 <%--</table>--%>
			 <%--</form>--%>

		 <%--</div>--%>
		 <%--<div data-options="region:'center',border:false" >--%>
			 <%--<table id="pageList"></table>--%>
		 <%--</div>--%>
	 <%--</div>--%>
</body>
</html>