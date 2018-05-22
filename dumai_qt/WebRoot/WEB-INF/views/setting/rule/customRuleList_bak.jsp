<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DocType html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>规则中心</title>
		<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/global.js"></script>
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
		<script type="text/javascript">
			$(function(){
				$(document).on("change","#type_code, input[name='status']",function(){
				loadPageList();
			});
				$.ajax({
					url:"${ctx}/rule/types.do",
					dataType:"json",
					success:function(data){
						$.each(data,function(i){
						console.info(data);
							$("#type_code").append("<option value='"+this.code+"' "+(i==0?"selected":"")+">"+this.name+"</option>");
							if(i==0){
								loadPageList();
							}
						});
					},
					error:function(){
					}
				});
			});
			function loadPageList(){
				$('#pageList').datagrid({
					title:'规则列表', //标题
					url  :"${ctx}/rule/customRuleList.do",
					nowrap: false,
					striped: true,
					fit:true,				
					fitColumns: true,
					pagination:true,
					singleSelect:true,
					rownumbers:true,
					remoteSort: false,
					queryParams:{
						type_code:$("#type_code").val(),
						/* rule_group_code:$("input[name='rule-group-code']:checked").val(), */
						status:$("input[name='status']:checked").val()
					},
					columns:[[
						{field:'code',title:'code',align:'center',width:10,hidden:true},
						{field:'name',title:'名称',width:30},
						{field:'description',title:'描述',width:35},
						{field:'status',align:'center',title:'状态',width:10,
							formatter:function(v,r,i){
							r.fk_guize_code?v=1:v=0;
                                return ["<font style='color:red;font-weight:bold'>停用</font>",
                                    "<font style='color:green;font-weight:bold'>启用</font>"]
                                    [v];
							}
						},
						{field:'operate',title:'操作',align:'center',width:10,
							formatter:function(v,r,i){
								r.fk_guize_code?v=1:v=0;
								return v == 0 ?
										"<a href='javascript:;' onclick='enable(\"" + r.code + "\",\"" + r.sys_company_type_code + "\")' >启用</a>"
										:
										"<a href='javascript:;' onclick='disable(\"" + r.code + "\",\"" + r.sys_company_type_code + "\")' >停用</a>";
							}
						} 
					]]
			    });
			}
			function enable(code,sys_company_type_code){
				$.ajax({
                    url: "${ctx}/rule/customEnabeRule.do?fk_guize_code=" + code + "&sys_company_type_code=" + sys_company_type_code,
					dataType:"json",
					success:function(result){
						if(result.code==0){
							$.messager.alert("启用", "启用成功");
							$('#pageList').datagrid("reload");
						}
						else $.messager.alert("启用", "启用失败");
					},
					error:function(){
						$.messager.alert("启用", "启用失败");
					}
					
				});
			}
			function disable(code,sys_company_type_code){
				$.ajax({
					url:"${ctx}/rule/customDisabeRule.do?fk_guize_code=" + code + "&sys_company_type_code=" + sys_company_type_code,
					dataType:"json",
					success:function(result){
						if(result.code==0){
							$.messager.alert("停用", "停用成功");
							$('#pageList').datagrid("reload");
						}
						else $.messager.alert("停用", "停用失败");
					},
					error:function(){
						$.messager.alert("停用", "停用失败");
					}
				});
			}
		</script>
		<style type="text/css">
			.radio-label{
				margin-right: 10px;
			}
		</style>
	</head>
	<body class="easyui-layout" data-options="fit:true,border:false">
		<common:head-tabs tabs="[
			{href:'loanFront/toLoanFront.do',name:'贷前审核'},
			{href:'rule/toCustomRuleList.do',name:'规则设定'}
		]"/>
		<div region="center"  data-options="border:false" >
			<div data-options="border:false,fit:true"  class="easyui-layout">
				<div data-options="region:'north',border:false" style="height:60px;">
					<table >
						<tr>
							<td class="label" >业务类型:</td>
							<td>
								<select id="type_code"></select>
							</td>
						</tr>
						<!-- <tr>
							<td class="label">规则集:</td>
							<td id="rule-group-code">
								<label class="radio-label"><input checked type='radio' name='rule-group-code' value='' />全部 </label>
							</td>
						</tr> -->
						<tr>
							<td class="label">状&nbsp;&nbsp;态:</td>
							<td>
								<label class="radio-label"><input type="radio" checked name="status" value="">全部</label>
								<label class="radio-label"><input type="radio" name="status" value="1">启用</label>
								<label class="radio-label"><input type="radio" name="status" value="0">停用</label>
								<!-- <a href="javascript:;" style="margin-left: 200px" id="search-button" data-options="iconCls:'icon-search'">查询</a> -->
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="pageList"></table>
				</div>
			</div>
		</div>
	</body>
</html>