<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>升级处理表</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/extension/datagrid-dnd.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/global.js"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	<script type="text/javascript" lang="">
		var fk_orderinfo_follow_code = '';
		var page_url = '${ctx}/loanOverdue/followList.do?code=${param.code}';
		var save_handle_url = '${ctx}/loanOverdue/saveHandle.do';

		//easyUI分页列表
		$(function(){
			loadPage();
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
					{field:'reason_request',title:'申请理由',width:40},
                    {field:'opt_request',title:'升级处理类型',width:25,
                        formatter:function(value,rec){
                            return [,"外访协催","展期","外包","诉讼"][value];
                        }
                    },
                    {field:'validate_status',title:'审核状态',width:25,
                        formatter:function(value,rec){
                            return ["待审核","同意","拒绝"][value];
                        }
                    },
					{field:'validate_remarks',align:'center',title:'审核备注',width:40},
					{field:'surname',align:'center',title:'申请人',width:20}
				]],
                onLoadSuccess: function () {
                    var data  = $('#pageList').datagrid('getData');
                    var handleData = data.rows[0];
                    console.log(data.rows[0]);
                    $("#follow_date").text(handleData.follow_date);
                    $("#opt_request").text([,"外访协催","展期","外包","诉讼"][handleData.opt_request]);
                    $("#reason_request").text(handleData.reason_request);
                    fk_orderinfo_follow_code = handleData.code;
                }
		    });
		}

		//保存处理结果
		function saveHandle(validate_status) {
            var data = $.deserialize($("#form-add").serialize() + "&validate_status=" + validate_status + "&fk_orderinfo_follow_code=" + fk_orderinfo_follow_code);
		    console.log(data);

            $.ajax({
                url: save_handle_url,
                type: 'post',
                data: data,
                success: function(res){
					if("1" == res){
					    alert("保存成功");
                        window.location.reload();
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
								<td class="label">申请日期:</td>
								<td  >
									<label id="follow_date" name="follow_date"></label>
								</td>
							</tr>
							<tr>
								<td class="label">申请处理类型:</td>
								<td>
									<label id="opt_request" name="opt_request"></label>
								</td>
							</tr>
							<tr>
								<td class="label">申请理由:</td>
								<td>
									<label id="reason_request" name="reason_request"></label>
								</td>
							</tr>
							<tr>
								<td class="label">审核备注:</td>
								<td>
									<textarea id="validate_remarks" name="validate_remarks" class="easyui-validatebox" ></textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<a href="javascript:void(0)" onclick="saveHandle(1);" class="easyui-linkbutton" iconCls="icon-ok">同意</a>
									<a href="javascript:void(0)" onclick="saveHandle(2);" class="easyui-linkbutton" iconCls="icon-no">拒绝</a>
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