/**
 * 电核员-分配电核任务
 * Created by Administrator on 2017/5/3 0003.
 */
var managerApp = angular.module('managerApp', ['ui.bootstrap']);
managerApp.controller("managerCtrl",["$scope","$http",function ($scope,$http) {
    $scope.username=sessionStorage.username;
    console.log("=====");
    console.log($scope.userNameht);
    $( "#datepicker1" ).datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
        },
        beforeShow:function () {
            $(".markLine").css({"border-right":"none","z-index":"120"});
        },
        onSelect:function () {
        },
        dateFormat: 'yy-mm-dd'
    });
    $( "#datepicker2" ).datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
        },
        beforeShow:function () {
            $(".markLine").css({"border-right":"none","z-index":"120"});
        },
        onSelect:function () {
        },
        dateFormat: 'yy-mm-dd'
    });
//分配列表

    $scope.currentPage = 1;
    $scope.maxSize = 7;   
    getPageList($scope.currentPage,"20")
    function getPageList(page,rows){
        $http({
            method: 'POST',
            url: contextPath + "/auditingTask/auditingTaskList.do",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: {
            	dist_status: '0',
            	page:page,
            	rows:rows
            },
            transformRequest: function (obj) {
                 str = [];
                for (var p in obj) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            }
        }).success(function (data) {
            $scope.managerLists = angular.fromJson(data.rows);
             $scope.totalItems=data.total;
            angular.forEach($scope.managerLists, function (item, index) {
                item.create_time = moment(item.create_time).format('YYYY-MM-DD HH:mm:ss')
                if (item.status == "0") {
                    item.status = "待审核";
                } else if (item.status == "1") {
                    item.status = "通过";
                } else if (item.status == "2") {
                    item.status = "拒绝";
                } else if (item.status == "3") {
                    item.status = "处理中";
                }
            });
        }).error(function(){
        	alert('分配电核任务数据请求失败');
        })
    }

    $scope.getPageLists=function(pageNum){
    	getPageList($scope.currentPage,"20")
    }
    
// 下拉列表请求；
     $http({
        method: 'POST',
        url: contextPath + "/auditingTask/getDhy.do",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function (obj) {
            var str = [];
            for (var p in obj) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
      }).success(function (data) {
    	 $scope.distributionLists=data;
    	$scope.disValue=$scope.distributionLists[0].code;
      }).error(function(){
    	alert('查询条件数据请求失败');
     })
  
//   多选框的选择操作
    $scope.change=function ($event){
    	if($($event.target).hasClass("checked")){
    		  $($event.target).removeClass("checked ")
    	}else{
    		 $($event.target).addClass("checked ")
    	}
    }
    
//    分配任务操作
    $scope.allocationTask=function(){
    	$scope.taskLists=[];var a=0;
    	angular.forEach($(".disList"),function(item,index){
    		if($(item).hasClass("checked")){
    			a=1;
    			$scope.taskLists.push($(item).children().val())
    		}
    	})
    	if(a == 0){
    		$(".allocationTip").css("display","block");
    	}else{
        	$http({
                method: 'POST',
                url: contextPath + "/auditingTask/saveTaskUser.do",
                data:{
                	sys_user_code:$scope.disValue,
                	taskDatas:$scope.taskLists
                },
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
              }).success(function (data) {
              		if(data.message == "成功"){
              		 $(".allocationOkTip").css("display","block");
              		}
              }).error(function(){
            	alert('分配失败');
             })	
    	}
    }
    $scope.tipclose=function(){
    	$(".allocationTip").css("display","none");
    };
    $scope.tipOkclose=function(){
    	window.location.reload();
    };
}]);
