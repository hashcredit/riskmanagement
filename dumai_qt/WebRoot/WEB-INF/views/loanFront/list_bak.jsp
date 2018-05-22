<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>贷前审核</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/extension/datagrid-dnd.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/global.js"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	<script type="text/javascript" lang="">
		var page_url = '${ctx}/loanFront/list.do';
		var headtype_url  = '${ctx}/rule/types.do';
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
				title:'贷前审核', //标题
				url  :page_url,
				columns:[[
					{field:'ID',title:'id',width:8,hidden:true},
					{field:'type_name',title:'业务类型',width:30},
					{field:'createtime',title:'提交日期',width:50,formatter:$.getFormatter("yyyy-MM-dd HH:mm:ss")},
					{field:'name',title:'姓名',width:25},
					{field:'card_num',title:'身份证号码',width:40},
					{field:'status1',align:'center',title:'自动审核',width:20,
						formatter:function(v,r,i){
							return ["未审核","通过","拒绝","错误"][v]||"";
						}
					},
					{field:'hit_counts',align:'center',title:'规则命中',width:20},
					{field:'organization',align:'center',title:'公司名称',width:40}
					//{field:'',title:'信用评分',width:80},
					<%--{field:'status2',align:'center',title:'人工审核',width:20,--%>
						<%--formatter:function(v,r,i){--%>
							<%--return ["未审核","通过","拒绝"][v]||"";--%>
						<%--}--%>
					<%--},--%>
					<%--{field:'audit_person',align:'center',title:'审核人',width:10},--%>
					<%--{field:'report_view2',title:'模型报告',width:10,align:'center',--%>
			            <%--formatter:function(value,rec){--%>
			            	<%--var code=rec.code;--%>
			            	<%--var url  = "${ctx}/report/toList.do?code="+code +"&type_code=" + rec.thetype ;--%>
			                <%--var btn = "<a onclick='openWin(\""+url+"\",\"报告\")'>查看</a>";--%>
			                <%--return btn;--%>
			            <%--}--%>
			        <%--}--%>
					<common:permission permission="view-report" >
					,{field:'report_view',title:'规则报告',width:10,align:'center',  
			            formatter:function(value,rec){  
			            	var code=rec.code;
			            	<!-- 演示用url -->
			            	<%--var url  = "${ctx}/newreport/toReport.do?code="+code +"&type_code=" + rec.thetype ;--%>
			            	<!-- 正式url -->
                            var url = "${ctx}/report/toReport.do?code=" + code;
			                var btn = "<a onclick='openWin(\""+url+"\",\"报告\")'>查看</a>";
			                return btn;  
			            }
			        }
					</common:permission>
			        <common:permission permission="delete-order" >
			        ,{field:'operate',title:'操作',width:10,align:'center',  
			            formatter:function(value,rec){  
			            	var code=rec.code;
			                var btn = "<a onclick='del(\""+code+"\")'>删除</a>";  
			                return btn;  
			            }  
			        }
			        </common:permission>
				]],
				onLoadSuccess:function(){
					//$('#pageList').datagrid('enableDnd');//启用拖拽实验性代码
				}
		    });
			
			initHeadtype();
		}
		
		function initHeadtype(){
			$.ajax({
				url: headtype_url,
				type: 'post',
				success: function(data){
					$("#headtype").append("<td class='radio-grid' ><label><input type='radio' name='headtype' value=''/> 全部</label></td>");
					$.each(eval(data),function(n,value) {   
						$("#headtype").append("<td class='radio-grid' ><label><input type='radio' name='headtype' value='"+value.code+"'/> "+value.name+"</label></td>");
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
			queryParams.filter_checkStatus = $("input[name='checkStatus']:checked").val();
			queryParams.filter_keyword = $("#keyword").textbox("getValue");
			$('#pageList').datagrid("reload");
		}
		     	
		//清空查询条件   
		function clearForm(){
			$('#pageList'). datagrid('clearSelections');  
			$('.search-form').form("reset");
		}
		
		function del(code){
			if(confirm("删除后无法恢复!确认删除?")){
				$.ajax({
					url:"${ctx}/loanFront/delete.do",
					type:"post",
					dataType:"json",
					data:{
						code:code
					},
					success:function(result){
						if(result.code==0){
							alert("删除成功");
							$("#pageList").datagrid("reload");
						}
						else{
							alert("删除失败");
						}
					},
					error:function(){
						alert("删除失败");
					}
				});
			}
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
			{href:'loanFront/toLoanFront.do',name:'贷前审核'},
			{href:'rule/toCustomRuleList.do',name:'规则设定'}
		]"/>
		<div data-options="region:'center',border:false" >
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'north',border:false" style="height:140px;">
					<form class="search-form">
						<table >
							<tr>
								<td class="label">业务类型:</td>
								<td rowspan="2" >
									<table style="height: 100%">
										<tr id="headtype" >
										
										</tr>
										<tr>
											<td class="radio-grid">
												<label><input type="radio" name="checkStatus" value="" /> 全部</label>
											</td>
											<td  class="radio-grid">
											   	<label><input type="radio" name="checkStatus" value="0"/> 未审核</label>
											</td>
											<td class="radio-grid" >
											    <label><input type="radio" name="checkStatus" value="1"/> 通过</label>
											</td>
											<td class="radio-grid" >
											    <label><input type="radio" name="checkStatus" value="2"/> 拒绝</label>
											</td>
											<td class="radio-grid" >
											    <label><input type="radio" name="checkStatus" value="3"/> 错误</label>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class="label">审核状态:</td>
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
	</body>
</html>