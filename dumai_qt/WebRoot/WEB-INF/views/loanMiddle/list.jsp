<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myIndex">
<head>
	<meta charset="UTF-8">
	<title>贷中跟踪</title>
	<link rel="stylesheet/less" href="${ctx}/static/css/model.less" type="text/less">
	<link rel="stylesheet" href="${ctx}/static/css/baseReport2.css"> 
	<link rel="stylesheet" href="${ctx}/static/dumai/global/css/default.css"> 
	<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
	<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
	<link rel="stylesheet" href="${ctx}/static/css/style.css">
	<link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
 	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
 	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
 	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.artDialog.js"></script>
 	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui-min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/bootstrap2.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.min.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/loading-bar.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/html2pdf/html2canvas.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/html2pdf/jspdf.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/html2pdf/renderPDF.js"></script>
    <script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
 	<script src="${ctx}/static/dumai/loanMiddle/js/loanMiddleController.js"></script>
 	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/layer/layer.js"></script>
	<style type="text/css">
		.label_btn{
		position: absolute;
	    margin-left: 8px;
	    border: 1px solid #43535b;
	    border-radius: 2px;
	    background: #304450;
	    color: #95a3c1;    
	    font-size: 16px;
	    margin-top: 10px;	   
	    width:107px;
	    height:32px;
	    line-height:32px;
		}
	</style>
</head>
<body>
<div id="menu" ng-controller="myMenu">
	<div class="header">
		<!-- 导入网页头 -->
		<%@ include  file="/WEB-INF/views/head/head.jsp"%>
		<div class="info_header">
			<label class="info_header_date">
			 	<div class="markLine"></div>
			 	<span class="dark"></span>
			 	<p>时间: <input type="text" id="datepicker" ng-model="date"><i class="spriteIcon time"></i></p>
			</label>
			
			<button class="label_btn" ng-click="getAllData($event,'','相关')">查询全部</button>
			
			<ul style="left:270px">
				<li><a href="${ctx}/loanFront/toLoanFront.do">贷前审核</a></li>
				<li class="current"><a href="${ctx}/loanMiddle/toLoanMiddle.do">贷中跟踪</a></li>
				<li><a href="${ctx}/loanOverdue/toList.do">逾期催款</a></li>
			</ul>
			<label class="selectNameBox"><i class="search spriteIcon" ng-click='searchMem()'></i><input type="text" placeholder="请输入借款人身份证或姓名" class="selectName"  ng-model="value" ng-blur="tipErr()" ng-keyup="search(e)"></label></div>
	</div>
	<div class="bodies">
		<div class="info_content">
			<div class="left" style='overflow: hidden;'>
				<div class="member">
					<!-- 导入用户信息 -->
                    <%@ include file="/WEB-INF/views/head/user.jsp" %>
					<font color=ffff66 >信贷流程跟踪</font>
				</div>
				<div class="business_type">
				 <ul class="business changeColor">
				 		<a style="display:none;" class="yinCang yinCangType">{{stautus3}}</a>
                        <h4 ng-click="togg('.business',togflag)"><span class="spriteIcon service_type"></span>业务类型<i class="spriteIcon xiala" ng-class="{true:'up',false:'down'}[togflag]" ></i></h4>
                         <li ng-click="getData($event,'','相关')" class="currColor">全部</li>
                        <li ng-repeat="typeList in typeLists" ng-click="getData($event,typeList.code,typeList.name)" ng-bind="typeList.name" render-finish></li>
                    </ul>
					<ul class="result">
						<a style="display:none;" class="yinCang yincangResult">{{typeCode}}</a>
						<li style="border-top: 1px solid #586772" ng-click="loanMiddleData(0)"><span class="normal spriteIcon"></span>正常 </li>
						<li  ng-click="loanMiddleData(1)"><span class="guanzhu spriteIcon"></span>关注</li>
						<li ng-click="loanMiddleGPS()"><span class="gps spriteIcon"></span>GPS</li>
					</ul>
					<!-- <ul class="waterBox" >
						<li><span class="ruleBox"></span><a href="javascript:;">规则池</a><i ng-class="{OFF:'bg1',ON:'bg2'}[flagNF]">启用</i></li>
						${ctx}/rule/toLoanMiddleRuleList.do 
						<li><span class="modelBox spriteIcon"></span><a>模型池</a></li>
					</ul> -->

				</div>
			</div>
			<div class="info_content1">
				<p ng-bind="tipNOdata"></p>
				 <ul ng-repeat="item in persons" class="perList" render-finish>
                    <li class="curDate">
                        <h5 ng-cloak><i class="calendar"></i>{{item.data}}</h5>
                    </li>
                    <li ng-repeat="every_member in item.person" ng-dblclick="baseReport(every_member.code,index)" target="_blank">
                        <div class="person_image"><p ng-bind="every_member.name"></p><i class="{{every_member.status2}} spriteIcon"></i><img data-ng-src="{{every_member.card_photo}}" realSrc="{{every_member.card_photo}}" ></div>
                       <p><span>身份证号：</span><span ng-bind="every_member.card_num"></span></p> 
                    </li>
                    <div class="clearfloat"></div>
                </ul>
			</div>
			 <div class="detailReport" >
			    <h4 ng-bind="reportName"></h4>
            	<span ng-click="closeReport2()" class="report_close spriteIcon"></span>
                <div class="detailtable">
                	<table class=" table table-bordered" style="table-layout: fixed;">
                        <thead>
    					<tr>
						  <th>类型</th>
						  <th>结果</th>
    					</tr>
   					 </thead>
                        <tbody>
                           <tr ng-repeat="item in detailInfos">
        			       <td ng-bind="item.name_zh"></td>
        			       <td ng-bind="item.value"></td>
    					</tr>
                        </tbody>
                     </table>
                </div>
            </div>
			 <div class="litigaReport" >
			    <h4 ng-bind="reportName"></h4>
                <h5><i ng-click="closeReportLiti()" class="report_close spriteIcon report_detail"></i></h5>
                <ul ng-repeat="litigation in litigations">
                      <table class="table table-bordered" style="table-layout: fixed;">
                        <thead>
    					  <tr>
						     <th>被执行人姓名或名称</th>
						     <th>证件号码</th>
						     <th>执行法院</th>
						     <th>执行案号</th>
						     <th>执行内容</th>
						     <th>日期类别</th>
						     <th>具体日期</th>
						     <th>执行状态</th> 
						     <th>异案备注</th>
    					 </tr>
   					 </thead>
                     <tbody>
                       <tr >
        			     <td ng-bind="item.shortValue"  ng-repeat="item in litigation.onemsglist" title="{{item.propervalue}}"></td>
        			     <td><input type="text" style="width:100%;"></td>
    				   </tr>
                     </tbody>
                 </table>
                </ul>
            </div>
		</div>
	</div>
	<div class="selectError">
          <div>
             <p>暂无搜索相关信息</p>
             <span class="sure" ng-click="closeTip()"></span>
          </div>
    </div>
      <div class="exit_tip">
          <div>
             <p>确认退出？</p>
             <span class="sure" ng-click="exit_tip()"></span>
             <span class="cancle_tip" ng-click="close_exit_Tip()"></span>
          </div>
        </div>
	<div class="markReport" >
        <div class="baseReport" id="baseReport">
              <!--  <ul style="overflow-y: auto;" class="base_header" style="height:5px; border:none;background:#16242E;">
                <li><a style="color:white"></a></li>
              </ul>  -->
              <ul class="base_header">
                <li><img src="${ctx}/static/images/dumai_logo.png" alt="logo"></li> 
                 <!--<li><img src="data:image/gif;base64,{{every_member.card_photo}}" alt="logo"></li>  -->
                <ul class="base_right">
                    <li><span>编号：</span><span ng-bind="loanReportFront.DeviceNumber"></span></li>
                    <li><span>日期：</span><span ng-bind="dateTime">2017-04-01</span></li>
                    <!-- <li><a id="btn" ng-click="exportImg()">导出</a><span ng-click="closeReport()" class="report_close spriteIcon"></span></li> -->
                    <li><a id="btn" ng-click="exportImg()">导出</a></li>
                </ul> 
            </ul> 
            
            <div class="base_title"><p>读脉风控评审报告</p> <span></span></div>
            <ul class="base_list" style="height:70%;overflow-y:auto;">
                <div class="soloInfo">
                    <p class="solo_title"><span class="spriteIcon icon_baseinfo"></span>基本信息</p>
                    <div class="basicInfo">
                        <ul>
                            <li><span>姓名：</span><span ng-bind="loanReportFront.name"></span></li>
                            <li><span>性别：</span><span ng-bind="sex"></span></li>
                            <li><span>年龄：</span><span ng-bind="age"></span></li>
                             <li><span>婚姻（查询）：</span><span ng-bind="loanReportFront.married"></span></li>
                            <li><span>学历：</span><span ng-bind="loanReportFront.education"></span></li>
                            <li><span>身份证号码：</span><span ng-bind="loanReportFront.card_num"></span></li>
                            <li><span>手机号码：</span><span ng-bind="loanReportFront.mobile"></span></li>
                            <li><span>银行卡：</span><span ng-bind="loanReportFront.bank_num"></span></li>
                            <li><span>职业情况 ：</span><span ng-bind="loanReportFront.profession"></span></li>
                            <li><span>常住地址：</span><span ng-bind="loanReportFront.address"  style="width:78%"></span></li>
                            <li><span>固定收入 ：</span><span ng-bind="loanReportFront.income"></span></li>
                            <li><span>贷款金额 ：</span><span ng-bind="loanReportFront.sqje"></span></li>
                            <li><span>车 牌 号 ：</span><span ng-bind="loanReportFront.plate"></span></li>
                            <li class="emeLink"><span>紧急联系人1：</span><span ng-bind="loanReportFront.linkname1"></span><span ng-bind="loanReportFront.linkphone1"></span><span ng-bind="loanReportFront.linkReation1"></span></li>
                            <li class="emeLink"><span>紧急联系人2：</span><span ng-bind="loanReportFront.linkname2"></span><span ng-bind="loanReportFront.linkphone2"></span><span ng-bind="loanReportFront.linkReation2"></span></li>
                            <ul class="generated">
                                <li ng-repeat="aa in arrays"><span ng-cloak>{{aa.titleName}} ：</span><span>{{aa.titleContent}}</span></li>
                            </ul>
                        </ul>
                         <img data-ng-src="{{loanReportFront.card_photo}}" alt="{{loanReportFront.name}}">
                        <!--<img src="data:image/gif;base64,{{peoImg}}" alt={{basics.name}}{{peoImg}} ng-cloak>-->
                    </div>
                </div>
                
                <div class="fraudRule">
                    <p class="fraudRule_title"><span class="spriteIcon icon_fraudRule"></span>反欺诈规则审核结果
                     <i class="fraud_unfold fraud_unfold_1 on" title="加载更多" ng-click="closeLiList('.fraud_unfold_1','.reportGrid_1')"></i>
                    </p>
                    <table class="reportGrid reportGrid_1" style="table-layout: fixed;">
                         <thead>
    						<tr>
							  <th>风控规则</th>
							  <th>结果</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in audit_results">
        				       <td ng-bind="item.guize_name"></td>
        				       <td ng-bind="item.result=='true'?'命中':'未命中'"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                <div class="creditCheck">
                    <div class="creditCheck_title" >
                        <span class="spriteIcon"></span>信用模型评分结果：</i>
                        <h5><i>得分：</i><em ng-bind="credit.total_score"></em><span></span></h5>
                         <i class="credit_unfold on" title="加载更多" ng-click="closeLiList('.credit_unfold','.reportGrid2')"></i>
                    </div>
                     <table class="reportGrid  reportGrid2" style="table-layout: fixed;">
                         <thead>
    						<tr>
							  <th style="width:50%;">结果描述</th>
							  <th style="width:50%;">得分</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in creditResult">
        				       <td ng-bind="item.manager_item_name" style="width:50%;"></td>
        				       <td ng-bind="item.weight_score" style="width:50%;"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                <div class="phoneCheck">
                    <div class="phoneCheck_title" >
                        <span class="spriteIcon"></span>电核审核结果
                        <!-- <h5><i>得分：</i><em ng-bind="score"></em><span></span></h5> -->
                        <i class="phone_unfold on" title="加载更多" ng-click="closeLiList('.phone_unfold','.reportGrid3')"></i>
                    </div>
                     <table class="reportGrid  reportGrid3" style="table-layout: fixed;">
                         <thead>
    						<tr>
							  <th>电核项</th>
							  <th>描述</th>
						 	  <th>电核内容</th>
						      <th>备注</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in dhResults">
        				       <td ng-bind="item.name"></td>
        				       <td ng-bind="item.value"></td>
        				       <td ng-bind="item.result" style="width:5%"></td>
        				       <td ng-bind="item.remark"></td>
    						</tr>
                         </tbody>
                      </table>
                    <div class="solution">
                        <span>电核的基本情况:</span>
                        <span ng-bind="dhDatas.description"></span>
                    </div>
                    <div class="solution">
                        <span>异常情况:</span>
                        <span ng-bind="dhDatas.other_exception"></span>
                    </div>
                </div>
                <!-- 分割线  -->
                <div class="divider"></div>
                
                 <div class="fraudRule fraudRule1">
                    <div class="fraudRule_title">
                      <span class="spriteIcon"></span>还款情况
                      <i class="fraud_unfold fraud_unfold_2 on" title="加载更多" ng-click="closeLiList('.fraud_unfold_2','.reportGrid4')"></i>
                    </div>
                    <table class="reportGrid reportGrid4" style="table-layout: fixed;">
                         <thead>
    						<tr>
    						  <th>期数</th>
							  <th>还款金额</th>
							  <th>还款日期</th>
							  <th>逾期天数</th>
							  <th>备注</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in orderBills">
        				       <td ng-bind="$index+1"></td>
        				       <td ng-bind="repayment"></td>
        				       <td ng-bind="item.st_repay_date"></td>
        				       <td ng-bind="item.st_overdue_days"></td>
        				       <td ng-bind="item.st_remark"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                 <div class="fraudRule fraudRule2">
                      <p class="fraudRule_title"><span class="spriteIcon"></span>GPS数据
                      <i class="fraud_unfold fraud_unfold_3 on" title="加载更多" ng-click="closeLiList('.fraud_unfold_3','.reportGrid5')"></i>
                      </p>
                     <table class="reportGrid reportGrid5 " style="table-layout: fixed;">
                         <thead>
    						<tr>
							  <th>反欺诈规则</th>
							  <th>结果</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in audit_result">
        				       <td ng-bind="item.guize_name"></td>
        				       <td ng-bind="item.result"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                
                <div class="detailList">
                    <p><span class="spriteIcon"></span>原始数据查询</p>
                    <ul>
                        <li ng-repeat="report in detailInterfaces" ><a href="javascript:;" ng-bind="report.name"  data-code="{{report.code}}"  ng-click="detailReport(report.code,report.name)"  class="detailReportSmall"></a></li>
                    </ul>
                </div>
            </ul>
        </div>
    </div>
    
    <div class="markReport markReport_1" ></div>
</div>
</body>
</html>
