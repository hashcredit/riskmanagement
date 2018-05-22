/**
 * 逾期跟踪设定
 * Created by Administrator on 2017/4/17 0017.
 */
var myIndex = angular.module('myIndex', ['chieffancypants.loadingBar']);
myIndex.directive('renderFinish', function ($timeout) {      //renderFinish自定义指令
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function() {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});
myIndex.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
myIndex.controller('myMenu',['$scope','$http','$filter','$rootScope',"cfpLoadingBar","$timeout",function ($scope,$http,$filter,$rootScope,cfpLoadingBar,$timeout) {
	 $scope.start = function() {
	        cfpLoadingBar.start();
	    };
	    $scope.complete = function () {
	        cfpLoadingBar.complete();
	    };
	    cfpLoadingBar.start();
	    var username=document.cookie.split(";")[0].split("=")[1];
		 if(sessionStorage.username){
			 if(sessionStorage.username == "iqianbang"){
					$scope.username="刘忠奎";
					$(".userImg").css({"background":'url("/dumai_qt/static/images/boss.png") no-repeat center -8px',
			        "background-size":"cover"});
				}else{
					$scope.username=sessionStorage.username;
					$(".userImg").css({"background":'url("/dumai_qt/static/images/user_manager.png") no-repeat',
				        "background-size":"cover"});
				} 
		 }else{
			 if(username == "iqianbang"){
				 $scope.username="刘忠奎";
					$(".userImg").css({"background":'url("/dumai_qt/static/images/boss.png") no-repeat center -8px',
			        "background-size":"cover"});
			 }else{
				 $scope.username=username;
					$(".userImg").css({"background":'url("/dumai_qt/static/images/user_manager.png") no-repeat',
				        "background-size":"cover"}); 
			 }
		 }
	//日历插件
	$( "#datepicker").datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
//            $( "#datepicker" ).val("");
        },        
        beforeShow:function () {
            $(".markLine").css({"border-right":"none","z-index":"120"});
        },
        onSelect:function (date) {
        	cfpLoadingBar.start();
        	getInfo(date,"","","相关");
        	$scope.date=date;
        	$(".business li").removeClass("currColor");
        	$(".result li").removeClass("addgreen");
        	$($(".business li")[0]).addClass("currColor")
        },
        dateFormat: 'yy-mm-dd'
    });
    $( "#datepicker2").datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
        },
        beforeShow:function () {
            $(".markLine").css({"border-right":"none","z-index":"120"});
        },
        dateFormat: 'yy-mm-dd'
    });
//   日期格式化
    $scope.date1=moment(new Date()).format('YYYY-MM-DD');
    $scope.date2=moment().add('days',1).format('YYYY-MM-DD');
    $scope.date3=moment(new Date()).format('YYYY-MM-DD');
    
    $scope.$on("ngRepeatFinished",function(){
	}) 
//   请求数据
    function getInfo(filter_dateTime,filter_headtype,status,typeName){
    	cfpLoadingBar.start();
    	if(!filter_dateTime){filter_headtype="";}
    	if(!filter_headtype){filter_headtype="";}
    	if(!status){status="";}
    	 $http({
    	        method:'POST',
    	        url:"/dumai_qt/loanOverdue/list.do",
    	        data:{
    	        	filter_dateTime:filter_dateTime,
	            	filter_headtype:filter_headtype,
	            	status3:status
    	        },
    	        //解决post请求
    	        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
    	        transformRequest: function(obj) {
    	            var str = [];
    	            for(var p in obj){
    	                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
    	            }
    	            return str.join("&");
    	        }
    	    }).success(function(data) {
    	    	if(data){
    	    		  $scope.members=data.rows;
    	    		  if(!$scope.members){
  	        			$scope.tipNOdata="暂无相关数据";
  	        		}else{
  	        			$scope.tipNOdata="";
  	        		}
    	    	        var  obj={};
    	    	        angular.forEach($scope.members,function (item, index) {
    	    	        var data1=moment(item.createtime).format("YYYY-MM-DD");
    	    	        if(obj[data1]){
    	    	         obj[data1].push(item);
    	    	        }else{
    	    	         obj[data1]=[item]
    	    	        }
    	    	      })
    	    	      $scope.persons=[];
    	    	      angular.forEach(obj,function(item,index){
    	    	        var obj2={};
    	    	        obj2.data=index;
    	    	        obj2.person=item;
    	    	        $scope.persons.push(obj2);
    	    	      });
    	    	       angular.forEach($scope.persons,function (item,index) {
    	    	          $scope.every_members=item.person; //包括的只是个人的情况，没有日期
    	    	          angular.forEach( $scope.every_members,function(item,index){
    	    	          if(item.card_photo){
	       	    	        item.card_photo="data:image/gif;base64,"+item.card_photo;
	       	    	      }else{
	       	    	        item.card_photo="/dumai_qt/static/images/userMO.png"
	       	    	      }  
    	    	          var reg=/\\u003d/g;
    	    	          if(reg.exec(item.card_photo)){
    	    	        	item.card_photo =item.card_photo.replace(reg,"");
    	    	          }else{
    	    	        	item.card_photo =item.card_photo;
    	    	          }
    	    	        })
    	    	       });
    	    	       var result = $filter('filter')(  $scope.data_members,$scope.value); 
    	    	 if(!data.total){
    	    		 $scope.tipNOdata="暂无"+typeName+"数据";
    	    	  }
    	       }else{
    	    	   $scope.tipNOdata="暂无"+typeName+"数据";
    	       }
    	      }).error(function(){
    	    	alert("列表请求失败")
    	    })
    }
    //人员信息
    getInfo($scope.date1,"","","相关")
    	   
// 左侧
    
     $http.get("/dumai_qt/loan/headtype.do",{timeout:5000}).success(function(data){
		 if(data){
			 $scope.typeLists=angular.fromJson(data);
		 }
	 }).error(function(){
		 alert("请求业务类型数据失败")
	 }) 

//  左侧收缩效果功能
    $scope.togflag=false;$scope.togflag1=false;
    $scope.togg=function (className) {
        $scope.togflag=! $scope.togflag;
        $(className).children("li").slideToggle("slow");
    };
    $scope.togg1=function (className) {
        $scope.togflag1=! $scope.togflag1;
        $(className).children("li").slideToggle("slow");
    };
//  左侧选择类型的筛选功能
   	$scope.getData=function($event,param,typeName){
   		cfpLoadingBar.start();
   		$(".overDueAccount").css("display","none");
     	$(".overDueCui").css("display","none");
     	$(".overDueSheng").css("display","none");
     	$scope.overdueFlag=false;
   		$($event.target).addClass("currColor").siblings().removeClass("currColor");
   		$scope.typeCode=param;
   		var stautus="";
   		if($(".yinCangType").html()){
   			stautus=$(".yinCangType").html();
   		};
   		getInfo($scope.date1,param,stautus,typeName);
   	};
   
    //文本框聚焦的时候的函数
    $(":text").focus( function () {
        $(this).css("color","#fdffff");
        $(this).attr("placeholder","");
    });
    $(".selectName").blur( function () {
        $(this).attr("placeholder","请输入借款人身份证或姓名");
    });
//逾期信息
    $scope.overdueFlag=false;
//  账户明细
    $scope.overDue_account=function (orderid,code) {
    	cfpLoadingBar.start();
    	$scope.orderid=orderid;
    	$scope.code=code;
    	$scope.overName=$(".yinCangName").html();
    	$scope.overSqje=$(".yinCangSqje").html();
    	$scope.overJkqx=$(".yinCangJkqx").html();
    	$(".account_green").addClass("addgreen").siblings().removeClass("addgreen");
    	$scope.overDqshsj=moment($(".yinCangDqshsj").html()).format('YYYY-MM-DD');
    	$http({
        method:'POST',
        url:"/dumai_qt/loanOverdue/stagesList.do",
        data:{
        	orderId:orderid
        },
        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function(obj) {
            var str = [];
            for(var p in obj){
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
    }).success(function(data){
    	$scope.overDues=angular.fromJson(data).rows;
    }).error(function(){
    	alert("账户明细请求失败")
    })
    	$(".overDueAccount").css("display","block");
    	$(".overDueCui").css("display","none");
    	$(".overDueSheng").css("display","none");
        $scope.overdueFlag=true;
        $(".info_content1").css("overflow","hidden");
    };
//	贷中报告的开启以及关闭以及里边的详细页的打开关闭
    $scope.baseReport2=function () {
    	 cfpLoadingBar.start();
    	$http({
            method:'POST',
            url:"/dumai_qt/report/report.do?code="+$scope.code+"&biz_range=2",
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
            	$scope.loanReportFronts=angular.fromJson(data);
            	$scope.loanReportFront=$scope.loanReportFronts.body;
            	$scope.age=utils.getAgeByIdCard($scope.loanReportFront.card_num);
            	$scope.sex=utils.getGenderByIdCard($scope.loanReportFront.card_num);
            	$scope.dateTime=moment($scope.loanReportFront.createtime).format("YYYY-MM-DD");
            	if(!$scope.loanReportFront.married){
    			   	$scope.loanReportFront.married="暂无";
    			  }
    			 if(!$scope.loanReportFront.education){
    			   	$scope.loanReportFront.education="暂无";
    			 }
    			 if(!$scope.loanReportFront.profession){
    			   	$scope.loanReportFront.profession="暂无";
    			 }
    			 if(!$scope.loanReportFront.address){
     			   	$scope.loanReportFront.address="暂无";
     			 }
            	if($scope.loanReportFront.basicinfo){
            		$scope.basicInfos=$scope.loanReportFront.basicinfo.detail;
            	}
            	if($scope.loanReportFront.card_photo){
               		$scope.loanReportFront.card_photo="data:image/gif;base64,"+$scope.loanReportFront.card_photo
               	}else{
               		$scope.loanReportFront.card_photo="/dumai_qt/static/images/userMO.png"
               	}
            	$scope.orderBill_result=$scope.loanReportFront.orderBill;
            	$scope.cheat=$scope.loanReportFront.cheat;
            	if($scope.loanReportFront.credit){
            		$scope.credit=$scope.loanReportFront.credit;
            		$scope.creditResult=angular.fromJson($scope.credit.result);
            	}
            	$scope.detailInterfaces=$scope.loanReportFront.detailInterfaces;
            	if($scope.loanReportFront.dhData){
            		$scope.dhDatas=$scope.loanReportFront.dhData;
	            	$scope.dhResults=$scope.dhDatas.dhResult;
	            	angular.forEach($scope.dhResults,function(item,index){
	            		if(item.result == "false"){
	            			item.result="未命中";
	            		}else if(item.result == "true"){
	            			item.result="命中";
	            		}
	            	});
            	}
            	if($scope.orderBill_result){
            		var ary=[];
                	for(var i=0;i<$scope.orderBill_result.total_count;i++){
                		var obj={};
                		obj["st_repay_date"]=$scope.orderBill_result["st_" + (i + 1) + "_repay_date"];
                		obj["st_overdue_days"]=$scope.orderBill_result["st_" + (i + 1) + "_overdue_days"];
                		obj["st_penalty"]=$scope.orderBill_result["st_" + (i + 1) + "_penalty"]/100;
                		obj["st_remark"]=$scope.orderBill_result["st_" + (i + 1) + "_remark"];
                		ary.push(obj);
                	}
                	$scope.orderBills=ary;
            	}
            	 var reg=/\\u003d/g;
      	        if(reg.exec($scope.loanReportFront.card_photo)){
      	        	$scope.loanReportFront.card_photo =$scope.loanReportFront.card_photo.replace(reg,"");
      	        }else{
      	        	$scope.loanReportFront.card_photo =$scope.loanReportFront.card_photo;
      	        }
            }
        }).error(function(){
            alert("请求贷中报告数据失败");
        });
        /*$(".markReport").css("display","block");*/
    	
    	function resizeReport(){
   		  var W=$(window).width()*0.5;
   		  var H=$(window).height()*0.7;
   		  $(".baseReport").css("height",H);
   	      var toDo = function () {
   			  /*$(".markReport_1").css("display","block")*/
   	    	  art.dialog({
    	        	content: $(".markReport").html(),
    	        	drag: true,
    	        	width:W,
    	        });
   			 $('.fraud_unfold_1').click(function(){
   				$(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
      				if($(this).hasClass("on")){
      					$(this).addClass("off").removeClass("on");
      	                $('.reportGrid_1').children("tbody").children("tr:first-child").nextAll().css("display","none");

      	            }else{
      	            	$(this).addClass("on").removeClass("off");
      	                $('.reportGrid_1').children("tbody").children("tr:first-child").nextAll().css("display","table-row");
      	            }
      			 })
      			 $('.fraud_unfold_2').click(function(){
      				 $(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
      				if($(this).hasClass("on")){
      					$(this).addClass("off").removeClass("on");
      	                $('.reportGrid4').children("tbody").children("tr:first-child").nextAll().css("display","none");

      	            }else{
      	            	$(this).addClass("on").removeClass("off");
      	                $('.reportGrid4').children("tbody").children("tr:first-child").nextAll().css("display","table-row");
      	            }
      			 })
      			 $('.fraud_unfold_3').click(function(){
      				 $(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
      				if($(this).hasClass("on")){
      					$(this).addClass("off").removeClass("on");
      	                $('.reportGrid5').children("tbody").children("tr:first-child").nextAll().css("display","none");

      	            }else{
      	            	$(this).addClass("on").removeClass("off");
      	                $('.reportGrid5').children("tbody").children("tr:first-child").nextAll().css("display","table-row");
      	            }
      			 })
   			 $(".phone_unfold").click(function(){
   				$(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
      				if($(this).hasClass("on")){
      					$(this).addClass("off").removeClass("on");
      	                $('.reportGrid3').children("tbody").children("tr:first-child").nextAll().css("display","none");

      	            }else{
      	            	$(this).addClass("on").removeClass("off");
      	                $('.reportGrid3').children("tbody").children("tr:first-child").nextAll().css("display","table-row");
      	            }
      			 })
      			  $(".credit_unfold").click(function(){
      				  $(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
      				if($(this).hasClass("on")){
      					$(this).addClass("off").removeClass("on");
      	                $('.reportGrid2').children("tbody").children("tr:first-child").nextAll().css("display","none");

      	            }else{
      	            	$(this).addClass("on").removeClass("off");
      	                $('.reportGrid2').children("tbody").children("tr:first-child").nextAll().css("display","table-row");
      	            }
      			 })
      			 $(".model_unfold").click(function(){
      				 $(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
      				if($(this).hasClass("on")){
      					$(this).addClass("off").removeClass("on");
      	                $('.reportGrid1').children("tbody").children("tr:first-child").nextAll().css("display","none");

      	            }else{
      	            	$(this).addClass("on").removeClass("off");
      	                $('.reportGrid1').children("tbody").children("tr:first-child").nextAll().css("display","table-row");
      	            }
      			 })
   	      };
   	      $timeout(toDo,3000)
   	}
   	resizeReport();	
   	$(window).resize(function() {
   		 var H=$(window).height()*0.7;
   		  $(".baseReport").css("height",H);	
   	});
    	
        angular.forEach($(".phoneCheck .aa"),function (item,index){
            $(item).children(".radioButt").children("label:first-child").addClass("checked").attr("checked","true")
            $(item).children(".radioButt").children("label").click(function(){
                var radioId = $(this).attr('name');
                $(this).addClass('checked').siblings().removeClass("checked");
            });
        })
    };
    
  //报告页中的加载更多
    $scope.closeLiList=function (iconClassName,fatherName) {
        if($(iconClassName).hasClass("on")){
            $(iconClassName).addClass("off").removeClass("on");
            $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","none");

        }else{
            $(iconClassName).addClass("on").removeClass("off");
            $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","table-row");
        }
    };
    // 关闭报告事件
    $scope.closeReport=function () {
    	$(".markReport").css("display","none");
    };  
// 点击右侧催收跟进导航  
    $scope.overDue_cui=function () {
    	 cfpLoadingBar.start();
    	var code=$(".yinCangCode").html();
    	$scope.overName=$(".yinCangName").html();
    	$scope.overSqje=$(".yinCangSqje").html();
    	$scope.overJkqx=$(".yinCangJkqx").html();
    	$scope.overDqshsj=moment($(".yinCangDqshsj").html()).format('YYYY-MM-DD');
//      催收跟进下拉框
        $http.get("/dumai_qt/loanOverdue/labels.do").success(function(data){
        	$scope.customLabels=angular.fromJson(data);
        	 $scope.selectValue=$scope.customLabels[0].value;
        })
// 右侧信息   	
        cuiData();
        function cuiData(){
        	if(!code){
        		code="";
        	}
        	$http({
                method:'POST',
                url:"/dumai_qt/loanOverdue/followList.do",
                data:{
                	code:code
                },
                headers:{'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj){
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
            }).success(function(data){
            	$scope.overDuesCuis=angular.fromJson(data);
            	angular.forEach($scope.overDuesCuis,function(item,index){
           		item.next_date=moment(item.next_date).format('YYYY-MM-DD');
            		item.follow_date=moment(item.follow_date).format('YYYY-MM-DD');
            		if(item.validate_status == "0"){
            			item.validate_status="待审核";
            		}else if(item.validate_status == "1"){
            			item.validate_status="同意";
            		}else if(item.validate_status == "2"){
            			item.validate_status="拒绝";
            		}
            		
            		if(item.opt_request == "1"){
            			item.opt_request = "外访协催"; 
                	}else if(item.opt_request == "2"){
                		item.opt_request = "展期"; 
                	}else if(item.opt_request == "3"){
                		item.opt_request = "外包"; 
                	}else if(item.opt_request == "4"){
                		item.opt_request = "诉讼"; 
                	}else if(!item.opt_request){
                		item.opt_request = "无"; 
                	}
            		
            		if(item.label == 1){
            			item.label="未接电话";
            		}else if(item.label == 2){
            			item.label="他人转告";
            		}else if(item.label == 3){
            			item.label="承诺还款";
            		}else if(item.label == 4){
            			item.label="周转困难";
            		}else if(item.label == 5){
            			item.label="高负债";
            		}else if(item.label == 6){
            			item.label="恶意拖欠";
            		}else if(item.label == 7){
            			item.label="涉嫌欺诈";
            		}else if(item.label == 8){
            			item.label="死亡/坐牢";
            		}else if(item.label == 9){
            			item.label="升级处理";
            		}else if(item.label == 0){
            			item.label="其他";
            		}else if(!item.label){
            			item.label="无";
            		}
            	})
            }).error(function(){
            	alert("催收信息请求失败")
            });
        }
//        $scope.change=function(){getCode($scope.selectValue);};
//   催收保存
        $scope.saveFollow=function(){
        	$scope.reason = $("#upGrade .checked input").val();
        	if($scope.reason == "外访协催"){
        		$scope.reason=1
        	}else if($scope.reason == "展期"){
        		$scope.reason=2
        	}else if($scope.reason == " 外包"){
        		$scope.reason=3
        	}else if($scope.reason == "诉讼"){
        		$scope.reason=4
        	}else if(!$scope.reason){
        		$scope.reason = "";
        	};
        
        	if(!$scope.applyReason){
        		$scope.applyReason="";
        	}
        	if(!$scope.applyContent){
        		$scope.applyContent="";
        	}
        	$http({
                method:'POST',
                url:"/dumai_qt/loanOverdue/saveFollow.do",
                data:{
                	next_date: $scope.date2 ,				//下次跟进时间
                	label:$scope.selectValue,          		//客户标签
                	content:$scope.applyContent,       		//沟通内容
                	opt_request:$scope.reason,   		//升级处理申请
                	reason_request:$scope.applyReason,  		//申请理由
                	fk_orderinfo_code:$scope.code,
                },
                headers:{'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj){
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
            }).success(function(data){
            	cuiData();
            }).error(function(){
            	alert("催收保存数据请求失败")
            })
     	   }
    	$(".overDueAccount").css("display","none");
    	$(".overDueCui").css("display","block");
    	$(".overDueSheng").css("display","none");
        $(".info_content1").css("overflow","hidden")
    };
    //升级处理右侧信息
    $scope.overDue_sheng=function () {
    	cfpLoadingBar.start();
    	var code=$(".yinCangSheng").html();
    	$scope.overName=$(".yinCangName").html();
    	$scope.overSqje=$(".yinCangSqje").html();
    	$scope.overJkqx=$(".yinCangJkqx").html();
    	$scope.overDqshsj=moment($(".yinCangDqshsj").html()).format('YYYY-MM-DD');
    	getUpData();
    	function getUpData(){
        	$http({
                method:'POST',
                url:"/dumai_qt/loanOverdue/handleList.do",
                data:{
                	code:code
                },
                headers:{'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj){
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
            }).success(function(data){
            	$scope.overDuesUp=angular.fromJson(data).rows;
            	$scope.firstoverDuesUp=$scope.overDuesUp[0];
            	$scope.follow_date_first=moment($scope.firstoverDuesUp.follow_date).format('YYYY-MM-DD');
            	$scope.opt_request_first=$scope.firstoverDuesUp.opt_request;
            	$scope.reason_request_first=$scope.firstoverDuesUp.reason_request;
            	$scope.code_first=$scope.firstoverDuesUp.code;
            	if($scope.opt_request == "1"){
            		$scope.opt_request = "外访协催"; 
            	}else if($scope.opt_request == "2"){
            		$scope.opt_request = "展期"; 
            	}else if($scope.opt_request == "3"){
            		$scope.opt_request = "外包"; 
            	}else if($scope.opt_request == "4"){
            		$scope.opt_request = "诉讼"; 
            	}
// 判断审核状态
            	angular.forEach($scope.overDuesUp,function(item,index){
            		item.next_date=moment(item.next_date).format('YYYY-MM-DD');
            		item.follow_date=moment(item.follow_date).format('YYYY-MM-DD');
            		if(item.validate_status == "0"){
            			item.validate_status="待审核";
            		}else if(item.validate_status == "1"){
            			item.validate_status="同意";
            		}else if(item.validate_status == "2"){
            			item.validate_status="拒绝";
            		}
            		if(item.opt_request == "1"){
            			item.opt_request = "外访协催"; 
                	}else if(item.opt_request == "2"){
                		item.opt_request = "展期"; 
                	}else if(item.opt_request == "3"){
                		item.opt_request = "外包"; 
                	}else if(item.opt_request == "4"){
                		item.opt_request = "诉讼"; 
                	}
            		
            		if(item.label == 1){
            			item.label="未接电话";
            		}else if(item.label == 2){
            			item.label="他人转告";
            		}else if(item.label == 3){
            			item.label="承诺还款";
            		}else if(item.label == 4){
            			item.label="周转困难";
            		}else if(item.label == 5){
            			item.label="高负债";
            		}else if(item.label == 6){
            			item.label="恶意拖欠";
            		}else if(item.label == 7){
            			item.label="涉嫌欺诈";
            		}else if(item.label == 8){
            			item.label="死亡/坐牢";
            		}else if(item.label == 9){
            			item.label="升级处理";
            		}else if(item.label == 0){
            			item.label="其他";
            		}else if(!item.label){
            			item.label="无";
            		}
            	})
            }).error(function(){
            	alert("升级数据请求失败")
            })
    	}; 
    //  升级保存
        $scope.saveHaddle=function(status){
        	cfpLoadingBar.start();
        	if(!$scope.applyRemark){
        		$scope.applyRemark="";
        	}

    	$http({
            method:'POST',
            url:"/dumai_qt/loanOverdue/saveHandle.do",
            data:{
            	validate_status:status,
            	validate_remarks: $scope.applyRemark ,				//审核备注
            	fk_orderinfo_follow_code:$scope.code_first,  
            },
            headers:{'Content-Type': 'application/x-www-form-urlencoded'},
            transformRequest: function(obj) {
                var str = [];
                for(var p in obj){
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            }
        }).success(function(result){
        	if(result == 1){
        		getUpData();
        		$(".statusTip").css("display","block");
        		}
     })
            $scope.closestatusTip=function(){
        		$(".statusTip").css("display","none");
        	}
        }
        $(".overDueAccount").css("display","none");
    	$(".overDueCui").css("display","none");
    	$(".overDueSheng").css("display","block");
        $(".info_content1").css("overflow","hidden")
    };
    //返回上一级
    $scope.backMain_account=function (){
    	$(".overDueAccount").css("display","none");
        $scope.overdueFlag=false;
        $(".info_content1").css("overflow","scroll");
    };
    $scope.backMain_cui=function (){
    	$(".overDueCui").css("display","none");
        $scope.overdueFlag=false;
        $(".info_content1").css("overflow","scroll");
    };
    $scope.backMain_sheng=function (){
    	$(".overDueSheng").css("display","none");
        $scope.overdueFlag=false;
        $(".info_content1").css("overflow","scroll");

    };
    //所有的单选
    $('label').click(function(){
        $(this).addClass('checked').siblings().removeClass("checked");
    });

    $(".result_2 li").click(function(){
    	$(this).addClass("addgreen").siblings().removeClass("addgreen");
    })
    
//按回车键或者搜索按钮搜索以及未搜索到信息提示框 
	 $scope.search=function(e){
		 e=e||window.event;
		 var keycode = window.event?e.keyCode:e.which;
		 if($scope.value){
			 if(keycode==13){
		         getInfo("","","","相关",$scope.value);
		          if(!$scope.persons.length){
		        	  $(".selectError").css("display","block");
		        	 
		          }
		      } 
		 }else{
			 if(keycode==13){
		          if(!$scope.persons.length){
		        	  $(".selectError").css("display","none");
		          }
		      } 
		 }
	};
	//搜索框
	 $scope.searchMem=function(){
		  if($scope.value){
			  getInfo("","","","相关",$scope.value);
			  if(!$scope.persons.length){
				  $(".selectError").css("display","block");
	          } 
		  }else{
			  if(!$scope.persons.length){
				  $(".selectError").css("display","none");
	          }
		  }
	 } ; 
	 //关闭搜索弹出框
	 $scope.closeTip=function () {
		 $(".selectError").css("display","none");
	};
	//退出
	$scope.exit_tip_box=function(){
 	   $(".exit_tip").css("display","block"); 
    }
	//关闭退出
    $scope.close_exit_Tip=function () {
    	 $(".exit_tip").css("display","none");
    };
    $scope.exit_tip=function () {
 	   window.location.href="/dumai_qt/logout.do";
    };
    
}]);
//过滤数字    
myIndex.filter('decimalFilter',function () {
//  定义过滤器的方法是return 后面的方法
  return function (data) {
	  //除以100
      return parseFloat(data)/100;
  };
});

