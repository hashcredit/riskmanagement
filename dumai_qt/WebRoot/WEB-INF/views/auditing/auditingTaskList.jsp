<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myPhoneApp">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>电核任务列表</title>
    <link rel="stylesheet/less" href="${ctx}/static/css/phoneReview.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/auditing/js/auditingTaskList.js"></script>
</head>
<body style="background:#f6f6f6">
    <div class="reviewContain" id="reviewContain" ng-controller="myCtrl">
        <div class="header">
           	 电核任务列表
            <div class="exit"><span>欢迎您,</span><em ng-bind="username"></em><a href="${ctx}">[退出]</a></div>
        </div>
        <div class="audioBodies">
           <table class="table table-bordered">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>客户名称</th>
                    <%--<th>业务类型</th>--%>
                    <th>机构名称</th>
                    <th>审核人</th>
                    <th>审核状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in phoneLists">
                    <td ng-bind="item.create_time"></td>
                    <td ng-bind="item.customer_name"></td>
                    <%--<td ng-bind="item.typeName"></td>--%>
                    <td ng-bind="item.organization"></td>
                    <td ng-bind="item.surname"></td>
                    <td ng-bind="item.status"></td><td>
                    <a ng-click="auditing(item.code)">操作</a></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>