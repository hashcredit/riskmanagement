<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
	<title>审核报告</title>
	<meta name="renderer" content="webkit">
	<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
	<link rel="stylesheet" href="${ctx}/css/app.css" />
	<link rel="stylesheet" href="${ctx}/css/report.css" />
	<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
	<script src="${ctx}/js/global.js"></script>
	<script src="${ctx}/js/amazeui.js"></script>
	<style type="text/css">
		*{
			box-sizing:border-box;
		}
		body{
			margin:0;
			width:100%;
			padding:1px;
		}
		
	</style>
	<script type="text/javascript">
		
		//请求数据
		$(function(){
			$.ajax({
				url:"${ctx}/newreport/xypfResult.do" + location.search,
				dataType:"json",
				type:"post",
				success:function(result){
					if (result.code==0) {
						if (result.body) {
							fillResult(result.body);
							$("#data-table").show();
							$("#no-data").hide();
						}
						else{
							$("#data-table").hide();
							$("#no-data").show();
						}
					}
					else {
						alert(result.error);
					}
				}
			});
			
			
			function fillResult(data){
				var xypfmx = $("#xypfmx").empty();
				
				$.each(data.result||[],function(i,value) {
					var tr = $("<tr></tr>");
					tr.append($("<td align='center'></td>").text(i+1));
					tr.append($("<td style='padding-left:15px'></td>").text(value.name));
					tr.append($("<td align='center'></td>").text(value.score));
					xypfmx.append(tr);
				});
				$("#xypf_total_score").text(data.total_score);
			}
		});
	</script>
	</head>

	<body >
		<common:head/>
		<div class="below"></div>
		<table class="table1" style="display: none" id="data-table">
			<tbody id="xypf-block" >
			<tr>
				<td colspan=3>
					<div style="padding-left:10px">信用评分</div>
					<div class="line"></div>
				</td>
			</tr>
			
						
			<tr align="center">
				<td width="2%" class="titletd" height="30" nowrap>序号</td>
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
			<tbody>
				<tr align="center" style="background: #dce0e6;font-weight:bold">
					<td colspan="2">合计</td>
					<td id="xypf_total_score"></td>
				</tr>
			</tbody>
		</table>
		<div id="no-data" style="text-align:center">信用评分评估中...</div>
	</body>
</html>
