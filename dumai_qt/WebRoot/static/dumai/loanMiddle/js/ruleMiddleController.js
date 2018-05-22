/**
 * 贷中规则设定
 * Created by Administrator on 2017/4/17 0017.
 */
var myRule = angular.module('myRule', []);

myRule.controller('myMenu3',['$scope','$http','$filter',function ($scope,$http,$filter) {
    $scope.users=[{"id":"1", "type":"银行卡实名", "time":"未命中","dh_type": "0","dh_content": "是_否"}, {"id":"2", "type":"房贷", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"3", "type":"银行及非银机构失联名单", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"4", "type":"房贷", "time":"正常" ,"dh_type": "0","dh_content": "男_女"}, {"id":"5", "type":"房贷", "time":"正常","dh_type": "2","dh_content": "是_否"}, {"id":"6", "type":"房贷", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"7", "type":"房贷", "time":"正常","dh_type": "1","dh_content": "是_否"}];
    $scope.reports=[{"id":"1", "name":"手机运营商数据", "time":"未命中"}, {"id":"2","name":"银行卡信息", "time":"正常"}, {"id":"3", "name":"同住人信息", "time":"正常"}, {"id":"4", "name":"涉诉", "time":"正常" }];
    $( "#datepicker" ).datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
        },
        beforeShow:function () {
            $(".markLine").css({"border-right":"none","z-index":"120"});
        },
        dateFormat: 'yy-mm-dd'
    });
    $scope.selectedIndex="credit_before";
    $scope.cutoverTab=function (type) {
        $scope.selectedIndex=type;
    };
    var aryNo=[],aryOk=[],aryRe=[];
    $scope.togflag=false;$scope.togflag1=false;
    $scope.togg=function (className) {
        window.location.href="#homePage";
        $scope.togflag=! $scope.togflag;
        $(className).children("li").slideToggle("slow");
    };
    $scope.togg1=function (className) {
        $scope.togflag1=! $scope.togflag1;
        $(className).children("li").slideToggle("slow");
    };
    $scope.flagNF="ON";
    $scope.$watch($scope.flagNF,function () {
        console.log() ;
    }) ;//这个以后要根据后台给的选择来写
    $(":text").focus( function () {
        $(this).css("color","#fdffff")
    });
    $scope.date=moment(new Date()).format('YYYY-MM-DD');

}]);