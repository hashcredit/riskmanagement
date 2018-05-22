<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	  
	    <title>审核报告</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		 <LINK href="${ctx}/css/style.css" type=text/css rel=stylesheet>
		<LINK href="${ctx}/css/oaframecontentliststyle.css" type=text/css rel=stylesheet>
	
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
		<script src="${ctx}/js/reports/ufcities.js"></script>
		<script src="${ctx}/js/reports/desensitizeUtil.js"></script>
		<script src="${ctx}/js/amazeui.js"></script>
		<style type="text/css">
			td.no-data{
				height: 30px;
				line-height: 30px;
				text-align:center;
				vertical-align:middle;
			}
		</style>
	</head>
	
	<body >
		<common:head/>
		<table style="width: 100%">
			<tr>
				<th colspan="4" style="text-align: center;font-size: 18px;height: 60px">单卡用户画像</th>
			</tr>
			
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td style="width: 60%">指标</td>
				<td>值</td>
			<tr>
			<tbody id="portrait">
			
			</tbody>
		</table>
	
	</body>
	<script type="text/javascript">
		
		function desensitize(key,data){
			var desensition = DesensitizeConfig[key].desensition;
			switch(desensition){
				case "AMOUNT":{
					return amountDesensitize(data[key]);
				};
				case "CITY":{
					var cities = data[key].split(";");
					var text = [];
					for(var i in cities){
						text.push(UFCITIES[cities[i]]);
					}
					return text.join(";");
				};
				case "RAW":;
				default:return data[key];
			}
		}
		
		var DesensitizeConfig={
			S0483:{
				name:"近12月常住城市（交易笔数最多）",
				desensition:"CITY"
			},
			S0474:{
				name:"还贷能力指标",
				desensition:"AMOUNT"
			},
			S0332:{
				name:"近12月银行卡转账金额",
				desensition:"AMOUNT"
			},
			S0686:{
				name:"近12月银行卡入账总金额",
				desensition:"AMOUNT"
			},
			S0670:{
				name:"信用卡还款金额",
				desensition:"AMOUNT"
			},
			S0147:{
				name:"近12月消费高额商户分布",
				desensition:"RAW"
			},
			S0057:{
				name:"近12月网购金额",
				desensition:"AMOUNT"
			},
			S0446:{
				name:"近12月纳税金额",
				desensition:"AMOUNT"
			},
			S0266:{
				name:"近12月大型超市消费金额",
				desensition:"AMOUNT"
			},
			S0242:{
				name:"近12月高档运动消费金额",
				desensition:"AMOUNT"
			},
			S0188:{
				name:"近12月餐饮类消费金额",
				desensition:"AMOUNT"
			},
			S0392:{
				name:"近12月航空消费金额",
				desensition:"AMOUNT"
			},
			S0398:{
				name:"近12月铁路消费金额",
				desensition:"AMOUNT"
			},
			S0410:{
				name:"近12月证券消费金额",
				desensition:"AMOUNT"
			},
			S0428:{
				name:"近12月博彩消费金额",
				desensition:"AMOUNT"
			},
			S0434:{
				name:"近12月法律服务消费金额",
				desensition:"AMOUNT"
			},
			S0440:{
				name:"近12月罚款金额",
				desensition:"AMOUNT"
			},
			S0470:{
				name:"是否有房",
				desensition:"RAW"
				
			},
			S0472:{
				name:"是否有车",
				desensition:"RAW"
			},
			S0135:{
				name:"近12月，最近一笔交易时间",
				desensition:"RAW"
			},
			S0136:{
				name:"近12月，最近一笔交易城市",
				desensition:"CITY"
			},
			S0571:{
				name:"每月高额交易统计",
				desensition:"RAW"
			},
			S0308:{
				name:"近12月大额入账金额 [1万，+)",
				desensition:"AMOUNT"
			},
			S0585:{
				name:"近12月TOP3交易地列表",
				desensition:"CITY"
			}
				
		};
		
		$.ajax({
			url:"${ctx}/report/dataDetail.do?report=7&code=${param.code}",
			type:"post",
			success:function(result){
				if(result.code==0){
					fillData(result.body[0]);
				}
			},
			error:function(){
				
			}
			
		});
		
		function fillData(body){
			if(body.resCode=="0000"){
				var data = body.data;
				var portrait = $("#portrait");
				if(data.statusCode=='2012'){
					var quota = data.result.quota;
					var i = 0;
					$.each(quota,function(key){
						var tr = $("<tr></tr>");
						tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
						tr.append("<td>"+DesensitizeConfig[key].name+"</td>");
						tr.append("<td>"+desensitize(key,quota)+"</td>");
						portrait.append(tr);
						i++;
					});
				}
				else{
					portrait.append("<tr><td class='no-data' colspan='3'>没有数据!</td></tr>");
				}
			}
		}
	</script>
</html>
