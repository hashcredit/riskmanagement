<%@ page contentType="text/html;charset=UTF-8" %>
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
		<link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
		<link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
		<%-- <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script> --%>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui-min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/bootstrap2.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/loading-bar.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.min.js"></script>
		<script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/loanFront/js/modelFrontController.js"></script>

        <style>
            .tJ{
                position: fixed;
                display: inline-block;
                width: 72px;
                height: 30px;
                line-height: 30px;
                text-align: center;
                background: #58d0a4;
                cursor: pointer;
                border-radius: 4px;
                outline: none;
                border: none;
                font-size: 12px;
                letter-spacing: 20px;
                padding-left: 15px;
                bottom:20px;
                left:54.5%;
                margin-left: -70px;
            }
        </style>
	</head>
<body>

<div id="menuModel" ng-controller="myMenu3">
    <div class="header">
        <!-- 导入网页头 -->
		<%@ include  file="/WEB-INF/views/head/head.jsp"%>
        <div class="info_header">
            <label class="info_header_date"> <div class="markLine"></div><span class="dark"></span><p>时间: <input type="text" id="datepicker" ng-model="date"><i class="spriteIcon time"></i></p></label>
             <ul>
                <li class="current"><a href="${ctx}/loanFront/toLoanFront.do">贷前审核</a></li>
				<li><a href="${ctx}/loanMiddle/toLoanMiddle.do">贷中跟踪</a></li>
				<li><a href="${ctx}/loanOverdue/toList.do">逾期催款</a></li>
            </ul>
          </div>
    </div>
    <div class="bodies">
        <div class="info_content" >
            <div class="left" style='overflow:hidden'>
                <div class="member">
                    <div class="userImg"></div>
                    <p ng-bind="username"></p>
               	<font color=ffff66 >模型池</font>
                </div>
                <div class="business_type">
                    <ul class="business changeColor rule_business">
                       	<a style="display:none;" class="yinCang yinCangType">{{code1}}</a>
                        <h4 ng-click="togg('.business',togflag)"><span class="spriteIcon service_type"></span>业务类型<i class="spriteIcon xiala" ng-class="{true:'up',false:'down'}[togflag]" ></i></h4>
                        <li ng-repeat="typeList in typeLists" ng-click="getData($event,typeList.code)" ng-bind="typeList.name" ></li>
                    	
                    </ul>
                   <!--  <ul class="result">
                        <li style="border-top: 1px solid #586772"><span class="no"></span>未审核</li>
                        <li><span class="refuse"></span>拒绝</li>
                    </ul> -->
                    <ul class="waterBox" >
                        <%-- <li  ng-click="ruleHref('/dumai_qt/rule/toLoanFrontRuleList.do')"><span class="ruleBox"></span><a href="${ctx}/rule/toLoanFrontRuleList.do">规则池</a><i ng-class="{OFF:'bg1',ON:'bg2'}[flagNF]">启用</i></li>
                         --%>
                         <li ng-click="ruleHref('/dumai_qt/loanFront/toLoanFront.do')"><span class="modelBox spriteIcon"></span><a href="javascript:;">信贷流程跟踪</a></li>
                    </ul>

                </div>
            </div>
            <div class="info_content1 info_rule" style="overflow-x:hidden;overflow-y:scroll;bottom: 0px;margin-bottom: 64px;top:0px;">
                <div class="rule_list">
                    <div class="ruleContentBox">
                        <ul class="square square1">
                            <h4>鉴权</h4>
                            <li ng-repeat="authenticationData in authenticationDatas" style='height: 65px;'>
                                <div class="rule_content"><span class="rule_icon {{authenticationData.id}}"></span></div>
                                <div class="rule_tit"><p style="margin-bottom: 8px" ng-bind="authenticationData.name"></p><p ng-bind="authenticationData.description"></p></div>
                               <!--  <div class="{{authenticationData.statusflag}}" ng-click="changeItem1(authenticationData)"></div> -->
                            </li>
                        </ul>
                      <ul class="square square2">
                            <h4>反欺诈</h4>
                            <li ng-repeat="lieData in lieDatas" style='height: 65px;'>
                                <div class="rule_content"><span class="rule_icon {{lieData.id}}"></span></div>
                                <div class="rule_tit"><p style="margin-bottom: 8px" ng-bind="lieData.name" class="getRule_title"></p><p ng-bind="lieData.description"></p></div>
                                <div class="{{lieData.statusflag}}" ng-click="changeItem2(lieData)"></div>
                             	<span class="ruleBUtton_status {{lieData.ruleBUtton_status}}" ng-bind="lieData.ruleBUtton_content"></span>
                            
                            </li>
                        </ul>
                         <ul class="square square3">
                            <h4>量化定价</h4>
                            <li ng-repeat="modelItem in modelDatas" style='height: 65px;'>
                                <div class="rule_content"><span class="rule_icon {{modelItem.id}}"></span></div>
                                <div class="rule_tit"><p ng-bind="modelItem.name" class="getRule_title"></p></div>
                                <div class="{{modelItem.statusflag}} modelButton" ng-click="changeItem_model(modelItem,$event)"></div>
                             	<span class="ruleBUtton_status {{modelItem.modelBUtton_status}}" ng-bind="modelItem.modelBUtton_content"></span>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="tipBox">
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
                        <ul>
                            <h5>量化定价</h5>
                            <li ng-repeat="cc in c" ng-bind="cc.name"></li>
                        </ul>
                        <div  class="okRule"><input type="button" ng-click="backIndex()" value="确定"></div>
                    </div>
                </div>
            </div>
                 <input type="button" value="提交" ng-click="tJRule()" class="tJ" >
        </div>
    </div>
    <div class="exit_tip">
          <div>
             <p>确认退出？</p>
             <span class="sure" ng-click="exit_tip()"></span>
             <span class="cancle_tip" ng-click="close_exit_Tip()"></span>
          </div>
     </div>
</div>
	
</body>
</html>