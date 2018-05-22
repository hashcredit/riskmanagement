<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myIndex">
<head>
	<meta charset="UTF-8">
	<title>逾期催款</title>
	<link rel="stylesheet/less" href="${ctx}/static/css/model.less" type="text/less">
	<link rel="stylesheet" href="${ctx}/static/css/baseReport2.css">
	<link rel="stylesheet" href="${ctx}/static/dumai/global/css/default.css">
	<link rel="stylesheet" href="${ctx}/static/css/bootstrap.css">
	<link rel="stylesheet" href="${ctx}/static/css/jquery-ui.css">
	<link rel="stylesheet" href="${ctx}/static/css/style.css">
	<link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
</head>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-1.11.1.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/less.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery.artDialog.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/angular.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/jquery-ui-min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/bootstrap2.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/loading-bar.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/layer/layer.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/loanOverdue/js/overdueController.js"></script>

<body>
<div id="menu" ng-controller="myMenu">
	<div class="header">
		<!-- 导入网页头 -->
		<%@ include  file="/WEB-INF/views/head/head.jsp"%>
		<div class="info_header">
			<label class="info_header_date"> <div class="markLine"></div><span class="dark"></span><p>时间: <input type="text" id="datepicker" ng-model="date1"><i class="spriteIcon time"></i></p></label>
			<ul>
				<li><a href="${ctx}/loanFront/toLoanFront.do">贷前审核</a></li>
				<li><a href="${ctx}/loanMiddle/toLoanMiddle.do">贷中跟踪</a></li>
				<li class="current"><a href="${ctx}/loanOverdue/toList.do">逾期催款</a></li>
			</ul>
			<label class="selectNameBox"><i class="search spriteIcon" ng-click='searchMem()'></i><input type="text" placeholder="请输入借款人身份证或姓名" class="selectName"  ng-model="value" ng-blur="tipErr()" ng-keyup="search(e)"></label>
		</div>
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
					<ul class="result result_3" ng-class="{true:'hide_1',false:'show_1'}[overdueFlag]"> 
						<li class="yincang" style="border-top: 1px solid #586772;border-bottom: none"><span class="account spriteIcon"></span>账户明细 </li>
						<li style="border-top: 1px solid #586772"><span class="follow_up spriteIcon"></span>催收跟进</li>
						<li><span class="upgrade spriteIcon"></span>升级处理</li>
					</ul>
					<ul class="result result_2" ng-class="{false:'hide_1',true:'show_1'}[overdueFlag]">
						<li ng-click="overDue_account(orderid,code)" class="yincang account_green" style="border-top: 1px solid #586772;border-bottom: none"><span class="account spriteIcon"></span>账户明细 </li>
						<li ng-click="overDue_cui()" style="border-top: 1px solid #586772"><span class="follow_up spriteIcon"></span>催收跟进</li>
						<li ng-click="overDue_sheng()"><span class="upgrade spriteIcon"></span>升级处理</li>
					</ul>
					<ul class="waterBox" >
						<li><span class="ruleBox spriteIcon"></span><a href="#/ruleWater">GPS</a></li>
						<!--  <li><span class="modelBox"></span><a>模型池</a><i ng-class="{OFF:'bg2',ON:'bg1'}[flagNF]">停用</i></li>-->
					</ul>

				</div>
			</div>
			<div class="info_content1">
			<p ng-bind="tipNOdata"></p>
				 <ul ng-repeat="item in persons" class="perList" render-finish>
                    <li class="curDate">
                        <h5 ng-cloak><i class="calendar"></i>{{item.data}}</h5>
                    </li>
                    <li ng-repeat="every_member in item.person" ng-dblclick="overDue_account(every_member.orderid,every_member.code)">
                        <div class="person_image"><p ng-bind="every_member.name" id="idName"></p><img data-ng-src="{{every_member.card_photo}}" realSrc="{{every_member.card_photo}}" alt=""></div>
                       <p><span>身份证号：</span><span ng-bind="every_member.card_num"></span></p> 
                       <a style="display:none;" class="yinCang yinCangName">{{every_member.name}}</a>
                       <a style="display:none;" class="yinCang yinCangCode">{{every_member.code}}</a>
                       <a style="display:none;" class="yinCang yinCangSqje">{{every_member.sqje}}</a>
                       <a style="display:none;" class="yinCang yinCangJkqx">{{every_member.Jkqx}}</a>
                       <a style="display:none;" class="yinCang yinCangDqshsj">{{every_member.dqshsj}}</a>
                    </li>
                    <div class="clearfloat"></div>
                </ul>
				<div class="overDuePage overDueAccount" >
					<h5 ng-click="backMain_account()"><span>返回到逾期催收主页面</span><span class="over_back spriteIcon"></span></h5>
					<div class="overCon">
						<ul class="over_title">
							<li><span class="icon_peop spriteIcon"></span><span ng-bind="overName"></span></li>
							<li><span>借款金额：</span><span ng-bind="overSqje"></span></li>
							<li><span>借款期数：</span><span ng-bind="overJkqx"></span></li>
							<!-- <li><span>还款日期：</span><span ng-bind="overDqshsj"></span></li> -->
							<li ng-click="baseReport2()" class="dzReport"><span>贷中报表</span></li>
						</ul>
						<div class="over_box">
							<div class="over_right over_right_account">
                                 <table class="result1" style="table-layout: fixed;width:100%;">
                                	 <thead>
    									<tr>
									        <th>还款序号</th>
									        <th>最后还款日</th>
									        <th>本期应还金额</th>
									        <th>本期应还本金</th>
									        <th>本期应还逾期利息</th>
									        <th>每日滞纳金</th>
									        <th>逾期天数</th>
									        <th>还款时间</th>
    									</tr>
   									 </thead>
                                 <tbody>
                                 	 <tr ng-repeat="item in overDues">
        								<td ng-bind="item.REPAYNO"></td>
        								<td ng-bind="item.LASTREPAYDATE"></td>
        								<td ng-bind="item.CUR_REPAY_AMT|decimalFilter|number:2"></td>
        								<td ng-bind="item.CUR_REPAY_PRINCIPAL|decimalFilter|number:2"></td>
        								<td ng-bind="item.CUR_REPAY_OVERDUE_INTEREST"></td>
        								<td ng-bind="item.PRE_OVERDUE_INTEREST|decimalFilter|number:2"></td>
        								<td ng-bind="item.OVERDUE_DAYS"></td>
        								<td ng-bind="item.CUR_REPAYDATE"></td>
    								 </tr>
                                    </tbody>
                                </table>
                                
							</div>
						</div>

					</div>
				</div>
				<div class="overDuePage overDueCui" >
					<h5 ng-click="backMain_cui()"><span>返回到逾期催收主页面</span><span class="over_back spriteIcon"></span></h5>
					<div class="overCon">
						<ul class="over_title">
							<li><span class="icon_peop spriteIcon"></span><span ng-bind="overName"></span></li>
							<li><span>借款金额：</span><span ng-bind="overSqje"></span></li>
							<li><span>借款期数：</span><span ng-bind="overJkqx"></span></li>
							<li><span>还款日期：</span><span ng-bind="overDqshsj"></span></li>
						</ul>
						
						<div class="over_box">
							<div class="over_left over_left_sheng">
								<ul>
									<li><span>催收日期：</span><span ng-bind="date3"></span></li>
									<li><span>下次跟进时间：</span>
										<label class="info_header_date"><p><input type="text" id="datepicker2" ng-model="date2"><i class="spriteIcon time"></i></p></label>
									</li>
									<li><span>客户标签：</span>
										<select id="type" ng-model="selectValue" ng-options="customLabel.value as customLabel.label for customLabel in customLabels"  ng-change='change()'></select></select>
									</li>
									<li style="margin-top: -10px"><span>沟通内容：</span>
										<textarea name="wenben"  rows="3" ng-model="applyContent"></textarea>
									</li>
									<li style="margin-top: 86px" class="soloSel">
										<span>升级处理申请：</span>
										<div id="upGrade">
											<label class="spriteIcon"><input type="radio" name="apply" value="外访协催" >外访协催</label>
											<label class="spriteIcon"><input type="radio" name="apply" value="展期">展期</label>
											<label class="spriteIcon"><input type="radio" name="apply" value="外包">外包</label>
											<label class="spriteIcon"><input type="radio" name="apply" value="诉讼">诉讼</label>
										</div>

									</li>
									<li style="margin-top: 10px"><span>申请理由：</span>
										<textarea name="wenben"  rows="3" ng-model="applyReason"></textarea>
									</li>
								</ul>
								<div class="anniu" style="width:100%;padding-left:35%;">
									<input type="button" value="保存" class="agree" ng-click="saveFollow()" >
								</div>

							</div>
							<div class="over_right over_right_sheng" >
									<a style="display:none;" class="yinCang yinCangSheng">{{code}}</a>
                                   <table class="result1" style="table-layout: fixed;">
                                	 <thead>
    									<tr>
									        <th>序号</th>
									        <th>催收日期</th>
									        <th>下次跟进时间</th>
									        <th>客户标签</th>
									        <th>沟通内容</th>
									        <th>申请理由</th>
									        <th>申请处理类型</th>
									        <th>审核状态</th>
									        <th>审核备注</th>
									        <th>申请人</th>
    									</tr>
   									 </thead>
                                 <tbody>
                                 	 <tr ng-repeat="item in overDuesCuis">
        <td ng-bind="$index+1"></td><td ng-bind="item.follow_date"></td><td ng-bind="item.next_date"></td><td ng-bind="item.label"></td><td ng-bind="item.content"></td><td ng-bind="item.surname"></td><td ng-bind="item.opt_request"></td><td ng-bind="item.validate_status"></td><td  ng-bind="item.validate_remarks"></td><td  ng-bind="item.surname"></td>
    								 </tr>
                                    </tbody>
                                </table>
							</div> 
						</div>
					</div>
				</div>
				<div class="overDuePage overDueSheng">
					<h5 ng-click="backMain_sheng()"><span>返回到逾期催收主页面</span><span class="over_back spriteIcon"></span></h5>
					<div class="overCon">
						<ul class="over_title">
							<li><span class="icon_peop spriteIcon"></span><span ng-bind="overName"></span></li>
							<li><span>借款金额：</span><span ng-bind="overSqje"></span></li>
							<li><span>借款期数：</span><span ng-bind="overJkqx"></span></li>
							<li><span>还款日期：</span><span ng-bind="overDqshsj"></span></li>
						</ul>
					<div class="over_box">
						<div class="over_left">
								<ul>
									<li><span>催收日期：</span><span ng-bind="follow_date_first"></span></li>
									<li><span>申请处理类型：</span><span ng-bind="opt_request_first"></span></li>
									<li><span>申请理由：</span><span ng-bind="reason_request_first"></span></li>
									<li><span>申请备注：</span>
										<textarea name="wenben"  rows="8" ng-model="applyRemark"></textarea>
									</li>
								</ul>
								<div class="anniu">
									<input type="button" value="同意" class="agree"  ng-click="saveHaddle(1)">
									<input type="button" value="拒绝" class="refuse"  ng-click="saveHaddle(2)">
								</div>

							</div>
							<div class="over_right over_right_cui" >
                                <table class="result1" style="table-layout: fixed;">
                                	 <thead>
    									<tr>
									        <th>序号</th>
									        <th>催收日期</th>
									        <th>下次跟进时间</th>
									        <th>客户标签</th>
									        <th>沟通内容</th>
									        <th>申请理由</th>
									        <th>申请处理类型</th>
									        <th>审核状态</th>
									        <th>审核备注</th>
									        <th>申请人</th>
    									</tr>
   									 </thead>
                                 <tbody>
                                 	 <tr ng-repeat="item in overDuesUp">
        <td ng-bind="$index+1"></td><td ng-bind="item.follow_date"></td><td ng-bind="item.next_date"></td><td ng-bind="item.label"></td><td ng-bind="item.content"></td><td ng-bind="item.reason_request"></td><td ng-bind="item.opt_request"></td><td ng-bind="item.validate_status"></td><td  ng-bind="item.validate_remarks"></td><td  ng-bind="item.surname"></td>
    								 <td style="display:none;" class="yinCang yinCangCui">{{item.code}}</td>
    								 </tr>
                                    </tbody>
                                </table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="selectError">
          <div>
             <p>暂无搜索相关信息</p>
             <span class="sure" ng-click="closeTip()"></span>
          </div>
    </div>
    <div class="statusTip">
          <div>
             <p>审批成功</p>
             <span class="sure" ng-click="closestatusTip()"></span>
          </div>
    </div>
      <div class="exit_tip">
          <div>
             <p>确认退出？</p>
             <span class="sure" ng-click="exit_tip()"></span>
             <span class="cancle_tip" ng-click="close_exit_Tip()"></span>
          </div>
        </div>
    <div class="markReport" id="markReport1">
        <div class="baseReport" id="baseReport" style="background: #fdfcfc;">
            <ul class="base_header">
                <li><img src="${ctx}/static/images/dumai_logo.png" alt="logo"></li> 
                 <!--<li><img src="data:image/gif;base64,{{every_member.card_photo}}" alt="logo"></li>  -->
                <ul class="base_right">
                    <li><span>编号：</span><span ng-bind="loanReportFront.DeviceNumber"></span></li>
                    <li><span>日期：</span><span ng-bind="dateTime">2017-04-01</span></li>
                    <!--  <li><span ng-click="closeReport()" class="report_close spriteIcon"></span></li> -->
                </ul>
            </ul>
            <div class="base_title"><p>读脉风控评审报告</p> <span></span></div>
            <ul class="base_list">
                <div class="soloInfo">
                    <p class="solo_title"><span></span>基本信息</p>
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
                    <p class="fraudRule_title"><span></span>风控规则审核结果
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
        				       <td ng-bind="item.result"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                <div class="fraudScore">
                    <div class="fraudScore_title" >
                        <span></span>反欺诈模型审核结果：
                        <h5><i>得分：</i><em ng-bind="score"></em><span></span></h5>
                         <i class="model_unfold on" title="加载更多" ng-click="closeLiList('.model_unfold','.reportGrid1')"></i>
                    </div>
                    <table class="reportGrid reportGrid1" style="table-layout: fixed;">
                         <thead>
    						<tr>
							  <th style="width:50%;">结果描述</th>
							  <th style="width:50%;">得分</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in cheat">
        				       <td ng-bind="item.type" style="width:50%;"></td>
        				       <td ng-bind="item.time" style="width:50%;"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                <div class="creditCheck">
                    <div class="creditCheck_title" >
                        <span></span>信用评分模型审核结果：</i>
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
                        <span></span>电核审核结果</i>
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
                <div class="divider"></div>
                
                 <div class="fraudRule fraudRule1">
                      <p class="fraudRule_title"><span></span>还款情况
                      <i class="fraud_unfold fraud_unfold_2 on" title="加载更多" ng-click="closeLiList('.fraud_unfold_2','.reportGrid4')"></i>
                      </p>
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
        				       <td ng-bind="item.st_penalty"></td>
        				       <td ng-bind="item.st_repay_date"></td>
        				       <td ng-bind="item.st_overdue_days"></td>
        				       <td ng-bind="item.st_remark"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                 <div class="fraudRule fraudRule2">
                      <p class="fraudRule_title"><span></span>GPS数据
                       <i class="fraud_unfold fraud_unfold_3 on" title="加载更多" ng-click="closeLiList('.fraud_unfold_3','.reportGrid5')"></i>
                      </p>
                     <table class="reportGrid reportGrid5" style="table-layout: fixed;">
                         <thead>
    						<tr>
							  <th>风控规则</th>
							  <th>结果</th>
							  <th>结果描述</th>
    						</tr>
   						 </thead>
                         <tbody>
                            <tr ng-repeat="item in audit_results">
        				       <td ng-bind="item.id"></td>
        				       <td ng-bind="item.type"></td>
        				        <td ng-bind="item.time"></td>
    						</tr>
                         </tbody>
                      </table>
                </div>
                <div class="detailList">
                    <p><span></span>原始数据查询</p>
                    <ul>
                        <li ng-repeat="report in detailInterfaces" ><a href="javascript:;" ng-bind="report.name" ng-click="detailReport(report.code,report.name)"></a></li>
                    </ul>
                </div>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
