<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<!DOCTYPE html>
<html>
	<head>
	  
	    <title>车辆基本信息</title>
		<meta name="renderer" content="webkit">
		<link rel="stylesheet" href="${ctx}/css/amazeui.css" />
		<link rel="stylesheet" href="${ctx}/css/app.css" />
		 <LINK href="${ctx}/css/style.css" type=text/css rel=stylesheet>
		<LINK href="${ctx}/css/oaframecontentliststyle.css" type=text/css rel=stylesheet>
	
		<script src="${ctx}/js/lib/jquery-easyui-1.5/jquery.min.js"></script>
		<script src="${ctx}/js/global.js"></script>
		<script src="${ctx}/js/reports/tcdics.js"></script>
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
		<h2 style="font-size:30px;text-align: center;height: 60px;line-height: 60px;vertical-align: middle;">车辆基本信息</h2>
		<table style="width: 100%" class="base-info">
			<caption style="border-top: solid 2px #FFF">基本信息</caption>
			<tr>
	           <td style="width: 200px">初次登记日期：</td>
	           <td style="width: 25%"><span id="ccdjrq"></span></td>
	           <td style="width: 200px">车辆类型：</td>
	           <td style="width: 25%"><span id="cllx"></span></td>
	           <td style="width: 200px">车辆识别代号：</td>
	           <td ><span id="clsbdh"></span></td>
	        </tr> 
			<tr>
	           <td style="width: 200px">车辆型号：</td>
	           <td style="width: 25%"><span id="clxh"></span></td>
	           <td style="width: 200px">车牌号码：</td>
	           <td style="width: 25%"><span id="cphm"></span></td>
	           <td style="width: 200px">厂牌型号：</td>
	           <td ><span id="cpxh"></span></td>
	        </tr> 
			<tr>
	           <td style="width: 200px">车身颜色：</td>
	           <td style="width: 25%"><span id="csys"></span></td>
	           <td style="width: 200px">发动机号：</td>
	           <td style="width: 25%"><span id="fdjh"></span></td>
	           <td style="width: 200px">发动机型号：</td>
	           <td ><span id="fdjxh"></span></td>
	        </tr> 
			<tr>
	           <td style="width: 200px">号牌种类：</td>
	           <td style="width: 25%"><span id="hpzl"></span></td>
	           <td style="width: 200px">机动车所有人：</td>
	           <td style="width: 25%"><span id="jdcsyr"></span></td>
	           <td style="width: 200px">机动车状态：</td>
	           <td ><span id="jdczt"></span></td>
	        </tr> 
			<tr>
	           <td style="width: 200px">检验有效期止：</td>
	           <td style="width: 25%"><span id="jyyxqz"></span></td>
	           <td style="width: 200px">使用性质：</td>
	           <td style="width: 25%"><span id="syxz"></span></td>
	           <td style="width: 200px">中文品牌：</td>
	           <td ><span id="zwpp"></span></td>
	        </tr> 
		</table>
		
		<script type="text/javascript">
			
			$.ajax({
				url:"${ctx}/report/dataDetail.do?report=8&code=${param.code}",
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
					if(data.authStatus!='0') return;
					
					$("#ccdjrq").text(data.ccdjrq,"");
					$("#cllx").text(CLLXS[data.cllx],"");
					$("#clsbdh").text(data.clsbdh.substring(0,13)+"***","");
					$("#clxh").text(data.clxh,"");
					$("#cphm").text(data.cphm,"");
					$("#cpxh").text(data.cpxh,"");
					$("#csys").text(COLORS[data.csys],"");
					$("#fdjh").text(data.fdjh,"");
					$("#fdjxh").text(data.fdjxh,"");
					$("#hpzl").text(HPZLS[data.hpzl],"");
					
					var jdcsyr = data.jdcsyr;
					if(jdcsyr){
						$("#jdcsyr").text(data.jdcsyr.substring(0,1) + "***" + jdcsyr.substring(4));
						//var length = jdcsyr.length;
						//$("#jdcsyr").text(data.jdcsyr.substring(0,Math.ceil(length*0.25)) + "***" + jdcsyr.substring(Math.ceil(length*0.75)));
					}
					
					$("#jdczt").text(JDCZTS[data.jdczt],"");
					$("#jyyxqz").text(data.jyyxqz,"");
					$("#syxz").text(XYXZS[data.syxz],"");
					$("#zwpp").text(data.zwpp,"");

				}
			}
		</script>
	</body>
</html>
