/**
 * 查询订单
 * Created by Administrator on 2017/5/3 0003.
 */
var managerApp = angular.module('managerApp', ['ui.bootstrap']);
managerApp.controller("managerCtrl",["$scope","$http",function ($scope,$http) {
	$scope.username=sessionStorage.username;
    $( "#datepicker1" ).datepicker({
        onClose:function () {
            $(".markLine").css({"border-right":"none","z-index":"-100"});
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
        onSelect:function () {
        },
        dateFormat: 'yy-mm-dd'
    });
    $scope.currentPage = 1;
    $scope.maxSize = 7;
    orderLists("1","20","1")
// 分页请求   
    function orderLists(page,rows,dist_status,sys_user_code,thetype,status,customerName,startTime,endTime){
    	if(!sys_user_code){sys_user_code="";};
    	if(!thetype){thetype="";};
    	if(!status){status="";};
    	if(!customerName){customerName="";};
    	if(!startTime){startTime="";};
    	if(!endTime){endTime="";};
        $http({
            method: 'POST',
            url: contextPath + "/auditingTask/auditingTaskList.do",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: {
            	dist_status: dist_status,
            	page:page,
            	rows:rows,
            	sys_user_code:sys_user_code,  //审核人code
            	// thetype:thetype,        //业务类型code
            	status:status,        //审核状态
            	customerName:customerName,        //姓名
            	startTime:startTime,
            	endTime:endTime,
            },
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            }
        }).success(function (data) {
            $scope.managerLists = angular.fromJson(data.rows);
            $scope.totalItems=data.total;
            if (0 == $scope.totalItems){
                alert("未查到信息");
                return;
            }
            $scope.orderNumber=20*($scope.currentPage-1);
            angular.forEach($scope.managerLists, function (item, index) {
                item.create_time = moment(item.create_time).format('YYYY-MM-DD HH:mm:ss');
                item.response_time=utils.calTime(item.response_time);
                item.handle_time=utils.calTime(item.handle_time);
                if (item.status == "0") {
                    item.status = "待审核";
                    item.playLook="暂无结果";
                    item.titlePlay="不可点击";
                } else if (item.status == "1") {
                    item.status = "通过";
                   item.playLook="查看";
                   item.titlePlay="";
                } else if (item.status == "2") {
                    item.status = "拒绝";
                    item.playLook="查看";
                    item.titlePlay="";
                } else if (item.status == "3") {
                    item.status = "处理中";
                   item.playLook="查看";
                   item.titlePlay="";
                }
               
                if(item.other_exception){
                	if (item.other_exception.length>30) {
                    	item.short_exception=item.other_exception.substring(0,30)+'...';
                    }else{
                    	item.short_exception=item.other_exception;
                    } 
                }
            });
        }).error(function(){
        	alert("分页请求列表数据请求失败");
        })
    }
//分页请求
    $scope.getPageLists=function(pageNum){
    	orderLists($scope.currentPage,"20","1",$scope.disValue,$scope.typeValue,$scope.typeStatus,$scope.name,$scope.startTime,$scope.endTime);
    }

//审核人   
    $http({
        method: 'POST',
        url: contextPath + "/auditingTask/getDhy.do",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        transformRequest: function (obj) {
            var str = [];
            for (var p in obj) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
      }).success(function (data) {
    	$scope.distributionLists=data;
    	var obj={"code":"","surname":"全部"};
    	$scope.distributionLists.unshift(obj);
    	$scope.disValue=$scope.distributionLists[0].code;
      }).error(function(){
    	alert('审核人数据请求失败');
     });    
    
// 业务类型  
    $http.get(contextPath + "/loan/headtype.do", {timeout: 3000}).success(function (data) {
		 if(data){
			 var obj={"code":"","name":"全部"};
			 $scope.typeLists=angular.fromJson(data);
			 $scope.typeLists.unshift(obj);
			 $scope.typeValue=$scope.typeLists[0].code;
		 }
	 }).error(function(){
		 alert("业务类型数据请求失败");
	 });
//查询
     $scope.askData=function(){
    	 $scope.startTime=$("#datepicker1").val();
    	 $scope.endTime=$("#datepicker2").val();
    	 orderLists("1","20","1",$scope.disValue,$scope.typeValue,$scope.typeStatus,$scope.name,$scope.startTime,$scope.endTime);
     }
//清空
     $scope.clear=function(){
    	 $scope.disValue="";
    	 $scope.typeValue="";
    	 $scope.typeStatus="";
    	 $scope.name="";
    	 $("#datepicker1").val("");
    	 $("#datepicker2").val("");
     }
//查看     
     function getAuditingList(code){
         $http({
             method:'POST',
             url: contextPath + "/auditingTask/dhTask.do",
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
         }).success(function(data) {
        	 $scope.dhBaseInfo=angular.fromJson(data);
        	 if(!$scope.dhBaseInfo.description){$scope.dhBaseInfo.description="无";};
        	 if(!$scope.dhBaseInfo.other_exception){$scope.dhBaseInfo.other_exception="无";};
        	 $scope.orderInfo=angular.fromJson(data).orderInfo;
        	 $scope.phoneLists = angular.fromJson(data).rows;
             angular.forEach($scope.phoneLists,function(item,index){
                 item.create_time = moment($scope.create_time).format('YYYY-MM-DD');
               
                 if (item.auto_result == "false") {
                     item.auto_result = "未命中";
                 } else if(item.auto_result == "true"){
                	 item.auto_result = "命中"; 
                 }
                 if (item.result == "false") {
                     item.result = "否";
                 }else if(item.result == "true"){
                	 item.result = "是"; 
                 }
              });
             
//   增项信息
             $http({
                 method:'POST',
                 url: contextPath + "/auditingTask/additional.do",
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
            	  $scope.addManagerLists = angular.fromJson(data);
                  if($scope.addManagerLists){
                  	angular.forEach($scope.addManagerLists,function(item){
                  		if(!item.dh_content){
                  			item.dh_content="无";
                  		}else if(item.dh_content == "true"){
                  			item.dh_content="是";
                  		}else if(item.dh_content == "false"){
                  			item.dh_content="否";
                  		}
                  		if(!item.remark){item.remark="无";}
                  	})
                  }
             }).error(function(){
            	 alert("增项信息数据请求失败");
             })
          }).error(function(){
              alert("电核信息数据请求失败");
          });
     }
// 操作-查看    
    $scope.dhPage=function (code,name) {
    	if(name == "查看"){
    		$(".reviewContain_tip").css("display","block");
            getAuditingList(code);
    	}
    };
    
    $scope.close=function () {
    	$(".reviewContain_tip").css("display","none");
    };
//    查看个人信息
    $scope.infoReport=function(code){
    	$(".infoReportdh").css("display","block");
        $http.post(contextPath + '/report/report.do?code=' + code).success(function (data) {
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
    		   		$scope.basics.address="无";
    		   	 }
    		   	 if(!$scope.basics.education){
    				$scope.basics.education="无";
    			}
    		   	 if(!$scope.basics.married){
    		   		$scope.basics.married="无";
    		   	 }
    		   	 if(!$scope.basics.profession){
     		   		$scope.basics.profession="无";
     		   	 }
    		   	if(!$scope.basics.income){
    		   		$scope.basics.income="无";
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
    	})	
    };
    $scope.closeReport=function () {
    	$(".infoReportdh").css("display","none");
    };
}]);
