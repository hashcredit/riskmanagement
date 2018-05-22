/**
 * 贷前审核
 * Created by Administrator on 2017/4/17 0017.
 */
	var myIndex = angular.module('myIndex', ['chieffancypants.loadingBar']);
	//angular的插件，负责loading，对应的css是loading-bar
	myIndex.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
	    cfpLoadingBarProvider.includeSpinner = true;
	    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
	}]);
	myIndex.controller('myMenu',['$scope','$http','$filter',"cfpLoadingBar","$timeout",function ($scope,$http,$filter,cfpLoadingBar,$timeout) {
		$scope.start = function() {
			//loading开始
		        cfpLoadingBar.start();
		    };
		    $scope.complete = function () {
		    	//loading结束
		        cfpLoadingBar.complete();
		    };
		    //请求开始
		    cfpLoadingBar.start();
		 var username=document.cookie.split(";")[0].split("=")[1];
		 
		 //session
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
	   $scope.liListFlag=false;
	    // 日历格式插件
	    $( "#datepicker").datepicker({
	        onClose:function () {
	            $(".markLine").css({"border-right":"none","z-index":"-100"});
//	            $( "#datepicker" ).val("");
	        },
	        beforeShow:function () {
	            $(".markLine").css({"border-right":"none","z-index":"120"});
	        },
	        onSelect:function (date){
	        	cfpLoadingBar.start();
	        	getInfo(date,"","","相关")
	        	$scope.dateTime=date;
	        	$(".business li").removeClass("currColor");
	        	$(".result li").removeClass("addgreen");
	        	$($(".business li")[0]).addClass("currColor")
	        },
	        dateFormat: 'yy-mm-dd'
	    });
	   
	    //日期格式化
	    $scope.dateTime=moment($scope.dateTime).format('YYYY-MM-DD');

// 规则池跳转
	$scope.ruleHref=function(url){
	    	 window.location.href=url;
	};
//	右侧人员信息列表
	 function getInfo(filter_dateTime,filter_headtype,stautus,typeName,filter_keyword){
		 cfpLoadingBar.start();
	   	if(!filter_dateTime){filter_headtype="";}
	   	if(!filter_headtype){filter_headtype="";}
	   	if(!status){status="";}
	   	if(!filter_keyword){filter_keyword="";}
	   	$http({
	           method:'POST',
	           url:"/dumai_qt/loanFront/list.do",
	           data:{
	           	filter_headtype:filter_headtype,
	           	status1:stautus,
	           	filter_dateTime:filter_dateTime,
	           	filter_keyword:filter_keyword
	           },
	           //post请求必须写header和transformRequest
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
	       	    	        	console.log(item)
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
	       	    	          angular.forEach($scope.every_members,function(item,index){
	       	    	        	  if(item.card_photo){
	       	    	        		 item.card_photo="data:image/gif;base64,"+item.card_photo;
	       	    	        	  }else{
	       	    	        		item.card_photo="/dumai_qt/static/images/userMO.png"
	       	    	        	  }
	       	    	        	 
	       	    	          if(item.status1 == 2){
	       	    	          	item.status1="loanFrontNo" ;
	       	    	           }else if(item.status1 == 0){
	       	    	            item.status1="loanFronting" ;
	       	    	           }else if(item.status1 == 1){
	       	    	        	   if(item.biz_range == 1){
	       	    	        		   item.status1="loanFronting" ; 
	       	    	        	   }
	       	    	          }
	       	    	        var reg=/\\u003d/g;
	       	    	        if(reg.exec(item.card_photo)){
	       	    	        	item.card_photo =item.card_photo.replace(reg,"");
	       	    	        }else{
	       	    	        	item.card_photo =item.card_photo;
	       	    	        }
	       	    	        })
	       	    	       });
	       	    	       //无用
	       	    	     /*  var result = $filter('filter')($scope.data_members,$scope.value); */
	       	    	 if(!data.total){
	       	    		 $scope.tipNOdata="暂无"+typeName+"数据";
	       	    	  }
	       	       }else{
	       	    	   $scope.tipNOdata="暂无"+typeName+"数据";
	       	       }
	       	      }  		
	       ).error(function(){
	       	alert("请求超时")
	       })
	   }
//	  默认列表信息
        getInfo($scope.dateTime,"","","相关")

//   左侧列表信息
	 $http.get("/dumai_qt/loan/headtype.do",{timeout:5000}).success(function(data){
		 if(data){
			 $scope.typeLists=angular.fromJson(data);
		 }
	 }).error(function(){
		 alert("业务类型请求失败");
	 })      
//  左侧下拉收缩功能
	        $scope.togflag=false;$scope.togflag1=false;
	        $scope.togg=function (className) {
	            $scope.togflag=!$scope.togflag;
	            $(className).children("li").slideToggle("slow");
	        };
	        $scope.togg1=function (className) {
	            $scope.togflag1=! $scope.togflag1;
	            $(className).children("li").slideToggle("slow");
	        };
//	左侧导航筛选功能（列表点击事件）
	 $scope.getData=function($event,param,typeName){
	    cfpLoadingBar.start();
	    $($event.target).addClass("currColor").siblings().removeClass("currColor");
	   $(".result li").removeClass("addgreen");
	    $scope.typeCode=param;
	    var stautus="";
	    if($(".yinCangType").html()){
	    	stautus=$(".yinCangType").html();
	    };
	    getInfo($scope.dateTime,param,stautus,typeName)
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
// 左侧拒绝、未审核筛选功能
   	 $(".result li").click(function(){
   		 cfpLoadingBar.start();
   		 $(this).addClass("addgreen").siblings().removeClass("addgreen");
   	})

       $scope.loanFrontData=function(stautus,$event){
   		 var typeName="";
   		 if(stautus == "0" ||stautus == "1" ){
   			 typeName="未审核" ;
   		 }else if(stautus == "2"){
   			 typeName="拒绝" ;
   		 }
       	var typeParam="";
       	if($(".yincangResult").html()){
       		var typeParam=$(".yincangResult").html();
       	};
       	getInfo($scope.dateTime,typeParam,stautus,typeName)
       }
   	 
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
   	 }  
   	 
   //关闭提示框
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
       $scope.flagNF="ON";
       $scope.$watch($scope.flagNF,function () {

       }) ;//这个以后要根据后台给的选择来写
      
//文本框聚焦的时候的函数
       $(":text").focus( function () {
           $(this).css("color","#fdffff");
           $(this).attr("placeholder","");
       });
       $(".selectName").blur( function () {
           $(this).attr("placeholder","请输入借款人身份证或姓名");
       });
//贷前报告的开启以及关闭以及里边的详细页的打开关闭
       


       $scope.baseReport=function (code,index) {
       /*	$(".markReport").css("display","block");*/
    	   cfpLoadingBar.start();
       		$scope.orderCode=code;
       		console.log(code)
       		$http({
               method:'POST',
               url:"/dumai_qt/report/report.do?code="+code,
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
               	$scope.iconImg=$($(".yinCangIcon").eq(index)).html();
                	$scope.loanReportFronts=angular.fromJson(data); //所有数据
               	  if($scope.loanReportFronts.body){
               		$scope.loanReportFront=$scope.loanReportFronts.body;  //body中的数据
                   	$scope.age=utils.getAgeByIdCard($scope.loanReportFront.card_num);  //获取年龄
                   	$scope.sex=utils.getGenderByIdCard($scope.loanReportFront.card_num); //获取性别
                   	$scope.dateTime=moment($scope.loanReportFront.createtime).format("YYYY-MM-DD"); //获取日期
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
                   	$scope.audit_results=$scope.loanReportFront.audit_result;
                   	console.log($scope.audit_results)
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
                   	if($scope.audit_results){
                       	angular.forEach($scope.audit_results,function(item,index){
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
               
               }else{
               	alert("暂无报告信息")
               }
           }).error(function(){
               alert("贷前报告数据请求失败");
           });
       	//保留，每个table的加载更多函数
        function closeLiList(iconClassName,fatherName) {
            if($(iconClassName).hasClass("on")){
                $(iconClassName).addClass("off").removeClass("on");
                $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","none");

            }else{
                $(iconClassName).addClass("on").removeClass("off");
                $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","table-row");
            }
        };
        //
       		function resizeReport(){
       			var W=$(window).width()*0.5;
       			var H=$(window).height()*0.7;
       			$(".baseReport").css("height",H);
       			//拖拽框和任意改变尺寸
       			var toDo = function () {
       				$(".markReport_1").css("display","block")
       				art.dialog({
       					content: $(".markReport").html(),
       					drag: true,
       					width:W,
       				});
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
       			 //详细报告显示
       	       $(".detailReportSmall").click(function detailReport(){
       	    	  cfpLoadingBar.start();
		       	   	var name=$(this).html();
		       		var code=$(this).next().html();
        	       	if(name == "司法涉诉查询"){
        	       		$(".litigaReport").css("display","block");
        	       	}else{
        	       		$(".detailReport").css("display","block");
        	       	}
        	       	$scope.reportName=name;
        	       	detailInfo(code,$scope.orderCode)
        	       	function detailInfo(dm_3rd_interface_code,orderCode){
        	       		$http({
        	                   method:'GET',
        	                   url:'/dumai_qt/report/dataDetail.do?dm_3rd_interface_code='+dm_3rd_interface_code+"&orderCode="+orderCode,
        	               }).success(function(data){
        	            	   if(data){
        		            		if(angular.fromJson(data.body)){
        		            			$scope.tipInfos={};
        		            			if(name == "司法涉诉查询"){
        		            				$scope.detailInfos=angular.fromJson(data.body);
        		            	       	}else{
        		            	       		$scope.detailInfos=angular.fromJson(data.body)[0];
        		            	       	}
        		            			if(angular.fromJson(data.body).allmsglist){
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
        	       })
       			};
       			$timeout(toDo,3000);
       		}
       		//分辨率不同的可拖拽弹出框
       		resizeReport();	
       		//窗口尺寸改变，设置报告的高度
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
    		
    		
    		//导出
           $scope.exportImg=function(){
        	   renderPDF(document.getElementById("baseReport"),"baseReport.pdf","a4")
           }
       		//无用
          /* angular.forEach($(".phoneCheck .aa"),function (item,index){
               $(item).children(".radioButt").children("label:first-child").addClass("checked").attr("checked","true")
               $(item).children(".radioButt").children("label").click(function(){
                   var radioId = $(this).attr('name');
                   $(this).addClass('checked').siblings().removeClass("checked");
               });
           })*/
       };
       // 关闭可拖拽报告事件
       $scope.closeReport=function () {
       	$(".markReport").css("display","none");
       };
       //报告页中的加载更多
      /* $scope.closeLiList=function (iconClassName,fatherName) {
           if($(iconClassName).hasClass("on")){
               $(iconClassName).addClass("off").removeClass("on");
               $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","none");

           }else{
               $(iconClassName).addClass("on").removeClass("off");
               $(fatherName).children("tbody").children("tr:first-child").nextAll().css("display","table-row");
           }
       };*/
       $scope.score=0;
	}]);  

