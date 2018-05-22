<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="authApp">
<head>
    <meta charset="UTF-8">
    <title>身份验证及银行流水查询</title>
    <link rel="stylesheet/less" href="${ctx}/static/css/phoneReview.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
   
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ui-bootstrap-tpls.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
    <script  charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/auditing/js/authentication.js"></script>
</head>
<body style="background:#f6f6f6" ng-controller="authCtrl">
    <div class="managerContain" id="managerContain" >
        <div class="leftList">
            <div class="leftList_top"></div>
            <div class="leftList_bottom">
                <ul>
                    <li ><i class="icon icon_task elvesIcon"></i><a href="${ctx}/auditingTask/toDistribution.do">分配电核任务</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auditingTask/toOrderList.do">查询订单</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toList.do">鉴权结果</a></li>
                    <li class="current"><i class="icon icon_order elvesIcon"></i><a href="${ctx}/auth/toAuth.do">身份验证</a></li>
                </ul>
            </div>
        </div>
        <div class="rightContain">
            <div class="rightContain_title">
                <div class="title">电核任务列表 > <em>身份验证及银行流水</em></div>
                <div class="exit"><span>欢迎您,</span><em ng-bind="username"></em><a  href="${ctx}">[退出]</a></div>
            </div>
            <div class="rightContain_bodies">
                <div class="conditions">
                    <ul>
                        <li>姓名：
                            <input type="text"  id="customerName" style="height:28px;outline:none;border: 1px solid #d8d8d8;" >
                        </li>
                        <li>身份证：
                            <input type="text"  id="idcard" style="height:28px;outline:none;border: 1px solid #d8d8d8;" >
                        </li>
                        <li>银行卡号：
                            <input type="text"  id="bankno" style="height:28px;outline:none;border: 1px solid #d8d8d8;" >
                        </li>
                        <li>
                            <span class="demand elvesIcon" ng-click="askData()"></span><span class="demand " style="line-height: 26px;font-weight: bolder;color:red" id="status"></span>
                        </li>
                    </ul>
                </div>
                <table class="table table-bordered" >
                    <thead>
                    <tr>
                        <th></th>
                        <th>交易日期</th>
                        <th>交易金额</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr ng-repeat="item in bankFlow">
                        <td ng-bind="$index+1"></td>
                        <td ng-bind="item.transTime"></td>
                        <td ng-bind="item.transAmount"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        	 <%--<section class="row pageDemo" >--%>
         		<%--<section style="margin:0px auto;">--%>
            		<%--<pagination  total-items="totalItems" ng-model="currentPage" class="pagination-sm"  max-size="maxSize" previous-text="〈" next-text="〉"  rotate="false" ng-change="getPageLists()"></pagination>--%>
           		<%--</section>--%>
       		<%--</section>--%>
        </div>
    </div>

</body>
</html>