/**
 * Created by Administrator on 2017/4/17 0017.
 */
var myMark = angular.module('myMark', []);
myMark.controller('markControll1',['$scope','$http','$filter',function ($scope,$http,$filter) {
    $scope.users=[{"id":"1", "type":"银行卡实名", "time":"未命中","dh_type": "0","dh_content": "是_否"}, {"id":"2", "type":"房贷", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"3", "type":"银行及非银机构失联名单", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"4", "type":"房贷", "time":"正常" ,"dh_type": "0","dh_content": "男_女"}, {"id":"5", "type":"房贷", "time":"正常","dh_type": "2","dh_content": "是_否"}, {"id":"6", "type":"房贷", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"7", "type":"房贷", "time":"正常","dh_type": "1","dh_content": "是_否"}];
    $scope.reports=[{"id":"1", "name":"手机运营商数据", "time":"未命中"}, {"id":"2","name":"银行卡信息", "time":"正常"}, {"id":"3", "name":"同住人信息", "time":"正常"}, {"id":"4", "name":"涉诉", "time":"正常" }];
    $scope.content="第三节课粉红色肯德基和福克斯静待花开还是客户反馈收到回复束带结发号可是节点恢复空间的首付款 考多少分看电视剧恢复是肯定会福克斯点击回复可接受的客户是否肯定会速度快很快收到货看黄师傅打开 恢复可视电话";
    angular.forEach($scope.users,function (item,index) {
        $scope.phoneAry=item.dh_content.split("_");
        if(item.dh_type == 1){
            item.dh_type="是";
        }else if(item.dh_type == 0){
            item.dh_type="否";
        }
    });
    $scope.reportFlag1=true;$scope.detailFlag=false;$scope.liListFlag1=false;$scope.liListFlag2=false;$scope.liListFlag3=false;$scope.tongzhuFlag=false;$scope.moveFlag=false;$scope.litiFlag=false;$scope.duoFlag=false;
    //贷前报告和贷中报告的开启以及关闭以及里边的详细页的打开关闭
    $scope.baseReport1=function () {
        $scope.reportFlag1=!$scope.reportFlag1;
        angular.forEach($(".phoneCheck .aa"),function (item,index){
            $(item).children(".radioButt").children("label:first-child").addClass("checked").attr("checked","true")
            $(item).children(".radioButt").children("label").click(function(){
                var radioId = $(this).attr('name');
                $(this).addClass('checked').siblings().removeClass("checked");
            });
        })
    };
    $scope.closeReport1=function () {
        $scope.reportFlag1=false;
        $scope.reportFlag2=false;
    };

    //报告页中的加载更多
    $scope.closeLiList1=function (className) {
        $scope.liListFlag1=!$scope.liListFlag1;
        if( $scope.liListFlag1){
            $(className).addClass("off").removeClass("on");
            $(".firstLi1").next().nextAll().slideDown("slow");
        }else{
            $(className).addClass("on").removeClass("off");
            $(".firstLi1").next().nextAll().slideUp("slow");
        }

    };
    $scope.closeLiList2=function (className) {
        $scope.liListFlag2=!$scope.liListFlag2;
        if( $scope.liListFlag2){
            $(className).addClass("off").removeClass("on");
            $(".firstLi2").next().nextAll().slideDown("slow");

        }else{
            $(className).addClass("on").removeClass("off");
            $(".firstLi2").next().nextAll().slideUp("slow");
            // $(".firstLi2").next().css("display","block");
        }

    };
    $scope.closeLiList3=function (className) {
        $scope.liListFlag3=!$scope.liListFlag3;
        if( $scope.liListFlag3){
            $(className).addClass("off").removeClass("on");
            $(".firstLi3").next().nextAll().slideDown("slow");

        }else{
            $(className).addClass("on").removeClass("off");
            $(".firstLi3").next().nextAll().slideUp("slow");
            // $(".firstLi2").next().css("display","block");
        }

    };
    $scope.$watch($scope.value,function () {});
    $scope.score=120;
    //涉诉信息
    $http.get("./data/shesu.json").success(function (data) {
        $scope.litigations=angular.fromJson(data);
        $.each( $scope.litigations,function (index,data) {
        })
    });
}]);