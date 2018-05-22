/**
 * Created by Administrator on 2017/4/18 0018.
 */
var mySystem = angular.module('mySystem', ["ngGrid",'chieffancypants.loadingBar']);
mySystem.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
mySystem.controller('mySystem',['$scope','$http',"cfpLoadingBar",function ($scope,$http,cfpLoadingBar) {
	$scope.start = function() {
        cfpLoadingBar.start();
    };
    $scope.complete = function () {
        cfpLoadingBar.complete();
    };
    cfpLoadingBar.start();
   $scope.addFlag=false;$scope.tipSelectFlag=false;
    $scope.addIP=function(){
        $scope.addFlag=!$scope.addFlag;
    };
//ip访问获取白名单
    $http.get("/dumai_qt/sysmgr/funsettings/load.do").success(function (data) {
        $scope.body=angular.fromJson(data).body;
        $scope.white_ips=$scope.body.white_ips;
        $scope.function_settings=angular.fromJson($scope.body.function_settings)
        console.log($scope.body.function_settings)
        if($scope.body.function_settings){
        	if($scope.function_settings.ip_access_ctrl){
            	$scope.addFlag=true;
            }
        }
    });
    $scope.saveIp=function(){
    	if(!$scope.white_ips){$scope.white_ips="";};
    	var data=null;
    	if($scope.addFlag){
    		data={white_ips:$scope.white_ips,ip_access_ctrl:"1"};
    	}else{
    		data={white_ips:$scope.white_ips};
    	}
    	$http({
			    method:'POST',
			    url:"/dumai_qt/sysmgr/funsettings/save.do",
			    data:data,
			    headers:{'Content-Type': 'application/x-www-form-urlencoded'},
			    transformRequest: function(obj) {
			        var str = [];
			        for(var p in obj){
			            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
			        }
			        return str.join("&");
			    }
		}).success(function(data){
			$scope.tipSelectFlag=true;
			//关闭提示框
		    $scope.closeTip=function () {
		       $scope.tipSelectFlag=false
		    };
			
		}).error(function(){});
    }
    
 /*// 获取审核方式的业务类型
    $http.get("/dumai_qt/sysmgr/funsettings/type.do").success(function(data){
		 if(data){
			 $scope.typeLists=angular.fromJson(data);
// 获取审核方式的业务类型变动
			 $scope.bValue=$scope.typeLists[0].code;
//	改变下拉框，业务类型的事件
			 $scope.changeData=function(){
		    	if($scope.bValue){
		    		$http.get("/dumai_qt/sysmgr/funsettings/bizLoad.do?type_code="+$scope.bValue).success(function(data){
		    			if(data){
		    				console.log(angular.fromJson(data))
		    			}
		    		}).error(function(){
		    			alert("无数据")
		    		})
		    	}else{
		    		alert("加载失败")
		    	}
		    }
//	点击单选框的事件		 
			 $scope.review_way=function(rule_model,$event){
				 $http({
					    method:'POST',
					    url:"/dumai_qt/sysmgr/funsettings/ruleOrModel.do",
					    data:{
					    	type_code:$scope.bValue,
					    	rule_model:rule_model
					    },
					    headers:{'Content-Type': 'application/x-www-form-urlencoded'},
					    transformRequest: function(obj) {
					        var str = [];
					        for(var p in obj){
					            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
					        }
					        return str.join("&");
					    }
				}).success(function(data){}).error(function(){});
			 }
// 点击单选框改变checked事件   
			 $('.box1').off().on("click",function(){
			     $(this).addClass('checked')
			     $(this).siblings().removeClass("checked");
			 });
			 
		 }
	 }).error(function(){
		 $scope.typeLists=[{"name":"无数据"}];
	 })*/

}]);

mySystem.controller('systemGridCtrl',['$scope','$http',function ($scope,$http) {
	 //获取数据查询设定业务类型-初始化
    $http.get("/dumai_qt/sysmgr/funsettings/type.do").success(function (data) {
	   $scope.types=angular.fromJson(data);
	   $scope.typeCode =  $scope.types[0].code;
	   $scope.dataViewLoad();
	 }).error(function(){
		$scope.types=[{"name":"无数据"}];
	 });
    
	$scope.dataViewLoad=function(){
    	$http.get("/dumai_qt/sysmgr/funsettings/dataViewLoad.do?type_code="+$scope.typeCode).success(function (data) {
            $scope.customs=angular.fromJson(data);
        }).error(function(){
			 alert("加载失败");
			 return false;
	    });
    }
	 $scope.gridOptions={
        data:'customs',
        columnDefs:[
            {
                field:'id',
                displayName:'序号',
                cellTemplate:' <div ><span ng-cell-text>{{row.rowIndex+1}}</span></div>'	
            },
            {
                field:'name',
                displayName:'名称'
            },
            {
            	field:'description',
            	displayName:'描述'
           	}
        ],
        showSelectionCheckbox:'true',
        checkboxCellTemplate:'<div id="dataViewPanel" style="margin-top: 9px"><input name="code" value="{{row.entity.code}}" type="checkbox" ng-disabled="row.entity.checkedByGuize>0?true:false" ng-checked="row.entity.checkedByGuize>0?true:row.entity.checked>0?true:false"></div>'
    }
	
	 // 数据查询设定业务类型变动 
	$scope.changeDatas=function(){
		$scope.dataViewLoad();
    }
    //保存设置
    $scope.dataViewSave=function(){
    	var codes = $("#dataViewPanel input[name='code']:checked:not(:disabled)");
    	var data = codes.serialize()+"&type_code="+$scope.typeCode;
    	console.info(data);
		$.ajax({
			url:"/dumai_qt/sysmgr/funsettings/dataViewSave.do",
			type:"post",
			data:data,
			success:function(result){
				if(result===true){
					alert("保存成功");
				}
				else{
					alert("保存失败");
				}
			},
			error:function(){
				alert("保存失败");
			}
		});
    }

}]);
