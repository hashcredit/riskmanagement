<%@page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	    <title>涉诉</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		<link href="${ctx}/css/style.css" type=text/css rel=stylesheet>
		<link href="${ctx}/css/oaframecontentliststyle.css" type=text/css rel=stylesheet>
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
		<script src="${ctx}/js/amazeui.js"></script>
		<style type="text/css">
			caption {
				padding-left:10px;font-weight:bold;font-size: 14px;line-height:30px;height: 30px;text-align: left;vertical-align:middle;
			}
			.base-info td{
				padding-left: 20px;
			}
			caption{
				color: #4d59ff;
			    background: #e6e6e6;
			}
		</style>
	</head>
	<body >
		<common:head/>
		<h4 style="text-align: center;height: 30px;line-height: 30px;vertical-align: middle;"></h4>
		<table style="width: 100%">
			<caption>执行公开信息</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td >被执行人姓名或名称</td>
				<td >证件号码</td>
				<td >执行法院</td>
				<td >执行案号</td>
				<td >执行内容</td>
				<td >立案时间</td>
				<td >执行状态</td>
				<td >异议备注</td>
			<tr>
			<tbody id="appeals-32">
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>失信老赖名单</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td  >被执行人姓名或名称</td>
				<td>证件号码</td>
				<td  >执行法院</td>
				<td  >执行案号</td>
				<td  width="30%" >执行内容</td>
				<td  >日期类别</td>
				<td  >具体日期</td>
				<td  >执行状态</td>
				<td  >异议备注</td>
			<tr>
			<tbody id="appeals-46-38">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>限制高消费名单</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td  >被执行人姓名或名称</td>
				<td  >证件号码</td>
				<td  >执行法院</td>
				<td  >执行案号</td>
				<td  >执行内容</td>
				<td  >日期类别</td>
				<td  >具体日期</td>
				<td  >执行状态</td>
				<td  >异议备注</td>
			<tr>
			<tbody id="appeals-148">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>限制出入境名单</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>被限制人姓名</td>
				<td>证件号码</td>
				<td>执行法院</td>
				<td>执行案号</td>
				<td>执行内容</td>
				<td>日期类别</td>
				<td>具体日期</td>
				<td>执行状态</td>
				<td>异议备注</td>
			<tr>
			<tbody id="appeals-149">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>民商事裁判文书</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td >当事人姓名或名称</td>
				<td >证件号码</td>
				<td >审理机关</td>
				<td >案号</td>
				<td >涉案事由</td>
				<td >涉案金额</td>
				<td >诉讼地位</td>
				<td >结案时间</td>
				<td >异议备注</td>
			<tr>
			<tbody id="appeals-31">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>民商事审判流程</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td >当事人姓名或名称</td>
				<td >证件号码</td>
				<td >审理机关</td>
				<td >案号</td>
				<td >涉案事由</td>
				<td >涉案金额</td>
				<td >诉讼地位</td>
				<td >结案时间</td>
				<td >异议备注</td>
			<tr>
			<tbody id="appeals-151">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>罪犯及嫌疑人名单</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td >当事人姓名或名称</td>
				<td >证件号码</td>
				<td >审理机关</td>
				<td >案号</td>
				<td >涉案事由</td>
				<td >涉案金额</td>
				<td >诉讼地位</td>
				<td >结案时间</td>
				<td >异议备注</td>
			<tr>
			<tbody id="appeals-31_fz">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>行政违法记录</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>当事人姓名或名称</td>
				<td>证件号码</td>
				<td>执法/复议/审判机关</td>
				<td>案号</td>
				<td>违法事由</td>
				<td>行政执法结果</td>
				<td>法院审理结果</td>
				<td>日期类别</td>
				<td>具体日期</td>
				<td>异议备注</td>
			<tr>
			<tbody id="appeals-134">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>欠税名单</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>纳税人名称</td>
				<td>证件号码</td>
				<td>主管税务机关</td>
				<td>所欠税种</td>
				<td>欠税属期</td>
				<td>欠税余额</td>
				<td>欠税发生时间</td>
				<td>异议备注</td>
			<tr>
			<tbody id="appeals-34">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>纳税非正常户</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>纳税人名称</td>
				<td>纳税人识别号</td>
				<td>主管税务机关</td>
				<td>认定日期</td>
				<td>异议备注<td>
			<tr>
			<tbody id="appeals-129">
			
			</tbody>
		</table>
		<table style="width: 100%">
			<caption>欠款欠费名单</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td>欠款人姓名/名称</td>
				<td>证件号码</td>
				<td>拖欠金额</td>
				<td>拖欠币种</td>
				<td>拖欠事由</td>
				<td>合同/借据编号</td>
				<td>拖欠起始日期</td>
				<td>异议备注</td>
			<tr>
			<tbody id="appeals-147">
			
			</tbody>
		</table>
	</body>
	<script type="text/javascript">
		$.ajax({
			url:"${ctx}/report/dataDetail.do?orderCode=${param.orderCode}&dm_3rd_interface_code=${param.dm_3rd_interface_code}",
			//url:"${ctx}/docs/${param.dm_3rd_interface_code}.json",
			type:"post",
			success:function(result){
				if(result.code==0){
					fillData($.parseJSON(result.body));
				}
			},
			error:function(){
				
			}
		});
		
		function inArray(arr,o){
			for(var i in arr){
				if(arr[i]==o) return true;
			}
			 return false;
		}
		
		function fillTable(allmsglist,tableId,typet,props){
			var tbody = $("#"+tableId);
			var i = 0;
			if(typeof(typet)!="array"){
				typet = [typet];
			}
			
			$.each(allmsglist,function(key,row){
				if(inArray(typet,row.typet)){
					var tr = $("<tr></tr>");
					tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
					/*$.each(allmsglist,function(idx,attr){
						tr.append("<td style='text-align:center'>"+(row[attr])+"</td>");
					});*/
					$.each(props,function(idx,prop){
						tr.append("<td style='text-align:center'>"+getPropValue(row.onemsglist,prop)+"</td>");
					});
					/* tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
					tr.append("<td style='text-align:center'>"+(row.type)+"</td>");
					tr.append("<td style='text-align:center'>"+rowData.reason+"</td>");
					tr.append("<td style='text-align:center'>"+rowData.position+"</td>");
					tr.append("<td style='text-align:center'>"+rowData.amount+"</td>");
					tr.append("<td style='text-align:center'>"+rowData.performStatus+"</td>");
					tr.append("<td style='text-align:center'>"+rowData.filingTime+"</td>");
					tr.append("<td style='text-align:center'>"+rowData.concludingTime+"</td>"); */
					
					tbody.append(tr);
					i++;
				}
			});
			
		}
		
		function getPropValue(onemsglist,prop){
			for(var i in onemsglist){
				var msg = onemsglist[i];
				var propername = msg.propername;
				if(propername==prop){
					return msg.propervalue;
				}
			}
			return "";
		}
		
		function fillData(data){
			
			
			if(data.success!="s") return;
			
			var allmsglist = data.allmsglist;
			
			fillTable(allmsglist,"appeals-32",32,  ["被执行人姓名或名称","证件号码","执行法院","执行案号","执行内容","立案时间","执行状态","异议备注"]);
			
			fillTable(allmsglist,"appeals-46-38",[46,38],["被执行人姓名或名称","证件号码","执行法院","执行案号","执行内容","日期类别","具体日期","执行状态","异议备注"]);
			fillTable(allmsglist,"appeals-148",148,["被执行人姓名或名称","证件号码","执行法院","执行案号","执行内容","日期类别","具体日期","执行状态","异议备注"]);
			fillTable(allmsglist,"appeals-149",149,["被限制人姓名","证件号码","执行法院","执行案号","执行内容","日期类别","具体日期","执行状态","异议备注"]);
			
			fillTable(allmsglist,"appeals-31",31,  ["当事人姓名或名称","证件号码","审理机关","案号","涉案事由","涉案金额","诉讼地位","结案时间","异议备注"]);
			fillTable(allmsglist,"appeals-31_fz",31,["当事人姓名或名称","证件号码","侦查/批捕/审判机关","案号","违法事由","处理结果","处理时间","异议备注"]);
			fillTable(allmsglist,"appeals-151",151, ["当事人姓名或名称","证件号码","审理机关","案号","涉案事由","诉讼地位","日期类别","具体日期","异议备注"]);
			fillTable(allmsglist,"appeals-134",134, ["当事人姓名或名称","证件号码","执法/复议/审判机关","案号","违法事由","行政执法结果","法院审理结果","日期类别","具体日期","异议备注"]);
			fillTable(allmsglist,"appeals-34",34, ["纳税人名称","证件号码","主管税务机关","所欠税种","欠税属期","欠税余额","欠税发生时间","异议备注"]);
			fillTable(allmsglist,"appeals-129",129, ["纳税人名称","纳税人识别号","主管税务机关","认定日期","异议备注"]);
			fillTable(allmsglist,"appeals-147",147, ["欠款人姓名/名称","证件号码","拖欠金额","拖欠币种","拖欠事由","合同/借据编号","拖欠起始日期","异议备注"]);
			
			
			
			/* data = JSON.parse(data);
			if(data.success!="s") return;
			
			var allmsglist = data.allmsglist;
			var tbody = $("#appeals");
			var i = 0;
			$.each(allmsglist,function(key,row){
				var tr = $("<tr></tr>");
				var rowData = getRowData(row.onemsglist);
				tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
				tr.append("<td style='text-align:center'>"+(row.type)+"</td>");
				tr.append("<td style='text-align:center'>"+rowData.reason+"</td>");
				tr.append("<td style='text-align:center'>"+rowData.position+"</td>");
				tr.append("<td style='text-align:center'>"+rowData.amount+"</td>");
				tr.append("<td style='text-align:center'>"+rowData.performStatus+"</td>");
				tr.append("<td style='text-align:center'>"+rowData.filingTime+"</td>");
				tr.append("<td style='text-align:center'>"+rowData.concludingTime+"</td>");
				
				tbody.append(tr);
				i++;
			}); */
		}
		
		function getRowData(onemsglist){
			var rowData = {
				position:"-",
				amount:"-",
				performStatus:"-",
				filingTime:"-",
				concludingTime:"-",
				reason:"-"
			};
			for(var i in onemsglist){
				var msg = onemsglist[i];
				var propername = msg.propername;
				switch(propername){
					case "诉讼地位":rowData.position=msg.propervalue;break;
					case "执行内容":rowData.amount=msg.propervalue.split(":")[1]||"-";break;
					case "涉案金额":rowData.amount=msg.propervalue;break;
					case "执行状态":rowData.performStatus=msg.propervalue;break;
					case "立案时间":rowData.filingTime=msg.propervalue;break;
					case "结案时间":rowData.concludingTime=msg.propervalue;break;
					case "涉案事由":rowData.reason=msg.propervalue;break;
				}
			}
			return rowData;
		}
	</script>
</html>
