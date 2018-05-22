<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en" ng-app="myMarker">
<head>
	<meta charset="UTF-8">
	<title>贷中审核报告</title>
	<!-- Set render engine for 360 browser -->
	<meta name="renderer" content="webkit">
    <link rel="stylesheet" href="${ctx}/static/css/assess.css">
    <link rel="stylesheet" href="${ctx}/static/css/baseReport2.css"> 
    <link rel="stylesheet" href="${ctx}/static/css/report.css"> 
    <link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
    
	<script src="${ctx}/static/script/lib/jquery.min.js"></script>
	<script src="${ctx}/static/script/lib/angular.min.js"></script>
	 <script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/moment.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/lib/loading-bar.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/html2pdf/html2canvas.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/html2pdf/jspdf.min.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/script/html2pdf/renderPDF.js"></script>
	<script charset="utf-8" type="text/javascript" src="${ctx}/static/dumai/global/js/utils.js"></script>
	<script src="${ctx}/static/dumai/loanMiddle/js/assess.js"></script>
	
</head>
	
<body ng-controller="markController">
<div id="baseReport">
	<div class="content" style="position: relative">
	
		<div class="evaluate">
			<div class="left">
				<h2>信用评估报告</h2>
				<div class="lbk">
					<p>编号：<span ng-bind="loanReportFront.DeviceNumber">DMYM_X0003 2016.04.14</span></p>
					<p>评估分数：320分</p>
					<p>评估日期：<span ng-bind="dateTime">2017-6-26</span></p>
				</div>
				<!--  <li><a id="btn" ng-click="exportImg()">导出</a></li> -->
				<button class="btn bttn" ng-click="exportImg()">导出</button>
				<button class="btn button">打印</button>			
			</div>
			
			<div class="basic">
				<span ng-click="dope()"><i></i>基本信息</span>
				<span><i></i>风控规则审核结果</span>
				<span><i></i>风控规则审核结果</span>
				<span><i></i>信用评分模型审核结果</span>
				<span><i></i>电核审核结果</span>
				<span><i></i>电核审核结果</span>			
			</div>
		</div>
		<div class="rightBox" style="width: 90%;left: 240px;top: 0;position: absolute;">

			<div class="main">
				<h1>读脉风险管理报告<i>已通过</i></h1>			
				<div class="main1">
				   <img data-ng-src="{{loanReportFront.card_photo}}" alt="{{loanReportFront.name}}" class="cont" style="width:126px;
				   height:167px;">
					<ul>
						<li class="name">
							<span>姓名：</span>
							<span ng-bind="loanReportFront.name">李丽</span>
						</li>
						<li class="name">
							<span>性别：</span>
							<span ng-bind="sex">女</span>
						</li>
						<li class="name">
							<span>年龄：</span>
							<span ng-bind="age">25岁</span>
						</li>
						<li class="name">
							<span>婚姻：</span>
							<span ng-bind="loanReportFront.married">未婚</span>
						</li>
						<li class="status">
							<span>手机号码：</span>
							<span ng-bind="loanReportFront.mobile"></span>
						</li>
						<li class="status ">
							<span>身份证号码：</span>
							<span ng-bind="loanReportFront.card_num">123456789101112131</span>
						</li>
	
						<li class="emeLink">
							<span>身份证地址：</span>
							<span ng-bind="loanReportFront.address">陕西省西安市紫玉家园2-2-3-4</span>
						</li>
						<li class="emeLink">
							<span>现居住地：</span>
							<span ng-bind="loanReportFront.address">北京朝阳区南礼士路23号3-201</span>
						</li>
						<!-- <li class="emeLink">
							<span>常用邮箱 ：</span>
							<span ng-bind="loanReportFront.bank_num">lily@gmail.com</span>
						</li> -->														
						<li class="emeLink">
							<span>紧急联系人1：</span>
							<span ng-bind="loanReportFront.linkname1">李明：<span ng-bind="loanReportFront.linkphone1"></span>183999009800</span>
						</li>
						<li class="emeLink">
							<span>紧急联系人2：</span>
							<span ng-bind="loanReportFront.linkname2">李明：<span ng-bind="loanReportFront.linkphone2"></span>183999009800</span>
						</li>
						<li class="emeLink">
							<span>工作单位：</span>
							<span  style="width:78%" ng-bind="loanReportFront.profession">北京市天际技术有限公司</span>
						</li>
						<li class="emeLink">
							<span>固定收入 ：</span>
							<span ng-bind="loanReportFront.income">5900元/月</span>
						</li>
					</ul>
				</div>
			</div>

			<h3 class="list">
				<e></e>风控规则审核结果
				<span>得分：480</span>
				<i>通过</i>
			</h3>
			<table class="tablelist table_list tab">
				<thead>
				<tr ng-click="closeLiList()">
					<th  style="background: #f5f5f5;">风控规则</th>
					<th  style="background: #f5f5f5;">结果</th>
				</tr>
				</thead>
				<tbody>
					<tr ng-repeat="item in audit_results">
						<td ng-bind="item.guize_name"></td>
						<td ng-bind="item.result"></td>
					</tr>
	
				</tbody>
			</table>
	
			<h3 class="list">
				<e></e>信用模型评分结果
				<span>得分：480</span>
				<i style="background: #f64067;">拒绝</i>
				 
			</h3>
			<table class="tablelist table_list tab">
				<thead>
					<tr ng-click="closeLiList()">
						<th  style="background: #f5f5f5;">结果描述</th>
						<th  style="background: #f5f5f5;">得分</th>					
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="item in creditResult">
						<td ng-bind="item.manager_item_name"></td>
						<td ng-bind="item.weight_score"></td>
					</tr>
				</tbody>
			</table>
	
			<h3 class="list">
				<e></e>电核审核结果
				<span style="margin-left: 933px;">得分：480</span>
				<i style="background: #f64067;">拒绝</i> 
			</h3>
			<table class="tablelist table tab">
				<thead>
					<tr ng-click="closeLiList()">
						<th style="background: #f5f5f5;" >电核项</th>
						<th style="background: #f5f5f5;">描述</th>
						<th style="background: #f5f5f5;">结果</th>
	
						<th style="background: #f5f5f5;">内容备注</th>
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
			<h4>电核基本情况： <span ng-bind="dhDatas.description"></span></h4>
			<h4>异常情况： <span ng-bind="dhDatas.other_exception"></span></h4>
	
			<!-- 分割线  -->
	         <div class="divider"></div>
	                      
	         <h3 class="list"><e></e>还款情况</h3>
	         <table class="tablelist table tab">
				<thead>
					<tr ng-click="closeLiList()">
						<th style="background: #f5f5f5;" >期数</th>
						<th style="background: #f5f5f5;">还款金额</th>
						<th style="background: #f5f5f5;">还款日期</th>
						<th style="background: #f5f5f5;">逾期天数</th>
						<th style="background: #f5f5f5;">备注</th> 
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
			
	         <h3 class="list"><e></e>GPS数据</h3>	            
	         <table class="tablelist table_list tab">
				<thead>
					<tr ng-click="closeLiList()">
						<th  style="background: #f5f5f5;">风控规则</th>
						<th  style="background: #f5f5f5;">结果</th>
						 <th  style="background: #f5f5f5;">结果描述</th>	 				
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
	
			<h1 class="original">原始数据查询结果</h1>
						
			<ul>		
				<li ng-repeat="report in detailInterfaces">
					<h3 class="data"><e></e><span  ng-bind="report.name"  ng-click="detailReport(report.code,report.name)">移动运营商数据</span></h3>
					<table class="tablelist table_list" style="display:none;">
						<thead style="width:100%;display:table;">
							<tr>
							<th style="background: #f5f5f5;">类型</th>
							<th style="background: #f5f5f5;">结果</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="item in detailInfos">
	        			       <td ng-bind="item.name_zh"  style="width:583px;"></td>
	        			       <td ng-bind="item.value"  style="width:583px;"></td>
	    					</tr>						
						</tbody>
					</table>
				</li>					
			</ul>			
		</div>
	
	</div>
</div>




<!-- 打印    -->
	<div id="print" style="position: absolute;top: 1900px;margin: 0 auto;width: 100%;display:none;">		
		<div class="logo1">
			<div class="logo">
				 <img class="log" src="${ctx}/static/images/报告_03.png" /> 				
				<p><span>编号：</span>DMYM_X0003 2016.04.14</p>
			</div>
		</div>
		
        <div class="content1">
        	<div class="message">
        	<!-- <img class="conte" src="images/报告_10.png"> -->
        	<img data-ng-src="{{loanReportFront.card_photo}}" alt="{{loanReportFront.name}}" class="conte">
            <h2>
            	<i class="rep"></i>读脉风险管理报告
            	<h5>已通过</h5>
            </h2>
            <div class="basicInfo">
                <ul>
                    <li>
                    	<span>姓名：</span>
                    	<span ng-bind="loanReportFront.name">李丽</span>
                    </li>
                    <li>
                    	<span>性别：</span>
                    	<span ng-bind="sex">女</span>
                    </li>
                    <li>
                    	<span>年龄：</span>
                    	<span ng-bind="age">25岁</span>
                    </li>
                    <li>
                    	<span>婚姻：</span>
                    	<span ng-bind="loanReportFront.married">未婚</span>
                    </li>
                    <li>
                    	<span>手机号码：</span>
						<span ng-bind="loanReportFront.mobile"></span>
                    </li>
                    <li>
                    	<span>身份证号码：</span>
                    	<span ng-bind="loanReportFront.card_num">123456789101112131</span>
                    </li>
                   
                    <li>
                    	<span>身份证地址：</span>
                    	<span ng-bind="loanReportFront.address">陕西省西安市紫玉家园2-2-3-4</span>
                    </li>
                    <li>
                    	<span>现居住地：</span>
                    	<span ng-bind="loanReportFront.address">北京朝阳区南礼士路23号3-201</span>
                    </li>
                    <li>
                    	<span>常用邮箱 ：</span>
                    	<span ng-bind="loanReportFront.bank_num">lily@gmail.com</span>
                    </li>
                    <li class="emeLink">
                    	<span>紧急联系人1：</span>
                    	<span ng-bind="loanReportFront.linkname1">李明：<span ng-bind="loanReportFront.linkphone1"></span>183999009800</span>
                    </li>
                    <li class="emeLink">
                    	<span>紧急联系人2：</span>
						<span ng-bind="loanReportFront.linkname2">李明：<span ng-bind="loanReportFront.linkphone2"></span>183999009800</span>
                    </li>
                    <li>
                     	<span>工作单位：</span>
                     	<span  style="width:78%" ng-bind="loanReportFront.profession">北京市天际技术有限公司</span>
                    </li>
                    <li>
                    	<span>固定收入 ：</span>
                    	<span ng-bind="loanReportFront.income">5900元/月</span>
                    </li>	                    
                </ul>
                </div>
            </div>               
    		
    		<div class="nav1">
    			<div class="nav">
    				<h3>风控规则审核结果</h3>
    				<table class="tableListData">
    					<thead>
    						<tr>
    							<th>风控规则</th>
    							<th>结果</th>    							
    						</tr>
    					</thead>
    					<tbody>
    						<tr ng-repeat="item in audit_results">
    							<td>基本信息验证</td>
    							<td>一致</td>    							
    						</tr>
    						<tr>
    							<td>涉诉情况</td>
    							<td>未命中</td>   							
    						</tr>
    					
    					</tbody>
    				</table>
    				<h3>信用模型评分结果<span>得分：480</span><i>通过</i></h3>
    				<table class="tableListData table_liste">
    					<thead>
    						<tr>
    							<th>结果描述</th>
    							<th>得分</th>   							
    						</tr>
    					</thead>
    					<tbody>
							<tr ng-repeat="item in creditResult">
								<td ng-bind="item.manager_item_name"></td>
								<td ng-bind="item.weight_score"></td>
							</tr>
						</tbody>
    				</table>
    				<h3>电核审核结果<span style="margin-left:825px;">得分：480</span><i>通过</i></h3>
    				<table class="tableListData table_liste">
						<thead>
							<tr ng-click="closeLiList()">
								<th>电核项</th>
								<th>描述</th>
								<th style="width:8%">结果</th>			
								<th>内容备注</th>
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
    				<h3>电核基本情况</h3>   
    				 <span ng-bind="dhDatas.description" style="width:1183px;height:125px;border:1px solid #000;display: block;"></span>									
					<h3>异常情况：</h3>
					<span ng-bind="dhDatas.other_exception"  style="width:1183px;height:125px;border:1px solid #000;display: block;"></span>
    			</div>
    		</div>
    		  		
        	
        	<h1 class="primeval"><e></e>  原始数据查询结果</h1>
        	<div class="main_con">
	        	<div class="main_cont">
	        		<ul>
		        		<li ng-repeat="report in detailInterfaces">
		        			<h3><span  ng-bind="report.name"  ng-click="detailReport(report.code,report.name)">电话号码相关查询</span></h3>
		        			<table class="tableListData tablelist_main" style="display:none;">
								<thead>
									<tr>
										<th>检查项目</th>
										<th>检查项目</th>							
									</tr>
								</thead>
								<tbody>
									<tr ng-repeat="item in detailInfos">
			        			       <td ng-bind="item.name_zh"></td>
			        			       <td ng-bind="item.value"></td>
			    					</tr>					
								</tbody>
							</table>
		        		</li>
	        		</ul>					
	        	</div>
        	</div>	
        </div>
		
			
	</div>











<script>
	$(document).ready(function(e) {
	    $(".button").click(function(e) {
	        $("#print").toggle();
	    });
	});
</script>



</body>
</html>
