<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
<body>
                <div class="member">
                    <div class="userImg"></div>
                    <p ng-bind="username"></p>
                </div>
                <div class="business_type">
                    <ul class="business changeColor" >
                    	<a style="display:none;" class="yinCang yinCangType">{{stautus1}}</a>
                        <h4 ng-click="togg('.business',togflag)"><span class="spriteIcon service_type"></span>业务类型<i class="spriteIcon xiala" ng-class="{true:'up',false:'down'}[togflag]" ></i></h4>
                         <li ng-click="getData($event,'','相关')" class="currColor">全部</li>
                        <li ng-repeat="typeList in typeLists track by $index" ng-click="getData($event,typeList.code,typeList.name)" ng-bind="typeList.name" ng-cloak></li>
                    </ul>
                    <ul class="result">
                    	<a style="display:none;" class="yinCang yincangResult">{{typeCode}}</a>
                        <li style="border-top: 1px solid #586772" ng-click="loanFrontData(0)"><span class="no spriteIcon"></span>未审核</li>
                        <li ng-click="loanFrontData(2)"><span class="refuse spriteIcon" ></span>拒绝</li>
                    </ul>
                    <ul class="waterBox" >
                        <li ng-click="ruleHref('/dumai_qt/rule/toLoanFrontRuleList.do')" style="cursor: pointer;"><span class="ruleBox"></span><a href="${ctx}/rule/toLoanFrontRuleList.do">规则池</a><i ng-class="{OFF:'bg1',ON:'bg2'}[flagNF]">启用</i></li>
                        
                        <li ng-click="ruleHref('/dumai_qt/model/toLoanFrontModelList.do')"><span class="modelBox spriteIcon"></span><a href="javascript:;">模型池</a></li>
                    </ul>

                </div>
</body>
</html>