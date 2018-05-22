<%@page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	    <title>公积金</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		<link href="${ctx}/css/style.css" type=text/css rel=stylesheet>
		<link href="${ctx}/css/oaframecontentliststyle.css" type=text/css rel=stylesheet>
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
		<script src="${ctx}/js/reports/tcdics.js"></script>
		<script src="${ctx}/js/reports/desensitizeUtil.js"></script>
		<script src="${ctx}/js/amazeui.js"></script>
		<style type="text/css">
			caption {
				padding-left:10px;font-weight:bold;font-size: 14px;line-height:30px;height: 30px;text-align: left;vertical-align:middle;
			}
			.base-info td{
				padding-left: 20px;
			}
		</style>
	</head>
	<body >
		<common:head/>
		<h2 style="text-align: center;height: 60px;line-height: 60px;vertical-align: middle;">公积金</h2>
		<table style="width: 100%">
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>单位名称</td>
				<td>单位性质</td>
				<td>单位组织机构代码</td>
				<td>个人联系电话</td>
				<td>开户日期</td>
				<td>初缴年月</td>
				<td>缴至年月</td>
				<td>公积金缴交状态</td>
				<td>最近一次缴交日期</td>
				<td>月缴存额</td>
				<td>单位缴存比例(%)</td>
				<td>个人缴存比例(%)</td>
				<td>信息获取时间</td>
				<td>数据发生年月</td>
			<tr>
			<tbody id="funds">
			
			</tbody>
		</table>
		<script type="text/javascript">
			$.ajax({
				url:"${ctx}/report/dataDetail.do?report=14&code=${param.code}",
				type:"post",
				success:function(result){
					if(result.code==0){
						fillData(result.body[0]);
					}
				},
				error:function(){
					
				}
			});
			
			function fillData(data){
				if(data.status!=0) return;
				
				var funds = data.data.funds;
				var tbody = $("#funds");
				var i = 0;
				$.each(funds,function(key,row){
					var tr = $("<tr></tr>");
					tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
					tr.append("<td style='text-align:center'>"+(companyDesensitize(row.company))+"</td>");
					tr.append("<td style='text-align:center'>"+NATURES[row.nature]+"</td>");
					tr.append("<td style='text-align:center'>"+companyCodeDesensitize(row.cocode)+"</td>");
					tr.append("<td style='text-align:center'>"+$.emptyIf(row.phone,"null")+"</td>");
					tr.append("<td style='text-align:center'>"+row.opendate+"</td>");
					tr.append("<td style='text-align:center'>"+$.emptyIf(row.firstMonth,"null")+"</td>");
					tr.append("<td style='text-align:center'>"+row.toMonth+"</td>");
					tr.append("<td style='text-align:center'>"+row.status+"</td>");
					tr.append("<td style='text-align:center'>"+$.emptyIf(row.lastDate,"null")+"</td>");
					tr.append("<td style='text-align:center'>"+amountDesensitize(row.pay)+"</td>");
					tr.append("<td style='text-align:center'>"+row.copercent+"</td>");
					tr.append("<td style='text-align:center'>"+row.ownpercent+"</td>");
					tr.append("<td style='text-align:center'>"+row.gettime+"</td>");
					tr.append("<td style='text-align:center'>"+row.curmonth+"</td>");
					tbody.append(tr);
					i++;
				});
			}
		</script>
	</body>
</html>
