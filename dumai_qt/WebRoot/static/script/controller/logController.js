/**
 * Created by Administrator on 2017/4/18 0018.
 */
var mySystem = angular.module('mySystem', ["ngGrid",'chieffancypants.loadingBar']);
mySystem.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
mySystem.controller('mySystem',['$scope','$http',"cfpLoadingBar",function ($scope,$http,cfpLoadingBar){
	$scope.start = function() {
        cfpLoadingBar.start();
    };
    $scope.complete = function () {
        cfpLoadingBar.complete();
    };
    cfpLoadingBar.start();
    $scope.selectedIndex="setup";$scope.addFlag=false;$scope.addUserFlag=false;
    $scope.cutoverTab=function (type) {
        $scope.selectedIndex=type;
    };
    $scope.addIP=function(){
        $scope.addFlag=!$scope.addFlag;
    };
    $scope.addGrid=function () {
        $scope.addUserFlag=true;
    };
    $scope.close=function () {
        $scope.addUserFlag=false;
    };
    $('.box1').click(function(){
        $(this).addClass('checked').siblings().removeClass("checked");
    });
    $scope.checkFlag1=true;$scope.checkFlag2=false;$scope.checkFlag3=false;
    $scope.change1=function (){
      console.log($scope.checkFlag1)
      $scope.checkFlag1=!$scope.checkFlag1;
  }
    $scope.change2=function (){
     console.log($scope.checkFlag2)
        $scope.checkFlag2=!$scope.checkFlag2;
    }
    $scope.change3=function (){
        console.log($scope.checkFlag3)
        $scope.checkFlag3=!$scope.checkFlag3;
    }
}]);

mySystem.controller('logGridCtrl',['$scope','$http',function ($scope,$http) {
    $http.get("/dumai_qt/sysmgr/logList.do").success(function (data) {
        $scope.customs=angular.fromJson(data).rows;
    });
    $scope.gridOptions={
        data:'customs',
        columnDefs:[
            {
                field:'id',
                displayName:'序号'
            },
            {
                field:'user_name',//每一列的属性名
                displayName:'用户名'
            },
            {
                field:'user_surname',
                displayName:'姓名'
            },
            {
                field:'logtime',
                displayName:'记录时间'
            },
            {
                field:'ipadress',
                displayName:'IP地址'
            },
            {
                field:'content',
                displayName:'操作内容'
            }
        ]
    }
}]);