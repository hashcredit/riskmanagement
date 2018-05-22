/**
 * Created by Administrator on 2017/4/17 0017.
 */
var myIndex = angular.module('myIndex', []);
myIndex.controller('myMenu',['$scope','$http','$filter',function ($scope,$http,$filter) {
    $scope.users=[{"id":"1", "type":"银行卡实名", "time":"未命中","dh_type": "0","dh_content": "是_否"}, {"id":"2", "type":"房贷", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"3", "type":"银行及非银机构失联名单", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"4", "type":"房贷", "time":"正常" ,"dh_type": "0","dh_content": "男_女"}, {"id":"5", "type":"房贷", "time":"正常","dh_type": "2","dh_content": "是_否"}, {"id":"6", "type":"房贷", "time":"正常","dh_type": "0","dh_content": "是_否"}, {"id":"7", "type":"房贷", "time":"正常","dh_type": "1","dh_content": "是_否"}];
    $scope.reports=[{"id":"1", "name":"手机运营商数据", "time":"未命中"}, {"id":"2","name":"银行卡信息", "time":"正常"}, {"id":"3", "name":"同住人信息", "time":"正常"}, {"id":"4", "name":"涉诉", "time":"正常" }];
    $scope.content="第三节课粉红色肯德基和福克斯静待花开还是客户反馈收到回复束带结发号可是节点恢复空间的首付款 考多少分看电视剧恢复是肯定会福克斯点击回复可接受的客户是否肯定会速度快很快收到货看黄师傅打开 恢复可视电话";
    $( "#datepicker" ).datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
            $( "#datepicker" ).val("");
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
        dateFormat: 'yy-mm-dd'
    });
    angular.forEach($scope.users,function (item,index) {
        $scope.phoneAry=item.dh_content.split("_");
        if(item.dh_type == 1){
            item.dh_type="是";
        }else if(item.dh_type == 0){
            item.dh_type="否";
        }
    });
    // 每一页的选项卡事件
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

    }) ;//这个以后要根据后台给的选择来写
    //人员信息
    $http.get("/dumai_qt/loanFront/list.do").success(function (data) {
        $scope.data_members=angular.fromJson(data);
        $.each(data,function (index,data) {
            $scope.every_members=angular.fromJson(data).person; //包括的只是个人的情况，没有日期
            $.each($scope.every_members,function (index,data) {
                if(data.status == 0){
                    aryNo.push(data)
                }else if(data.status == 1){
                    aryOk.push(data)
                }else if(data.status == 2){
                    aryOk.push(data)
                }

                if(data.status1 == 0){
                    data.status1="refuse" ;
                }else if(data.status1 == 1){
                    data.status1="weish" ;
                }

                if(data.status2 == 0){
                    data.status2="attention" ;
                }else if(data.status2 == 1){
                    data.status2="zhengchang" ;
                }
            })

        });

        var result = $filter('filter')(  $scope.data_members,$scope.value);

        $scope.tipErr=function () {
            $scope.n=$(".info_header ul li").length;
            $scope.m=$scope.data_members.length;
            if($(".curDate").siblings().length == $scope.n*$scope.m){
                $scope.tipSelectFlag=true;
            }
        };
        $scope.closeTip=function () {
            $scope.tipSelectFlag=false
        };
        $scope.noNum=aryNo.length;
        $scope.okNum=aryOk.length;
        $scope.reNum=aryRe.length;

    }).error(function () {
        alert("请求失败")
    });
    //文本框聚焦的时候的函数
    $(":text").focus( function () {
        $(this).css("color","#fdffff")
    });
    //日期格式化
    $scope.date=moment(new Date()).format('YYYY-MM-DD');
    $scope.reportFlag1=false;$scope.reportFlag2=false;$scope.detailFlag=false;$scope.liListFlag1=false;$scope.liListFlag2=false;$scope.liListFlag3=false;$scope.tongzhuFlag=false;$scope.moveFlag=false;$scope.litiFlag=false;$scope.duoFlag=false;
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
    $scope.baseReport2=function () {
        $scope.reportFlag2=!$scope.reportFlag2;
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
    $scope.closeReport2=function () {
        $scope.detailFlag=false;//普通样式
    };
    $scope.closeReportMove=function () {//移动运营数据
        $scope.moveFlag=false;
    };
    $scope.closeReportTong=function () {//移动运营数据
        $scope.tongzhuFlag=false; //同住人样式
    };
    $scope.closeReportLiti=function () {//移动运营数据
        $scope.litiFlag=false; //同住人样式
    };
    $scope.closeReportduo=function () {//移动运营数据
        $scope.duoFlag=false; //同住人样式
    };
    $scope.detailReport=function (e) {
        e=window.event||event.which;
        $scope.con=$(e.target).html();
        if($scope.con == "手机运营商数据"){
            $scope.moveFlag=!$scope.moveFlag; //移动运营数据
            // $scope.duoFlag=!$scope.duoFlag;//多条信息
        }else if($scope.con == "银行卡信息"){
            $scope.detailFlag=!$scope.detailFlag;//普通样式
        }else if($scope.con == "同住人信息"){
            $scope.tongzhuFlag=!$scope.tongzhuFlag;  //同住人样式
        }else if($scope.con == "涉诉"){
            $scope.litiFlag=!$scope.litiFlag; //涉诉
        }

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
    $http.get("../static/data/shesu.json").success(function (data) {
        $scope.litigations=angular.fromJson(data);
        $.each( $scope.litigations,function (index,data) {

        })
    });
    //逾期信息
    $scope.overdueFlag=false;$scope.overdue_cui_Flag=false;$scope.overdue_accound_Flag=false;$scope.overdue_shengji_Flag=false;
    $scope.overDue_account=function () {
        $scope.overdue_accound_Flag=true;
        $scope.overdue_cui_Flag=false;
        $scope.overdue_shengji_Flag=false;
        $scope.overdueFlag=true;
        $(".info_content1").css("overflow","hidden");
    };
    $scope.overDue_cui=function () {
        $scope.overdue_accound_Flag=false;
        $scope.overdue_cui_Flag=true;
        $scope.overdue_shengji_Flag=false;
        $(".info_content1").css("overflow","hidden")
    };
    $scope.overDue_sheng=function () {
        $scope.overdue_accound_Flag=false;
        $scope.overdue_cui_Flag=false;
        $scope.overdue_shengji_Flag=true;
        $(".info_content1").css("overflow","hidden")
    };
    $scope.backMain_account=function (){
        $scope.overdue_accound_Flag=false;
        $scope.overdueFlag=false;
        $(".info_content1").css("overflow","scroll");
    };
    $scope.backMain_cui=function (){
        $scope.overdue_cui_Flag=false;
        $scope.overdueFlag=false;
        $(".info_content1").css("overflow","scroll");
    };
    $scope.backMain_sheng=function (){
        $scope.overdue_shengji_Flag=false;
        $scope.overdueFlag=false;
        $(".info_content1").css("overflow","scroll");

    };
}]);