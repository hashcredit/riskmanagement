<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	  
	    <title>同住人信息</title>
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
				padding-left:10px;font-weight:bold;font-size: 14px;line-height:30px;height: 30px;text-align: left;vertical-align:middle;
			}
			.base-info td{
				padding-left: 20px;
			}
		</style>
	</head>
	
	<body >
		<common:head/>
		<h2 style="text-align: center;height: 60px;line-height: 60px;vertical-align: middle;"><!-- 同住人信息 --></h2>
		<table style="width: 100%" class="base-info">
			<caption style="border-top: solid 2px #FFF">个人基本信息</caption>
			
			<tr>
	            <td >姓名：<span id="name">${param.name}</span></td>
	            <td >民族：<span id="nationality"></span></td>
	            <td >性别：<span id="gender"></span></td>
	         </tr> 
	         <tr>   
	            <td>服务处所（工作的公司）:<span id="department"></span></td>
	            <td>文化程度（学历水平）：<span id="education"></span></td>
	            <td>籍贯：<span id="birthplace"></span></td>
	         </tr> 
	    	<tr>   
	            <td >婚姻状况：<span id="maritalStatus"></span></td>
	            <td >曾用名：<span id="usedName"></span></td>
	            <td >身份证当前状态：<span id="idcardStatus"></span></td>
	         </tr> 
	      	 <tr>   
	            <td >生日：<span id="birthday"></span></td>
	            <td colspan="2">地址：<span id= "address" ></span></td>
	         </tr>
		</table>
		<table style="width: 100%">
			<caption>同住人列表</caption>
			<tr style="text-align: center;font-size: 14px">
				<td style="width: 100px">序号</td>
				<td style="width: 20%">姓名</td>
				<td style="width: 20%">性别</td>
				<td style="width: 20%">民族</td>
				<td >出生日期</td>
			<tr>
			<tbody id="cohabiters">
			
			</tbody>
		</table>
	<script type="text/javascript">
		
		$.ajax({
			url:"${ctx}/report/dataDetail.do?orderCode=${param.orderCode}&dm_3rd_interface_code=${param.dm_3rd_interface_code}",
			type:"post",
			success:function(result){
				
				if(result.code==0){
					fillData(result.body);
				}
			},
			error:function(){
				
			}
			
		});
		
		function fillData(body){
			body = JSON.parse(body);
			if(body.resCode=="0000"){
				var data = body.data;
				if(data.statusCode!="2012") return;
				
				
				var info = data.result.info;
				
				if(info){
					$("#usedName").text(info.usedName,"");
					$("#idcardStatus").text(info.idcardStatus,"");
				}
				
				var detail = data.result.detail;
				$("#gender").text(detail.gender,"");
				$("#nationality").text(detail.nationality,"");
				$("#department").text(detail.department,"");
				$("#education").text(detail.education,"");
				var birthplace = detail.birthplace;
				if(birthplace){
					$("#birthplace").text(birthplace.substring(0,birthplace.indexOf("市") +1),"");
				}
				$("#maritalStatus").text(detail.maritalStatus,"");
				$("#birthday").text(detail.birthday,"");
				$("#address").text((detail.censusAddress.district||"")+"****");
					
					
				var cohabiters = data.result.cohabiters;
				var tbody = $("#cohabiters");
				var i = 0;
				$.each(cohabiters,function(key){
					var tr = $("<tr></tr>");
					tr.append("<td style='text-align:center'>"+(i+1)+"</td>");
					tr.append("<td style='text-align:center'>"+this.name.substring(0,1)+"**</td>");
					tr.append("<td style='text-align:center'>"+this.gender+"</td>");
					tr.append("<td style='text-align:center'>"+this.nationality+"</td>");
					tr.append("<td style='text-align:center'>"+this.birthday+"</td>");
					tbody.append(tr);
					i++;
				});
			}
		}
	</script>
	</body>
</html>
