<%@tag import="com.newdumai.web.util.SystemConst" trimDirectiveWhitespaces="true"%>
<%@tag language="java" pageEncoding="UTF-8" body-content="tagdependent"%>
<%@attribute name="back" required="false" type="java.lang.String"  rtexprvalue="true"%>
<%@attribute name="tabs" required="false" type="java.lang.String"  rtexprvalue="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	request.setAttribute("BASE_PATH",basePath );
	request.setAttribute("ctx",path );
	String uri = request.getRequestURI();
	request.setAttribute("URI",uri );
%>
<c:url var="baseUrl" value="${BASE_PATH}" ></c:url>
<div data-options="region:'north',border:false" id="-x-global-header-container" style="height:94spx">
	<div data-am-widget="header" class="am-header am-header-default">
	    <div class="am-header-left am-header-nav">
		    <c:choose>
		    	<c:when test="${back==null}">
		        	<i onclick="history.go(-1)" class="am-header-icon am-icon-angle-left iconf"></i>
		    	</c:when>
		    	<c:when test="${back=='app:home'}">
		    		<a href="${baseUrl}/toPortal.do'}">
			        	<i class="am-header-icon am-icon-angle-left iconf"></i>
			        </a>	
		    	</c:when>
		    	<c:when test="${back=='history:back'}">
		    		<a href="javascript:history.go(-1)">
			        	<i class="am-header-icon am-icon-angle-left iconf"></i>
			        </a>	
		    	</c:when>
		    	<c:otherwise>
		    		<a href="${back}">
			        	<i class="am-header-icon am-icon-angle-left iconf"></i>
			        </a>
		    	</c:otherwise>
		    </c:choose>
        	<i onclick="window.location.href='${baseUrl}/toPortal.do'" class="am-header-icon am-icon-home iconf"></i>
	    </div>
	    <h1 id="header-title" class="am-header-title">
	        
	    </h1>
	    <div class="am-header-right am-header-nav">
	     	<span class="userinfo-logout" >${sessionScope.login.username} [${sessionScope.login.username}]&nbsp;<a onclick="logout()">退出</a></span>
            &nbsp;
            <a class="am-header-icon am-icon-bars iconf bar-menu" id="bar-menu"></a>
	    </div>
	</div>
	<div data-am-widget="tabs" id="-x-global-tabs-container" class="am-tabs am-tabs-d2">
        <ul id="-x-global-tabs" class="am-tabs-nav am-cf">
            
        </ul>
     </div>
</div>
<section id="addItem" class="addItem am-collapse" >
   <div class="m-options">
        <div class="wids1 am-cf ">
            <div class="am-u-sm-4 b1">
                <h2>贷前审核</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                <span class="num"></span>
                 <a href="${baseUrl}/loanFront/toLoanFront.do" class="btn">&nbsp;</a>
            </div>
            <div class="am-u-sm-8 b2">
                <h2>贷后跟踪</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                <span class="num"></span>
               	<a href="#${baseUrl}/Fk_dhgzAction?command=QueryList&resId=15b5d3e2-8" class="btn">&nbsp;</a>

            </div>
            <div class="am-u-sm-4 b3">
                <h2>参数设置</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                <a href="#${baseUrl}/system/codeList.jsp??resId=1fc9072c-8" class="btn">&nbsp;</a>
            </div>
            <div class="am-u-sm-4 b8">
                <h2>数据管理</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                <a href="#${baseUrl}/about/idumaiDatabase.htm" target="_blank" class="btn">&nbsp;</a>
            </div>
        </div>
        <div class="wids2 am-cf">
            <div class="am-u-sm-4 b5">
                <h2>逾期催款</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                <a href="#${baseUrl}/Fk_billAction?command=OverdueQuery&resId=a97c8310-1" class="btn">&nbsp;</a>
            </div>
            <div class="am-u-sm-4 b6">
                <h2>趋势分析</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                 <a href="#${baseUrl}/statistics/statistics.jsp" class="btn">&nbsp;</a>
            </div>
            <div class="am-u-sm-4 b7">
                <h2>系统管理</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                 <a href="${baseUrl}/sysmgr/toUpateUserPwd.do" class="btn">&nbsp;</a>
            </div>
            <div class="am-u-sm-4 b4">
                <h2>产品说明 </h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
                 <a href="${baseUrl}/docs/" class="btn">&nbsp;</a>
            </div>
            <div class="am-u-sm-4 b9">
                <h2>行业资讯</h2>
                <div class="sub"></div>
                <span class="icon">&nbsp;</span>
               	<a href="#${baseUrl}/about/newsList.jsp" class="btn">&nbsp;</a>
            </div>
        </div>
        <a href="javascript:void(0)" id="-x-bar-menuClose" class="menuClose"></a>
    </div>
</section>
<c:set var="superCode" value="<%=com.newdumai.web.util.SystemConst.SUPER_CODE%>"/>
<script type="text/javascript">

	$("#bar-menu").click(function(){
		$("#addItem").animate({ 
			height: 'toggle', opacity: 'toggle' 
		}, "fast"); 
	});
	$("#-x-bar-menuClose").click(function(){
		$("#addItem").animate({ 
			height: 'toggle', opacity: 'toggle' 
		}, "fast");
	});
	
	function logout(){
		location.href="${baseUrl}/logout.do";
	}
	
	function containsValue(arr,e){
		for(var i in arr){
			if(arr[i]===e) return true;
		}
		return false;
	}

	
	var isLeader = "${sessionScope.login.isLeader}";
	var function_settings = {};//默认为空对象
	try{
		function_settings = $.parseJSON('${sessionScope.login.function_settings}');
	}catch(e){
		//无法解析json时忽略
	}
	
	$("#header-title").text($("title").text());
	
	var tabs = ${tabs==null?"[]":tabs};
	
	if(tabs.length==0){
		
		$("#-x-global-header-container").css("height","49px");
		$("#-x-global-tabs-container").css("display","none");
	}
	else {
		
		var tabsContainer = $("#-x-global-tabs");
		
		var pathname = window.location.pathname;
		
		$.each(tabs,function(i){
			
			var thisLeader = this.isLeader;
			if(thisLeader){
				if(!$.isArray(thisLeader)){
					thisLeader = [thisLeader];
				}
				if(!containsValue(thisLeader, isLeader)){
					return true;
				}
			}
			var thisFunction_settings = this.function_settings;
			if(thisFunction_settings){
				if(!$.isArray(thisFunction_settings)){
					thisFunction_settings = [thisFunction_settings];
				}
				
				var flag = false;
				for(var i in thisFunction_settings){
					var key = thisFunction_settings[i];
					if(function_settings[key]!==undefined){
						flag = true;
					}
				}
				
				if(!flag){
					return true;
				}
			}
			
			var href = ("${ctx}/" +this.href).replace(/(\\)+/g,"/").replace(/(\/)+/g,"/");
			pathname = pathname.replace(/(\\)+/g,"/").replace(/(\/)+/g,"/");
			var li = "<li " + (pathname == href || this.active ? "class='am-active'":"") + " ><a href='" + href + "'>"+this.name+"</a></li>";
			tabsContainer.append(li);
		});
		
	}
</script>