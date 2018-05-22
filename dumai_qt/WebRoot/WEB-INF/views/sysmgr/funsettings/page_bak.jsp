<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %> 
<!DocType html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>功能设定</title>
		<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/global.js"></script>
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
		<script type="text/javascript">
			$(function(){

				$("input:checkbox[name='ip_access_ctrl']").change(function(){
					var $this = $(this);
					var checked = $this.prop("checked");
					if(checked) $("#white_ips-wrapper").show(0);
					else  $("#white_ips-wrapper").hide(0);
					
				});
				
				$.ajax({
					url:"${ctx}/sysmgr/funsettings/load.do",
					type:"get",
					success:function(result){
						if(result.code==0){
							var body = result.body;
							var function_settings = $.parseJSON(body.function_settings);
							var white_ips = body.white_ips;
							for(var k in function_settings){
								if(function_settings[k]==1){
									$("input:checkbox[name='"+k+"']").prop("checked",true);
								}
							}
							$("textarea[name='white_ips']").val(white_ips);
							$("input:checkbox[name='ip_access_ctrl']").change();
						}
					}
				});
			});
			
			function tabSelect(title,index){
				var loadFuns = [,,dataViewTypeLoad];
				var fn = loadFuns[index];
				if(fn) fn();	
			}
			
			function bizLoad(){
				$("#bizForm").form("reset");
				$.ajax({
					url:"${ctx}/sysmgr/funsettings/bizLoad.do",
					type:"get",
					data:{
						type_code:$("#type").combobox("getValue")
					},
					success:function(result){
						if(result.code==0){
							var body = result.body;
							var report_para = $.parseJSON(body.report_para);
							for(var k in report_para){
								if(report_para[k]==1){
									$("input:checkbox[name='"+k+"']").prop("checked",true);
								}
							}
                            var rule_model = body.rule_model;
                            if(2 == rule_model){
                                $("input[name='loanfront_rule'][value='2']").attr("checked",true);
                            }
							$("#bizForm input[name='code']").val(body.code);
						}
					}
				});
			}
			
			function dataViewTypeLoad(){
				$("#dataView-type").combobox("reload","${ctx}/sysmgr/funsettings/type.do");
			}
			function dataViewListLoad(){
				$('#dataViewList').datagrid({
					//title:'原始数据接口列表',
					singleSelect:false,
					pagination:false,
					border:false,
					onUncheckAll:function(){
						var rows = $('#dataViewList').datagrid("getRows");
						for(var index in rows){
							if(rows[index].checkedByGuize>0){
								$('#dataViewList').datagrid("checkRow",index);
							}
						}
					},
					onUncheck:function(index,row){
						if(row.checkedByGuize>0){
							$('#dataViewList').datagrid("checkRow",index);
						}
					},
					url  :"${ctx}/sysmgr/funsettings/dataViewLoad.do?type_code="+$("#dataView-type").combobox("getValue"),
					columns:[[
						{field:'code',halign:'center',title:'code',checkbox:true,formatter:function(v,r,i){
							var disabled = r.checkedByGuize>0?"disabled":"";
							var checked = r.checkedByGuize>0?"checked":r.checked>0?"checked":"";
							return "<input " + disabled + " "+checked+" type='checkbox' name='code' value='"+v+"'>";
						}},
						{field:'name',halign:'center',title:'名称',width:10},
						{field:'description',halign:'center',title:'描述',width:40},
						{field:'cost_out',halign:'center',title:'价格',width:10,hidden:true}/* ,
						{field:'operate',title:'范例',align:'center',width:5,
							formatter:function(v,r,i){
								return "<a href='javascript:;' onclick='viewDemo()' >查看</a>";
							}
						} */
					]]
			    });
			}
			
			function doSave(){
				var saveFuns = [doSysSave,doBizSave,doDataViewSave];
				var tab = $("#tabs").tabs("getSelected");
				var index = $("#tabs").tabs("getTabIndex",tab);
				saveFuns[index]();
			}
			
			function doSysSave(){
				var data = $("#pageForm").serialize();
				$.ajax({
					url:"${ctx}/sysmgr/funsettings/save.do",
					type:"post",
					data:data,
					success:function(result){
						if(result.code==0 && result.body===true){
							$.messager.alert("功能设置", "保存成功", "icon-info");
						}
						else{
							$.messager.alert("功能设置", "保存失败", "icon-info");
						}
					},
					error:function(){
						$.messager.alert("功能设置", "保存失败", "icon-info");
					}
				});
			}
			
			function doBizSave(){
				var data = $("#bizForm").serialize();
				$.ajax({
					url:"${ctx}/sysmgr/funsettings/bizSave.do",
					type:"post",
					data:data,
					success:function(result){
						if(result.code==0 && result.body===true){
							$.messager.alert("功能设置", "保存成功", "icon-info");
						}
						else{
							$.messager.alert("功能设置", "保存失败", "icon-info");
						}
					},
					error:function(){
						$.messager.alert("功能设置", "保存失败", "icon-info");
					}
				});
			}
			
			function doDataViewSave(){
				var codes = $("#dataViewPanel input[name='code']:checked:not(:disabled)");
				var data = codes.serialize()+"&type_code="+$("#dataView-type").combobox("getValue");
				$.ajax({
					url:"${ctx}/sysmgr/funsettings/dataViewSave.do",
					type:"post",
					data:data,
					success:function(result){
						if(result===true){
							$.messager.alert("功能设置", "保存成功", "icon-info");
						}
						else{
							$.messager.alert("功能设置", "保存失败", "icon-info");
						}
					},
					error:function(){
						$.messager.alert("功能设置", "保存失败", "icon-info");
					}
				});
			}
			
		</script>
		<style type="text/css">
			.sub-fun-list{
				padding-left: 20px;
				font-size:14px;
			}
			.sub-fun-list>div{
				margin: 4px 0;
			}
			
			.sub-block:not(:last-child){
				padding:5px 0;
				border-bottom: inset 1px #d5dbe4;
			}
			.sub-block:last-child{
				padding:5px 0;
				border-bottom: none;
			}
			
			.sub-title{
				font-size:15px;
			}
			.fit-from{
				padding: 0;
				margin: 0;
				width: 100%;
				height: 100%;
			}
			.form{
				padding: 0;
				margin: 0;
			}
			
			#footer{
				padding: 15px 0;
				text-align: center;
			}
			#white_ips{
				display: block;
				resize:none;
				width: 100%;
				outline: none;
				height: 120px;
			}
			#white_ips-wrapper{
				vertical-align: bottom;
				display: none;
			}
			.setting-group:not(:first-of-type){
				border-top:solid 1px #95B8E7;
			}
			.tabs-title {
			    font-size: 15px;
			}
			.top-panel{
				border-bottom:solid 1px #95B8E7;
			}
		</style>
	</head>
	<body class="easyui-layout" data-options="fit:true,border:false">
		<common:head-tabs tabs="[
			{href:'sysmgr/toUpateUserPwd.do',name:'修改密码'},
			{href:'sysmgr/funsettings/toPage.do',name:'功能设定',isLeader:['1','2']},
			{href:'sysmgr/toUserList.do',name:'用户管理',isLeader:['1','2']},
 			//{href:'cost/toList.do',name:'对帐信息',isLeader:['1','2']}, 
			{href:'sysmgr/toLogList.do',name:'日志',isLeader:['1','2']}
		]"/>
		<div data-options="region:'center',border:false,footer:'#footer'">
			<div class="easyui-tabs" id="tabs" data-options="fit:true,border:false,tabPosition:'top',tabHeight:35,tabWidth:150,onSelect:tabSelect" >
				<div title="安全设定"  >
					<form id="pageForm" class="fit-from">
						<div data-options="border:false,title:'通用',headerCls:'setting-group',width:'100%'" class="easyui-panel" style="padding:0px 20px">
							<div class="sub-block">
								<div class="sub-title"><input name="ip_access_ctrl" value="1"  type="checkbox"/>IP访问控制</div>
								<div class="sub-fun-list">
									<div id="white_ips-wrapper" ><textarea id="white_ips" placeholder='多个ip用","分隔' name="white_ips"></textarea>注：多个ip用","分隔</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div title="审核方式">
					<div class="easyui-panel" data-options="fit:true,border:false">
						
						<div data-options="border:false,width:'100%',height:42" class="easyui-panel top-panel" style="padding:7px 20px">
							业务类型 <input id="type" class="easyui-combobox" name="type" data-options="editable:false,onChange:bizLoad,
								valueField:'code',panelHeight:'auto',panelMaxHeight:300,method:'GET',textField:'name',url:'${ctx}/sysmgr/funsettings/type.do',
								onLoadSuccess:function(){var data = $(this).combobox('getData');$(this).combobox('select',data[0].code);}
								" />  
						</div>
						<form class="form" id="bizForm">
						<input type="hidden" name="code" />
						<div style="display: none">
						<div data-options="border:false,title:'订单录入',headerCls:'setting-group',width:'100%'" class="easyui-panel" style="padding:0px 20px;">
							<div class="sub-block">
								<div  class="sub-title"><input name="inputorder_mobile_validation" checked="checked" value="1" type="checkbox"/>手机实名认证校验</div>
								<div  class="sub-title"><input name="inputorder_bank_validation" checked="checked" value="1"  type="checkbox"/>银行卡实名认证校验</div>
							</div>
						</div>
						</div>
						<div data-options="border:false,title:'贷前',headerCls:'setting-group',width:'100%'" class="easyui-panel" style="padding:0px 20px">
							<div class="sub-block" id="ruleOrMode">
								<div class="sub-title"><input name="loanfront_rule" value="1"  type="radio" checked/>规则<input name="loanfront_rule" value="2"  type="radio"/>模型</div>
							</div>
							<!-- <div class="sub-block">
								<div class="sub-title">报告</div>
								<div class="sub-fun-list" id="report">
									<div><input name="loanfront_report3" type="checkbox" value="1" />涉诉</div>
									<div><input name="loanfront_report10" type="checkbox" value="1" />通信运营商数据</div>
									<div><input name="loanfront_report4" type="checkbox" value="1" />银行卡验证</div>
									<div><input name="loanfront_report8" type="checkbox" value="1" />车辆基本信息</div>
									<div><input name="loanfront_report6" type="checkbox" value="1" />同住人信息</div>
									<div><input name="loanfront_report14" type="checkbox" value="1" />公积金</div>
									<div><input name="loanfront_report15" type="checkbox" value="1" />犯罪</div>
								</div>
							</div> -->
						</div>
						<%-- <div data-options="border:false,title:'贷中',headerCls:'setting-group',width:'100%'" class="easyui-panel" style="padding:0px 20px">
							<div class="sub-block">
								<div class="sub-title"><input name="loanmiddle_rule" value="1"  type="checkbox"/>规则</div>
							</div>
							<div class="sub-block">
								<div class="sub-title">报告</div>
								<div class="sub-fun-list">
									<div><input name="loanmiddle_report3" type="checkbox" value="1" />涉诉</div>
									<div><input name="loanmiddle_report10" type="checkbox" value="1" />通信运营商数据</div>
									<div><input name="loanmiddle_report4" type="checkbox" value="1" />银行卡验证</div>
									<div><input name="loanmiddle_report8" type="checkbox" value="1" />车辆基本信息</div>
									<div><input name="loanmiddle_report6" type="checkbox" value="1" />同住人信息</div>
									<div><input name="loanmiddle_report14" type="checkbox" value="1" />公积金</div>
									<div><input name="loanmiddle_report15" type="checkbox" value="1" />犯罪</div>
								</div>
							</div>
						</div> --%>
					</form>
					</div>
				</div>
				<div title="数据查询设定">
					<div id="dataViewPanel" class="easyui-panel" data-options="fit:true,border:false">
						<table id="dataViewList" data-options="toolbar:'#dataViewList-toolBar'"></table>
					</div>
					<div id="dataViewList-toolBar" data-options="border:false,width:'100%',height:42" class="easyui-panel top-panel" style="padding:7px 20px">
						<span style="vertical-align: middle">业务类型</span> <input id="dataView-type" class="easyui-combobox" name="type" data-options="editable:false,onChange:dataViewListLoad,
							valueField:'code',panelHeight:'auto',panelMaxHeight:300,method:'GET',textField:'name',
							onLoadSuccess:function(){var data = $(this).combobox('getData');$(this).combobox('select',data[0].code);}
							" />  
					</div>
				</div>
			</div>
		</div>
		<div id="footer">
			<a class="easyui-linkbutton" data-options="iconCls:'icon-ok',width:100,height:30,onClick:doSave">应用</a>
		</div>
	</body>
</html>