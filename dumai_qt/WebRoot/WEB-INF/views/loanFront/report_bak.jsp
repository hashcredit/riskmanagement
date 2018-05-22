<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myApp">
<head>
    <meta charset="UTF-8">
    <title>风控评审报告</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
     <link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
      <link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
    <link rel="stylesheet" href="${ctx}/static/css/dumai/manager/model/report/report.css">
    <style>
    .loading.rhomb::after {
    font-size:40px;
		color:blue;
	}
    </style>
</head>
<body>
	<div id="loading-bar-container"></div>
  <div class="container" ng-controller="myCtrl">
    <div class="logo">
        <img src="${ctx}/img/logo_x.png" alt="logo">
        <p>风控评审报告</p>
       <!--  <div class="shenhe"></div>	 -->
    </div>
<div class="title">
        <ul>
            <li><span>客户：</span><span ng-bind="basics.companyName"></span></li>
            <li><span>业务类型：</span><span ng-bind="basics.typename"></span></li>
            <li style="position: relative;top:20px; margin-left:50px;">
              <select ng-model="selectValue" ng-options="data.code as '第'+(idx*1+1)+'条' for (idx,data) in aryData"   ng-change='change()'></select> 
                <br/><br />
                 <div  class="reports">
                    <div >
                        <div class="soloInfo">
                          <p>个人详细信息</p>
                        	<div class="basicInfo">
                        	   <ul>
                        		<li><span>姓名：</span><span ng-bind="basics.name"></span></li>
                        		<li><span>性别：</span><span ng-bind="basics.sex"></span></li>
                        		<li><span>年龄：</span><span ng-bind="basics.age"></span></li>
                        		 <li><span>婚姻（查询）：</span><span ng-bind="basicDetail.maritalStatus"></span></li> 
                        		<li><span>学历：</span><span ng-bind="basicDetail.education"></span></li> 
                        		<li><span>身份证号码：</span><span ng-bind="basics.card_num"></span></li>
                        		<li><span>手机号码：</span><span ng-bind="basics.mobile"></span></li>
                        		<li><span>银行卡：</span><span ng-bind="basics.bank_num"></span></li>
                        		<li><span>职业情况 ：</span><span ng-bind="basicDetail.department"></span></li>
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
                        <div class="fraudRule">
                        <p>反欺诈规则</p>
                        <h5>审核结果：<span ng-bind="tongguo"></span></h5>
                        <div class="gridStyle" ng-grid="gridOptions"></div>
                        </div>
                        <div class="fraudScore" >
                        <h4 my-directive>反欺诈评分 <span ng-cloak>总分：{{totals|aa|number:2}}</span></h4>
                        <ul class="first " ng-repeat="cheat in cheats">
                        <li><span ng-cloak>{{cheat.manager_item_name}}</span><span ng-cloak>得分：{{cheat.weight_score|aa|number:2}}</span></li>
                        </ul>
                        <div ng-controller="gridCtrl_rule" style="height:260px;" class="fraudGrid">
                           <div class="gridStyle" ng-grid="gridOptions"></div>
                         </div>
                       </div>
                        <div class="creditCheck">
                        <h4 my-directive>信用评分 <span ng-cloak>总分：{{creditTotals|aa|number:2}}</span></h4>
                        <ul class="first" ng-repeat="credit in credits">
                        <li><span ng-cloak>{{credit.manager_item_name}}</span><span ng-cloak>得分：{{credit.weight_score|aa|number:2}}</span></li>
                        </ul>
                        <div ng-controller="gridCtrl_check" style="height:260px;" class="creditGrid">
                           <div class="gridStyle" ng-grid="gridOptions"></div>
                         </div>
                        </div>
                        <div class="dataDetailed">
                        <p>详细数据</p>
                        <ul class="detailList">
                           <li ng-repeat="detailList in detailLists"><span ng-cloak>{{detailList.name}}：</span><a href='${ctx}/report/toDataDetail.do?dm_3rd_interface_code={{detailList.code}}&orderCode=${param.code}'>查看数据</a></li> 
                        </ul>
                        </div>
                    </div> 
                </div>
            </li>
        </ul>
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