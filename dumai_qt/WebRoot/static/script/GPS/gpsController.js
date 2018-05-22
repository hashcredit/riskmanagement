/**
 * Created by Administrator on 2017/4/11 0011.
 */
var GPSMod = angular.module('GPSMod', ['chieffancypants.loadingBar']);
GPSMod.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
GPSMod.controller('myCtrl',["$scope","$http",function () {
    $(".con_list").click(function (e) {
        e=e||window.event;
        if(e.target.tagName.toUpperCase() == "A"){
            $(e.target).parent().addClass("current").siblings().removeClass("current");
        }
    });
}]);
GPSMod.controller('gpsAlarmCtrl', ["$scope","$http","cfpLoadingBar","$rootScope",function ($scope,$http,cfpLoadingBar,$rootScope) {
	 $(".scroll-cur").css("left",0);
    $scope.start = function() {
        cfpLoadingBar.start();
    };
    $scope.complete = function () {
        cfpLoadingBar.complete();
    };
    cfpLoadingBar.start();
    var mapAlarm = new BMap.Map("posMap");
        $http.get("../loanMiddle/gps_alarm.do" + $rootScope.param).success(function (data){
        	
            if(data){
                $scope.allCarInfos=data;
                $scope.all= $scope.allCarInfos.length+"条";
                var pointAlarm = new BMap.Point($scope.allCarInfos[0].Lng, $scope.allCarInfos[0].Lat);
                var myIconAlarm = new BMap.Icon("../img/car.png", new BMap.Size(70, 32), {
                    imageOffset: new BMap.Size(0, 0)
                });
                var markerAlarm = new BMap.Marker(pointAlarm,{icon:myIconAlarm});
                mapAlarm.addOverlay(markerAlarm);
                mapAlarm.centerAndZoom(pointAlarm, 18);
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
                        $(target).css({'background':"#65bf53","color":"#fff"}).siblings().css({'background':"none","color":"black"});
                        $scope.index=$(target).prevAll().length;
                        $scope.aryDetail= $scope.allCarInfos[$scope.index];

                    };
                    pointAlarm = new BMap.Point($scope.aryDetail.Lng, $scope.aryDetail.Lat);
                    markerAlarm = new BMap.Marker(pointAlarm,{icon:myIconAlarm});
                    mapAlarm.addOverlay(markerAlarm);
                    mapAlarm.centerAndZoom(pointAlarm, 18);
                };
            }else{
                alert("暂未GPS报警数据");
            }
        }).error(function(){
            alert("请求失败");
                var pointAlarm2 = new BMap.Point(116.404, 39.915);
                var markerAlarm2 = new BMap.Marker(pointAlarm2);
                var marker2 = new BMap.Marker(pointAlarm2);
                mapAlarm.centerAndZoom(pointAlarm2,18);
                mapAlarm.addOverlay(markerAlarm2);
        }
        );


}]);
GPSMod.controller('gpsInfoCtrl', ["$scope","$http","cfpLoadingBar","$rootScope",function ($scope,$http,cfpLoadingBar,$rootScope){
	
	$scope.start = function() {
        cfpLoadingBar.start();
    };
    $scope.complete = function () {
        cfpLoadingBar.complete();
    };
    cfpLoadingBar.start();
    $(".end").attr("disabled",true).css("background","lightgrey");
    var map = new BMap.Map("traceMap");
    var pointInfo = new BMap.Point(116.404, 39.915);
    map.centerAndZoom(pointInfo ,12);
    map.enableScrollWheelZoom(true);
    map.clearOverlays();
    var timer,marker,carMk,pts,carStartMk,speed,startDate=null,endDate=null,pointArr=null,result,speed,carMk,carStartMk,carMkOne,carMkMn,startMk,endMk,tem,myP1,myP2,polyline,p2,p1,p3,p4,flag=false,playFlag=false,line;
        startDate=moment().subtract(1,'days').format('YYYY-MM-DD');
        endDate=moment(new Date()).format('YYYY-MM-DD');
        $(".startTime").val(startDate);
        $(".endTime").val(endDate);
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
        flag=false;
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
                    speed=300/$(".speed").val();
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
                        if(i == paths){
                            $('.player').attr("disabled",false).css("background","lightblue");
                            $(".end").attr("disabled",true).css("background","lightgrey");
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
            if(pts){
                carStartMk = new BMap.Marker(pts[0],{icon:myIcon});
                map.addOverlay(carStartMk);
            }
            $(".end").attr("disabled",true).css("background","lightgrey");
            $('.player').attr("disabled",false).css("background","lightblue");
            map.centerAndZoom(new BMap.Point(pointArr[0].lng,pointArr[0].lat),13);
        });
        $(".scroll-wrap").click(function(){
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
            map.centerAndZoom(new BMap.Point(pointArr[n].lng,pointArr[n].lat),16);
        });
        var clickDis;
        function uniform() {
            var distance=parseFloat($(".scroll-bg").width());
            return distance;
        }
        var myIcon = new BMap.Icon("../img/car.png", new BMap.Size(70, 32), {
            imageOffset: new BMap.Size(0, 0),    //图片的偏移量。为了是图片底部中心对准坐标点。
            enableRotation:true,
        });
       
        $http({
            method:'POST',
            url:"../loanMiddle/gps_info.do"+$rootScope.param,
            data:{
                
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
            if(data){
                pointArr = [];
                result=data.slice(0,200);
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

}])