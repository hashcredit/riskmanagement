/**
 * Created by Administrator on 2017/5/3 0003.
 */
var managerApp = angular.module('managerApp', []);
managerApp.controller("managerCtrl",["$scope","$http",function ($scope,$http) {
	$scope.username=sessionStorage.username;
    $http.get(contextPath + "/auth/list.do").success(function (data) {
        $scope.managerLists = angular.fromJson(data);
        angular.forEach($scope.managerLists, function (item, index) {
            item.opttime = moment(item.opttime).format('YYYY-MM-DD HH:mm:ss')
        });
    }).error(function(){
    	alert("鉴权数据请求失败");
    });
}]);
