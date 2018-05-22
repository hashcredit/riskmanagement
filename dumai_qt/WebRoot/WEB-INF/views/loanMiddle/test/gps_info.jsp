<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<!DOCTYPE html>
<html ng-app="warnApp">
<head>
	<title>GPS信息</title>
	<link rel="stylesheet" href="${ctx}/static/css/gps/gps_info.css">
</head>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=zmAWIlHnn8qQ4qMKkBGtypeoZio5q86s"></script>
<body>
<div class="trajectory">
    <div class="traje_title">
        <div class="traje_title_up">
            <!--<span>浙A6610W-1</span>-->
            从 <input type="date" class="startTime"> 至 <input type="date" class="endTime">
             <input type="button" value="日期确认" class="queren">
            <input type="button" value="轨迹回放" class="player">
            <input type="button" value="结束回放" class="end">
            速度：慢X1 <input type="range" class="speed"  min="1" max="10" value="5" onchange="change()">快X10
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
<script src="${ctx}/static/script/lib/jquery.min.js"></script>
<script type="text/javascript">
    var map = new BMap.Map("traceMap");
    var point2 = new BMap.Point(116.404, 39.915);
    map.centerAndZoom(point2,12);
    var marker2 = new BMap.Marker(point2);
  	map.addOverlay(marker2);    
  	var opts = {
  		  width : 200,     // 信息窗口宽度
  		  height: 50,     // 信息窗口高度
  		  title : "天安门" , // 信息窗口标题
  		  enableMessage:true,//设置允许信息窗发送短息
  		  message:"这里是北京天安门"
  		};
  		var infoWindow = new BMap.InfoWindow("地址：北京市", opts);  // 创建信息窗口对象 
  		marker2.addEventListener("click", function(){          
  			map.openInfoWindow(infoWindow,point2); //开启信息窗口
  		});
    map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
    var flag=false;
    var clickDis;
    function change(){  //暂无用，后续删除
        flag=true;
        return parseFloat($(".speed").val());
    };
    function uniform() {
        var distance=parseFloat($(".scroll-bg").width());
        return distance;
    }
    function getTrace(data){
    	if(data){
    		var pointArr = [];
            var result=data;
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
            var myIcon = new BMap.Icon("${ctx}/img/car.png", new BMap.Size(70, 32), {
                //offset: new BMap.Size(0, -5),    //相当于CSS精灵
                imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
            });
            $(".scroll-wrap").click(function getDis(){
                map.removeOverlay(carMk);
                map.removeOverlay(carStartMk);
                var e=e||window.event;
                clickDis=parseFloat(e.pageX);
                var eachDis=uniform()/pointArr.length;
                var n=parseInt(clickDis/eachDis);
                $(".scroll-cur").css("left",eachDis*n);
                $(".scroll-up").width(eachDis*n);
                carMk = new BMap.Marker(pointArr[n],{icon:myIcon});
                map.addOverlay(carMk);
               map.centerAndZoom(new BMap.Point(pointArr[n].lng,pointArr[n].lat),20);
            });
            var tem,myP1,myP2,polyline;
            window.traceLine = function (){
                var  str= "";
                for(var i=0;i<pointArr.length;i++){
                    if(i === 0){
                        myP1 = new BMap.Point(pointArr[0].lng,pointArr[0].lat);    //起点
                        myP2 = new BMap.Point(pointArr[1].lng,pointArr[1].lat);
                        str+=(map.getDistance(myP1,myP2)).toFixed(2)+",";
                        var point1 = new BMap.Point(pointArr[0].lng, pointArr[0].lat);
                        marker = new BMap.Marker(point1);
                        // 创建标注
                        map.addOverlay(marker);
                        var label = new BMap.Label("起点",{offset:new BMap.Size(-5,-20)});
                        marker.setLabel(label);
                        tem=myP2;
                    }else if(i>0 && i<pointArr.length-1){
                        myP1=tem;
                        myP2 = new BMap.Point(pointArr[i+1].lng,pointArr[i+1].lat);
                        tem=myP2;
                        str+=(map.getDistance(myP1,myP2)).toFixed(2)+",";
                    }else if(i=pointArr.length-1){
                        myP1=new BMap.Point(pointArr[pointArr.length-2].lng,pointArr[pointArr.length-2].lat);
                        myP2 = new BMap.Point(pointArr[pointArr.length-1].lng,pointArr[pointArr.length-1].lat);
                        var point2 = new BMap.Point(pointArr[pointArr.length-1].lng, pointArr[pointArr.length-1].lat);
                        var marker2 = new BMap.Marker(point2);
                        var ary222=eval((str.split(",")).join('+')+"0").toFixed(2)+"米";
                        var label2 = new BMap.Label("终点 | 总路程是："+ary222,{offset:new BMap.Size(-5,-20)});
                        marker2.setLabel(label2);
                        map.addOverlay(marker2);
                        str+=(map.getDistance(myP1,myP2)).toFixed(2)+",";
                    }
                    //定义折线
                    polyline = new BMap.Polyline([myP1,myP2], {strokeColor:"black", strokeWeight:3, strokeOpacity:0.8});
                    map.addOverlay(polyline);
                }
            };
            traceLine();
            var timer,marker,carMk,pts,carStartMk,speed;
            $(".player").click(function () {
                map.removeOverlay(carMk);
               $(".mark").css("display","block");
                speed=900;
                if(flag){speed=500/$(".speed").val();}
                    map.removeOverlay(carStartMk);
                    window.run = function (){
                        var driving = new BMap.DrivingRoute(map);
                        driving.search(myP1, myP2);
                        driving.setSearchCompleteCallback(function(){
                            pts = pointArr;
                            var paths = pts.length;    //获得有几个点
                            var view = map.getViewport(eval(pts));
                            var mapSize = view.zoom;
                            totalSpeed=uniform()/paths;
                            carMk = new BMap.Marker(pts[0],{icon:myIcon});
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
                    },100);
            });
            $(".end").click(function () {
                $(".mark").css("display","none");
                map.removeOverlay(carMk);
                $(".scroll-cur").css("left",0);
                $(".scroll-up").width("10px");
                carStartMk = new BMap.Marker(pts[0],{icon:myIcon});
                map.addOverlay(carStartMk);
                $(".end").attr("disabled",true).css("background","lightgrey");
                $('.player').attr("disabled",false).css("background","lightblue");
                map.centerAndZoom(new BMap.Point(pointArr[i].lng,pointArr[i].lat),18);
                clearTimeout(timer);
            });
    		
    	}else{
    		alert("暂无相关数据");
    	}
    };
    var startDate=null,endDate=null;
    $(".queren").click(function(){
    	 startDate=$(".startTime").val();
    	 endDate=$(".endTime").val();
    	 $.ajax({
		      type: 'POST',
			  url: "${ctx}/loanMiddle/gps_info.do",
			  data:{ 
				startDate:startDate,
				endDate:endDate
			  },
			  success:getTrace,
			  error:function(error){
				alert("请确认提交日期正确");
			  }
		}); 
    });
    $.ajax('${ctx}/loanMiddle/gps_info.do?code=${param.code}').success(getTrace);
</script>
</body>
</html>
