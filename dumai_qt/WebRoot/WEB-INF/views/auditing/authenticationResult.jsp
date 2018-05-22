<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="managerApp">
<head>
    <meta charset="UTF-8">
    <title>鉴权结果查询</title>
    <link rel="stylesheet/less" href="${ctx}/static/css/phoneReview.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
    <script src="${ctx}/static/script/lib/jquery.min.js"></script>
    <script src="${ctx}/static/script/lib/angular.min.js"></script>
    <script src="${ctx}/static/script/lib/jquery-ui.js"></script>
    <script src="${ctx}/static/script/lib/moment.js"></script>
    <script src="${ctx}/static/dumai/auditing/js/authenticationResult.js"></script>
</head>
<body style="background:#f6f6f6">
    <div class="managerContain" id="managerContain" ng-controller="managerCtrl">
        <div class="leftList">
            <div class="leftList_top"></div>
            <div class="leftList_bottom">
                <ul>
                    <li ><i class="icon icon_task elvesIcon"></i><a href="${ctx}/auditingTask/toDistribution.do">分配电核任务</a></li>
                    <li><i class="icon icon_order elvesIcon"></i><a href="${ctx}/auditingTask/toOrderList.do">查询订单</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toList.do">鉴权结果</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toAuth.do">身份验证</a></li>
                </ul>
            </div>
        </div>
        <div class="rightContain">
            <div class="rightContain_title">
                <div class="title">电核任务列表 > <em>鉴权结果</em></div>
                <div class="exit"><span>欢迎您,</span><em ng-bind="username"></em><a href="${ctx}">[退出]</a></div>
            </div>
            <div class="rightContain_bodies" style="height:94%;">
                <table class="table table-bordered" style="margin-top:0;">
                    <thead>
                    <tr>
                        <th></th>
                      <!--   <th>订单号</th> -->
                        <th>姓名</th>
                        <th>身份证</th>
                        <th>手机号</th>
                        <th>银行卡号</th>
                        <th>是否通过</th>
                        <th>详细信息</th>
                        <th>鉴权时间</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr ng-repeat="item in managerLists" class="cell">
                        <td ng-bind="$index+1"></td>
                       <!--  <td ng-bind="item.orderid" width="5%"></td> -->
                        <td ng-bind="item.name"></td>
                        <td ng-bind="item.card_num"></td>
                        <td ng-bind="item.mobile"></td>
                        <td ng-bind="item.bank_num"></td>
                        <td ng-bind="item.validate_result==1?'是':'否'"></td>
                        <td ng-bind="item.validate_message" width="25%"></td>
                        <td ng-bind="item.opttime"></td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</body>
</html>