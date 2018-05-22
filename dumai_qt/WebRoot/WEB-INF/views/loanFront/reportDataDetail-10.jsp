<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	  
	    <title>通信运营商数据</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		
		<link href="${ctx}/css/oaframecontentliststyle.css" type="text/css" rel="stylesheet">
	
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
		<script src="${ctx}/js/reports/desensitizeUtil.js"></script>
		<script src="${ctx}/js/amazeui.js"></script>
		<style type="text/css">
			td.align-center{
				text-align: center;
			}
			td.general-text{
				padding-left: 10px;
			}
			html{
				overflow-x:hidden;
				margin-bottom: 20px;
			}
			.title{
				margin: 1em 0;
				text-align: center;
			}
			.header th{
				text-align: center;
				height:30px;
				background: #eaeaea;
			}
			.sub-title{
				text-align: left;
				font-size: 16px;
				height: 30px;
				padding-left: 10px;
				font-weight: bold;
				background: #dedddd;
			}
		</style>
	</head>
	
	<body style="display: none">
		<common:head/>
		<h1 class="title">通信运营商数据</h1>
		<table style="width: 100%">
			<tr>
				<th class="sub-title" colspan="7" >电话活跃度</th>
			</tr>			
			
			<tr class="header">
				<!-- <td >电话</td> -->
				<th >月分段</th>
				<th >号码归属地</th>
				<th >用户标注标签</th>
				<th >有效联系人个数</th>
				<th >日均通话次数</th>
			<tr>
			<tbody id="tags">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<tr>
				<th colspan="7" class="sub-title">染黑度数据</th>
			</tr>
			
			<tr class="header">
				<!-- <td >电话号码</td> -->
				<th >月份</th>
				<th >机构类型</th>
				<th >呼叫次数</th>
				<th >机构数</th>
				<th >第一次呼叫时间</th>
				<th >最后一次呼叫时间</th>
			<tr>
			<tbody id="grayscale">
			
			</tbody>
		</table>
		<table style="width:100%">
			<caption class="sub-title">与紧急联系人的直接通话</caption>
			<tr class="header">
				<th>号码</th>
				<th>月分段</th>
				<th>是否周期性联系</th>
				
			</tr>
			<tbody id="directCall">
			
			</tbody>
		</table>
		<table style="width:100%">
			<caption class="sub-title">与紧急联系人的间接通话</caption>
			<tr class="header">
				<th>号码</th>
				<th>是否周期性联系</th>
			</tr>
			<tbody id="commonContacts">
			
			</tbody>
		</table>
		<table style="width:100%">
			<tr>
				<th colspan="7" class="sub-title">逾期数据</th>
			</tr>
			<tr class="header">
				<th>机构类型</th>
				<th>涉及机构数</th>
				<th>逾期记录数</th>
				<th>最大逾期金额</th>
			<tr>
			<tbody id="overdueClassify">
			
			</tbody>
		</table>
		<table style="width:100%">
			<caption class="sub-title">贷款信息</caption>
			<tr class="header">
				<th>机构类型</th>
				<th>涉及机构数</th>
				<th>贷款总金额</th>
				<th>月均还款额</th>
				<th>月需还款最大金额</th>
			</tr>
			<tbody id="loanClassify">
			
			</tbody>
		</table>
		<table style="width:100%">
			<caption class="sub-title">黑名单数据</caption>
			<tr class="header">
				<th>机构失联</th>
				<th>银行失联</th>
				<th>机构逾期期数</th>
				<th>银行逾期期数</th>
				<th>严重逾期时间</th>
				<th>催收电话的呼叫时间</th>
				<th>机构诉讼</th>
				<th>银行诉讼</th>
				<th>列为黑名单的机构</th>
				<th>开户30天有逾期</th>
			</tr>
			<tbody id="blackList">
			
			</tbody>
		</table>
		<script type="text/javascript">
		
			var MONTH = {
				T6:"六个月整体",
				M0:"当前月汇总",
				M1:"前一个月的汇总",
				M2:"前二个月的汇总",
				M3:"前三个月的汇总",
				M4:"前四个月的汇总",
				M5:"前五个月的汇总"
			};
		
			$.ajax({
				url:"${ctx}/report/dataDetail.do?report=10&report=11&report=13&report=12&code=${param.code}",
				type:"post",
				success:function(result){
					
					if(result.code==0){
						fillData(result.body);
					}
					else{
						alert("数据加载失败!");
					}
				},
				error:function(){
					alert("数据加载失败!");
				}
				
			});
			
			
			
			function fillData(body){
				
				
				
				fillGrayScale(body[0]);
				fillTags(body[0]);
				fillDirectCall(body[0]);
				fillCommonContacts(body[0]);
				
				fillOverdueClassify(body[1]);
				
				fillBlackList(body[2]);
				
				fillLoanClassify(body[3]);
				
				$(document.body).show();
			}
			
			//11
			function fillOverdueClassify(data){
				var typeMap={
					"bankLoan":"银行",
					"bankCredit":"银行信用卡",
					"otherLoan":"非银机构贷款",
					"otherCredit":"非银机构虚拟信用卡"
				};
				
				var tbody = $("#overdueClassify");
				
				if(data && data.result=="0"){
					
					
					var records = data.data.record;
					$.each(records,function(i){
						var classifications = this.classification;
						$.each(classifications,function(j){
							var classification = this;
							$.each(classification,function(mx){
								$.each(this,function(org,obj){
									if(obj){
										var tr = $("<tr></tr>");
										tr.append("<td width='15%' class='general-text'>"+typeMap[org]||""+"</td>");
										tr.append("<td class='align-center'>"+obj.orgNums+"</td>");
										tr.append("<td class='align-center'>"+obj.recordNums+"</td>");
										tr.append("<td class='align-center'>"+obj.maxAmount+"</td>");
										tbody.append(tr);
									}
								});
							});
						});
					});
				}
				
				if(tbody.children().length==0){
					tbody.parent().hide();
				}
			}
			
			//12
			function fillLoanClassify(data){
				
				var orgTypeMap = {
					bank:"银行机构",
					other:"非银行机构"
				};
				
				var tbody = $("#loanClassify");
				if(data && data.result=="0"){
					
					
					var records = data.data.record;
					$.each(records,function(i){
						var classifications = this.classification;
						
						$.each(classifications,function(j,classification){
							//var classification = this;
							$.each(classification,function(mx,mxv){//mx M0..M5;mxv M0...M6对应的值
								$.each(mxv,function(org,val){
									var tr = $("<tr></tr>");
									tr.append("<td width='15%' class='general-text'>"+orgTypeMap[org]||""+"</td>");
									tr.append("<td class='align-center'>"+val.orgNums+"</td>");
									tr.append("<td class='align-center'>"+val.loanAmount+"</td>");
									tr.append("<td class='align-center'>"+val.totalAmount+"</td>");
									tr.append("<td class='align-center'>"+val.repayAmount+"</td>");
									tbody.append(tr);
								});
							});
						});
					});
				}	
				if(tbody.children().length==0){
					tbody.parent().hide();
				}	
			}
			
			function fillDirectCall(data){
				var tbody = $("#directCall");
				if(data && data.result=="0"){
					
					
					var directCalls = data.data.directCall;
					$.each(directCalls,function(phone){
						
						$.each(this,function(key){
									
							var tr = $("<tr></tr>");
							tr.append("<td class='align-center'>"+mobileDesensitize(phone.split("_")[0])+"</td>");
							tr.append("<td class='align-center'>"+MONTH[key]+"</td>");
							tr.append("<td class='align-center'>"+(this.periodicity?"是":"否")+"</td>");
							
							tbody.append(tr);
						});
					});
				}
				if(tbody.children().length==0){
					tbody.parent().hide();
				}
			}
			function fillCommonContacts(data){
				var tbody = $("#commonContacts");
				if(data && data.result=="0"){
					var commonContacts = data.data.commonContacts;
					$.each(commonContacts,function(phone){
						var T6 = this.T6;
						if(T6){
							 $.each(T6,function(phoneMD5){
								$.each(this,function(key){
									var tr = $("<tr></tr>");
									tr.append("<td class='align-center'>"+mobileDesensitize(key.split("_")[0])+"</td>");
									tr.append("<td class='align-center'>"+(this.periodicity?"是":"否")+"</td>");
									tbody.append(tr);
								});
							});
						}
					});
				}
				if(tbody.children().length==0){
					tbody.parent().hide();
				}
			}
			//13
			function fillBlackList(data){
				var tbody = $("#blackList");
				if(data && data.result=="0"){
					var others = data.data.others;
					$.each(others,function(i){
			
						var tr = $("<tr></tr>");
						tr.append("<td class='align-center'>"+this.orgLostContact+"</td>");
						tr.append("<td class='align-center'>"+this.bankLostContact+"</td>");
						tr.append("<td class='align-center'>"+this.orgOverduePeriod+"</td>");
						tr.append("<td class='align-center'>"+this.bankOverduePeriod+"</td>");
						tr.append("<td class='align-center'>"+this.seriousOverdueTime+"</td>");
						tr.append("<td class='align-center'>"+this.dunTelCallTime+"</td>");
						tr.append("<td class='align-center'>"+this.orgLitigation+"</td>");
						tr.append("<td class='align-center'>"+this.bankLitigation+"</td>");
						tr.append("<td class='align-center'>"+this.orgBlackList+"</td>");
						tr.append("<td class='align-center'>"+this.orgOneMonthOvedue+"</td>");
						tbody.append(tr);
					});
				}
				if(tbody.children().length==0){
					tbody.parent().hide();
				}
			}
			
			function fillTags(data){
				var tbody = $("#tags");
				if(data && data.result=="0"){
					var tags = data.data.tags;
					$.each(tags,function(key,val){
						
						var _this = val;
						
						$.each(_this,function(prop){
							if(!/^[TM]\d+$/.test(prop)) return true;//T6 和M0...5
							var tr = $("<tr></tr>");
							
							/* tr.append("<td class='align-center'>"+key.split("_")[0]+"</td>"); */
							tr.append("<td class='align-center'>"+MONTH[prop]+"</td>");
							tr.append("<td class='align-center'>"+(_this.city||"")+"</td>");
							tr.append("<td class='align-center'>"+(_this.label||"")+"</td>");
							tr.append($("<td class='align-center'></td>").text(this.contactAmount,""));
							tr.append($("<td class='align-center'></td>").text(this.dailyCallTimes,""));
							tbody.append(tr);
						});
					});
				}
				if(tbody.children().length==0){
					tbody.parent().hide();
				}
			}
			
			//10
			function fillGrayScale(data){
	
				var orgType = {
					bank:"银行",
					NBFI:"非银行金融机构",
					collectionAgency:"第三方机构/个人"	
				};
				
				var tbody = $("#grayscale");
				if(data && data.result=="0"){
					var grayscale = data.data.grayscale;
					if(grayscale){
						$.each(grayscale,function(key){
							var i = 0;
							//var phoneNum = key.split("_")[0];
							//var tdPhone = $("<td class='align-center'>"+phoneNum+"</td>");
							var phone = this;
							$.each(phone,function(key){
								var j = 0;
								var mX = this;
								var month = this.month;
								var tdMonth = $("<td class='align-center'>"+month+"</td>");
								$.each(mX,function(key){
									if(key=="month") return true;
									var tr = $("<tr></tr>");
									/* if(i==0){
										tr.append(tdPhone);
									} */
									if(j==0){
										tr.append(tdMonth);
									}
									tr.append(
										"<td class='general-text'>"+orgType[key]+"</td>" +
										"<td class='align-center'>"+this.contactTimes+"</td>" +
										"<td class='align-center'>"+this.orgNums+"</td>" +
										"<td class='align-center'>"+this.earliestTime+"</td>" + 
										"<td class='align-center'>"+this.latestTime+"</td>");
									tbody.append(tr);
									
									tdMonth.attr("rowspan",j+1);
									//tdPhone.attr("rowspan",i+1);
									i++;
									j++;
								});
							});
						});
					}
				}
				if(tbody.children().length==0){
					tbody.parent().hide();
				}
			}
		</script>
	</body>
</html>
