<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
	
	<title>审核报告</title>
	<!-- Set render engine for 360 browser -->
	<meta name="renderer" content="webkit">
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" href="${ctx}/css/app.css" />
	<link rel="stylesheet" href="${ctx}/css/report.css" />

	<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script src="${ctx}/js/global.js"></script>
	<script src="${ctx}/js/amazeui.js"></script>

	<script type="text/javascript">
		
		//请求数据
		$(function(){
			$.ajax({
				url:"${ctx}/report/report.do?code=${param.code}&type_code=${param.type_code}",
				dataType:"json",
				type:"post",
				success:function(result){
					if(result.code==0){
						fillReport(result.body);
					}
					else{
						alert(result.error);
					}
				}
			});
			
		});
		
		var typeInputFieldMap={
			"21ff186a-77c6-45ec-a78c-956b2a31e092":{
				Co_borrowing:"共借人",
				marital_status:"婚姻状态",
				address:"家庭住址",
				enterprise_name:"单位名称",
				enterprise_nature:"单位性质",
				enterprise_address:"单位地址",
				housing_location:"房屋坐落",
				housing_area:"房屋面积",
				housing_type:"房屋类型",
				housing_property_period:"产权年限",
                housing_evaluation:"评估值",
				housing_mortgaged:"是否有一抵",
				housing_mortgage_amount:"一抵金额"
			},
			"f7516887-6465-463d-a7b4-64e2e866f192":{
				
			},
			"2b6cdbf1-44c9-431c-a147-75206c82dab5":{
				Co_borrowing:"共借人",
				marital_status:"婚姻状态",
				address:"家庭住址",
				enterprise_name:"单位名称",
				enterprise_nature:"单位性质",
				enterprise_address:"单位地址",
				housing_location:"房屋坐落",
				housing_area:"房屋面积",
				housing_type:"房屋类型",
				housing_property_period:"产权年限",
				housing_evaluation:"评估值",
				housing_mortgaged:"是否有一抵",
				housing_mortgage_amount:"一抵金额"
			}
		};
		
		//填充数据
		function fillReport(report){
			var status2 = report.status2;
			
			if(status2==0){
				$("#audit_stamp").text("未审核");
			}
			else if(status2==1){
				$("#audit_stamp").append("<img src='${ctx}/images/sh_tongguo.png' width='100' />");
			}
			if(status2==2){
				$("#audit_stamp").append("<img src='${ctx}/images/sh_jujue.png' width='100' />");
			}
			$("#audit_stamp").text(report.custom);
			
			$("#custom").text(report.companyName,"");
			$("#typename").text(report.typename,"");
			$("#code_name_consume").text(report.typename,"");
			$("#name").text(report.name,"");
			$("#cohabiters").attr("href","${ctx}/report/toDataDetail.do?report=6&code=${param.code}&name=" + encodeURIComponent(report.name));
			
			var basicinfo = report.basicinfo;
			if(basicinfo){
				if(basicinfo.resCode=="0000"){
					var data = basicinfo.data;
					if(data.statusCode=="2012" && data.result.detail){
						var censusAddress = data.result.detail.censusAddress;
						if(censusAddress){
							$("#address").text(censusAddress.district,"");
						}
						var detail = data.result.detail;
						$("#sex").text(detail.gender,"");
						$("#education").text(detail.education,"");
						$("#married").text(detail.maritalStatus,"");
						$("#age").text((new Date().getFullYear()-new Date(detail.birthday).getFullYear()),"");
					}
				}
			}
			
			$("#card_num").text(report.card_num,"");
			$("#banknum").text(report.banknum,"");
			$("#mobile").text(report.mobile,"");
			$("#profession").text(report.profession,"");
			$("#income").text(report.income,"");
			$("#insuranceid").text(report.insuranceid,"");
			$("#insurancepwd").text(report.insurancepwd,"");
			$("#fundid").text(report.fundid,"");
			$("#fundpwd").text(report.fundpwd,"");
			$("#otherincome").text(report.otherincome,"");
			$("#linkname1").text(report.linkname1,"");
			$("#linkphone1").text(report.linkphone1,"");
			$("#linkReation1").text(report.linkReation1,"");
			$("#linkname2").text(report.linkname2,"");
			$("#linkphone2").text(report.linkphone2,"");
			$("#linkReation2").text(report.linkReation2,"");
			$("#sqje").text(report.sqje,"");
			$("#Jkqx").text(report.Jkqx,"");
			
			var input =  report.input;
			var inputFieldMap = typeInputFieldMap[report.thetype];
			var inputJSON = [];
			try{
				inputJSON = $.parseJSON(input);
			}catch(e){
				//异常说明input可能为空，忽略
			}
			if(inputFieldMap && inputJSON){
				var inputFields = $("#input-fields");
				$.each(inputJSON,function(key,value){
					inputFields.append("<tr><td>"+inputFieldMap[key]+"：</td><td>"+value+"</td></tr>");
				});
			}
			
			var bankValidation = report.bankValidation;
			if(bankValidation!=null){
				$("#bankValidation").text(bankValidation.statusCode=='2005'?"一致":"不一致");
			}
			
			var companyName = report.companyName;
			if(companyName=="读脉"){
				$("#fqzpf-block,#xypf-block").show();
			}
			else{
				$("#fqzsh-block").show();
				if(report.audit_result){
					var fqzshmx = $("#fqzshmx").empty();
					
					$.each(report.audit_result,function(idx){
						var tr = $("<tr></tr>");
						tr.append("<td align='center'>"+(idx+1)+"</td>");
						tr.append("<td>&nbsp;"+(this.guize_name)+"</td>");
						tr.append("<td align='center'>"+(this.result=="false"?"未命中":"命中")+"</td>");
						fqzshmx.append(tr);
					});
				}
			}
			
			//Base64编码图片加上ata:image/png;base64,前缀
			$("#xiaoshi-picture").attr("src","data:image/png;base64," + report["xiaoshi-picture"]);
			
			
			var status1 = report.status1;
			$("#audit_result").append(["未审核","通过","拒绝","错误"][report.status1]).append("&nbsp;");
			if(status1==0){
				$("#audit_result").append($("<a href='javascript:;'>立即审核</a>").click(function(){
					audit("${param.code}");
				}));
			}
			$("#dqspyj").val(report.dqspyj,"");
			$("#manualSelect").val(report.status2,"");
			
			var orders = report.orders||[];
			var orderSelect = $("#orderSelect");
			$.each(orders,function(i,v){
				orderSelect.append($("<option>").val(v.code).text("第"+(i+1)+"条").attr("type_code",v.thetype));
			});
			orderSelect.val("${param.code}");
			orderSelect.change(function(){
				var option = orderSelect.find("option:selected");
				window.location.href="${ctx}/report/toReport.do?code=" + orderSelect.val()+"&type_code=" + option.attr("type_code");
			});
			
		}
		
		function audit(){
			$.ajax({
				url:"${ctx}/report/audit.do",
				dataType:"json",
				data:{
					code:"${param.code}",
				},
				type:"post",
				success:function(result){
					console.log(result);
					if(result.code==0){
						alert("成功");
					}
					else{
						alert(result.error);
					}
				}
			});
			
		}
		
		function manualAudit(){
			$.ajax({
				url:"${ctx}/report/manaulAudit.do",
				dataType:"json",
				data:{
					code:"${param.code}",
					dqspyj:$("#dqspyj").val(),
					status2:$("#manualSelect").val()
				},
				type:"post",
				success:function(result){
					console.log(result);
					if(result.code==0){
						if(window.opener && window.opener.onManualAuditSuccess){
							window.opener.onManualAuditSuccess("${param.code}");
						}
						window.location.reload();
					}
					else{
						alert(result.error);
					}
				}
			});
		}
		//-->
	</script>

	</head>

	<body >
	
	<table width="90%" border="0" align="center">
		<tr>
			<td  width="35%" align="right" id="audit_stamp">			
			</td>
				
			<td align="center" nowrap><font size="5"><b>风控评审报告</b></font>
			</td>
			<td  rowspan="top"   width="35%">
				<img src="${ctx}/img/logo_x.png" width="100" border="0" />
				<select style="margin-left:100px;width:100px;" id="orderSelect" >
					<%-- <%
					for(int i=0;i < orderIds.size();i++){%>
						<option <%if(orderIds.get(i).equals(orderinfoForm.getId())){%>selected<%} %> value="<%=orderIds.get(i)%>" >第<%=i+1 %>条</option>
					<%} %> --%>
				</select>
			</td>
		</tr>
	</table>



	<table width="90%" bgcolor="#ffffff" align="center" style="margin:20px auto;font-size:18px;font-weight:bold">
		<tr>
			<td align="right">客户:<span id="custom"></span></td>
			<td width="5%"></td>
			<td width="50%">业务类型:<span id="typename"></span></td>
		</tr>
	</table>
	<table width="90%" bgcolor="#ffffff" align="center">

  	<tr>
		<td width="49%" align="center" valign="top">
			
		<table width="100%">
			<tr>
				<th colspan=2>基本信息
				 <div class="line"></div>
				</th>
			</tr>
			<tr>
			  <td colspan=2>
				 <table class="table2">						
						<tr>
							<td width="40%">姓名：<span id="name"></span></td>
							<td rowspan="5" align="center">
								<img alt="照片" src="" id="xiaoshi-picture"/>
							</td>
						</tr>
						<tr>
							<td>性别：<span id="sex"></span></td>
						</tr>
						<tr>
							<td>年龄：<span id="age"></span></td>
						</tr>
						<tr>
							<td>婚姻：<span id="married"></span></td>
						</tr>
						
						<tr>
							<td nowrap>学历:<span id="education"></span></td>
						</tr>
						<tr>
							<td >身份证号码：</td>
							<td><span id="card_num"></span></td>
						</tr>
						<tr>
							<td>手机号码：</td>
							<td><span id="mobile"></span></td>
						</tr>
						
						
						<tr>
							<td>银行卡：</td>
							<td><span id="banknum"></span></td>
						</tr>

						<tr>
							<td>常住地址：</td>
							<td><span id="address"></span></td>
						</tr>
					</table>
					</td>
					</tr>

					  <tr>
							<th colspan=2>职业信息
							<div class="line"></div>
							</th>
						</tr>
					<tr>
						<td  colspan=2>
						<table width="100%" class="table2">					
						<tr>
							<td width="30%">职业情况：</td>
							<td><span id="profession" ></span></td>
						</tr>

						<tr>
							<td>年收入：</td>
							<td><span id="income" ></span></td>
						</tr>
						<!-- <tr>
							<td>其它收入：</td>
							<td><span id="otherincome" ></span></td>
						</tr>
						<tr>
							<td>社保账号：<span id="insuranceid" ></span></td>
							<td></td>
						</tr>
						<tr>
							<td>社保密码：<span id="insurancepwd" ></span></td>
							<td></td>
						</tr>
						<tr>
							<td>公积金账号：<span id="fundid" ></span></td>
							<td></td>
						</tr>
						<tr>
							<td>公积金密码：<span id="fundpwd" ></span></td>
							<td></td>
						</tr> -->
						</table>
						</td>
						</tr>
  						<tr>
							<th colspan=2>
								紧急联系人
				 				<div class="line"></div>
				 			</th>
						</tr>
							
						<tr>
						  <td  colspan=2>
							 <table class="table2">	
								<tr>
									<td >紧急联系人1:</td>
									<td style="width:20%" id="linkname1"></td>
									<td id="linkphone1"></td>
									<td id="linkReation1"></td>
								</tr>
								<tr>
									<td >紧急联系人2:</td>
									<td id="linkname2"></td>
									<td id="linkphone2"></td>
									<td id="linkReation2"></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<th colspan=2>项目详情<div class="line"></div></th>
						</tr>

						<tr>
			 			 <td  colspan=2>
						 <table class="table2">	
							<tr>
								<td style="width:30%">业务类型：</td>
								<td id="code_name_consume"></td>
							</tr>
							<!-- <tr>
								<td>项目机构：</td>
								<td></td>
							</tr>
							<tr>
								<td>消费项目：</td>
								<td></td>
							</tr>
	 						-->
							<tr>
								<td>借款金额：</td>
								<td id="sqje"></td>
							</tr>
	
							<tr>
								<td>借款期限：</td>
								<td id="Jkqx"></td>
							</tr>
							<tbody id="input-fields">
								
							</tbody>
						</table>
						</td>
						</tr>
					<tr>
						<th colspan=2>详细数据
				 			<div class="line"></div>
				 		</th>
				 			
					</tr>
						
					<tr>
					  	<td  colspan=2>
						 	<table class="table2">	
							<!-- <tr>
								<td width="30%">通讯运营商数据：</td>
								<td>
								</td>
							</tr>

							<tr>
								<td>涉诉数据：</td>
								<td>
									
								</td>
							</tr>
				
				
							<tr>
								<td>
									个人基本信息数据：
								</td>
							</tr> -->
							<common:function-settings function_settings="loanfront_report3" type_code="${param.type_code}">
								<tr>
									<td>涉诉:</td>
									<td>
										<a href="${ctx}/report/toDataDetail.do?report=3&code=${param.code}">查看数据</a>
									</td>
								</tr>
							</common:function-settings>
							<common:function-settings function_settings="loanfront_report10" type_code="${param.type_code}">
							<tr>
								<td>通信运营商数据:</td>
								<td>
									<a href="${ctx}/report/toDataDetail.do?report=10&code=${param.code}">查看数据</a>
								</td>
							</tr>
							</common:function-settings>
							<common:function-settings function_settings="loanfront_report4" type_code="${param.type_code}">
							<tr>
								<td>银行卡验证:</td>
								<td>
									<span id="bankValidation"></span>
								</td>
							</tr>
							</common:function-settings>
							<common:function-settings function_settings="loanfront_report8" type_code="${param.type_code}">
							<tr>
								<td>车辆基本信息：</td>
								<td>
									<a href="${ctx}/report/toDataDetail.do?report=8&code=${param.code}">查看数据</a>
								</td>
							</tr>
							</common:function-settings>
							<%-- <tr>
								<td>4S维修记录：</td>
								<td>
									<a href="${ctx}/report/toDataDetail.do?report=9&code=${param.code}">查看数据</a>
								</td>
							</tr> --%>
							<common:function-settings function_settings="loanfront_report6" type_code="${param.type_code}">
							<tr>
								<td>同住人信息：</td>
								<td>
									<a id="cohabiters" href="#">查看数据</a>
								</td>
							</tr>
							</common:function-settings>
							<%-- <tr>
								<td>单卡用户画像：</td>
								<td>
									<a href="${ctx}/report/toDataDetail.do?report=7&code=${param.code}">查看数据</a>
								</td>
							</tr> --%>
							<common:function-settings function_settings="loanfront_report14" type_code="${param.type_code}">
							<tr>
								<td>公积金：</td>
								<td>
									<a href="${ctx}/report/toDataDetail.do?report=14&code=${param.code}">查看数据</a>
								</td>
							</tr>
							</common:function-settings>
							<common:function-settings function_settings="loanfront_report15" type_code="${param.type_code}">
							<tr>
								<td>犯罪：</td>
								<td>
									<a href="${ctx}/report/toDataDetail.do?report=15&code=${param.code}">查看数据</a>
								</td>
							</tr>
							</common:function-settings>
							</table>
						</td>
					</tr>
					
					</table>
				</td>
				
				<td style="vertical-align:top"></td>
				<td style="width:2px;vertical-align:top;background:#0050B6"></td>
				<td style="vertical-align:top"></td>


				<td style="width:49%;vertical-align:top">			
					<table style="width:100%">
						<common:function-settings function_settings="loanfront_rule" type_code="${param.type_code}">
							<tbody id="fqzsh-block" style="display: none">
							<tr>
								<th  colspan=2>反欺诈规则</th>
								</tr>
								
								<tr>
								<td colspan=2>
									审核结果:
									<span id="audit_result">
										
									</span>
									 
									<div class="line"></div>
								</td>
							</tr>
							<tr>
				  				<td colspan=2>
									<table style="width:100%" class="table1">	
										<tr align="center">
											<td width="5%" class="titletd" height="30" nowrap>序号</td>
											<td width="30%" class="titletd">反欺诈规则</td>
											<td width="15%" class="titletd">结果</td>
											<!-- <td class="titletd">证据</td> -->
										</tr>
										<tbody id="fqzshmx">
										
										</tbody>
									</table>
						 	 	</td>
							</tr>
							</tbody>
						</common:function-settings>
						<tbody id="fqzpf-block" style="display: none">
						<tr>
							<th  colspan=2>反欺诈评分 <div class="line"></div></th>
						</tr>
						<tr>
			  				<td colspan=2>
								<table style="width:100%" class="table1">
									
									<tr align="center">
										<td width="5%" class="titletd" height="30" nowrap>序号</td>
										<td width="30%" class="titletd">评分项</td>
										<td width="15%" class="titletd">分值</td>
									</tr>
									<tbody id="fqzpfmx">
										<tr align="center">
											<td>1</td>
											<td>身份证存在且姓名一致</td>
											<td>182.88</td>
										</tr>
										<tr align="center">
											<td>2</td>
											<td>银行卡验证</td>
											<td>14.6</td>
										</tr>
										<tr align="center">
											<td>3</td>
											<td>年龄1</td>
											<td>7.125</td>
										</tr>
										<tr align="center">
											<td>4</td>
											<td>车辆认证</td>
											<td>124.96</td>
										</tr>
										<tr align="center">
											<td>5</td>
											<td>姓名与手机号是否一致</td>
											<td>21.6</td>
										</tr>
										<tr align="center">
											<td>6</td>
											<td>拖欠起始日期</td>
											<td>10.4</td>
										</tr>
										<tr align="center">
											<td>7</td>
											<td>最大逾期金额的逾期天数</td>
											<td>3.36</td>
										</tr>
										<tr align="center">
											<td>8</td>
											<td>逾期记录数</td>
											<td>3.185</td>
										</tr>
										<tr align="center">
											<td>9</td>
											<td>案件类别</td>
											<td>10.08</td>
										</tr>
										<tr align="center">
											<td>10</td>
											<td>近6月博彩消费笔数</td>
											<td>1.3</td>
										</tr>
										<tr align="center" style="background: #dce0e6;font-weight:bold">
											<td colspan="2">合计</td>
											<td>379.49</td>
										</tr>
									</tbody>
									
								</table>
					 	 	</td>
						</tr>
						</tbody>
						<tbody id="xypf-block" style="display: none">
						<tr>
							<th  colspan=2>信用评分 <div class="line"></div></th>
						</tr>
						<tr>
			  				<td colspan=2>
								<table style="width:100%" class="table1">
									
									<tr align="center">
										<td width="5%" class="titletd" height="30" nowrap>序号</td>
										<td width="30%" class="titletd">评分项</td>
										<td width="15%" class="titletd">分值</td>
									</tr>
									<tbody id="xypfmx">
										<tr align="center">
											<td>1</td>
											<td>性别</td>
											<td>2.73</td>
										</tr>
										<tr align="center">
											<td>2</td>
											<td>年龄2</td>
											<td>15</td>
										</tr>
										<tr align="center">
											<td>3</td>
											<td>婚姻状况</td>
											<td>7.125</td>
										</tr>
										<tr align="center">
											<td>4</td>
											<td>籍贯</td>
											<td>2.38</td>
										</tr>
										<tr align="center">
											<td>5</td>
											<td>教育状况</td>
											<td>4.284</td>
										</tr>
										<tr align="center">
											<td>6</td>
											<td>欠款欠费记录</td>
											<td>11.6</td>
										</tr>
										<tr align="center">
											<td>7</td>
											<td>拖欠起始日期</td>
											<td>124.96</td>
										</tr>
										<tr align="center">
											<td>8</td>
											<td>失信老赖名单,限制高消费名单出现的次数</td>
											<td>24.92</td>
										</tr>
										<tr align="center" style="background: #dce0e6;font-weight:bold">
											<td colspan="2">合计</td>
											<td>193.00</td>
										</tr>
									</tbody>
									
								</table>
					 	 	</td>
						</tr>
						</tbody>
								<%--
								commented by 岳晓 2016-09-22 由于反欺诈规则客户可选择的需求，但全部实现比较复杂，暂时注销，请勿删除 
								<tr>
									<th  colspan=2>
										自定义规则评分
					 					<div class="line"></div>
				 					</th>								
								</tr>
							
								<tr>
	 						 		<td  colspan=2>
										<table width="100%" class="table1">	
										<tr align="center">
											<td width="40%" class="titletd" height="30" nowrap>名称</td>
											<td width="20%" class="titletd">权重</td>
											<td  class="titletd">结果</td>
										</tr>
										<%=auditForm.getZdygzmx()%>
									</table>
									<br>
									 <table width="100%" class="table1">	
										<tr >
										<td align="right" width="40%">得分：<%=auditForm.getZdygzpf()%></td>
										<td >满分：500</td>
									    </tr>
									</table>
								
							  		</td>
								</tr>

								<tr>
									<th>模型评分
				 					<div class="line"></div></th>
								
									</tr>
							
								<tr>
									<td colspan=2>
										<table width="100%" class="table1">	
										<tr align="center">
											<td width="40%" class="titletd" height="30" nowrap>名称</td>
											<td width="20%" class="titletd">权重</td>
											<td  class="titletd">结果</td>
										</tr>
										<%=auditForm.getMxpfmx()%>
										</table>
										<br>
										 <table width="100%" class="table1">	
											<tr >
												<td align="right" width="40%">得分：<%=auditForm.getMxpf()%></td>
												<td>满分：500</td>
											</tr>
										</table>
										
								  	</td>
								</tr> --%>
											
							    <tr>
									<th colspan=2>
										人工审核
									 <div class="line"></div>
								</th>
								</tr>
												
								<tr>
									<td  colspan=2>
										<table class="table1">	
												<tr>
													<td width="15%" align="center">结果:</td>
														<td>
															<select id="manualSelect">
																<option value="0" >待审核</option>
																<option value="1" >通过</option>
																<option value="2" >拒绝</option>
															</select>
															
														</td>
												</tr>
												
											<tr>
												<td style="text-align:center">意见</td>
												<td>
													<textarea rows="3" id="dqspyj" style="width:100%"></textarea>
												</td>
											</tr>
											<tr>
												<td style="text-align:center;height: 36px;line-height: inherit;" colspan="2" >
													<input type="button" value="提交审核结果" onclick="manualAudit()" style=" width:120px; height:25px; border:none; background:#69F; color:#FFF" />
												</td>
											</tr>
								
										</table>
									</td>
								</tr>
						</table>
					</td>
				</tr>
			
				<common:permission permission="export-report">
					<tr>
			       	 <td  align="center" colspan=5>
						
					   <div class="button3"  onclick="dosubmit();">
						 <span class="submit-btn2" >导&nbsp;&nbsp;出</span>
				    	</div>
					</td>
				 </tr>
				</common:permission>	
			 
		</table>

</body>
</html>
