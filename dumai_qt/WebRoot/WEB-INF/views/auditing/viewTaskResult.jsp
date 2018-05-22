<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>电核任务</title>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery-easyui-1.5/jquery.easyui.min.js"></script>
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/jquery-easyui-1.5/themes/icon.css">
	<script type="text/javascript" src="${ctx}/js/global.js"></script>
	<script>
        var page_url = "${ctx}/auditingTask/dhTask.do?code=${param.code}";
        var page_url_add = "${ctx}/auditingTask/additional.do?code=${param.code}";
        var opentime = '${param.opentime}';
        console.log(opentime);
        //easyUI分页列表
        $(function(){
            $("#pageList1").datagrid({
                url: page_url_add,
                loadMsg: '数据载入中，请稍后',
                method: "post",
                nowrap: false,
                striped: true,
                fitColumns: true,
                singleSelect: true,
                rownumbers: true,
                remoteSort: false,
                pageList: [10, 20, 50],
                pageSize: 10,
                queryParams: {}, //查询条件
                columns: [[
                    {field: 'item_name', title: '电核项',width: 10},
                    {field: 'item_value', title: '电核项内容',width: 10},
                    {field: 'dh_content', title: '电核内容', width: 5,
                        formatter: function (value, rec, index) {
                            var btn = '';
                            if("true" == value){
                                btn = '是' + '<input type="radio" name="fix_radios' + index + '" value="true" checked/>';
                                btn += '否' + '<input type="radio" name="fix_radios' + index + '" value="false" />';
                            } else if ("false" == value) {
                                btn = '是' + '<input type="radio" name="fix_radios' + index + '" value="true" />';
                                btn += '否' + '<input type="radio" name="fix_radios' + index + '" value="false" checked/>';
                            }
                            return btn;
                        }
                    },
                    {field: 'remark', title: '备注', width: 10,
                        formatter: function (value, rec, index) {
                            if (undefined == value) {
                                value = '';
                            }
                            var btn = '<input type="text" style="width:100%;" name="fix_txt' + index + '" id="fix_txt' + index + '" value="' + value + '"/>';
                            return btn;
                        }
                    }
                ]]
            });

            $("#pageList").datagrid({
                url  :page_url,
                loadMsg:'数据载入中，请稍后',
                method:"post",
                nowrap: false,
                striped: true,
                fitColumns: true,
                singleSelect:true,
                footer:"#footer",
                rownumbers:true,
                remoteSort: false,
                pageList:[10,20,50],
                pageSize:10,
                queryParams: {}, //查询条件
                columns:[[
                    {field:'code',title:'code',hidden:true},
                    {field:'manager_item_code',title:'manager_item_code',hidden:true},
                    {field:'manager_item_name',title:'电核项',width:20},
                    {field:'dh_description',title:'描述',width:20},
                    {field:'auto_result',title:'结果',width:10,
                        formatter: function(value) {
                            var btn = '';

                            if ('true' == value) {
                                btn = '命中';
                            } else if ('false' == value) {
                                btn = '未命中';
                            } else {
                                btn = '无结果';
                            }
                            return btn;
                        }
                    },
//			{field:'description',title:'查看详情',width:10,
//                formatter:function(value,rec,index){
//                    var btn = '<a>查看</a>';
//                    return btn;
//                }
//			},
                    {field:'dh_content',title:'电核内容',width:5,
                        formatter: function(value, rec, index) {
                            var dhtype = rec.dh_type;
                            var btn = '';
                            if ('0' == dhtype) {
                                btn = '<input type="text" id="input' + index + '"/>';
                            } else if ('1' == dhtype) {
                                var content = rec.result;
                                var radios = rec.dh_content.split('_');
                                for (var i = 0; i < radios.length; i++) {
                                    if("true" == content && "是" == radios[i]){
                                        btn += radios[i] + '<input type="radio" name="radios' + index + '" value="' + radios[i] + '" checked/>';
                                    } else if("false" == content && "否" == radios[i]){
                                        btn += radios[i] + '<input type="radio" name="radios' + index + '" value="' + radios[i] + '" checked/>';
                                    } else {
                                        btn += radios[i] + '<input type="radio" name="radios' + index + '" value="' + radios[i] + '" />';
                                    }
                                }
                            } else if ('2' == dhtype) {
                                var checkboxs = rec.dh_content.split('_');
                                for (var i = 0; i < checkboxs.length; i++) {
                                    btn += checkboxs[i] + '<input type="checkbox" name="checkboxs' + index + '" value="' + checkboxs[i] + '"/>';
                                }
                            }
                            return btn;
                        }
                    },
                    {field:'remark',title:'备注',width:40,
                        formatter:function(value,rec,index){
                            if (undefined == value) {
                                value = '';
                            }
                            var btn = '<input type="text" style="width:100%;" name="txt' + index + '" id="txt' + index + '" value="' + value + '"/>';
                            return btn;
                        }
                    }
                ]],
                onLoadSuccess:function(){
                    var data = $('#pageList').datagrid('getData').orderInfo;
                    $('#name').text(data.name);
                    $('#mobile').text(data.mobile);
                    $('#card_num').text(data.card_num);
                    $('#bank_num').text(data.bank_num);
                    $('#organization').text(data.organization);
                    $('#thetype').text(data.typeName);
                    $('#sqje').text(data.sqje);
                    $('#Jkqx').text(data.Jkqx);
                    $('#orderCode').val(data.code);
                    var description = $('#pageList').datagrid('getData').description;
                    if (undefined != description) {
                        $('#description').text(description);
                    }
                    var other_exception = $('#pageList').datagrid('getData').other_exception;
                    if (undefined != other_exception) {
                        $('#other_exception').text(other_exception);
                    }
                }
            });
        });


        // 保存电核结果
        function ok(flag){
            var rowsData = $('#pageList').datagrid('getRows');
            var rowsData1 = $('#pageList1').datagrid('getRows');
            var description =  $('#description').val();
            var other_exception =  $('#other_exception').val();
            var have_content = false;
            var json1 = [];
            var json2 = [];
            var data1;
            var data2;
            //电核项结果处理
            $.each(rowsData,function(i) {
                var rows = rowsData[i];
                var dh_type = rows.dh_type;
                var dhContent='';
                if('0'==dh_type){
                    var id='#input' +i;
                    dhContent = $(id).val();
                }
                if('1'==dh_type){
                    var radios = $("input:radio[name='radios"+i+"']");
                    var len = radios.length;
                    for(var j=0;j< len;j++){
                        if(true == radios.get(j).checked){
                            var content = radios.get(j).value;
                            if("是" == content) {//命中
                                dhContent = "true";
                            } else if("否" == content) {//未命中
                                dhContent = "false";
                            } else {
                                dhContent = content;
                            }
                            break;
                        }
                    }
                }
                if('2'==dh_type){
                    var checkboxs = $("input:checkbox[name='checkboxs" + i + "']");
                    var len = checkboxs.length;
                    var contents = '';
                    for (var j = 0; j < len; j++) {
                        if (true == checkboxs.get(j).checked) {
                            var content = checkboxs.get(j).value;
                            if ('' == contents) {
                                contents = content;
                            } else {
                                contents = contents + ";" + content;
                            }
                        }
                    }
                    dhContent = contents;
                }
                if('' == dhContent && flag == '1'){
                    have_content = true;
                }

                var remark = $('#txt' +i).val();
                data1 = {
                    "manager_item_code": rows.manager_item_code,
                    "dh_terms": rows.code,
                    "dh_type": dh_type,
                    "dh_source_type": rows.dh_source_type,
                    "dh_content": dhContent,
                    "remark": remark
                };
                json1.push(data1);
            });
            //电核增项结果处理
            $.each(rowsData1,function(i) {
                var rows = rowsData1[i];
                var addContent = '';

                var radios = $("input:radio[name='fix_radios" + i + "']");
                var len = radios.length;
                for (var j = 0; j < len; j++) {
                    if (true == radios.get(j).checked) {
                        var content = radios.get(j).value;
                        if ("是" == content) {//命中
                            addContent = "true";
                        } else if ("否" == content) {//未命中
                            addContent = "false";
                        } else {
                            addContent = content;
                        }
                        break;
                    }
                }
                var remark = $('#fix_txt' +i).val();
                data2 = {
                    "item_name": rows.item_name,
                    "item_value": rows.item_value,
                    "dh_content": addContent,
                    "remark": remark
                };
                json2.push(data2);
            });
            if(have_content){
                alert("请完善电核内容");
                return;
            }

            $.ajax({
                url: "${ctx}/auditingTask/saveJson.do",
                method: "post",
                traditional: true,
                data: {
                    data1: JSON.stringify(json1),
                    data2: JSON.stringify(json2),
                    other_exception:other_exception,
                    description:description,
                    flag:flag,
                    opentime:opentime,
                    taskCode: '${param.code}'
                },
                success: function(result) {
                    if (result == 'success') {
                        window.location="${ctx}/auditingTask/toList.do?code=${param.code}"
                        alert("保存成功");
                    } else {
                        alert("保存失败");
                    }
                },
                error:function() {
                    alert("保存失败");
                }
            });

        }

        //查看贷前报告
        function openWin(){
            var url = "${ctx}/auditingTask/toReport.do?code=" + $('#orderCode').val();
            var win=window.open(url," ",'width='+ (screen.availWidth*0.5) +',height='+ (screen.availHeight*0.5) +',left='+ (screen.availWidth*0.25) +',top='+(screen.availHeight*0.25) +',toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes',true);
            win.focus();
            return win;
        }
	</script>
</head>
<body class="easyui-layout" data-options="fit:true">
<common:dh_head-tabs/>

<div data-options="region:'center',border:false" >
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="margin-bottom:10px;height: 80px;">
			<a href="javascript:void(0)" onclick="openWin();" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:20px">查看个人详细信息</a>
			<input type="hidden" id="orderCode" />
			<table width="100%">
				<tr height="25px">
					<td class="label">姓名：</td>
					<td>
						<label id="name" ></label>
					</td>
					<td class="label">电话：</td>
					<td>
						<label id="mobile" ></label>
					</td>
					<td class="label">身份证号：</td>
					<td>
						<label id="card_num" ></label>
					</td>
					<td class="label">银行卡号：</td>
					<td>
						<label id="bank_num" ></label>
					</td>
				</tr>
				<tr height="25px">
					<td class="label">机构名称：</td>
					<td>
						<label id="organization" ></label>
					</td>
					<td class="label">业务类型：</td>
					<td>
						<label id="thetype" ></label>
					</td>
					<td class="label">贷款金额：</td>
					<td>
						<label id="sqje" ></label>
					</td>
					<td class="label">贷款期限：</td>
					<td>
						<label id="Jkqx" ></label>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',border:false" >
			<label >电核信息</label>
			<table id="pageList"></table>

			<label style="text-align:left">增项信息</label>
			<table id="pageList1"></table>

			<div style="text-align:center;" >
				<div style="text-align:left">电核基本情况</div>
				<textarea id="description" style="width:98%;" rows="10" placeholder="电核基本情况"></textarea>
				<div style="text-align:left">异常情况</div>
				<textarea id="other_exception" style="width:98%;" rows="5" placeholder="异常情况"></textarea>
			</div>
		</div>
	</div>
</div>

</body>
</html>