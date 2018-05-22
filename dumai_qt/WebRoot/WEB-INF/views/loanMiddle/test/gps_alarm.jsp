<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html ng-app="warnApp">
	<head>
	<title>GPS报警</title>
	<link rel="stylesheet" href="${ctx}/static/css/gps/gps.css">
	 <link rel="stylesheet" href="${ctx}/static/css/loading-bar.css">
	</head>
	<script src="http://api.map.baidu.com/api?v=2.0&ak=zmAWIlHnn8qQ4qMKkBGtypeoZio5q86s" type="text/javascript"></script>
	<body>
	<div id="loading-bar-container"></div>
 <div class="content" ng-controller="warnCtrl">
    <h4 class="header">GPS</h4>
    <ul class="con_list">
        <li class="current">GPS报警</li>
        <li>GPS信息</li>
    </ul>
    <div class="con_info ">
        <div class="con_block">
            <div class="messbox" >
                <div class="warntitle">
                 <!--    <h4>19条 <span>未解除14条</span></h4> -->
                    <!-- <div class="warntitleRight">
                        <input type="checkbox" checked>
                        <span>开启报警声音</span>
                    </div> -->
                </div>
                <div class="warnContent">
                    <div class="title"><span>报警时间</span><span>报警类型</span><span style="width: 60%">报警位置</span></div>
                    <ul class="selectLi">
                        <li ng-repeat="allCarInfo in allCarInfos"  ng-click="selectLi()"><span ng-bind="allCarInfo.Time"></span><span ng-bind="allCarInfo.AlarmType"></span><span style="width: 50%" ng-bind="allCarInfo.Location"></span><i style="display: none" ng-bind="allCarInfo"></i></li>
                    </ul>
                    <div class="right">
                        <div class="rightBottom">
                            <div id="posMap" style="width:100%;height: 100%;border: 1px solid;z-index:1000" ></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="con_block ">
            <div class="trajectory">
                <div class="traje_title">
                    <div class="traje_title_up">
                        <!--<span>浙A6610W-1</span>-->
                        从 <input type="date" class="startTime"> 至 <input type="date" class="endTime">
                         <input type="button" value="日期确认" class="queren">
                        <input type="button" value="轨迹回放" class="player">
                        <input type="button" value="结束回放" class="end">
                        速度：慢X1 <input type="range" class="speed"  min="1" max="30" value="15" >快X30
                        <label>停留标识
                            <select name="time" id="stayTime">
                                <option value="5分钟">5分钟</option>
                                <option value="15分钟">15分钟</option>
                                <option value="30分钟">30分钟</option>
                                <option value="1小时">1小时</option>
                                <option value="6小时">6小时</option>
                                <option value="12小时">12小时</option>
                                <option value="1天">1天</option>
                            </select>
                        </label>
                        <!--<label>-->
                        <!--<input type="checkbox" value="pos">敏感区域[公]-->
                        <!--<input type="checkbox" value="pos">敏感区域[私]-->
                        <!--<input type="checkbox" value="pos">常用区域-->
                        <!--</label>-->
                        <!--<label style="float: right;margin-right: 20px">-->
                        <!--<input type="button" value="明细">-->
                        <!--</label>-->
                    </div>
                    <div class="traje_title_down">
                        <div class="scroll-wrap">
                            <div class="scroll-bg fl" id="scrollBg">
                                <div id="scroll-green" class="scroll-up" >
                                    <div class="scroll-cur fr" id="scrollHandle"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mark"></div>
                </div>
                <div id="traceMap"></div>
            </div>
        </div>
    </div>
</div>
    <script src="${ctx}/static/script/lib/jquery.min.js"></script>
	<script src="${ctx}/static/script/lib/angular.min.js"></script>
	  <script src="${ctx}/static/script/lib/loading-bar.js" type="text/javascript" charset="utf-8"></script>
	    <script src="${ctx}/static/script/lib/moment.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
    var warnApp = angular.module('warnApp', ['chieffancypants.loadingBar']);
    warnApp.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
        cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
    }]);
    warnApp.controller("warnCtrl",function($scope,$http,cfpLoadingBar){
    	$scope.start = function() {
            cfpLoadingBar.start();
        };
        $scope.complete = function () {
            cfpLoadingBar.complete();
        };
        cfpLoadingBar.start();
        var mapAlarm = new BMap.Map("posMap");
        var pointAlarm2 = new BMap.Point(116.404, 39.915);
        var markerAlarm2 = new BMap.Marker(pointAlarm2);
        var marker2 = new BMap.Marker(pointAlarm2);
        mapAlarm.centerAndZoom(pointAlarm2,12);
        mapAlarm.addOverlay(markerAlarm2);
        function loadJScript1() {
        	  var script = document.createElement("script");
              script.type = "text/javascript";
              script.src = "http://api.map.baidu.com/api?v=2.0&ak=zmAWIlHnn8qQ4qMKkBGtypeoZio5q86s&callback=init1";
              document.body.appendChild(script);
        }
        function init1() {
        	 var mapAlarm = new BMap.Map("posMap");
             var pointAlarm2 = new BMap.Point(116.404, 39.915);
             var markerAlarm2 = new BMap.Marker(pointAlarm2);
             var marker2 = new BMap.Marker(pointAlarm2);
             mapAlarm.centerAndZoom(pointAlarm2,12);
             mapAlarm.addOverlay(markerAlarm2);
             map.enableScrollWheelZoom();      
        };
        function loadJScript2() {
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = "http://api.map.baidu.com/api?v=2.0&ak=zmAWIlHnn8qQ4qMKkBGtypeoZio5q86s&callback=init2";
            document.body.appendChild(script);
            return function gps_info() {
                var map = new BMap.Map("traceMap");
                var pointArr=null;
                var result,speed;
                var carMk,carStartMk,carMkOne,carMkMn,startMk,endMk;
                var tem,myP1,myP2,polyline,p2,p1,p3,p4;
                var flag=false,playFlag=false;
                var line;
                function traceLine(){
                    var points = [],str="";
                    for(var j=0;j<pointArr.length;j++){
                        var point1 = new BMap.Point(pointArr[0].lng, pointArr[0].lat);
                        var point2 = new BMap.Point(pointArr[pointArr.length-1].lng, pointArr[pointArr.length-1].lat);
                        startMk = new BMap.Marker(point1);
                        endMk = new BMap.Marker(point2);
                        map.addOverlay(endMk);
                        map.addOverlay(startMk);
                        var label = new BMap.Label("起点",{offset:new BMap.Size(-5,-20)});
                        startMk.setLabel(label);
                        if(j >= pointArr.length-1){
                            p1 =p3 = pointArr[j].lng;
                            p2 = p4 = pointArr[j].lat;
                        }else{
                            p1 = pointArr[j].lng;
                            p2 = pointArr[j].lat;
                            p3 = pointArr[j+1].lng;
                            p4 = pointArr[j+1].lat;
                        }
                        points.push(new BMap.Point(p1,p2));
                        myP1 = new BMap.Point(p1,p2);    //起点
                        myP2 = new BMap.Point(p3,p4);
                        str+=map.getDistance(myP1,myP2).toFixed(2)+",";
                    }
                    var dis=eval((str.split(",")).join('+')+"0").toFixed(2)+"米";
                    var label2 = new BMap.Label("终点 | 总路程是："+dis,{offset:new BMap.Size(-5,-20)});
                    endMk.setLabel(label2);
                    line = new BMap.Polyline(points,{strokeWeight:3,strokeColor:"black",strokeOpacity:0.5});
                    map.addOverlay(line);
                };
                $(".player").click(function () {
                    map.clearOverlays(line);
                    timer1=null, timer3=null, timer3=null,driving=null,paths=null;
                    playFlag=true;
                    traceLine();
                    map.removeOverlay(carMk);
                    map.removeOverlay(carMkOne);
                    map.removeOverlay(carMkMn);
                    $(".mark").css("display","block");
                    if(!flag){
                        if(pointArr.length<100){
                            speed=800/$(".speed").val();
                        }else if(pointArr.length>100&&pointArr.length<200){
                            speed=700/$(".speed").val();
                        }else if(pointArr.length>200&&pointArr.length<400){
                            speed=500/$(".speed").val();
                        }else if(pointArr.length>400){
                            speed=400/$(".speed").val();
                        }
                    }
                    map.removeOverlay(carStartMk);
                    window.run = function (){
                        var driving = new BMap.DrivingRoute(map);
                        driving.search(myP1, myP2);
                        driving.setSearchCompleteCallback(function(){
                            pts = pointArr;
                            var paths = pts.length;    //获得有几个点
                            /*  var view = map.getViewport(eval(pts));
                             var mapSize = view.zoom; */
                            totalSpeed=uniform()/paths;
                            carMk = new BMap.Marker(pts[0],{icon:myIcon});
                            carMk.setRotation(-50);
                            map.addOverlay(carMk);
                            i=0;
                            function resetMkPoint(i){
                                carMk.setPosition(pts[i]);
                                if(i < paths){
                                    timer=setTimeout(function(){
                                        i++;
                                        resetMkPoint(i);
                                        if($(".scroll-cur")){
                                            if(i*totalSpeed == uniform()){
                                                $(".scroll-cur").css("left",uniform()-10);
                                            }else{
                                                $(".scroll-cur").css("left",i*totalSpeed);
                                            }
                                            $(".scroll-up").width(i*totalSpeed);
                                        }else {
                                            return;
                                        }

                                        map.centerAndZoom(new BMap.Point(pointArr[i-1].lng,pointArr[i-1].lat),18);
                                    },speed);
                                }

                            }
                            timer=setTimeout(function(){
                                resetMkPoint(0);
                            },speed);
                        });
                    };
                    $('.player').attr("disabled",true).css("background","lightgrey");
                    $(".end").attr("disabled",false).css("background","lightblue");
                    timer=setTimeout(function(){
                        run();
                    },30);
                });
                $(".end").click(function () {
                    window.run=null;
                    clearTimeout(timer);
                    map.removeOverlay(carMk);
                    map.removeOverlay(carStartMk);
                    map.removeOverlay(carMkOne);
                    map.removeOverlay(carMkMn);
                    $(".mark").css("display","none");
                    $(".scroll-cur").css("left",0);
                    $(".scroll-up").width("10px");
                    carStartMk = new BMap.Marker(pts[0],{icon:myIcon});
                    map.addOverlay(carStartMk);
                    $(".end").attr("disabled",true).css("background","lightgrey");
                    $('.player').attr("disabled",false).css("background","lightblue");
                    map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),13);
                });
                $(".scroll-wrap").click(function getDis(){
                    if(!playFlag){
                        traceLine();
                    }
                    map.removeOverlay(carMk);
                    map.removeOverlay(carStartMk);
                    map.removeOverlay(carMkMn);
                    map.removeOverlay(carMkOne);
                    var e=e||window.event;
                    clickDis=parseFloat(e.pageX);
                    var eachDis=uniform()/pointArr.length;
                    var n=parseInt(clickDis/eachDis);
                    $(".scroll-cur").css("left",eachDis*n);
                    $(".scroll-up").width(eachDis*n);
                    carMkMn = new BMap.Marker(pointArr[n],{icon:myIcon});
                    carMkMn.setRotation(-50);
                    carMkMn.enableRotation="true";
                    map.addOverlay(carMkMn);
                    map.centerAndZoom(new BMap.Point(pointArr[n].lng,pointArr[n].lat),20);
                });
                var clickDis;
                function uniform() {
                    var distance=parseFloat($(".scroll-bg").width());
                    return distance;
                }
                var myIcon = new BMap.Icon("images/car.png", new BMap.Size(70, 32), {
                    imageOffset: new BMap.Size(0, 0),    //图片的偏移量。为了是图片底部中心对准坐标点。
                    enableRotation:true,
                });
                return function () {
                    console.log(map)
                    $(".scroll-cur").css("left",0);
                    try{
                        map.removeOverlay(carMk);
                        map.removeOverlay(carStartMk);
                        map.removeOverlay(carMkMn);
                        $(".mark").css("display","none");
                        $(".scroll-cur").css("left",0);
                        $(".scroll-up").width("10px");
                        carStartMk = new BMap.Marker(pts[0],{icon:myIcon});
                        map.addOverlay(carStartMk);
                        $(".end").attr("disabled",true).css("background","lightgrey");
                        $('.player').attr("disabled",false).css("background","lightblue");
                        map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),18);
                        clearTimeout(timer);
                    }catch(e){};
                    $http({
                        method:'POST',
                        url:"data/route.json",
                        data:{
                            code:"${param.code}",
                            startDate:startDate,
                            endDate:endDate
                        },
                        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
                        transformRequest: function(obj) {
                            var str = [];
                            for(var p in obj){
                                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                            }
                            return str.join("&");
                        }
                    }).success(function (data) {
                        map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
                        flag=false;
                        if(data){
                            pointArr = [];
                            result=data;
                            $.each(result,function (index,value) {
                                var x = value["Lng"];
                                var y = value["Lat"];
                                var ggPoint = new BMap.Point(x,y);
                                pointArr.push(ggPoint);
                                map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),12);
                                map.addControl(new BMap.NavigationControl());
                                map.enableScrollWheelZoom();//启用滚轮放大缩小，默认禁用
                                map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
                            });
                            if(!carMkOne){
                                carMkOne = new BMap.Marker(pointArr[0],{icon:myIcon});
                                carMkOne.setRotation(-50);
                                map.addOverlay(carMkOne);
                            }
                        }else{
                            var pointInfo = new BMap.Point(116.404, 39.915);
                            var markerInfo = new BMap.Marker(pointInfo);
                            map.addOverlay(markerInfo);
                            map.centerAndZoom(pointInfo ,12);
                            alert("暂无相关GPS数据");
                        }
                    }).error(function(){
                        var pointInfo = new BMap.Point(116.404, 39.915);
                        var markerInfo = new BMap.Marker(pointInfo);
                        map.addOverlay(markerInfo);
                        map.centerAndZoom(pointInfo ,12);
                        alert("请求失败");
                    });
                };
            };
        }
        function init2() {
            var map = new BMap.Map("traceMap");
            var pointArr=null;
            var result,speed;
            var carMk,carStartMk,carMkOne,carMkMn,startMk,endMk;
            var tem,myP1,myP2,polyline,p2,p1,p3,p4;
            map.enableScrollWheelZoom();                 //启用滚轮放大缩小
        }
       
        firstLi();
        function firstLi() {
        	var markerAlarm;
            $http.get("${ctx}/loanMiddle/gps_alarm.do?code=${param.code}").success(function (data){
                if(data){
                    $scope.allCarInfos=data;
                    $scope.all= $scope.allCarInfos.length+"条";
                    var pointAlarm = new BMap.Point($scope.allCarInfos[0].Lng, $scope.allCarInfos[0].Lat);
                    mapAlarm.centerAndZoom(pointAlarm, 11);
                    var myIconAlarm = new BMap.Icon("${ctx}/img/car.png", new BMap.Size(70, 32), {
                        imageOffset: new BMap.Size(0, 0)
                    });
                    mapAlarm.addControl(new BMap.MapTypeControl());
                    mapAlarm.enableScrollWheelZoom(true);
                    $scope.selectLi=function (e) {
                    	mapAlarm.removeOverlay(markerAlarm);
                        e=e||window.event;
                        if(e.target.tagName.toUpperCase() == "LI"){
                            $(e.target).css({'background':"#65bf53","color":"#fff"}).siblings().css({'background':"none","color":"black"});
                            $scope.index=$(target).prevAll().length;
                            $scope.aryDetail= $scope.allCarInfos[$scope.index];
                        }else if(e.target.tagName.toUpperCase() == "SPAN"){
                            var target=$(e.target).parent();
                            $(target).css({'background':"#65bf53","color":"#fff"}).siblings().css({'background':"none","color":"black"})
                            $scope.index=$(target).prevAll().length;
                            $scope.aryDetail= $scope.allCarInfos[$scope.index];
                        };
                        pointAlarm = new BMap.Point($scope.aryDetail.Lng, $scope.aryDetail.Lat);
                        markerAlarm = new BMap.Marker(pointAlarm,{icon:myIconAlarm});
                        mapAlarm.addOverlay(markerAlarm);
                        mapAlarm.centerAndZoom(pointAlarm, 11);
                    };
                }else{
                    alert("暂未GPS报警数据");
                }
            }).error(function(){alert("请求失败");});
        }
        var timer,marker,carMk,pts,carStartMk,speed;
        var startDate=null,endDate=null;
        var $div_li = $(".con_list li");
        var gps_info_handle=gps_info();
        $div_li.click(function(){
            $(this).addClass('current').siblings().removeClass('current');
            var index =  $div_li.index(this);
            if($(this).html() == "GPS报警"){
            	 loadJScript1();
            	 firstLi();
            } else if($(this).html() == "GPS信息"){
            	loadJScript2()
            	$(".end").attr("disabled",true);
            	$(".queren").click(function(){
                    startDate=$(".startTime").val();
                    endDate=$(".endTime").val();
                    $http({
                    	method: 'POST',
                        url: "${ctx}/loanMiddle/gps_info.do",
                        data:{
                        	 code:"${param.code}",
                            startDate:startDate,
                            endDate:endDate
                        },
                        headers:{'Content-Type': 'application/x-www-form-urlencoded'},  
                        transformRequest: function(obj) {  
                          var str = [];  
                          for(var p in obj){  
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));  
                          }  
                          return str.join("&");  
                        }  
                    }).success(gps_info_handle()).error(function(error){
                        alert("请确认提交日期正确");
                    });
                });
            		gps_info_handle();
            }
            $(".con_info .con_block").eq(index).css("display","block").siblings().css("display","none");
            
        });
        startDate=moment().subtract(1,'days').format('YYYY-MM-DD');
        endDate=moment(new Date()).format('YYYY-MM-DD');
        $(".startTime").val(startDate);
        $(".endTime").val(endDate);
        function gps_info() {
            var map = new BMap.Map("traceMap");
            var pointArr=null;
            var result,speed;
            var carMk,carStartMk,carMkOne,carMkMn,startMk,endMk;
            var tem,myP1,myP2,polyline,p2,p1,p3,p4;
            var flag=false,playFlag=false;
            var line;
            map.clearOverlays();
            function traceLine(){
               var points = [],str="";
               for(var j=0;j<pointArr.length;j++){
            	   var point1 = new BMap.Point(pointArr[0].lng, pointArr[0].lat);
            	   var point2 = new BMap.Point(pointArr[pointArr.length-1].lng, pointArr[pointArr.length-1].lat);
            	   startMk = new BMap.Marker(point1);
                   endMk = new BMap.Marker(point2);
                   map.addOverlay(endMk);
                   map.addOverlay(startMk);
                   var label = new BMap.Label("起点",{offset:new BMap.Size(-5,-20)});
                   startMk.setLabel(label);
            	   if(j >= pointArr.length-1){
            		   p1 =p3 = pointArr[j].lng;
            		   p2 = p4 = pointArr[j].lat;  
            	   }else{
            		  p1 = pointArr[j].lng;
                      p2 = pointArr[j].lat;
                      p3 = pointArr[j+1].lng;
                      p4 = pointArr[j+1].lat;
            	   }
                    points.push(new BMap.Point(p1,p2));
                    myP1 = new BMap.Point(p1,p2);    //起点
                    myP2 = new BMap.Point(p3,p4);
                    str+=map.getDistance(myP1,myP2).toFixed(2)+",";
                }
               var dis=eval((str.split(",")).join('+')+"0").toFixed(2)+"米";
               var label2 = new BMap.Label("终点 | 总路程是："+dis,{offset:new BMap.Size(-5,-20)});
               endMk.setLabel(label2);
               line = new BMap.Polyline(points,{strokeWeight:3,strokeColor:"black",strokeOpacity:0.5});
               map.addOverlay(line);
            };
            $(".player").click(function () {
            	map.clearOverlays(line);
            	 timer1=null, timer3=null, timer3=null,driving=null,paths=null;
            	playFlag=true;
                traceLine();
                map.removeOverlay(carMk);
                map.removeOverlay(carMkOne);
                map.removeOverlay(carMkMn);
                $(".mark").css("display","block");
                if(!flag){
                	if(pointArr.length<100){
                		speed=800/$(".speed").val();
                	}else if(pointArr.length>100&&pointArr.length<200){
                		speed=700/$(".speed").val();
                	}else if(pointArr.length>200&&pointArr.length<400){
                		speed=500/$(".speed").val();
                	}else if(pointArr.length>400){
                		speed=400/$(".speed").val();
                	}
                }
                map.removeOverlay(carStartMk);
                window.run = function (){
                     var driving = new BMap.DrivingRoute(map);
                     driving.search(myP1, myP2); 
                     driving.setSearchCompleteCallback(function(){
                        pts = pointArr;
                        var paths = pts.length;    //获得有几个点
                       /*  var view = map.getViewport(eval(pts));
                        var mapSize = view.zoom; */
                        totalSpeed=uniform()/paths;
                        carMk = new BMap.Marker(pts[0],{icon:myIcon});
                        carMk.setRotation(-50);
                        map.addOverlay(carMk);
                        i=0;
                        function resetMkPoint(i){
                            carMk.setPosition(pts[i]);
                            if(i < paths){
                               timer=setTimeout(function(){
                                    i++;
                                    resetMkPoint(i);
                                    if($(".scroll-cur")){
                                        if(i*totalSpeed == uniform()){
                                            $(".scroll-cur").css("left",uniform()-10);
                                        }else{
                                            $(".scroll-cur").css("left",i*totalSpeed);
                                        }
                                        $(".scroll-up").width(i*totalSpeed);
                                    }else {
                                        return;
                                    }
                                   
                                    map.centerAndZoom(new BMap.Point(pointArr[i-1].lng,pointArr[i-1].lat),18);
                                },speed);
                            }
                          
                        }
                          timer=setTimeout(function(){
                            resetMkPoint(0);
                        },speed);
                    });
                };
                $('.player').attr("disabled",true).css("background","lightgrey");
                $(".end").attr("disabled",false).css("background","lightblue");
                timer=setTimeout(function(){
                    run();
                },30);
            });
            $(".end").click(function () {
            	 window.run=null;
            	 clearTimeout(timer);
            	 map.removeOverlay(carMk);
            	 map.removeOverlay(carStartMk);
            	 map.removeOverlay(carMkOne);
            	 map.removeOverlay(carMkMn);
                $(".mark").css("display","none");
                $(".scroll-cur").css("left",0);
                $(".scroll-up").width("10px");
                carStartMk = new BMap.Marker(pts[0],{icon:myIcon});
                map.addOverlay(carStartMk);
                $(".end").attr("disabled",true).css("background","lightgrey");
                $('.player').attr("disabled",false).css("background","lightblue");
                map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),13);
            });
            $(".scroll-wrap").click(function getDis(){
            	if(!playFlag){
            		traceLine();
            	}
                map.removeOverlay(carMk);
                map.removeOverlay(carStartMk);
                map.removeOverlay(carMkMn);
                map.removeOverlay(carMkOne);
                var e=e||window.event;
                clickDis=parseFloat(e.pageX);
                var eachDis=uniform()/pointArr.length;
                var n=parseInt(clickDis/eachDis);
                $(".scroll-cur").css("left",eachDis*n);
                $(".scroll-up").width(eachDis*n);
                carMkMn = new BMap.Marker(pointArr[n],{icon:myIcon});
                carMkMn.setRotation(-50);
                carMkMn.enableRotation="true";
                map.addOverlay(carMkMn);
                map.centerAndZoom(new BMap.Point(pointArr[n].lng,pointArr[n].lat),20);
            });
            var clickDis;
            function uniform() {
                var distance=parseFloat($(".scroll-bg").width());
                return distance;
            }
            var myIcon = new BMap.Icon("${ctx}/img/car.png", new BMap.Size(70, 32), {
                imageOffset: new BMap.Size(0, 0),    //图片的偏移量。为了是图片底部中心对准坐标点。
                enableRotation:true,
            });
            return function () {
               $(".scroll-cur").css("left",0);
               try{
            	 map.removeOverlay(carMk);
              	 map.removeOverlay(carStartMk);
              	 map.removeOverlay(carMkMn);
                  $(".mark").css("display","none");
                  $(".scroll-cur").css("left",0);
                  $(".scroll-up").width("10px");
                  carStartMk = new BMap.Marker(pts[0],{icon:myIcon});
                  map.addOverlay(carStartMk);
                  $(".end").attr("disabled",true).css("background","lightgrey");
                  $('.player').attr("disabled",false).css("background","lightblue");
                  map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),18);
                  clearTimeout(timer);
               }catch(e){};
            
               $http({
            	   method:'POST',
            	   url:"${ctx}/loanMiddle/gps_info.do",
            	   data:{
            		   code:"${param.code}",
                       startDate:startDate,
                       endDate:endDate 
                   },
                   headers:{'Content-Type': 'application/x-www-form-urlencoded'},  
                   transformRequest: function(obj) {  
                     var str = [];  
                     for(var p in obj){  
                       str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));  
                     }  
                     return str.join("&");  
                   }  
               }).success(function (data) {
            	   console.log(data)
                    map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
                    flag=false;
                    if(data){
                         pointArr = [];
                         result=data;
                        $.each(result,function (index,value) {
                            var x = value["Lng"];
                            var y = value["Lat"];
                            var ggPoint = new BMap.Point(x,y);
                            pointArr.push(ggPoint);
                            console.log("===========")
                            console.log(pointArr[0].lng,pointArr[0].lat)
                            map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),12);
                            map.addControl(new BMap.NavigationControl());
                            map.enableScrollWheelZoom();//启用滚轮放大缩小，默认禁用
                            map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
                        });
                        if(!carMkOne){
                        	carMkOne = new BMap.Marker(pointArr[0],{icon:myIcon});
                            carMkOne.setRotation(-50);
                            map.addOverlay(carMkOne);
                        }
                    }else{
                    	var pointInfo = new BMap.Point(116.404, 39.915);
                        var markerInfo = new BMap.Marker(pointInfo);
                        map.addOverlay(markerInfo);
                        map.centerAndZoom(pointInfo ,12);
                        alert("暂无相关GPS数据");
                    }
                }).error(function(){
                	var pointInfo = new BMap.Point(116.404, 39.915);
                    var markerInfo = new BMap.Marker(pointInfo);
                    map.addOverlay(markerInfo);
                    map.centerAndZoom(pointInfo ,12);
                	alert("请求失败");
                });
            };
        }
    });
	</script>
	</body>
</html>
