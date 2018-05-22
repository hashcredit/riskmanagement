/**
 * Created by Administrator on 2017/5/3 0003.
 */
var myPhoneApp= angular.module('myPhoneApp', []);
myPhoneApp.controller("myCtrl",["$scope","$http",function ($scope,$http) {
	$scope.username=sessionStorage.username;
	var ary=window.location.search.split("?")[1].split("&");
	var code=ary[0].split("=")[1];
	var opentime=ary[1].split("=")[1];
	var reg=/\%20/g;
	opentime=opentime.replace(reg,function(){
		return " ";
	})
//	进入页面信息
    $http({
        method: 'POST',
        url: contextPath + "/auditingTask/dhTask.do",
        headers: {'Content-Type':'application/x-www-form-urlencoded'},
        data:{
        	code:code,
        	dist_status:'0'
        },
        transformRequest: function (obj) {
            var str = [];
            for (var p in obj) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
    }).success(function (data) {
        $scope.managerLists = angular.fromJson(data);
        if($scope.managerLists.orderInfo){
        	 $scope.orderInfo= $scope.managerLists.orderInfo;
        }
        if($scope.managerLists.rows){
        	$scope.phoneLists=angular.fromJson($scope.managerLists.rows);
        	$scope.phoneListsB=angular.copy($scope.phoneLists);
        	angular.forEach($scope.phoneLists,function(item){
        		if(item.auto_result == "true"){
        			item.auto_result="命中";
        		}else if(item.auto_result == "false"){
        			item.auto_result="未命中";
        		}
        	})
        }
    }).error(function(){
    	alert("电核信息数据请求失败;");
    });
   
// 增项信息   
        $http({
        method: 'POST',
        url: contextPath + "/auditingTask/additional.do",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        data:{
        	code:code
        },
        transformRequest: function (obj) {
            var str = [];
            for (var p in obj) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
    }).success(function (data) {
        $scope.addManagerLists = angular.fromJson(data);
        $scope.addManagerListsB=angular.copy($scope.addManagerLists);
        console.log($scope.addManagerLists)
        if($scope.addManagerLists){
        	angular.forEach($scope.addManagerLists,function(item){
        		if(!item.dh_content){
        			item.dh_content="无";
        		}
        	})
        }
    }).error(function(data){
    	alert("增项信息数据请求失败");
    })
//  查看个人信息
    $scope.infoReport=function(code){
    	$(".infoReportdh").css("display","block");
    	$http.post(contextPath + '/report/report.do?code='+code).success(function(data){
    		console.log(data)
    		if(data.body){
    			$scope.basics=angular.fromJson(data.body);	
    			$scope.basics=angular.fromJson(data.body);
    		   	$scope.aryData=$scope.basics.orders;
    		   	 if($scope.basics["xiaoshi-picture"]){
    		   		$scope.peoImg=$scope.basics["xiaoshi-picture"];
    		   	 }else{
    		   		$scope.peoImg="暂无照片";
    		   	 }
    		   	 if($scope.basics.card_num){
    		   		$scope.basics["age"]=utils.getAgeByIdCard($scope.basics.card_num);
    		   		$scope.basics["sex"]=utils.getGenderByIdCard($scope.basics.card_num); 
    		   	 }
    		   	 if(!$scope.basics.address){
    		   		$scope.basics.address="暂无";
    		   	 }
    		   	 if(!$scope.basics.married){
    		   		$scope.basics.married="暂无";
    		   	 }
    		   	if(!$scope.basics.education){
    		   		$scope.basics.education="暂无";
    		   	 }
    		   	 if(!$scope.basics.profession){
     		   		$scope.basics.profession="暂无";
     		   	 }
    		   	 if($scope.basics.input){
    		   		 $scope.birthplace=$scope.basics.input.enterprise_address;
    		   		 $scope.homeAddress=$scope.basics.input.address;
    		   	 }
    		   	 if($scope.basics["xiaoshi-picture"]){
    		   		$scope.perImg=$scope.basics["xiaoshi-picture"];
    		   	 }
    	 	    $scope.infoAge=utils.getGenderByIdCard($scope.basics.card_num);
    		} 
    	}).error(function(){
    		alert("个人信息报告数据请求失败");
    	});	
    };
    $scope.closeReport=function () {
    	$(".infoReportdh").css("display","none");
    };   
      
//    确认/暂停按钮
    function infoMation(baseAry,abnormalAry,flag){
    	var checkRadioAry=[],remarkAry=[];abnormal_conAry=[],abnormal_remarkAry=[];
    	angular.forEach($(".dh_con"),function(item,$index){
    		var checkRadio=$(item).children("input:checked").val();
    		if(checkRadio == "true"){
    			checkRadio="true";
    		}else if(checkRadio == "false"){
    			checkRadio="false";
    		}
    		checkRadioAry.push(checkRadio)
    	});
    	angular.forEach($(".abnormal_con"),function(item,$index){
    		var abnormalCheckRadio=$(item).children("input:checked").val();
    		if(abnormalCheckRadio == "true"){
    			abnormalCheckRadio="true";
    		}else if(abnormalCheckRadio == "false"){
    			abnormalCheckRadio="false";
    		}
    		abnormal_conAry.push(abnormalCheckRadio)
    	});
    	angular.forEach($(".remarkValue"),function(item,$index){
    		var remarkValue=$(item).val();
    		remarkAry.push(remarkValue);
    	});
    	angular.forEach($(".abnormal_remark"),function(item,$index){
    		var abnormal_remarkValue=$(item).val();
    		abnormal_remarkAry.push(abnormal_remarkValue);
    	});
    	var codeAry=[];
    	angular.forEach($scope.phoneLists,function(item,$index){
    		codeAry.push(item.code);
    	});
    	angular.forEach(baseAry,function(item,$index){
    		delete item.$$hashKey;
    		delete item.ID;
    		delete item.auto_result;
    		delete item.del_flag;
    		delete item.dh_description;
    		delete item.dh_item_group_code;
    		delete item.manager_item_name;
    		delete item.opttime;
    		delete item.result;
    		delete item.code;
    		item.dh_content=checkRadioAry[$index];
    		item.remark=remarkAry[$index];
    		item.dh_terms=codeAry[$index];
    	});
    	angular.forEach(abnormalAry,function(item,$index){
    		item.dh_content=abnormal_conAry[$index];
    		item.remark=abnormal_remarkAry[$index];
    	});
    	if(!$(".base").val()){$(".base").val("");};
    	if(!$(".abnormal").val()){$(".abnormal").val("")}
    	baseAry=JSON.stringify(baseAry);
    	abnormalAry=JSON.stringify(abnormalAry);
   	 	$http({
	        method: 'POST',
	        url: contextPath + "/auditingTask/saveJson.do",
	        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
	        data:{
	        	data1:baseAry,
	            data2:abnormalAry,
	            taskCode:code,  //电核任务code,
	            description:$(".base").val(),   //电核基本情况
	            other_exception:$(".abnormal").val(),   //异常情况
	            flag:flag,
	        	opentime:opentime
	        },
	        transformRequest: function (obj) {
	            var str = [];
	            for (var p in obj) {
	                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
	            }
	            return str.join("&");
	        }
	    }).success(function (data) {
	    	window.location.href=contextPath + "/auditingTask/toList.do";
	    }).error(function(error, status){
	    	alert("数据请求失败");
	    })
    }
//确认按钮
    $scope.referInfo=function(baseAry,abnormalAry,flag){
    	infoMation(baseAry,abnormalAry,flag);
    	
    }
// 暂停按钮
    $scope.pauseInfo=function(baseAry,abnormalAry,flag){
    	infoMation(baseAry,abnormalAry,flag);
    }
    
    
}]);
