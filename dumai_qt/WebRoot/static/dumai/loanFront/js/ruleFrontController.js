/**
 * 贷前规则设定
 * Created by Administrator on 2017/4/17 0017.
 */
var myRule = angular.module('myRule', []);
myRule.controller('myMenu3',['$scope','$http','$filter',function ($scope,$http,$filter) {
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
//  规则池跳转
    $scope.ruleHref=function(url){
    	 window.location.href=url;
    };
//规则数据的处理    
    function ruleData(data){
    	$scope.lieDatas=angular.fromJson(data).rule;
    	$scope.authenticationDatas=angular.fromJson(data).authentication;
//    反欺诈规则-icon
    	angular.forEach($scope.lieDatas,function(item,index){
    		console.log(item.id)
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
//    	默认反欺诈按钮的和状态的开启关闭
    		item.statusflag="button_kaiqi";
             item.status1="0";
    		if(item.fk_guize_code){
    			item.statusflag="button_guanbi";
	             item.status1="0";
	             item.ruleBUtton_content="已启用";
	             item.ruleBUtton_status="ruleBUtton_status_close";
    		}else{
    			  item.statusflag="button_kaiqi";
		            item.status1="1";
		            item.ruleBUtton_content="已停用";
		            item.ruleBUtton_status="ruleBUtton_status_open";
    		}
    		
//    反欺诈每一项规则的开启/关闭		
    		 $scope.changeItem2=function(item,$event){
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
				        	rule_model:'1'
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
    	})
//    规则-鉴权icon
    	angular.forEach($scope.authenticationDatas,function(item,index){
    		 item.statusflag="button_kaiqi";
             item.status1="0";
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
    	
    	function changeAll_rules(ruleStatus,fk_guize_code,sys_company_type_code){
    		   $http({
			        method:'POST',
			        url:"/dumai_qt/rule/modifyAllRules.do",
			        data:{
			        	ruleStatus:ruleStatus,
			        	fk_guize_codes:fk_guize_code,
			        	sys_company_type_code:sys_company_type_code
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
			    }).error(function(){}) 
    	}
    	
    	
    	
//   反欺诈启用/停用全部规则 	
    	$scope.changeAll_2=function(){
//    		刚开始进来的时候是判断全部按钮，如果是false->关闭按钮,判断下面的按钮中是否有已关闭的，
//    		如果有已关闭的，将a=1;点击的时候，将所有的已关闭变成已开启；将所有的按钮变成button_guanbi,发送请求，把已关闭的这几个的code传送给后台;
//    		如果全部是已开启的，不发请求，只是把全部的按钮变成开启状态;
    		
//    		如果是true;全部按钮时开启状态，判断下面的状态是否都是已开启；b=0；
//    		如果有已开启的，将这几个已开启的按钮的code发送给后台；将所有的按钮变成button_kaiqi
//    		如果全部是已关闭的，b=1；只是 吧按钮变成关闭，不发请求
    		if(!$scope.ruleFlag2){
    			var str="";var a=0;
    			angular.forEach($scope.lieDatas,function(item,index){
        			if(item.statusflag == "button_kaiqi"){
        				$scope.ruleFlag2=true;
        				a=1;
        				item.statusflag = "button_guanbi";
        				 item.ruleBUtton_content="已开启";
        				 item.ruleBUtton_status="ruleBUtton_status_close";
        				 $scope.sysCode_open=item.sys_company_type_code;
        				str+=item.code+",";
        			}
    				
        		})
        		if(a == 0){
    				$scope.ruleFlag2=true;
    				return;
    			}else{
    				str=str.substring(0,str.length-1);
        			changeAll_rules(1,str,$scope.sysCode_open)	
    			}
    		}else{
    			var str="";var b=0;
    			angular.forEach($scope.lieDatas,function(item,index){
        			if(item.statusflag == "button_guanbi"){
        				$scope.ruleFlag2=false;
        				b=1;
        				item.statusflag = "button_kaiqi";
        				 item.ruleBUtton_content="已关闭";
        				 item.ruleBUtton_status="ruleBUtton_status_open";
        				 $scope.sysCode_close=item.sys_company_type_code;
        				str+=item.code+",";
//        				一会发送请求
        			}
        			
        		})
        		str=str.substring(0,str.length-1);
        		changeAll_rules(0,str,$scope.sysCode_close)
        		if(b == 0){
    				$scope.ruleFlag2=false;
    				return;
    			}
    		}
        } 
//  规则池提交  	
    	  $scope.tJRule=function () {
               $scope.a=[];$scope.b=[];
               angular.forEach($scope.authenticationDatas,function(item,index){
                       $scope.a.push(item)
                   });
               angular.forEach($scope.lieDatas,function(item,index){
                   if(item.statusflag == "button_guanbi"){ 
                       $scope.b.push(item)
                   }});
               if(!$scope.a.length){
               		return;
               }else{
               	$scope.tipFlag=true;
               }
           };
//     提交弹出框关闭按钮
           $scope.tipClose=function () {
               $scope.tipFlag=false;
           };
    }
//  提交按钮弹出框确认按钮
    $scope.backIndex=function () {
        window.location.href="/dumai_qt/loanFront/toLoanFront.do";
    };    
    
//    左侧的类型数据
    $http.get("/dumai_qt/loan/headtype.do").success(function(data){
		 if(data){
			 $scope.typeLists=angular.fromJson(data);
			 angular.forEach( $scope.typeLists,function(item,index){
				 
			 });
			 $scope.code=$scope.typeLists[0].code;
			 // 规则的全部选择事件
			 $scope.ruleFlag1=false; $scope.ruleFlag2=false;$scope.tipFlag=false;
			 $scope.ruleAry1=[]; $scope.ruleAry2=[];
			 $http({
			        method:'POST',
			        url:"/dumai_qt/rule/loanFrontRuleList.do",
			        data:{
			          type_code: $scope.code,
			    	  status:2
			        },
			        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
			        transformRequest: function(obj) {
			            var str = [];
			            for(var p in obj){
			                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
			            }
			            return str.join("&");
			        }
			    }).success(ruleData).error(function(){
			    	alert("请求失败")
			    })
		 }
	 }).error(function(){
		 alert("请求失败")
	 }) 
	 
	 
//	左侧点击高亮功能 
	 $(".result li").click(function(){
		$(this).addClass("addgreen").siblings().removeClass("addgreen");
	})
//	左侧导航筛选功能
	 $scope.getData=function($event,param){
    	$($event.target).css("color","lightgreen").siblings().css("color","#d1def5");
    	$scope.typeCode=param;
    	var stautus="";
    	if($(".yinCangType").html()){
    		stautus=$(".yinCangType").html();
    	};
    	 $http({
		        method:'POST',
		        url:"/dumai_qt/rule/loanFrontRuleList.do",
		        data:{
		        	type_code: param,
		        	status:2
		        },
		        headers:{'Content-Type': 'application/x-www-form-urlencoded'},
		        transformRequest: function(obj) {
		            var str = [];
		            for(var p in obj){
		                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
		            }
		            return str.join("&");
		        }
		    }).success(ruleData).error(function(){})
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
        $(this).css({"color":"#fdffff"})
    });
}]);