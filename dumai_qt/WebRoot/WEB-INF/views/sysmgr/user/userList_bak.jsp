<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%
	String root=request.getContextPath();//获取项目根目录名称
%>
<!DocType html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户管理</title>
		<script type="text/javascript" src="<%=root%>/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script type="text/javascript" src="<%=root%>/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=root%>/js/global.js"></script>
		<link rel="stylesheet" href="<%=root%>/css/amazeui.css" />
		<%-- <link rel="stylesheet" type="text/css" href="<%=root%>/js/lib/jquery-easyui-1.5/themes/default/easyui.css"> --%>
		<link rel="stylesheet" type="text/css" href="<%=root%>/js/lib/jquery-easyui-1.5/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=root%>/css/app.css">
		 
		<script type="text/javascript">
			
			$(function(){
				$('#pageList').datagrid({
					title:'用户列表', //标题
					url  :"${ctx}/sysmgr/userList.do",
					border:false,
					loader:function(param,success,error){
						
						$.fn.datagrid.defaults.loader.call(this,param,function(){
							
							var rs = arguments[0];
							
							if(rs.code==0){
								arguments[0] = rs.body;
								success.apply(this,arguments);
							}
							else{
								error.apply(this,arguments);
							}
							
						},function(){
							error.apply(this,arguments);
						});
					},			
					
	
					columns:[[
						{field:'code',title:'id',hidden:true},
						{field:'USER_NAME',title:'用户名',width:20},
						{field:'SURNAME',title:'姓名',width:20},
						{field:'OFFICE_TEL',title:'工作电话',width:25},
						{field:'SEX',align:'center',title:'性别',width:20},	
						{field:'ISVALID',align:'center',title:'是否可用',
							formatter:function(v,r){
								return ["否","是"][v];
							},
							width:20
						},
						{field:'EMAIL',title:'邮箱地址',width:60},
						{field:'user_dept',title:'部门',width:20},
						{field:'user_role',title:'角色',width:20},
						{field:'user_permission',align:'center',title:'权限',
							formatter:function(v,r){
								var user_permissions = v.split(":");
								var user_permissionsText = [];
								var user_permissionsTextDef = ["查看","删除","导出"];
								
								for(var i in user_permissions){
									user_permissionsText.push(user_permissions[i]=="1"?user_permissionsTextDef[i]:"<span style='color:#adabab'>"+user_permissionsTextDef[i]+"</span>");
								}
								
								return user_permissionsText.join(" | ");
							},
							width:20
						},
						{field:'moddate',align:'center',title:'修改时间',width:30,formatter:$.getFormatter("yyyy-MM-dd HH:mm:ss")},
						{field:'opt',align:'center',title:'操作',width:20,
							formatter:function(v,r,i){
								if(r.ISLEADER=="1"){
									return "<span title='初始用户为您的最高用户不可删除' style='color:gray' >删除</span> | <a href='#' onclick='showUpdateWin("+i+")'>修改</a>";
								}
								else {
									return "<a href='#' onclick='del(\""+r.code+"\")'>删除</a> | <a href='#' onclick='showUpdateWin("+i+")'>修改</a>";
								}
								//return "<a href='#' onclick='del(\""+r.code+"\")'>删除</a> | <a href='#' onclick='showUpdateWin("+i+")'>修改</a>";
							}
						}
					]],
					toolbar:[
						{
							text:'增加',
							iconCls:'icon-add',
							handler:function(){
								showAddWin();
							}
						}
			       ]
			    });
			});
			
			
			function showAddWin(){
				$("#form-add").form('reset');
				$('#addWin').show().dialog({ 
					modal:true,
					buttons: [{ 
							text: '保存', 
							iconCls: 'icon-ok',
							handler: function() { 
								add();
							} 
						},{ 
							text: '取消',
							iconCls: 'icon-cancel', 
							handler: function() { 
								$('#addWin').dialog('close'); 
							} 
						}
					] 
				}); 
			}
			
			function add(){
				var formAdd = $("#form-add");
				if(!formAdd.form("validate")){
					return false;
				}
				var data = $.deserialize($("#form-add").serialize());
				var user_permissions = $("#form-add [name='user_permission']");
				
				var user_permission = [];
				user_permissions.each(function(){
					user_permission.push($(this).prop("checked")?1:0);
				});
				
				data.user_permission = user_permission.join(":");
				
				
				$.ajax({
					url:"${ctx}/sysmgr/userAdd.do",
					data:data,
					dataType:"json",
					type:"post",
					success:function(result){
						if(result.code==0 && result.body==true){
							alert("添加成功");
							
							$('#addWin').dialog("close");
							$('#pageList').datagrid("reload");
						}
						else{
							alert("添加失败");
						}
					},
					error:function(){
						alert("添加失败");
					}
				});
			}
			
			function showUpdateWin(i){
				
				
				$("#form-update").form('clear');
				var data = $('#pageList').datagrid("getRows")[i];
				
				$('#updateWin').show().dialog({ 
					collapsible: false, 
					minimizable: true, 
					maximizable: true, 
					buttons: [{ 
							text: '保存', 
							iconCls: 'icon-ok',
							handler: function() { 
								update(data);
							} 
						},{ 
							text: '取消',
							iconCls: 'icon-cancel', 
							handler: function() { 
								$('#updateWin').dialog('close');
							} 
						}
					] 
				}); 
				
				$("#form-update #update_user_name").textbox("setValue",data.USER_NAME);
				$("#form-update #update_surname").textbox("setValue",data.SURNAME);
				$("#form-update [value='"+data.SEX+"']").prop("checked",true);
				$("#form-update [textboxname='user_dept']").textbox("setValue",data.user_dept);
				$("#form-update [textboxname='user_role']").textbox("setValue",data.user_role);
				
				var user_permissions = $("#form-update [name='user_permission']");
				var user_permission = (data.user_permission ||[]).split(":");
				user_permissions.each(function(i){
					$(this).prop("checked",user_permission[i]=="1"?true:false);
				});
				
				
				$("#form-update #update_email").textbox("setValue",data.EMAIL);
				$("#form-update #update_office_tel").textbox("setValue",data.OFFICE_TEL);
				$("#form-update #update_mobile").textbox("setValue",data.MOBILE);
				$("#form-update [name='sub_entity_id']").val(data.sub_entity_id);
				$("#form-update [name='isvalid'][value='"+data.ISVALID+"']").prop("checked",true);
				
				if(data.ISLEADER==1){
					$("#isLeader-row").hide();
				}
				else{
					$("#isLeader-row").show();
				}
				$("#form-update [name='isLeader'][value='"+data.ISLEADER+"']").prop("checked",true);
				
				
			}
			function update(row){
				
				var data = $.deserialize($("#form-update").serialize());
				var user_permissions = $("#form-update [name='user_permission']");
				
				var user_permission = [];
				user_permissions.each(function(){
					user_permission.push($(this).prop("checked")?1:0);
				});
				
				data.user_permission = user_permission.join(":");
				
				$.ajax({
					url:"${ctx}/sysmgr/userUpdate.do?code=" + row.code,
					data:data,
					dataType:"json",
					type:"post",
					success:function(result){
						if(result.code==0 && result.body==true){
							alert("修改成功");
							
							$('#updateWin').dialog("close");
							$('#pageList').datagrid("reload");
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
			function del(user_id){
				if(confirm("删除后将无法恢复,确认删除吗?")){
					$.ajax({
						url:"${ctx}/sysmgr/userDel.do?user_id=" + user_id,
						dataType:"json",
						success:function(result){
							if(result.code==0 && result.body==true){
								alert("删除成功");
								$('#pageList').datagrid("reload");
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
			<table id="pageList"></table>
		</div>
		<div id="addWin" style="display:none;width:400px;height:410px;" title="添加用户">
			<form class="form-input" id="form-add">
			<table class="add-input" style="width:100%" >
				<tr >
					<td class="label">用户名</td>
					<td><input name="user_name" data-options="validType:{remote:['${ctx}/sysmgr/usernameNotDuplicated.do','user_name']}"  required="required" invalidMessage="用户名已存在" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">姓名</td>
					<td><input name="surname" required="required" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">性别</td>
					<td>
						<input name="sex" checked="checked" type="radio" value="男"/>男
						<input name="sex" type="radio" value="女"/>女
					</td>
				</tr>
				<tr>
					<td class="label">是否是管理员</td>
					<td>
						<input name="isLeader" checked="checked" type="radio" value="2"/>是
						<input name="isLeader" type="radio" value="3"/>否
					</td>
				</tr>
				<tr>
					<td class="label">部门</td>
					<td><input name="user_dept" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">角色</td>
					<td><input name="user_role" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">权限</td>
					<td>
						<input name="user_permission" type="checkbox" value="1"/>查看
						<input name="user_permission" type="checkbox" value="1"/>删除
						<input name="user_permission" type="checkbox" value="1"/>导出
					</td>
				</tr>
				<tr>
					<td class="label">邮箱</td>
					<td><input name="email" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">工作电话</td>
					<td><input name="office_tel" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">手机号</td>
					<td><input name="mobile" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">账户启用</td>
					<td>
						<input name="isvalid" checked="checked" type="radio" value="1"/>是
						<input name="isvalid"  type="radio" value="0"/>否
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div id="updateWin" style="display:none;width:400px;height:410px;" title="修改用户">
			<form class="form-input" id="form-update">
			<table class="add-input" style="width:100%" >
				<tr >
					<td class="label">用户名</td>
					<td><input name="user_name" data-options="readonly:true" id="update_user_name" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">姓名</td>
					<td><input name="surname" required="required" id="update_surname" class="easyui-textbox"/></td>
				</tr>
				<tr id="isLeader-row">
					<td class="label">是否是管理员</td>
					<td>
						<input name="isLeader" style="display:none" type="radio" value="1"/>
						<input name="isLeader" type="radio" value="2"/>是
						<input name="isLeader" type="radio" value="3"/>否
					</td>
				</tr>
				<tr>
					<td class="label">性别</td>
					<td>
						<input name="sex" type="radio" value="男"/>男
						<input name="sex" type="radio" value="女"/>女
					</td>
				</tr>
				<tr>
					<td class="label">部门</td>
					<td><input name="user_dept" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">角色</td>
					<td><input name="user_role" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">权限</td>
					<td>
						<input name="user_permission" type="checkbox" value="1"/>查看
						<input name="user_permission" type="checkbox" value="1"/>删除
						<input name="user_permission" type="checkbox" value="1"/>导出
					</td>
				</tr>
				<tr>
					<td class="label">邮箱</td>
					<td><input name="email" id="update_email" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">工作电话</td>
					<td><input name="office_tel" id="update_office_tel" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">手机号</td>
					<td><input name="mobile" id="update_mobile" class="easyui-textbox"/></td>
				</tr>
				<tr>
					<td class="label">账户启用</td>
					<td>
						<input name="isvalid" type="radio" value="1"/>是
						<input name="isvalid"  type="radio" value="0"/>否
					</td>
				</tr>
			</table>
			</form>
		</div> 
	</body>
</html>