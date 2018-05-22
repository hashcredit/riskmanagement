<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	  
	    <title>维修记录</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		 <LINK href="${ctx}/css/style.css" type=text/css rel=stylesheet>
		<LINK href="${ctx}/css/oaframecontentliststyle.css" type=text/css rel=stylesheet>
	
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
		<script src="${ctx}/js/amazeui.js"></script>
		<style type="text/css">
			caption {
				padding-left:10px;font-weight:bold;font-size: 16px;line-height:30px;height: 30px;text-align: left;vertical-align:middle;
			}
			.base-info td{
				padding-left: 20px;
			}
		</style>
	</head>
	<body >
		<common:head/>
		<h2 style="font-size:30px;text-align: center;height: 60px;line-height: 60px;vertical-align: middle;">4S维修记录</h2>
		<table style="width: 100%" class="base-info">
			<caption style="border-top: solid 2px #FFF">基本信息</caption>
			
			<tr>
	           <td style="width: 200px">车辆识别码(VIN)：</td>
	           <td style="width: 40%"><span id="vin"></span></td>
	           <td style="width: 200px">报告生成时间：</td>
	           <td ><span id="year"></span></td>
	        </tr> 
	    	
	        <tr>   
	           <td>生产厂商：</td>
	           <td><span id="brand"></span></td>
	           <td>车型：</td>
	           <td><span id="model"></span></td>
	        </tr> 
	        <tr>   
	           <td>排量：</td>
	           <td><span id="displacement"></span></td>
	           <td>变速器：</td>
	           <td><span id="gearbox"></span></td>
	        </tr> 
		</table>
		<table style="width: 100%">
			<caption>详细报告</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td style="width: 20%">日期</td>
				<td style="width: 20%">公里数</td>
				<td style="width: 20%">类型</td>
				<td >维修保养内容</td>
			<tr>
			<tbody id="reports">
			
			</tbody>
		</table>
	<script type="text/javascript">
		
		$.ajax({
			url:"${ctx}/report/dataDetail.do?report=9&code=${param.code}",
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
			if(body.status==0){
				var data = body.data;
				if(data.result!=5) return;
				
				
				var basic = data.basic;
				
				if(basic){
					$("#vin").text(basic.vin?(basic.vin.substring(0,13) +"***"):"");
					$("#year").text(basic.year,"");
					$("#brand").text(basic.brand,""); 
					$("#model").text(basic.model,""); 
					$("#displacement").text(basic.displacement,""); 
					$("#gearbox").text(basic.gearbox,""); 
				}
				
				
				var reports = data.report;
				var tbody = $("#reports");
				var i = 0;
				$.each(reports,function(key){
					var tr = $("<tr></tr>");
					tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
					tr.append("<td style='text-align:center'>"+this.repairDate+"</td>"); 
					tr.append("<td style='text-align:center'>"+this.mileage+"</td>"); 
					tr.append("<td style='text-align:center'>"+this.type+"</td>");
					tr.append("<td >" + 
							"<div>项目："+this.content+ "</div>" +
							"<div>材料："+this.material+ "<div>" +
						"</td>");
					tbody.append(tr);
					i++;
				});
			}
		}
	</script>
	</body>
</html>
