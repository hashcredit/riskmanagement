<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myApp">
<head>
    <meta charset="UTF-8">
    <title>个人详细信息</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
     <link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
      <link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
    <link rel="stylesheet" href="${ctx}/static/css/dumai/auditing/report.css">
    <style>
    .loading.rhomb:after {
    font-size:40px;
		color:blue;
	}
    </style>
</head>
<body>
    <common:head/>
	<div id="loading-bar-container"></div>
    <div class="container" ng-controller="myCtrl">
    <div class="title">
         <div  class="reports">
            <div >
                <div class="soloInfo">
                  <p>个人详细信息</p>
                    <div class="basicInfo">
                       <ul>
                        <li><span>姓名：</span><span ng-bind="basics.name"></span></li>
                        <li><span>性别：</span><span ng-bind="basics.sex"></span></li>
                        <li><span>年龄：</span><span ng-bind="basics.age"></span></li>
                        <li><span>婚姻：</span><span ng-bind="basics.married"></span></li>
                        <li><span>学历：</span><span ng-bind="basics.education"></span></li>
                        <li><span>身份证号码：</span><span ng-bind="basics.card_num"></span></li>
                        <li><span>手机号码：</span><span ng-bind="basics.mobile"></span></li>
                        <li><span>银行卡：</span><span ng-bind="basics.bank_num"></span></li>
                        <li><span>职业情况 ：</span><span ng-bind="basics.profession"></span></li>
                        <li><span>常住地址：</span><span ng-bind="changAddress"  style="width:78%"></span></li>
                        <li><span>固定收入 ：</span><span ng-bind="basics.sqje"></span></li>
                        <li><span>紧急联系人1：</span><span ng-bind="basics.linkname1"></span><span ng-bind="basics.linkphone1"></span><span ng-bind="basics.linkReation1"></span></li>
                        <li><span>紧急联系人2：</span><span ng-bind="basics.linkname2"></span><span ng-bind="basics.linkphone2"></span><span ng-bind="basics.linkReation2"></span></li>
                        <ul class="generated">
                          <li ng-repeat="aa in arrays"><span ng-cloak>{{aa.titleName}} ：</span><span>{{aa.titleContent}}</span></li>
                        </ul>
                      </ul>
                    <img src="data:image/gif;base64,{{peoImg}}" alt={{basics.name}}{{peoImg}} ng-cloak>
                   </div>
                </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${ctx}/static/script/lib/jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/angular.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/angular-animate.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/loading-bar.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/angular-route.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/ng-grid.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/dumai/manager/model/report/reportJs.js" type="text/javascript" charset="utf-8"></script>

</body>
</html>