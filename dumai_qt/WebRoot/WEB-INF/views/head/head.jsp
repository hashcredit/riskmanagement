<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="common" tagdir="/WEB-INF/tags/common"%>

<!DOCTYPE html>
<html>
<head>
<style media="screen,print">
	#body {
		width:70em;
		max-width:100%;
		margin:0 auto;
	}
	iframe {
		width:100%;
		margin:0 0 1em;
		border:0;
	}
	.modal-dialog {  
	
	}
</style>
</head>
<body>
	<ul>
            <li><i class="spriteIcon icon_system"></i><span id="settingBtn">系统设置</span></li>
            <li><i class="spriteIcon icon_data"></i><span id="dataForm">数据报表</span></li>
           <!--  <li><i class="spriteIcon icon_traffic"></i><span>数据流量</span></li> -->
            <h5>[ <i ng-bind="username" class="user_name_right"></i> ]<a><span class="spriteIcon" ng-click="exit_tip_box()"></span></a></h5>
    </ul>
        
<%-- 	<div class="modal fade" id="modal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" >系统设置</h4>
					<iframe id="setting" src="${ctx}/sysmgr/funsettings/toPage.do"></iframe> 
				</div>
			</div>
		</div>
	</div>--%>
	
	<div class="modal fade" id="modal2" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
						<h4 class="modal-title" >数据报表</h4>
					<iframe id="setting2"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	function WH(modal,ifram,W){
		modal.draggable();
		modal.modal().css({
			"margin-left": function () {
					return  -100;
				}
			});
		if(W>1900){
			modal.css("width",1800);
			modal.css("height",1950);
			ifram.css("width",1500);
			ifram.css("height",860);
		}else if(W>1400){
			modal.css("width",1500);
			modal.css("height",1950);
			ifram.css("width",1250);
			ifram.css("height",700);
		}else if(W>1024){
			modal.css("width",1500);
			modal.css("height",1950);
			ifram.css("width",1250);
			ifram.css("height",550);
		}else{
			modal.css("width",1376);
			modal.css("height",1950);
			ifram.css("width",1150);
			ifram.css("height",550);
		}
	}
		$(function(){
			var W=$(window).width();
			var H=$(window).height();
			/* 系统设置模态窗口 */
			$("#settingBtn").click(function(){
				layer.open({
					 type: 2,
					 title: '系统设置',
					 shadeClose: true,
					 shade: 0.8,
					 area: ['80%', '90%'],
					 maxmin: true,
					 anim: 2,
					 content: '${ctx}/sysmgr/funsettings/toPage.do' //iframe的url
				});
				WH($("#modal"), $("#setting"),W);
				$("#modal").modal("show");
			})

			/* 数据报表模态窗口 */
			$("#dataForm").click(function(){
				WH($("#modal2"), $("#setting2"),W);
 				$("#setting2").attr("src","${ctx}/sysmgr/echarts/toDataForm.do");
				$("#modal2").modal("show");
			})
		})
	</script>