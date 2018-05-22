/**
 * Created by Administrator on 2017/1/4 0004.
 */
define(['jquery',"angular","angular-route","angular-ui-router","ng-grid"],function ($,angular) {
    var ruleCtrlMod = angular.module('ruleCtrlMod', ['ngGrid']);
    ruleCtrlMod.controller('loginCtrl',['$scope','$http','$state','$interval','$timeout',function ($scope,$http,$state,$interval,$timeout) {
        function yzm() {
            $scope.num1 = Math.floor(Math.random() * 10);
            $scope.num2 = Math.floor(Math.random() * 10);
            $scope.ary=["+","-","*","/"];
            $scope.fuhao=$scope.ary[Math.floor(Math.random() * 3)];
            $scope.code=eval($scope.num1+''+$scope.fuhao+''+$scope.num2);
        }
        yzm();
        $scope.changeYzm=function () {
            yzm()
        };
        $(".yam_num").blur(function(){
            if($scope.code !== $scope.value){
                $(".tip_error2").css("z-index","120");
                $(".close").click(function () {
                    $(".tip_error2").css("z-index","-1")
                })
            };
        });

        $scope.login=function () {
            if(!$scope.user_name ||!$scope.user_password){
                $(".tip_error3").css("z-index","120");
                $(".close").click(function () {
                    $(".tip_error3").css("z-index","-1")
                })
            }
            $scope.userInfo={
                name:$scope.user_name,
                password:$scope.user_password
            };

            $(".circle").css({"transform":"rotate(360deg)","transition": "all 3s cubic-bezier(.11,.81,.92,.21) 0s "});
            $http.get('./data/user.json').success(function (data) {
                $scope.user = angular.fromJson(data);
                angular.forEach($scope.user,function (item) {
                    if(item.password !==$scope.userInfo.password){
                        $(".tip_error").css("z-index","120");
                        $(".close").click(function () {
                            $(".tip_error").css("z-index","-1")
                        })
                    }else if($scope.value !== 0 && !$scope.value){
                        alert("请输入验证码")
                    }
                    else if(item.email==$scope.userInfo.name&&item.password==$scope.userInfo.password&&$scope.code == $scope.value){
                            $(".allContent").fadeOut('slow',function () {
                                window.location.href="#homePage";
                            });
                    }else{
                        $(".circle").css({"transform":"none","transition": "none "});
                        alert('请先注册')
                    }

                })
            });
        };
        $scope.myKeyup = function(e){
            //IE 编码包含在window.event.keyCode中，Firefox或Safari 包含在event.which中
            var keycode = window.event?e.keyCode:e.which;
            if(keycode==13){
                $scope.login();
            }
        };
        var flag=false;
        $(":text").focus( function () {
         $(this).css("color","#fdffff")
        });
        $scope.extra=function () {
           flag=!flag;
           if(flag){
               $(".extra img").css("display","none");
           }else{
               $(".extra img").css("display","block");
           }
       };
        $(".circle_pure").mouseenter(function () {
            $(this).addClass("on");
        });

    }]);
    ruleCtrlMod.controller('myMenu',['$scope','$http','$state','$interval','$timeout','$filter',function ($scope,$http,$state,$interval,$timeout,$filter) {
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
        $http.get("data/member.json").success(function (data) {
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
        $http.get("./data/shesu.json").success(function (data) {
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
    ruleCtrlMod.controller('myMenu2',['$scope','$http','$state','$interval','$timeout','$filter',function ($scope,$http,$state,$interval,$timeout,$filter) {
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
        $http.get("data/member.json").success(function (data) {
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

                })

            });
            var result = $filter('filter')(  $scope.data_members,$scope.value);

            $scope.tipErr=function () {
                if($(".curDate").siblings().length == 4){
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
        $(":text").focus( function () {
            $(this).css("color","#fdffff")
        });
        $scope.date=moment(new Date()).format('YYYY-MM-DD');
        $scope.reportFlag1=false;$scope.reportFlag2=false;$scope.detailFlag=false;$scope.liListFlag1=false;$scope.liListFlag2=false;$scope.liListFlag3=false;$scope.tongzhuFlag=false;$scope.moveFlag=false;$scope.litiFlag=false;$scope.duoFlag=false;
        $scope.baseReport=function () {
            $scope.reportFlag1=!$scope.reportFlag1;
            $scope.reportFlag2=!$scope.reportFlag2;
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

        $http.get("./data/shesu.json").success(function (data) {
            $scope.litigations=angular.fromJson(data);
            $.each( $scope.litigations,function (index,data) {

            })
        })

    }]);
    ruleCtrlMod.controller('myMenu3',['$scope','$http','$state','$interval','$timeout','$filter',function ($scope,$http,$state,$interval,$timeout,$filter) {
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
        $http.get("data/member.json").success(function (data) {
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
        $(":text").focus( function () {
            $(this).css("color","#fdffff")
        });
        $scope.date=moment(new Date()).format('YYYY-MM-DD');
        $scope.reportFlag1=false;$scope.detailFlag=false;$scope.liListFlag1=false;$scope.liListFlag2=false;$scope.liListFlag3=false;$scope.tongzhuFlag=false;$scope.moveFlag=false;$scope.litiFlag=false;$scope.duoFlag=false;
        $scope.baseReport=function () {

            $scope.reportFlag1=!$scope.reportFlag1;
        };
        $scope.closeReport1=function () {
            $scope.reportFlag=false;
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

        $scope.ruleFlag1=false; $scope.ruleFlag2=false;
        $scope.openClose1=function () {
            $scope.ruleFlag1=!$scope.ruleFlag1;
        };
        $scope.openClose2=function () {
            $scope.ruleFlag2=!$scope.ruleFlag2;
        };

        $scope.ruleAry=[];
        $scope.tipFlag=false;
        $scope.tJRule=function () {
                var a=0;
                var bb=$(".rule_open p");
                angular.forEach(bb,function (item,index) {
                    if($(item).html() == "已开启"){
                        a=1;
                        $scope.ruleAry.push($(item).parent().parent().find(".rule_tit").children("p").html());
                        if(a == 1){
                            $scope.tipFlag=true;
                        }

                    }
                });
            };
        $scope.tipClose=function () {
            $scope.tipFlag=false;
        };


        $scope.backIndex=function () {
            console.log($scope.ruleAry)
        };


        $http.get("./data/shesu.json").success(function (data) {
            $scope.litigations=angular.fromJson(data);
            $.each( $scope.litigations,function (index,data) {

            })
        })

    }]);
    ruleCtrlMod.controller('gridCtrl_account',['$scope','$http',function ($scope,$http) {
            $http.get("data/overdue.json").success(function (data) {
                $scope.customs=angular.fromJson(data);
            });

        $scope.gridOptions={
            data:'customs',
            enableRowSelection:false,
            columnDefs:[
                {
                    field:'id',
                    displayName:'还款序号',
                    cellClass:'noCenter'
                },
                {
                    field:'overDate',//每一列的属性名
                    displayName:'最后还款日',//每一列表头展示的名字
                    cellClass:'noCenter'
                },
                {
                    field:'nextDate',
                    displayName:'本期应还金额',
                    cellClass:'noCenter'
                },
                {
                    field:'customName',
                    displayName:'本期应还本金',
                    cellClass:'noCenter'
                },
                {
                    field:'chatContent',
                    displayName:'本期应还逾期利息',
                    cellClass:'noCenter'
                },
                {
                    field:'applyReason',
                    displayName:'每日滞纳金',
                    cellClass:'noCenter'
                },

                {
                    field:'applyType',
                    displayName:'逾期天数',
                    cellClass:'noCenter'
                },

                {
                    field:'status',
                    displayName:'还款时间',
                    cellClass:'noCenter'
                },
                {
                    field:'review_bz',
                    displayName:'审核备注',
                    cellClass:'noCenter'
                }
            ]
        }

    }]);
    ruleCtrlMod.controller('gridCtrl_cui',['$scope','$http',function ($scope,$http) {
        $http.get("data/overdue.json").success(function (data) {
            $scope.customs=angular.fromJson(data);
        });
        $scope.gridOptions={
            data:'customs',
            enableRowSelection:false,
            columnDefs:[
                {
                    field:'id',
                    displayName:'序号',
                    cellClass:'noCenter'
                },
                {
                    field:'overDate',//每一列的属性名
                    displayName:'催收日期',//每一列表头展示的名字
                    cellClass:'noCenter'
                },
                {
                    field:'nextDate',
                    displayName:'下次跟进时间',
                    cellClass:'noCenter'
                },
                {
                    field:'customName',
                    displayName:'客户标签',
                    cellClass:'noCenter'
                },
                {
                    field:'chatContent',
                    displayName:'沟通内容',
                    cellClass:'noCenter'
                },
                {
                    field:'applyReason',
                    displayName:'申请理由',
                    cellClass:'noCenter'
                },

                {
                    field:'applyType',
                    displayName:'申请处理类型',
                    cellClass:'noCenter'
                },

                {
                    field:'status',
                    displayName:'审核状态',
                    cellClass:'noCenter'
                },
                {
                    field:'review_bz',
                    displayName:'审核备注',
                    cellClass:'noCenter'
                },
                {
                    field:'applicant',
                    displayName:'申请人',
                    cellClass:'noCenter'
                }
            ]
        }

    }]);
    ruleCtrlMod.controller('gridCtrl_sheng',['$scope','$http',function ($scope,$http) {
        $http.get("data/overdue.json").success(function (data) {
            $scope.customs=angular.fromJson(data);
        });

        $scope.gridOptions={
            data:'customs',
            enableRowSelection:false,
            columnDefs:[
                {
                    field:'id',
                    displayName:'序号',
                    cellClass:'noCenter'
                },
                {
                    field:'overDate',//每一列的属性名
                    displayName:'催收日期',//每一列表头展示的名字
                    cellClass:'noCenter'
                },
                {
                    field:'nextDate',
                    displayName:'下次跟进时间',
                    cellClass:'noCenter'
                },
                {
                    field:'customName',
                    displayName:'客户标签',
                    cellClass:'noCenter'
                },
                {
                    field:'chatContent',
                    displayName:'沟通内容',
                    cellClass:'noCenter'
                },
                {
                    field:'applyReason',
                    displayName:'申请理由',
                    cellClass:'noCenter'
                },

                {
                    field:'applyType',
                    displayName:'申请处理类型',
                    cellClass:'noCenter'
                },

                {
                    field:'status',
                    displayName:'审核状态',
                    cellClass:'noCenter'
                },
                {
                    field:'review_bz',
                    displayName:'审核备注',
                    cellClass:'noCenter'
                },
                {
                    field:'applicant',
                    displayName:'申请人',
                    cellClass:'noCenter'
                }
            ]
        }

    }]);

});