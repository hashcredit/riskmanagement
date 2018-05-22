<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true" %>
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
    <link rel="stylesheet" href="${ctx}/css/amazeui.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/app.css">
    <script type="text/javascript" lang="">
        var page_url = '${ctx}/loanOverdue/list.do';
        var headtype_url = '${ctx}/loan/headtype.do';
        //easyUI分页列表
        $(function () {
            loadPage();
        });

        function loadPage() {
            $('#pageList').datagrid({
                title: '', //标题
                url: page_url,
                columns: [[
                    {field: 'code', title: 'code', width: 30, hidden: false},
                    {field: 'name', title: '姓名', width: 25},
                    {field: 'typeName', title: '业务类型', width: 30},
                    {field: 'organization', title: '合作机构', width: 40},
                    {field: 'Jkqx', align: 'center', title: '借款期限', width: 20},
                    {field: 'sqje', align: 'center', title: '借款金额', width: 20},
                    {field: 'createtime', title: '放款日期', width: 30, formatter: $.getFormatter("yyyy-MM-dd")},
//                    {field:'createtim1',title:'逾期开始日期',width:30,formatter:$.getFormatter("yyyy-MM-dd")},
//					{field:'hit_count3',align:'center',title:'逾期天数',width:20},
//					{field:'hit_count4',align:'center',title:'逾期金额',width:20},
                    {
                        field: 'opt1', title: '操作', width: 10, align: 'center',
                        formatter: function (value, rec) {
                            var code = rec.code;
                            var btn = "<a onclick='toFollow(\"" + code + "\")'>跟进</a>";
                            return btn;
                        }
                    }
                ]],
                onLoadSuccess: function () {
                    //$('#pageList').datagrid('enableDnd');
                }
            });

            initHeadtype();
        }

        function initHeadtype() {
            $.ajax({
                url: headtype_url,
                type: 'post',
                success: function (data) {
                    $("#headtype").append("<label style='margin-right:10px;'><input type='radio' name='headtype' value=''/> 全部</label>");
                    $.each(eval(data), function (n, value) {
                        $("#headtype").append("<label style='margin-right:10px;'><input type='radio' name='headtype' value='" + value.code + "'/> " + value.name + "</label>");
                    });
                }
            });
        }
        //查询
        function searchList() {
            var queryParams = $('#pageList').datagrid('options').queryParams;
            queryParams.filter_startTime = $('#filter_startTime').datebox('getValue');
            queryParams.filter_endTime = $('#filter_endTime').datebox('getValue');
            queryParams.filter_headtype = $("input[name='headtype']:checked").val();
            queryParams.filter_keyword = $("#keyword").textbox("getValue");
            $('#pageList').datagrid("reload");
        }

        //清空查询条件
        function clearForm() {
            $('#pageList').datagrid('clearSelections');
            $('.search-form').form("reset");
        }

        function toFollow(code) {
            window.location.href = "${ctx}/loanOverdue/toFollowList.do?flag=1&code=" + code;
        }

    </script>
    <style type="text/css">
        .radio-grid {
            padding-right: 20px;
        }

        #headtype {
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
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true,border:false">
        <div data-options="region:'north',border:false" style="height:140px;">
            <form class="search-form">
                <table>
                    <tr>
                        <td class="label">业务类型:</td>
                        <td id="headtype">
                        </td>
                    </tr>
                    <tr>
                        <td class="label">关键字:</td>
                        <td id="rule-group-code">
                            <input type="text" class="easyui-textbox" prompt="姓名/身份证号码" id="keyword"/>
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
        <div data-options="region:'center',border:false">
            <table id="pageList"></table>
        </div>
    </div>
</div>
</body>
</html>