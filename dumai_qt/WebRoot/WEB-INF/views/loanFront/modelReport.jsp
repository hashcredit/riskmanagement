<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myApp">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
     <link rel="stylesheet" href="${ctx}/static/css/ng-grid.css">
    <link rel="stylesheet" href="${ctx}/static/css/dumai/manager/model/report/report.css">
</head>
<body>
  <div class="container" ng-controller="myCtrl">
    <div class="logo">
        <img src="${ctx}/img/logo_x.png" alt="logo">
        <p>风控评审报告</p>
    </div>
<div class="title">
        <ul>
            <li><span>客户：</span><span>{{basics.name}}</span></li>
            <li><span>业务类型：</span><span>{{personinfos.thetype_name}}</span></li>
            <li style="position: relative;top:20px; margin-left:50px;">
                <select ng-model="selectValue" ng-options="item.id as item.value for item in data"></select><br /><br />
                <div ng-switch="selectValue" class="reports">
                    <div ng-switch-when="1" >
                        <div class="soloInfo">
                        <p>个人详细信息</p>
                        <div class="basicInfo">
                        <ul>
                        <li><span>姓名：</span><span>{{basics.name}}</span></li>
                        <li><span>性别：</span><span>{{personinfos.sex}}</span></li>
                        <li><span>年龄：</span><span>{{personinfos.age}}</span></li>
                        <li><span>婚姻：</span><span>{{basicInputs.marital_status}}</span></li>
                        <li><span>学历：</span><span>{{personinfos.education}}</span></li>
                        <li><span>身份证号码：</span><span>{{basics.card_num}}</span></li>
                        <li><span>手机号码：</span><span>{{basics.mobile}}</span></li>
                        <li><span>银行卡：</span><span>{{basics.bank_num}}</span></li>
                        <li><span>常住地址：</span><span>{{basicInputs.address}}</span></li>
                        <li><span>籍贯：</span><span>{{personinfos.birthplace}}</span></li>
                        <li><span>业务类型 ：</span><span>{{personinfos.thetype_name}}</span></li>
                        <li><span>借款金额 ：	</span><span>{{basics.sqje}}</span></li>
                        <li><span>借款期限 ：	</span><span>{{basics.Jkqx}}</span></li>
                        <li><span>家庭住址 ：	</span><span>{{basicInputs.address}}</span></li>
                        <li><span>单位性质 ：	</span><span>{{basicInputs.enterprise_nature}}</span></li>
                        <li><span>单位名称 ：	</span><span>{{basics.organization}}</span></li>
                        <li><span>单位地址 ：	</span><span>{{basicInputs.enterprise_address}}</span></li>
                        <li><span>职业情况 ：</span><span>{{personinfos.profession}}</span></li>
                        <li><span>固定收入 ：</span><span>{{personinfos.income}}</span></li>
                        <li><span>紧急联系人1：</span><span>{{personinfos.linkname1}}</span><span>{{personinfos.linkphone1}}</span><span>{{personinfos.linkReation1}}</span></li>
                        <li><span>紧急联系人2：</span><span>{{personinfos.linkname2}}</span><span>{{personinfos.linkphone2}}</span><span>{{personinfos.linkReation2}}</span></li>

                        </ul>
                        <img src="data:image/gif;base64,{{personinfos.PHOTO}}" alt={{basics.name}}>
                        </div>

                        </div>
                        <div class="fraudRule">
                        <p>反欺诈规则</p>
                        <h5>审核结果：<span>正常</span></h5>
                        <div class="gridStyle" ng-grid="gridOptions"></div>
                        </div>
                        <div class="fraudScore" >
                        <h4 my-directive>反欺诈评分 <span>总分：{{totals|aa|number:2}}</span></h4>
                        <ul class="first " ng-repeat="cheat in cheats">
                        <li><span>{{cheat.manager_item_name}}</span><span>得分：{{cheat.weight_score|aa|number:2}}</span></li>
                        </ul>
                        <div class="gridStyle" ng-grid="gridOptions"></div>
                        </div>
                        <div class="creditCheck">
                        <h4 my-directive>信用评分 <span>总分：{{creditTotals|aa|number:2}}</span></h4>
                        <ul class="first" ng-repeat="credit in credits">
                        <li><span>{{credit.manager_item_name}}</span><span>得分：{{credit.weight_score|aa|number:2}}</span></li>
                        </ul>
                        <div class="gridStyle" ng-grid="gridOptions"></div>
                        </div>
                        <div class="dataDetailed">
                        <p>详细数据</p>
                        <ul>
                        <li><span>涉诉：</span><a href="data.html">查看数据</a></li>
                        <li><span>通信运营商数据:</span><a href="data.html">查看数据</a></li>
                        <li><span>银行卡验证:</span><a href="#">一致</a></li>
                        <li><span>车辆基本信息：</span><a href="#">查看数据</a></li>
                        <li><span>同住人信息：</span><a href="#">查看数据</a></li>
                        <li><span>公积金：</span><a href="#">查看数据</a></li>
                        <li><span>犯罪：</span><a href="#">查看数据</a></li>
                        </ul>
                        </div>
                    </div>
                    <div ng-switch-when="2" >222</div>
                </div>
            </li>
        </ul>
    </div>
        
</div>
    <script src="${ctx}/static/script/lib/jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/angular.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/angular-route.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/lib/ng-grid.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/static/script/dumai/manager/model/report/reportJs.js" type="text/javascript" charset="utf-8"></script>

</body>
</html>