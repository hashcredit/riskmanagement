/**
 * 贷前模型池
 * Created by Administrator on 2017/4/17 0017.
 */
var myRule = angular.module('myRule', ['chieffancypants.loadingBar']);
myRule.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
myRule.controller('myMenu3',['$scope','$http','$filter',"cfpLoadingBar",function ($scope,$http,$filter,cfpLoadingBar) {
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
//   日期插件和日期格式化
    $( "#datepicker" ).datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
        },
        beforeShow:function () {
            $(".markLine").css({"border-right":"none","z-index":"120"});
        },
        dateFormat: 'yy-mm-dd'
    });
    $scope.date=moment(new Date()).format('YYYY-MM-DD');
    $scope.ruleHref=function(url){
   	 window.location.href=url;
   };
//    左侧的类型数据
   $scope.start = function() {
       cfpLoadingBar.start();
   };
   $scope.complete = function () {
       cfpLoadingBar.complete();
   };
   cfpLoadingBar.start();
    $http.get("/dumai_qt/loan/headtype.do").success(function(data){
	 if(data){
		 $scope.typeLists=angular.fromJson(data);
		 $scope.code=$scope.typeLists[0].code
		 angular.forEach($scope.typeLists,function(item,index){
		    $scope.code1=item.code;
		 })
// 规则的全部选择事件
	     $scope.ruleFlag1=false; $scope.ruleFlag2=false;$scope.tipFlag=false;
	     $scope.ruleAry1=[]; $scope.ruleAry2=[];
	     getRule($scope.code)
	     function getRule(code){
	    	 $http({
				    method:'POST',
				    url:"/dumai_qt/model/getModelList.do",
				    data:{
				      type_code: code
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
				console.log(data)
		   	    $scope.lieDatas=angular.fromJson(data).rule;
		   	    $scope.authenticationDatas=angular.fromJson(data).authentication;
		   	    $scope.modelDatas=angular.fromJson(data).model;
		   	    angular.forEach($scope.lieDatas,function(item,index){
		   	    	if(item.id == 1 || item.id == 16){
		    			item.id="bank_card_yz";
		    		}else if(item.id == 2 || item.id == 14){
		    			item.id="bank_card";
		    		}else if(item.id == 3 ||item.id == 19){
		    			item.id="telPhone";
		    		}else if(item.id == 4){
		    			item.id="message";
		    		}else if(item.id == 5 || item.id == 17){
		    			item.id="telBlack";
		    		}else if(item.id == 6){
		    			item.id="bank";
		    		}else if(item.id == 7){
		    			item.id="case";
		    		}else if(item.id == 8){
		    			item.id="bank";
		    		}else if(item.id == 9){
		    			item.id="bank";
		    		}else if(item.id == 11){
		    			item.id="bank";
		    		}else if(item.id == 10 || item.id == 18){
		    			item.id="online";
		    		}else if(item.id == 12){
		    			item.id="phone";
		    		}else if(item.id == 13){
		    			item.id="lost";
		    		}
//	默认反欺诈按钮的和状态的开启关闭
	item.statusflag="button_kaiqi";
        item.status1="0";
        if(item.fk_guize_code){
        	item.statusflag="button_guanbi";
            item.ruleBUtton_content="已启用";
            item.ruleBUtton_status="ruleBUtton_status_close";
	   }else{
		   item.statusflag="button_kaiqi";
           item.ruleBUtton_content="已停用";
           item.ruleBUtton_status="ruleBUtton_status_open";
	}
//	 反欺诈每一项规则的开启/关闭
   	function changeButton(item,$event){
   		 var url="";
   		 if(item.statusflag == "button_kaiqi"){
   			 url="/dumai_qt/rule/customEnabeRule.do";
   				  item.statusflag="button_guanbi";
   				  item.status2="1";
   				  item.ruleBUtton_content="已启用";
   		             item.ruleBUtton_status="ruleBUtton_status_close";
   		 }else{
   			 	url="/dumai_qt/rule/customDisabeRule.do";
   				item.statusflag="button_kaiqi";
	            item.status1="0";
	            item.ruleBUtton_content="已停用";
	            item.ruleBUtton_status="ruleBUtton_status_open";
   		 }
            $http({
		        method:'POST',
		        url:url,
		        data:{
		        	fk_guize_code: item.code,
		        	sys_company_type_code:item.sys_company_type_code,
		        	rule_model:'2'
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

		    }).error(function(){})
        };	    		
        $scope.changeItem2=changeButton;
   })
//	鉴权
	angular.forEach($scope.authenticationDatas,function(item,index){
//	鉴权的icon
		if(item.id == 1 || item.id == 16){
			item.id="bank_card_yz";
		}else if(item.id == 2 || item.id == 14){
			item.id="bank_card";
		}else if(item.id == 3 ||item.id == 19){
			item.id="telPhone";
		}else if(item.id == 4){
			item.id="message";
		}else if(item.id == 5 || item.id == 17){
			item.id="telBlack";
		}else if(item.id == 6){
			item.id="bank";
		}else if(item.id == 7){
			item.id="case";
		}else if(item.id == 8){
			item.id="bank";
		}else if(item.id == 9){
			item.id="bank";
		}else if(item.id == 11){
			item.id="bank";
		}else if(item.id == 10 || item.id == 18){
			item.id="online";
		}else if(item.id == 12){
			item.id="phone";
		}else if(item.id == 13){
			item.id="lost";
		}
    })
//	量化定价规则
	angular.forEach($scope.modelDatas,function(item,index){
//	icon的判断
		if(item.id == 1 || item.id == 16){
			item.id="bank_card_yz";
		}else if(item.id == 2 || item.id == 14){
			item.id="bank_card";
		}else if(item.id == 3 ||item.id == 19){
			item.id="telPhone";
		}else if(item.id == 4){
			item.id="message";
		}else if(item.id == 5 || item.id == 17){
			item.id="telBlack";
		}else if(item.id == 6){
			item.id="bank";
		}else if(item.id == 7){
			item.id="case";
		}else if(item.id == 8){
			item.id="bank";
		}else if(item.id == 9){
			item.id="bank";
		}else if(item.id == 11){
			item.id="bank";
		}else if(item.id == 10 || item.id == 18){
			item.id="online";
		}else if(item.id == 12){
			item.id="phone";
		}else if(item.id == 13){
			item.id="lost";
		}
//	首次进入页面按钮开关的判断
		if(item.status == "0"){
			item.statusflag="button_kaiqi";
			 item.modelBUtton_content="已停用";
	         item.modelBUtton_status="ruleBUtton_status_open";
		}else if(item.status == "1"){
			item.statusflag="button_guanbi";
			item.modelBUtton_content="已启用";
	        item.modelBUtton_status="ruleBUtton_status_close";
		}
		
	  })
//改变按钮的事件	  
	  $scope.changeItem_model=function(item,$event){
		var a=item.statusflag  //当 当前的item既可以点开启，也能关闭的时候，需要保存一份
		angular.forEach($scope.modelDatas,function(item,index){
			item.statusflag="button_kaiqi";
			item.modelBUtton_content="已停用";
	        item.modelBUtton_status="ruleBUtton_status_open";
		})
		if(a == "button_guanbi"){
			item.statusflag = "button_kaiqi";
			item.modelBUtton_content="已停用";
	        item.modelBUtton_status="ruleBUtton_status_open";
		}else {
			item.statusflag = "button_guanbi";
			item.modelBUtton_content="已启用";
	        item.modelBUtton_status="ruleBUtton_status_close";
		}
		var url;
	    if(item.statusflag == "button_guanbi"){
	    	url="/dumai_qt/model/enableModel.do"
	    }else if(item.statusflag == "button_kaiqi"){
	    	url="/dumai_qt/model/disableModel.do"
	    }
	        $http({
		        method:'POST',
		        url:url,
		        data:{
		        	manager_model_code: item.code,
		        	sys_company_type_code:item.sys_company_type_code
		        },
		        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
		        transformRequest: function(obj) {
		            var str = [];
		            for(var p in obj){
		                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
		            }
		            return str.join("&");
		        }
		    }).success(function(data){}).error(function(){
		    	alert("修改失败")
		    })
	 };

   }).error(function(){
			alert("请求失败")
	})
}
//  规则池提交  	
		$scope.tJRule=function () {
		     $scope.a=[];$scope.b=[];$scope.c=[];
		     angular.forEach($scope.authenticationDatas,function(item,index){
		            $scope.a.push(item)
		     });
		     angular.forEach($scope.lieDatas,function(item,index){
		        if(item.statusflag == "button_guanbi"){ 
		            $scope.b.push(item)
		    }});
		     angular.forEach($scope.modelDatas,function(item,index){
			        if(item.statusflag == "button_guanbi"){ 
			            $scope.c.push(item)
			}});
		     
		        if(!$scope.a.length){
		              return;
		         }else{
		          $(".tipBox").css("display","block");
		          }
		      };	
//		  提交弹出框关闭按钮
				$scope.tipClose=function () {
					$(".tipBox").css("display","none");
				};	 
		 
//		左侧点击高亮功能 
		 $(".result li").click(function(){
			$(this).addClass("addgreen").siblings().removeClass("addgreen");
		})
//		左侧导航筛选功能
		 $scope.getData=function($event,param){
		    cfpLoadingBar.start();
	    	$($event.target).css("color","lightgreen").siblings().css("color","#d1def5");
	    	$scope.typeCode=param;
	    	var stautus="";
	    	if($(".yinCangType").html()){
	    		stautus=$(".yinCangType").html();
	    	};
	    	getRule(param)
	    } 	
	// 左侧下拉收缩功能 
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
	    

	//  文本聚焦的事件
	    $(":text").focus( function () {
	        $(this).css("color","#fdffff")
	    });

//	    提交按钮弹出框事件
	    $scope.backIndex=function () {
//	        window.location.href="/dumai_qt/loanFront/toLoanFront.do";
	        window.location.href="/dumai_qt/model/toLoanFrontModelList.do";
	    };
		 }
	 }).error(function(){
		 alert("请求失败")
	 }) 

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