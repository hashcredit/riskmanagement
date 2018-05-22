<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myPhoneApp">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>电核任务</title>
    <link rel="stylesheet/less" href="${ctx}/static/css/phoneReview.less" type="text/less">
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
    <script charset="utf-8" type="text/javascript"  src="${ctx}/static/script/lib/jquery.min.js"></script>
    <script charset="utf-8" type="text/javascript"  src="${ctx}/static/script/lib/angular.min.js"></script>
    <script charset="utf-8" type="text/javascript"  src="${ctx}/static/script/lib/jquery-ui.js"></script>
    <script charset="utf-8" type="text/javascript"  src="${ctx}/static/script/lib/moment.js"></script>
    <script charset="utf-8" type="text/javascript"  src="${ctx}/static/dumai/global/js/utils.js"></script>
    <script charset="utf-8" type="text/javascript"  src="${ctx}/static/dumai/auditing/js/auditingTask.js"></script>
</head>
<body style="background:#f6f6f6" ng-controller="myCtrl">
<div class="reviewContain" id="reviewContain" >
    <div class="header">
        <div class="title">电核任务列表 > <em>审核</em></div>
        <div class="exit"><span>欢迎您,</span><em ng-bind="username"></em><a href="${ctx}">[退出]</a></div>
    </div>
    <div class="clearfix"></div>
    <div class="bodies">
        <div class="info_top">
            <div class="top_left">
                <table class="table table-bordered" style="margin-bottom:0;">
                    <tbody>
                    <tr>
                        <td><span>姓名：</span><span ng-bind="orderInfo.name"></span></td>
                        <td><span>身份证号：</span><span ng-bind="orderInfo.card_num"></span></td>
                        <td><span>银行卡号：</span><span ng-bind="orderInfo.bank_num"></span></td>
                        <td><span>电话：</span><span ng-bind="orderInfo.mobile"></span></td>
                    </tr>
                    </tbody>
                </table>
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td><span>机构名称：</span><span ng-bind="orderInfo.organization"></span></td>
                        <td><span>业务类型：</span><span ng-bind="orderInfo.typeName"></span></td>
                        <td><span>贷款金额：</span><span ng-bind="orderInfo.sqje"></span></td>
                        <td><span>贷款期限：</span><span ng-bind="orderInfo.Jkqx"></span></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="top_right" ng-click="infoReport(orderInfo.code)">
                <a href="javascript:;">查看个人详细信息</a>
            </div>
        </div>
        <div class="clearfix"></div>
        <div class="info_middle">
            <h5><span class="elvesIcon"></span>电核信息</h5>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>电核项</th>
                    <th>描述</th>
                    <th>结果</th>
                    <th>是否命中</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in phoneLists">
                    <td ng-bind="item.manager_item_name"></td><td ng-bind="item.dh_description"></td><td ng-bind="item.auto_result"></td>
                    <td>
                    	<label style="width:100%;" class="dh_con">
                    		<input type="radio" name="{{$index}}" style="width:30%;height: 16px; vertical-align: sub;" value="true" ng-checked="item.result== 'true'">是
                    		<input type="radio" name="{{$index}}" style="width:30%;height: 16px; vertical-align: sub;" value="false" ng-checked="item.result== 'false'">否
                    	</label>
                    </td>
                    <td><input type="text" class="remarkValue" value="{{item.remark}}"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="info_bottom">
            <h5><span class="elvesIcon"></span>增项信息</h5>
            <table class="table table-bordered">
                <thead>
                  <tr>
                    <th>电核项</th>
                    <th>描述</th>
                    <th>是否命中</th>
                    <th>备注</th>
                  </tr>
                </thead>
                <tbody>
                	<tr ng-repeat="item in addManagerLists">
                        <td ng-bind="item.item_name"></td><td ng-bind="item.item_value"></td>
                        <td>
                           <label style="width:100%;" class="abnormal_con">
                    		  <input type="radio" name="{{$index}}+type" style="width:30%;height: 16px;vertical-align: sub;" value="true" ng-checked="item.dh_content == 'true'">是
                    		  <input type="radio" name="{{$index}}+type" style="width:30%;height: 16px;vertical-align: sub;" value="false" ng-checked="item.dh_content == 'false'">否
                    	   </label>
                        <td>
                           <input type="text" class="abnormal_remark" value="{{item.remark}}">
                        </td> 
                     </tr>
                </tbody>
            </table>
            <div class="text_content">
                <div id="left" style="width: 60%">
                   <span>电核基本情况：</span>
            	   <textarea rows="10" placeholder="电核基本情况：" ng-model="base" ng-bind="managerLists.description" class="base"></textarea>
                </div>
                <div id="right" style="width: 39%;margin-left: 0.7%">
                	<span>异常情况：</span>
                   <textarea rows="10"  placeholder="异常情况：" ng-model="abnormal" ng-bind="managerLists.other_exception" class="abnormal"></textarea>
                </div>
            </div>
            <div class="playButton">
                <span class="refer elvesIcon" ng-click="referInfo(phoneListsB,addManagerListsB,'1')" disabled=""></span>
                <span class="pause elvesIcon" ng-click="pauseInfo(phoneListsB,addManagerListsB,'2')"></span>
            </div>
        </div>
      </div>
	</div>
	<div class="infoReportdh">
	  <div class="infoReport2">
		<h4>个人详细报告 <span ng-click="closeReport()" class="elvesIcon"></span></h4>
          <ul>
            <li><span>姓名：</span><span ng-bind="basics.name"></span></li>
            <li><span>性别：</span><span ng-bind="basics.sex"></span></li>
            <li><span>年龄：</span><span ng-bind="basics.age"></span></li>
            <li><span>婚姻：</span><span ng-bind="basics.married"></span></li>
            <li><span>学历：</span><span ng-bind="basics.education"></span></li>
            <li><span>身份证号码：</span><span ng-bind="basics.card_num"></span></li>
            <li><span>手机号码：</span><span ng-bind="basics.mobile"></span></li>
            <li><span>银行卡：</span><span ng-bind="basics.bank_num"></span></li>
            <li><span>常住地址：</span><span ng-bind="basics.address"></span></li>
            <li><span>籍贯：</span><span ng-bind="birthplace"></span></li>
            <li><span>业务类型 ：</span><span ng-bind="basics.typename"></span></li>
            <li><span>借款金额 ：	</span><span ng-bind="basics.sqje"></span></li>
            <li><span>借款期限 ：	</span><span ng-bind="basics.Jkqx"></span></li>
            <li><span>家庭住址 ：	</span><span ng-bind="homeAddress"></span></li>
            <li><span>职业情况 ：</span><span ng-bind="basics.profession"></span></li>
            <li><span>固定收入 ：</span><span ng-bind="basics.income"></span></li>
            <li><span>紧急联系人1：</span><span style="width:15%;" ng-bind="basics.linkname1"></span><span style="width:20%;" ng-bind="basics.linkphone1"></span><span style="width:15%;"  ng-bind="basics.linkReation1"></span></li>
            <li><span>紧急联系人2：</span><span style="width:15%;" ng-bind="basics.linkname2"></span><span style="width:20%;" ng-bind="basics.linkphone2"></span><span style="width:15%;"  ng-bind="basics.linkReation2"></span></li>
            <ul class="generated">
                <li ng-repeat="aa in arrays"><span ng-cloak>{{aa.titleName}} ：</span><span>{{aa.titleContent}}</span></li>
            </ul>
          </ul>
         <img data-ng-src="data:image/gif;base64,{{perImg}}" alt={{basics.name}}{{perImg}} ng-cloak>
	  </div>
	</div>
</body>
</html>