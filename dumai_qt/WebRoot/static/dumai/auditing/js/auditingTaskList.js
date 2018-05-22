/**
 * Created by Administrator on 2017/5/3 0003.
 */
var myPhoneApp= angular.module('myPhoneApp', []);
myPhoneApp.controller("myCtrl",["$scope","$http",function ($scope,$http) {
	$scope.username=sessionStorage.username;
     $http({
        method:'POST',
        url:contextPath + "/auditingTask/auditingList.do",
        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function(obj) {
            var str = [];
            for(var p in obj){
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
    }).success(function(data) {
        $scope.phoneLists = angular.fromJson(data.rows);
        angular.forEach($scope.phoneLists,function(item,index){
            item.create_time = moment(item.create_time).format('YYYY-MM-DD')
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
         alert("电核任务列表数据请求失败");
     });

     //跳到电核
     $scope.auditing = function (code,time) {
    	 var myDate = new Date(); 
    	 var time=moment(myDate).format("YYYY-MM-DD HH:mm:ss")
         window.location.href = contextPath + "/auditingTask/toDhTask.do?code="+code+"&opentime="+time;
     }
}]);
