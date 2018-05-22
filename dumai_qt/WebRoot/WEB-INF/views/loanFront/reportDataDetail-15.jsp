<%@page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	    <title>犯罪</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		<link href="${ctx}/css/style.css" type=text/css rel=stylesheet>
		<link href="${ctx}/css/oaframecontentliststyle.css" type=text/css rel=stylesheet>
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
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
		<h2 style="text-align: center;height: 60px;line-height: 60px;vertical-align: middle;">犯罪</h2>
		<table style="width: 100%">
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>涉案类型</td>
				<td>案件类别</td>
				<td>案发时间</td>
				<td>案件来源</td>
			<tr>
			<tbody id="crimes">
			
			</tbody>
		</table>
		<script type="text/javascript">
			$.ajax({
				url:"${ctx}/report/dataDetail.do?report=15&code=${param.code}",
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
				if(data.resCode!='0000') return;
				
				var theData = data.data;
				if(theData && theData.statusCode!='2012') return;
				
				var crimes = theData.result;
				var tbody = $("#crimes");
				var i = 0;
				$.each(crimes,function(key,row){
					var tr = $("<tr></tr>");
					tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
					tr.append("<td style='text-align:center'>"+$.emptyIf(row.crimeType,"null")+"</td>");
					tr.append("<td style='text-align:center'>"+$.emptyIf(row.caseType,"null")+"</td>");
					tr.append("<td style='text-align:center'>"+(row.caseTime?$.formatDate($.parseDate(row.caseTime),"yyyy-MM-dd"):"")+"</td>");
					tr.append("<td style='text-align:center'>"+$.emptyIf(row.caseSource,"null")+"</td>");
					tbody.append(tr);
					i++;
				});
			}
		</script>
	</body>
</html>
