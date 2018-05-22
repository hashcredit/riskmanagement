<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>催收记录表</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/extension/datagrid-dnd.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/global.js"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	<script type="text/javascript" lang="">
		var page_url = '${ctx}/loanOverdue/followList.do?code=${param.code}';
		var save_follow_url = '${ctx}/loanOverdue/saveFollow.do?fk_orderinfo_code=${param.code}';
		console.log('${param.code}');

		//easyUI分页列表
		$(function(){
			
			loadPage();
            $("#follow_date").text($.formatDate(new Date(),"yyyy-MM-dd"));
            $("#next_date").datebox("setValue",$.formatDate(new Date(),"yyyy-MM-dd"));
        });
		
		function loadPage(){
			$('#pageList').datagrid({
//				title:'', //标题
				url  :page_url,
				columns:[[
					{field:'code',title:'code',width:8,hidden:true},
					{field:'follow_date',title:'催收日期',width:30,formatter:$.getFormatter("yyyy-MM-dd")},
					{field:'next_date',title:'下次跟进时间',width:30,formatter:$.getFormatter("yyyy-MM-dd")},
					{field:'label',title:'客户标签',width:25,
                        formatter:function(value,rec){
                            return [,"未接电话","他人转告","承诺还款","周转困难","高负债","恶意拖欠","涉嫌欺诈","死亡/坐牢","升级处理"][value];
                        }
					},
					{field:'content',title:'沟通内容',width:40},
					{field:'surname',align:'center',title:'催收负责人',width:20}
				]]
		    });
		}

		function saveFollow() {
//		    var data = $("#form-add").serialize();
            var data = $.deserialize($("#form-add").serialize());
		    console.log(data);

            $.ajax({
                url: save_follow_url,
                type: 'post',
                data: data,
                success: function(res){
//                    var queryParams = $('#pageList').datagrid('options').queryParams;
//                    queryParams.filter_startTime = $('#code').datebox('getValue');
					if("1" == res){
					    alert("保存成功");
                        $('#pageList').datagrid("reload");
                    } else {
					    alert("保存失败");
					}
                }
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
			{href:'loanOverdue/toList.do',name:'逾期列表'},
			{href:'loanOverdue/toFollowList.do',name:'催收跟进'},
			{href:'loanOverdue/toHandleList.do',name:'升级处理'}
		]"/>
		<div data-options="region:'center',border:false" >
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'north',border:false" style="">
					<form id="form-add">
						<table >
							<tr>
								<td class="label">催收日期:</td>
								<td  >
									<label id="follow_date" name="follow_date"></label>
								</td>
							</tr>
							<tr>
								<td class="label">下次跟进时间:</td>
								<td>
									<input type="text" class="easyui-datebox" id="next_date" name="next_date" size="20"/>
								</td>
							</tr>
							<tr>
								<td class="label">客户标签:</td>
								<td>
									<select class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" id="label" name="label" style="width:419px;">
										<option value="1">未接电话</option>
										<option value="2">他人转告</option>
										<option value="3">承诺还款</option>
										<option value="4">周转困难</option>
										<option value="5">高负债</option>
										<option value="6">恶意拖欠</option>
										<option value="7">涉嫌欺诈</option>
										<option value="8">死亡/坐牢</option>
										<option value="9">升级处理</option>
										<option value="0">其它</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="label">沟通内容:</td>
								<td>
									<textarea id="content" name="content" class="easyui-validatebox" ></textarea>
								</td>
							</tr>
							<tr>
								<td class="label">升级处理申请:</td>
								<td>
									<input name="opt_request" type="radio" value="1"/>外访协催
									<input name="opt_request" type="radio" value="2"/>展期
									<input name="opt_request" type="radio" value="3"/>外包
									<input name="opt_request" type="radio" value="4"/>诉讼
								</td>
							</tr>
							<tr>
								<td class="label">申请理由:</td>
								<td>
									<textarea id="reason_request" name="reason_request" class="easyui-validatebox" ></textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<a href="javascript:void(0)" onclick="saveFollow();" class="easyui-linkbutton" iconCls="icon-add">保存</a>
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