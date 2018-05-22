<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DocType html>
<html lang="en" ng-app="myRule">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>规则中心</title>
		<link rel="stylesheet/less" href="${ctx}/static/css/model.less" type="text/less">
		<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
		<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
		<link rel="stylesheet" href="${ctx}/static/css/style.css">
		<link rel="stylesheet" href="${ctx}/static/css/load.css">
		<link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
		<%-- <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script> --%>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui-min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/controller/ruleController.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
	</head>
<body>
<div id="menuRule" ng-controller="myMenu3">
    <div class="header">
        <ul>
            <li><i class="icon_system"></i><span><a href="${ctx}/sysmgr/funsettings/toPage.do">系统设置</a></span></li>
            <li><i class="icon_data"></i><a href="${ctx}/static/data_display/data_display.html">数据报表</a></li>
            <li><i class="icon_traffic"></i><span>数据流量</span></li>
        </ul>
        <h5><a href="${ctx}/logout.do">退出</a></h5>
        <div class="info_header">
            <label class="info_header_date"> <div class="markLine"></div><span class="dark"></span><p>时间: <input type="text" id="datepicker" ng-model="date"><i ng-click="dispear()" ></i></p></label>
            <ul>
                <li ><a href="${ctx}/loanFront/toLoanFront.do">贷前审核</a></li>
				<li class="current"><a href="${ctx}/loanMiddle/toLoanMiddle.do">贷中跟踪</a></li>
				<li><a href="${ctx}/loanOverdue/toList.do">逾期催款</a></li>
            </ul>
           </div>
    </div>
    <div class="bodies">
        <div class="info_content" >
            <div class="left">
                <div class="member">
                    <img src="${ctx}/static/images/photosh.png" alt="">
                    <p>XXXXXXX</p>
                </div>
                <div class="business_type">
                    <ul class="business changeColor">
                       <ul class="business changeColor rule_business">
                    	<a style="display:none;" class="yinCang yinCangType">{{typeList.code}}</a>
                        <h4 ng-click="togg('.business',togflag)"><span></span>业务类型<i class="xiala" ng-class="{true:'up',false:'down'}[togflag]" ></i></h4>
                        <li ng-repeat="typeList in typeLists" ng-click="getData($event,typeList.code)" ng-bind="typeList.name" ></li>
                    </ul>
                    </ul>
                    <ul class="result">
                        <li style="border-top: 1px solid #586772"><span class="no"></span>未审核</li>
                        <li><span class="refuse"></span>拒绝</li>
                    </ul>
                    <ul class="waterBox" >
                       <%--  <li><span class="ruleBox"></span><a href="${ctx}/rule/toLoanFrontRuleList.do">规则池</a><i ng-class="{OFF:'bg1',ON:'bg2'}[flagNF]">启用</i></li>
                         --%>
                         <li><span class="modelBox"></span><a href="${ctx}/rule/toLoanFrontModelList.do">模型池</a></li>
                         
                    </ul>

                </div>
            </div>
            <div class="info_content1 info_rule" style="overflow: hidden;">
                <div class="rule_list">
                    <div class="ruleContentBox">
                        <ul class="square square1">
                          <div class="ruleButt" style="opacity:0;"><h5>启用全部规则</h5><span ng-click="changeAll_1()" ng-class="{true:'openRule',false:'closeRule'}[ruleFlag1]"></span></div> 
                            <h4>鉴权</h4>
                            <li ng-repeat="authenticationData in authenticationDatas">
                                <div class="rule_content"><span class="rule_icon {{authenticationData.id}}"></span></div>
                                <div class="rule_tit"><p style="margin-bottom: 8px" ng-bind="authenticationData.name"></p><p ng-bind="authenticationData.description"></p></div>
                               <!--  <div class="{{authenticationData.statusflag}}" ng-click="changeItem1(authenticationData)"></div> -->
                            </li>
                        </ul>
                        <ul class="square square2">
                            <div class="ruleButt"><h5>启用全部规则</h5><span ng-click="changeAll_2()" ng-class="{true:'openRule',false:'closeRule'}[ruleFlag2]"></span></div>
                            <h4>反欺诈</h4>
                            <li ng-repeat="lieData in lieDatas">
                                <div class="rule_content"><span class="rule_icon {{lieData.id}}"></span></div>
                                <div class="rule_tit"><p style="margin-bottom: 8px" ng-bind="lieData.name" class="getRule_title"></p><p ng-bind="lieData.description"></p></div>
                                <div class="{{lieData.statusflag}}" ng-click="changeItem2(lieData)"></div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="tipBox" ng-class="{true:'show_tip',false:'hide_tip'}[tipFlag]">
                    <div class="tipRule">
                        <h5><span  ng-click="tipClose()"></span></h5>
                        <ul>
                            <h5>鉴权规则</h5>
                            <li ng-repeat="aa in a" ng-bind="aa.name"></li>
                        </ul>
                        <ul>
                            <h5>反欺诈</h5>
                            <li ng-repeat="bb in b" ng-bind="bb.name"></li>
                        </ul>
                        <div  class="okRule"><input type="button" ng-click="backIndex()" value="确定"></div>
                    </div>
                </div>
                <input type="button" value="提交" ng-click="tJRule()" class="tJ">
            </div>
        </div>
      </div>
</div>
</body>
</html>