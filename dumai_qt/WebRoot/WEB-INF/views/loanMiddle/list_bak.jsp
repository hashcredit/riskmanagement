<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>贷中跟踪</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/extension/datagrid-dnd.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/global.js"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	<script type="text/javascript" lang="">
		var page_url = '${ctx}/loanMiddle/list.do';
		var headtype_url  = '${ctx}/loan/headtype.do';
		function onManualAuditSuccess(){
			$("#pageList").datagrid("reload");
		}
		function openWin(url,name){
			var win=window.open(url,name,'width='+ (screen.availWidth*0.9) +',height='+ (screen.availHeight*0.9) +',left='+ (screen.availWidth*0.05) +',top='+(screen.availHeight*0.01) +',toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes',true);  	
			win.focus();
	    	return win;
		}
		
		//easyUI分页列表
		$(function(){
			
			loadPage();
			//setTimeout(loadPage,50);//防止ie 主题未加载完毕时表格列重叠的问题
		});
		
		function loadPage(){
			$('#pageList').datagrid({
				title:'贷中跟踪', //标题
				url  :page_url,
				columns:[[
					{field:'ID',title:'id',width:8,hidden:true},
					{field:'type_name',title:'业务类型',width:30},
					{field:'createtime',title:'提交日期',width:30,formatter:$.getFormatter("yyyy.MM.dd")},
					{field:'name',title:'姓名',width:25},
					{field:'card_num',title:'身份证号码',width:40},
					{field:'status1',align:'center',title:'自动审核',width:20,
						formatter:function(v,r,i){
							return ["未审核","通过","拒绝","错误"][v]||"";
						}
					},
					{field:'hit_counts',align:'center',title:'规则命中',width:20},
					{field:'organization',align:'center',title:'公司名称',width:40},
					//{field:'',title:'信用评分',width:80},
					{field:'audit_person',align:'center',title:'审核人',width:10}
					<common:permission permission="view-report" >
					 ,{field:'report_view',title:'报告',width:10,align:'center',  
			            formatter:function(value,rec){  
			            	var code=rec.code;
			            	var url  = "${ctx}/loanMiddle/toReport.do?code="+code +"&type_code=" + rec.thetype ;
			                var btn = "<a onclick='openWin(\""+url+"\",\"报告\")'>查看</a>";  
			                return btn;  
			            }
			        },
					</common:permission>
			       {field:'is_alarm',align:'center',title:'GPS报警',width:10,
					   formatter:function(v,r,i){
			           		var btn = '无';
			           		if('1' == v){
			           		    btn = "<font style='color:red;font-weight:bold'>有</font>";
							}
			           		return btn;
					   }
			       },
			        {field:'gps_device',align:'center',title:'GPS设备维护',width:20,formatter:function(v,r,i){
			        	if(!(r.DeviceNumber && r.DeviceNumber_wireless)){
			        		return "<a onclick='gps_device("+i+")' >操作</a>"; 
			        	}
			        	else{
			        		return "已维护";
			        	}
			        }},
			        {field:'gps',align:'center',title:'GPS',width:15,formatter:function(v,r,i){
			        	if(r.DeviceNumber && r.DeviceNumber_wireless){
				        	return "<a onclick='openWin(\"${ctx}/loanMiddle/gps_route.do?code="+r.code+"\",\"GPS\")' >查看</a>";
			        	}
			        	else{
			        		return "请维护GPS";
			        	}
			        }}
				]],
				onLoadSuccess:function(){
					//$('#pageList').datagrid('enableDnd');
				}
		    });
			
			initHeadtype();
		}
		
		function initHeadtype(){
			$.ajax({
				url: headtype_url,
				type: 'post',
				success: function(data){
					$("#headtype").append("<label style='margin-right:10px;'><input type='radio' name='headtype' value=''/> 全部</label>");
					$.each(eval(data),function(n,value) {   
						$("#headtype").append("<label style='margin-right:10px;'><input type='radio' name='headtype' value='"+value.code+"'/> "+value.name+"</label>");
					});
				}
			});
		}
	
		//查询
		function searchList(){
		
			var queryParams = $('#pageList').datagrid('options').queryParams;
			queryParams.filter_startTime = $('#filter_startTime').datebox('getValue');
			queryParams.filter_endTime = $('#filter_endTime').datebox('getValue');
			queryParams.filter_headtype = $("input[name='headtype']:checked").val();
			queryParams.filter_keyword = $("#keyword").textbox("getValue");
			$('#pageList').datagrid("reload");
		}
		     	
		//清空查询条件   
		function clearForm(){
			$('#pageList'). datagrid('clearSelections');  
			$('.search-form').form("reset");
		}
		
		function gps_device(i){
			var data = $('#pageList').datagrid("getRows")[i];
			var code = data.code;
			$("#form-gpsdevice [textboxname='DeviceNumber']").textbox("setValue",data.DeviceNumber);
			$("#form-gpsdevice [textboxname='DeviceNumber_wireless']").textbox("setValue",data.DeviceNumber_wireless);
			$("#GpsDeviceWin").show().dialog({ 
				collapsible: false, 
				minimizable: false, 
				maximizable: false, 
				buttons: [{ 
						text: '保存', 
						iconCls: 'icon-ok',
						handler: function() {
							if(!$("#form-gpsdevice").form("validate")){
								return;
							}
							$.ajax({
								url:"${ctx}/loanMiddle/gps_device_upate.do",
								data:{
									code:code,
									DeviceNumber:$("#form-gpsdevice [textboxname='DeviceNumber']").textbox("getValue"),
									DeviceNumber_wireless:$("#form-gpsdevice [textboxname='DeviceNumber_wireless']").textbox("getValue")
								},
								dataType:"json",
								type:"post",
								success:function(result){
									if(result.code==0 && result.body==true){
										alert("维护成功");
										$('#GpsDeviceWin').dialog("close");
										$('#pageList').datagrid("reload");
									}
									else{
										alert("维护失败");
									}
								},
								error:function(){
									alert("维护失败");
								}
							});
						}
					},{ 
						text: '取消',
						iconCls: 'icon-cancel', 
						handler: function() { 
							$('#GpsDeviceWin').dialog('close');
						} 
					}
				] 
			}); 
		}
	</script>
	<style type="text/css">
		.radio-grid{
			padding-right: 20px;
		}
		#headtype{
			height: 50%;
		}
	</style>
	</head>
	<body class="easyui-layout" data-options="fit:true,border:false">
	
		<common:head-tabs tabs="[
			{href:'loanMiddle/toLoanMiddle.do',name:'贷中跟踪'},
			{href:'loanMiddle/toRuleList.do',name:'规则设定'}
		]"/>
		<div data-options="region:'center',border:false" >
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'north',border:false" style="height:140px;">
					<form class="search-form">
						<table >
							<tr>
								<td class="label">业务类型:</td>
								<td id="headtype" >
								</td>
							</tr>
							<tr>
								<td class="label">关键字:</td>
								<td id="rule-group-code">
									<input type="text" class="easyui-textbox" prompt="请填写关键字" id="keyword" />
								</td>
							</tr>
							<tr>
								<td class="label">提交日期:</td>
								<td>
									<input type="text" class="easyui-datetimebox" id="filter_startTime" name="filter_startTime" size="20"/>
									至
									<input type="text" class="easyui-datetimebox" id="filter_endTime" name="filter_endTime" size="20"/>
									
									<a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-cancel">清空</a>
									<a href="javascript:void(0)" onclick="searchList();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</td>
							</tr>
						</table>
					</form>
				
				</div>
				<div data-options="region:'center',border:false" >
			    	<table id="pageList"></table>
			    </div>
			</div>
		</div>
		<div id="GpsDeviceWin" style="display:none;width:400px;height:200px;" title="GPS设备维护">
			<form class="form-input" id="form-gpsdevice">
			<table class="add-input" style="width:100%" >
				<tr >
					<td class="label">有线设备编号</td>
					<td><input name="DeviceNumber" required class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">无线设备编号</td>
					<td><input name="DeviceNumber_wireless" required class="easyui-textbox"/></td>
				</tr>
			</table>
			</form>
		</div>
	</body>
</html>