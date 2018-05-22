<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="managerApp">
<head>
    <meta charset="UTF-8">
    <title>分配电核任务</title>
    <link rel="stylesheet/less" href="${ctx}/static/css/phoneReview.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/ui-bootstrap-tpls.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/auditing/js/distributionTask.js"></script>
</head>
<body style="background:#f6f6f6">
    <div class="managerContain" id="managerContain" ng-controller="managerCtrl">
        <div class="leftList">
            <div class="leftList_top"></div>
            <div class="leftList_bottom">
                <ul>
                    <li class="current"><i class="icon icon_task elvesIcon"></i><a href="${ctx}/auditingTask/toDistribution.do">分配电核任务</a></li>
                    <li><i class="icon icon_order elvesIcon"></i><a href="${ctx}/auditingTask/toOrderList.do">查询订单</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toList.do">鉴权结果</a></li>
                    <li><i class="icon icon_power elvesIcon"></i><a href="${ctx}/auth/toAuth.do">身份验证</a></li>
                </ul>
            </div>
        </div>
        <div class="rightContain">
            <div class="rightContain_title">
                <div class="title">电核任务列表 > <em>分配电核任务</em></div>
                <div class="exit"><span>欢迎您,</span><em ng-bind="username"></em><a href="${ctx}">[退出]</a></div>
            </div>
            
            <div class="rightContain_bodies">
			    <div class="conditions">
                    <ul>
                        <li>审核人：
                        	<select ng-Model="disValue" ng-change="changeDatas()" class="elvesIcon">
								<option ng-repeat="distributionList in distributionLists"  ng-bind="distributionList.surname" value="{{distributionList.code}}" ></option>
							</select>
                        </li> 
                        <li class="allocation" ng-click="allocationTask()">
                    		<span class="elvesIcon"></span>
                		</li>
                    </ul>
                </div>
                <table class="table table-bordered speicalTable">
                    <thead>
                    <tr >
                        <th></th>
                        <th>订单生成日期</th>
                        <th>客户姓名</th>
                        <th>贷款金额</th>
                        <%--<th>业务类型</th>--%>
                        <th>机构名称</th>
                        <th>审核状态</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr ng-repeat="item in managerLists" >
                            <td style="padding-left: 0;padding-right: 0;" ><span ng-click="change($event)" style="vertical-align: middle;" class="disList elvesIcon"><input type="checkbox" class="changeStyle" ng-bind="item.code" value="{{item.code}}"></span></td>
                            <td ng-bind="item.create_time"></td>
                            <td ng-bind="item.customer_name"></td>
                            <td ng-bind="item.sqje"></td>
                            <%--<td ng-bind="item.typeName"></td>--%>
                            <td ng-bind="item.organization"></td>
                            <td ng-bind="item.status"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        	<section class="row pageDemo" >
         		<section style="margin:0px auto;">
            		<pagination  total-items="totalItems" ng-model="currentPage" class="pagination-sm"  max-size="maxSize" previous-text="〈" next-text="〉"  rotate="false" ng-change="getPageLists()"></pagination>
            	</section>
       		</section>
        </div>
    	<div class="allocationTip">
    		<div>请选择订单，不能为空！
    		  <input value="确定" type="button" ng-click="tipclose()">
    		</div>
    	</div>	
    	<div class="allocationOkTip">
    		<div>分配成功！
    		  <input value="确定" type="button" ng-click="tipOkclose()">
    		</div>
    	</div>	
    </div>
</body>
</html>