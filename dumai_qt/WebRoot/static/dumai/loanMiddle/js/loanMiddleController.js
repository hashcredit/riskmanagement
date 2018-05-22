/**
 * 贷中跟踪
 * Created by Administrator on 2017/4/17 0017.
 */
var myIndex = angular.module('myIndex', ['chieffancypants.loadingBar']);

myIndex.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
myIndex.controller('myMenu',['$scope','$http','$filter','cfpLoadingBar','$timeout',function ($scope,$http,$filter,cfpLoadingBar,$timeout) {
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
// 		日历格式
	 
//		日期格式化
	    $scope.date=moment(new Date()).format('YYYY-MM-DD');
	    $( "#datepicker" ).datepicker({
	        onClose:function () {
	            $(".markLine").css({"border-right":"none","z-index":"-100"});
//	            $( "#datepicker" ).val("");
	        },
	        beforeShow:function () {
	            $(".markLine").css({"border-right":"none","z-index":"120"});
	        },
	        onSelect:function (date){
	        	cfpLoadingBar.start();
	        	getInfo(date,"","","相关");
	        	$scope.date=date;
	        	$(".business li").removeClass("currColor");
	        	$(".result li").removeClass("addgreen");
	        	$($(".business li")[0]).addClass("currColor")
	        },
	        dateFormat: 'yy-mm-dd'
	    });
//	    规则池跳转	    
	    $scope.ruleHref=function(){
	    	 window.location.href="/dumai_qt/rule/toCustomRuleList.do";
	    };
//GPS跳转
	    $scope.loanMiddleGPS=function(){
	    	 window.location.href="/dumai_qt/loanMiddle/gps_route.do?code=fb42604f-7ff3-48fc-8483-ace2b9a25890";
	    }; 

//	获取列表信息
	    function getInfo(filter_dateTime,filter_headtype,status,typeName,filter_keyword){
	    	cfpLoadingBar.start();
	    	if(!filter_dateTime){filter_headtype="";}
	    	if(!filter_headtype){filter_headtype="";}
	    	if(!status){status="";}
	    	if(!filter_keyword){filter_keyword="";}
	    	$http({
	            method:'POST',
	            url:"/dumai_qt/loanMiddle/list.do",
	            data:{
	            	filter_dateTime:filter_dateTime,
	            	filter_headtype:filter_headtype,
	            	status3:status,
	            	filter_keyword:filter_keyword
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
		    	        	  if(item.status2 == 1){
		   	    	          	item.status2="attention" ;
		   	    	           }else if(item.status2 == 0){
		   	    	            item.status2="zhengchang" ;
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
	        }).error(function () {
		          alert("列表请求失败");
		    });
	    }
		getInfo($scope.date,"","","相关")  ;
	  
//	左侧
	    $http.get("/dumai_qt/loan/headtype.do",{timeout:5000}).success(function(data){
			 if(data){
				 $scope.typeLists=angular.fromJson(data);
			 }
		 }).error(function(){
			alert("请求业务类型数据失败");
		 })   
//	  左侧收缩功能
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
//	 左侧导航筛选功能
		 $scope.getData=function($event,param,typeName){
			 cfpLoadingBar.start();
	    	$($event.target).addClass("currColor").siblings().removeClass("currColor");
	    	 $(".result li").removeClass("addgreen");
	    	$scope.typeCode=param;
	    	var stautus="";
	    	if($(".yinCangType").html()){
	    		stautus=$(".yinCangType").html();
	    	};
	    	getInfo($scope.date,param,stautus,typeName)  
		};
	  
		//查询全部
		 $scope.getAllData=function($event,param,typeName){
			    cfpLoadingBar.start();
			    $($event.target).addClass("currColor").siblings().removeClass("currColor");
			   $(".result li").removeClass("addgreen");
			   /* $scope.typeCode=param;
			    var stautus="";
			    if($(".yinCangType").html()){
			    	stautus=$(".yinCangType").html();
			    };		  */
			    getInfo('','','','相关')
			};
		
//  左侧正常和关注筛选
	    $(".result li").click(function(){
	    	cfpLoadingBar.start();
	    	$(this).addClass("addgreen").siblings().removeClass("addgreen");
	    })
	    $scope.loanMiddleData=function(stautus,$event){
	    	 var typeName="";
			 if(stautus == "0"){
				 typeName="正常" ;
			 }else if(stautus == "1"){
				 typeName="关注" ;
			 }
	    	var typeParam="";
	    	if($(".yincangResult").html()){
	    		var typeParam=$(".yincangResult").html();
	    	};
	    	$scope.stautus3=stautus;
	    	getInfo($scope.date,typeParam,stautus,typeName)   

	    }

	    $scope.flagNF="ON";
	    $scope.$watch($scope.flagNF,function () {

	    }) ;//这个以后要根据后台给的选择来写
	    //文本框聚焦的时候的函数
	    $(":text").focus( function () {
	    	 $(this).attr("placeholder","");
	        $(this).css("color","#fdffff")
	    });
	    $(".selectName").blur( function () {
	        $(this).attr("placeholder","请输入借款人身份证或姓名");
	    });
	//拖拽  
	    
//		贷中报告的开启以及关闭以及里边的详细页的打开关闭
	    $scope.baseReport=function (code,index) {
	    	cfpLoadingBar.start();
	    	$scope.orderCode=code;
	     	$http({
	            method:'POST',
	            url:"/dumai_qt/report/report.do?code="+code+"&biz_range=2",
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
                    if(!$scope.loanReportFront.plate){
                        $scope.loanReportFront.plate="无";
                    }
	            	if($scope.loanReportFront.basicinfo){
	            		$scope.basicInfos=$scope.loanReportFront.basicinfo.detail;
	            	}
	            	if($scope.loanReportFront.card_photo){
                   		$scope.loanReportFront.card_photo="data:image/gif;base64,"+$scope.loanReportFront.card_photo
                   	}else{
                   		$scope.loanReportFront.card_photo="/dumai_qt/static/images/userMO.png"
                   	}
	            	if($scope.loanReportFront.orderBill){
	            		$scope.orderBill_result=$scope.loanReportFront.orderBill;
		            	$scope.repayment=($scope.orderBill_result.repay_principal/100).toFixed(2);
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
	            	}
	            	$scope.audit_results=$scope.loanReportFront.audit_result;
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
	        			 $(".detailReportSmall").click(function detailReport() {
	        				 if(name == "司法涉诉查询"){
	        					 $(".litigaReport").css("display","block");
	        				 }else{
	        					 $(".detailReport").css("display","block");
	        				 }
	                  	$scope.reportName=name;
	                   	var code=$(this).attr("data-code");
	                  	detailInfo(code,$scope.orderCode)	                
	                  	function detailInfo(dm_3rd_interface_code,orderCode){	                  	
	                  		$http({
	                              method:'GET',
	                              url:'/dumai_qt/report/dataDetail.do?dm_3rd_interface_code='+dm_3rd_interface_code+"&orderCode="+orderCode,
	                          }).success(function(data){
	                          	if(data){
	                          		if(angular.fromJson(data.body)){
	                          			$scope.tipInfos={};
	                          			$scope.detailInfos=angular.fromJson(data.body)[0];
	                          			if(angular.fromJson(data.body).allmsglist.length){
	                          				$scope.litigations=angular.fromJson(data.body).allmsglist;
	                          				if($scope.litigations){
	                          					$scope.onemsglist=$scope.litigations.onemsglist;
	                              				angular.forEach($scope.litigations,function(item,index){
	                              					angular.forEach(item.onemsglist,function(item,index){
	                              						if(item.propervalue.length>30){
	                              							item.shortValue=item.propervalue.substring(0,15);
	                              							
	                              						}else{
	                              							item.shortValue=item.propervalue;	
	                              						}
	                  	            					item.index=index;
	                  	            				})
	                              				})
	                          				}
	                          			}else{
	                          				$scope.litigations=[{"type":"暂无相关数据","onemsglist":[{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"},{"shortValue":"暂无相关数据"}]}];	
	                          			}
	                          			angular.forEach($scope.detailInfos,function(item){
	                                  		if(!item.value){
	                                  			item.value="无";
	                                  		}
	                                  	})
	                          		}else{
	                              		$scope.detailInfos=[{"name_zh":"暂无相关数据","value":"暂无相关数据"}];
	                              		$scope.litigations=[{"type":"暂无相关数据","onemsglist":[{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"},{"propervalue":"暂无相关数据"}]}];	
	                              	}
	                          	}else{
	                          		alert("暂无相关数据");
	                          	};
	                          	
	                          })
	                  	} 
	    	
	                            e=window.event||event.which;
	                            $scope.con=$(e.target).html();
	                            $(e.target).css("color","#5b7c95");
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
                               
	               })
	     	      };
	     	               $timeout(toDo,3000)
	     	 }
	     	resizeReport();	
	     	$(window).resize(function() {
	     		 var H=$(window).height()*0.7;
	     		  $(".baseReport").css("height",H);	
	     	});
	     	//普通报告关闭
       		$(".report_close").click(function(){
    		    $(".detailReport").css("display","none");
    		   })
    		   //司法涉诉报告关闭
    		$(".report_detail").click(function(){
    		   $(".litigaReport").css("display","none");
    		})  
	    	 $scope.exportImg=function(){
	        	     // renderPDF(document.getElementById("baseReport"),"baseReport.pdf","a4")
                 var form=$("<form>");//定义一个form表单
                 form.attr("style","display:none");
                 form.attr("target","");
                 form.attr("method","post");
                 form.attr("action","/dumai_qt/exportPdf.do?code="+code);
                 $("body").append(form);//将表单放置在web中
                 form.submit();
	           }
	        angular.forEach($(".phoneCheck .aa"),function (item,index){
	            $(item).children(".radioButt").children("label:first-child").addClass("checked").attr("checked","true")
	            $(item).children(".radioButt").children("label").click(function(){
	                var radioId = $(this).attr('name');
	                $(this).addClass('checked').siblings().removeClass("checked");
	            });
	        })
	    };
	  
	    // 关闭报告事件
	    $scope.closeReport=function () {
	    	$(".markReport").css("display","none");
	    };
//	详细报告    
	    $scope.tongzhuFlag="false";$scope.detailFlag="false";$scope.moveFlag="false";$scope.litiFlag="false";

//关闭报告	    
	    $scope.closeReport2=function(){
	    	$(".detailReport").css("display","none");
	    };
//关闭详细报告
	    $scope.closeReportLiti=function(){
	    	$(".litigaReport").css("display","none");
	    };
//报告页中的加载更多
	    $scope.closeLiList=function (iconClassName,fatherName) {
	    	$(".reportGrid").children("tbody").children("tr:first-child").nextAll().css("display","none");
	        if($(iconClassName).hasClass("on")){
	            $(iconClassName).addClass("off").removeClass("on");
	            $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","none");

	        }else{
	            $(iconClassName).addClass("on").removeClass("off");
	            $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","table-row");
	        }
	    };
	    
	    $scope.$watch($scope.value,function () {});
	    $scope.score=0;
	    
//按回车键或者搜索按钮搜索以及未搜索到信息提示框 
		 $scope.search=function(e){
			 var e=e||window.event;
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
		}
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
		 };
//关闭搜索提示框
		    $scope.closeTip=function () {
		    	$(".selectError").css("display","none");
		    };
		    $scope.exit_tip_box=function(){
		    	   $(".exit_tip").css("display","block"); 
		   }
		  $scope.close_exit_Tip=function () {
		       $(".exit_tip").css("display","none");
		  };
		  $scope.exit_tip=function () {
		     window.location.href="/dumai_qt/logout.do";
		  };
}]);