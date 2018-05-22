/**
 * Created by Administrator on 2017/1/3 0003.
 */
var myApp = angular.module('myApp', ['ngRoute','ngGrid','chieffancypants.loadingBar']);
myApp.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
myApp.controller("myCtrl",['$scope',"$http","$routeParams","$location","cfpLoadingBar",function ($scope,$http,$routeParams,$location,cfpLoadingBar) {
	  $scope.start = function() {
          cfpLoadingBar.start();
      };

      $scope.complete = function () {
          cfpLoadingBar.complete();
      };
      cfpLoadingBar.start();
	
	//  获取code
   function  getUrlParams(str) {
        var url = location.search;
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for(var i = 0; i < strs.length; i ++) {
                theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
            }
        }
        return theRequest;
    }
   $scope.code=getUrlParams($location.$$absUrl);
//每次进来请求数据   
   getCode($scope.code.code);
// 每次改变下拉列表的时候需要发送请求
   $scope.change=function(){getCode($scope.selectValue);};
   function getAgeByIdCard(idCard){
		var iAge = 0;
       var year = idCard.substring(6, 10);
       iAge = new Date().getFullYear() - parseInt(year);
       return iAge;
	}
	function getGenderByIdCard(idCard){
		var gender = null;
		var sCardNum = idCard.substring(16, 17);
       if (parseInt(sCardNum) % 2 != 0) {
       	gender = "男";
       } else {
       	gender = "女";
       }
       return gender;
	}

   function getCode(code){
	   $http.post('/dumai_qt/report/report.do?code='+code).success(function(data){
		 
		   	$scope.selectValue=code;
		   	$scope.basics=angular.fromJson(data.body);
		   	$scope.aryData=$scope.basics.orders;
		   	 if($scope.basics["xiaoshi-picture"]){
		   		$scope.peoImg=$scope.basics["xiaoshi-picture"];
		   	 }else{
		   		$scope.peoImg="暂无照片";
		   	 }
		   	 if(!$scope.basics["age"]){
		   		$scope.basics["age"]=getAgeByIdCard($scope.basics.card_num);
		   	 }
		   	 if(!$scope.basics["sex"]){
		   		$scope.basics["sex"]=getGenderByIdCard($scope.basics.card_num); 
		   	 }
		   	 console.log($scope.basics.married)
		   	 if(!$scope.basics.married){
			   	$scope.basics.married="暂无";
			  }
			 if(!$scope.basics.education){
			   	$scope.basics.education="暂无";
			 }
			 if(!$scope.basics.profession){
			   	$scope.basics.profession="暂无";
			 }
		   	 if($scope.basics.basicinfo){
		   		 $scope.basicInfos=$scope.basics.basicinfo;
		   		 $scope.basicDetail=$scope.basicInfos.detail;
		   		 if(!$scope.basicDetail.department){
		   			$scope.basicDetail.department="无";
		   		 }
		   		 $scope.changAddress=$scope.basicDetail.censusAddress.district+$scope.basicDetail.censusAddress.location;
		   		
		   	 }
		   	if($scope.basics.detailInterfaces){
		   		$scope.detailLists= $scope.basics.detailInterfaces;
		   	}
		   	 if($scope.basics.audit_result){
		   		 $scope.auditResult=$scope.basics.audit_result; 
		   	 }
		   	
				$('.fraudScore .fraudGrid').css("display","none");
				$('.creditCheck .creditGrid').css("display","none");
		    	if($scope.basics.input){
		      		$scope.input=angular.fromJson($scope.basics.input); 
		      		$scope.arrays=[];
		      		$('.generated').css('display','block');
		      		$.each($scope.input,function(index,data){ //第一次循环 ，遍历input，将他的属性拿出来 
		      			if($scope.basics.dictionary){
		      				$scope.dictionary=angular.fromJson($scope.basics.dictionary);
		      				$.each($scope.dictionary,function(index1,data1){ //第二次遍历，对dic对象进行遍历，获得里边的每个数组，
		      					$.each(data1,function(index2,data2){ //对每个数组进行遍历，将第一次遍历拿到的属性与他里面的每个属性值进行比较，比较一样的，就获取这个数组中的label的值；
		      						if(index == data2){
		      							$scope.genName=data1["label"];
		      							var obj={};
		      							if(data){
		      								obj["titleContent"]=data;
		      							}else{
		      								obj["titleContent"]="无";
		      							}
		      							
		      							obj["titleName"]=$scope.genName;
		      							$scope.arrays.push(obj)	;
		      						};
		      					});
		      				});
		      			};
		     
		      		});
		      	}
		   
			   	 $scope.$broadcast('toSon',$scope.basics);
		   	 
//		   	 判断反欺诈规则的结果
			   	
		   	$.each($scope.auditResult,function(index,data){
		   		if(data["result"]==="false"){
		   			data["result"] ="未命中";
		   		}else if(data["result"]==="error"){
		   			data["result"] ="无结果";
		   		}else if(data["result"]==="true"){
		   			data["result"] ="命中";
		   		}
		   	});

//		   	判断是否审核(预留)
//		   	if($scope.basics.status2 == "0"){
//		   		$('.shenhe').html("未审核");
//		   		$('.shenhe').css({'lineHeight':"156px","color":"red","fontWeight":"bold","fontSize":"16px"});
//		   	}else if($scope.basics.status2 == "1"){
//		   		$('.shenhe').html($("<img src='/dumai_qt/img/sh_tongguo.png'>").css({"width":"100px","height":"100px"}));
//		   	}
		   	
			//判断第二项反欺诈规则的显示问题(未解决)
//   	$.each($scope.basics.orders,function(index,data){
//   		if(!data["audit_result"]){
//   			$('.fraudRule .gridStyle').css("display","none");
//    		}else{
//    			$('.fraudRule .gridStyle').css("display","block");
//   		}
//    	});
		   	
		   	
//		   	判断反欺诈规则
			if($scope.basics.status1 == "0"){
		   		$scope.tongguo = "未审核";
		   	}else if($scope.basics.status1 == "1"){
		   		$scope.tongguo = "通过";
		   	}else if($scope.basics.status1 == "2"){
		   		$scope.tongguo = "不通过";
		   	}else if($scope.basics.status1 == "3"){
		   		$scope.tongguo = "异常";
		   	}
			
//	    	判断反欺诈评分和信用评分是否显示
		    if(!data.fraud){
		        $('.fraudScore .fraudGrid').css("display","none");

		    }else{
		        $('.fraudScore .fraudGrid').css("display","block");
		    }
		    if(!data.credit){
		        $('.creditCheck .creditGrid').css("display","none");
		    }else{
		        $('.creditCheck .creditGrid').css("display","block");
		    }
		   	
		   }).error(function(data){
			   alert("数据请求失败，请重试！");
		   });
}

    $scope.pagingOptions={
            pageSizes:[2,4,6],//定义下拉框的内容 ，显示当前页的数据个数
            pageSize:2,//定义默认下拉框的内容
            currentPage:1,//定义默认当前页
        };
 
        $scope.gridOptions={
            data:'auditResult',
            columnDefs:[//定义列
               
                {
                    field:'guize_name',
                    displayName:'评分项',
                    width:550
                },
                {
                    field:'result',
                    displayName:'结果',
                  
                }
            ],
        };
}]).directive('myDirective', function() {//反欺诈评分和信用的点击事件 
    return {
        restrict: 'ECMA',
        link:function(scope,element,attrs){
        	element.on('click',function(){
        		element.nextAll().toggle('slow');      		
        	});
        }
    };
});

//对信用评分和反欺诈评分之后的分数数据进行过滤
myApp.filter('aa',function () {
//  定义过滤器的方法是return 后面的方法
  return function (data) {
      return parseFloat(data)/100;
  };
});

//规则评分的表格
myApp.controller('gridCtrl_rule',['$scope','$http',function($scope,$http){
	 $scope.$on('toSon', function (event, data){
        //这里可以得到上面的数据，不必再次发送请求
     });
	 $scope.pagingOptions={
	            pageSizes:[2,4,6],//定义下拉框的内容 ，显示当前页的数据个数
	            pageSize:2,//定义默认下拉框的内容
	            currentPage:1,//定义默认当前页
	        };
	 
	        $scope.gridOptions={
	            data:'users',
	            columnDefs:[//定义列
	               
	                {
	                    field:'id',
	                    displayName:'评分项',
	                    width:550
	                },
	                {
	                    field:'type',
	                    displayName:'结果',
	                  
	                }
	            ],
	        };
}]);
//信用评分的表格
myApp.controller('gridCtrl_check',['$scope','$http',function($scope,$http){
	 $scope.$on('toSon', function (event, data){
	        //这里可以得到上面的数据，不必再次发送请求
	 });
	 $scope.pagingOptions={
	            pageSizes:[2,4,6],//定义下拉框的内容 ，显示当前页的数据个数
	            pageSize:2,//定义默认下拉框的内容
	            currentPage:1,//定义默认当前页
	        };
	 
	        $scope.gridOptions={
	            data:'users',
	            columnDefs:[//定义列
	                {
	                    field:'id',
	                    displayName:'评分项',
	                    width:550
	                },
	                {
	                    field:'type',
	                    displayName:'结果',
	                  
	                }
	            ],
	        };
}]);


