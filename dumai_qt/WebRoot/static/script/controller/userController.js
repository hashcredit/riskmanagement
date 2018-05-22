/**
 * Created by Administrator on 2017/4/18 0018.
 */
var mySystem = angular.module('mySystem', ["ngGrid",'chieffancypants.loadingBar']);

mySystem.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);
mySystem.controller('mySystemUserCtrl',['$scope','$http',"cfpLoadingBar",function ($scope,$http,cfpLoadingBar) {
	$scope.start = function() {
        cfpLoadingBar.start();
    };
    $scope.complete = function () {
        cfpLoadingBar.complete();
    };
    cfpLoadingBar.start();
  $scope.addFlag=false;$scope.addUserFlag=false;

    $scope.addGrid=function () {
        $scope.addUserFlag=true;
    };
    $scope.close=function () {
        $scope.addUserFlag=false;
    };
//    单选选择
    $('.box1').click(function(){
        $(this).addClass('checked').siblings().removeClass("checked");
    });
//    权限选择
    $scope.checkFlag=false;
    $(".checkBoxButton").click(function(){
    	$scope.checkFlag=!$scope.checkFlag;
    	if($scope.checkFlag){
    		$(this).addClass('checked')
    	}else{
    		$(this).removeClass("checked")
    	}
    })
    
 // 验证用户名是否存在
    $scope.checkUserExist=function(){
    	if($scope.user_name!=undefined||$scope.user_name!="undefined"||$scope.user_name!=""||$scope.user_name!=" "){
	    	$.ajax({
				url:"/dumai_qt/sysmgr/usernameNotDuplicated.do",
				data:{"user_name":$scope.user_name},
				dataType:"json",
				type:"post",
				success:function(result){
					if(result==true){
						return true
					}
					else{
						alert("用户名已存在");
						return false
					}
				},
				error:function(){
					alert("不可用");
					return false;
				}
			});
    	}else{
    		return false;
    	}
    }
    // 添加新用户
    $scope.addUser=function(){
    	$scope.user_permission=$(".checkBoxButton");
    	$scope.user_permissions = "";
    	for(var i=0;i<$scope.user_permission.length;i++){
    		$($scope.user_permission[i]).hasClass('redColor')
    		if($($scope.user_permission[i]).hasClass('checked')){
    			$scope.user_permissions += "1";
    		}else{
    			$scope.user_permissions += "0";
    		}
    		if(i<$scope.user_permission.length-1){
    			$scope.user_permissions += ":";
    		}
    	}
    	$scope.checkUserExist();
		var sex = document.getElementsByName("sex")[0].value;
		var isLeader = document.getElementsByName("isLeader")[0].value;
		var isvalid = document.getElementsByName("isvalid")[0].value;
		if(!$scope.user_name){$scope.user_name=""};
		if(!$scope.surname){$scope.surname=""};
		if(!sex){sex=""};
		if(!isLeader){isLeader=""};
		if(!$scope.user_dept){$scope.user_dept=""};
		if(!$scope.user_role){$scope.user_role=""};
		if(!$scope.user_permissions){$scope.user_permissions=""};
		if(!$scope.email){$scope.email=""};
		if(!$scope.office_tel){$scope.office_tel=""};
		
		$http({
		    method:'POST',
		    url:"/dumai_qt/sysmgr/userAdd.do",
		    data:{
		    	"user_name":$scope.user_name,
				"surname":$scope.surname ,
				"sex":sex ,
				"isLeader":isLeader ,
				"user_dept":$scope.user_dept ,
				"user_role":$scope.user_role ,
				"user_permission":$scope.user_permissions ,
				"email":$scope.email ,
				"office_tel":$scope.office_tel,
				"mobile":$scope.mobile ,
				"isvalid":isvalid
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
		if(result.code==0 && result.body==true){
			alert("添加成功");
			window.location.reload();
		}
		else{
			alert("添加失败");
		}
		
	}).error(function(){
			alert("添加失败");
		
	});
    }		

}]);

mySystem.controller('userGridCtrl',['$scope','$http',function ($scope,$http) {
    $http.get("/dumai_qt/sysmgr/userList.do").success(function (data) {
        $scope.customs=angular.fromJson(data).body.rows;
        angular.forEach($scope.customs,function(item){    
        	if(item.ISVALID == 1){
        		item.ISVALID="是";
        	}else{
        		item.ISVALID="否";
        	}
        })
    });
    $scope.gridOptions={
        data:'customs',
        columnDefs:[
	         {
	            field:'id',
	            displayName:'序号',
                cellTemplate:' <div style="line-height:32px"><span ng-cell-text>{{row.rowIndex+1}}</span></div>'	
	        },
	        {
	            field:'USER_NAME',//每一列的属性名
	            displayName:'用户名'
	        },
	        {
	            field:'SURNAME',
	            displayName:'姓名'
	        },
	        {
	            field:'OFFICE_TEL',
	            displayName:'工作电话'
	        },
	        {
	            field:'SEX',
	            displayName:'性别'
	        },
	        {
	            field:'ISVALID',
	            displayName:'是否可用'
	        },
	
	        {
	            field:'EMAIL',
	            displayName:'邮箱地址'
	        },
	
	        {
	            field:'user_dept',
	            displayName:'部门'
	        },
	        {
	            field:'user_role',
	            displayName:'角色'
	        },
            {
                field:'user_permission',
                displayName:'权限',
                cellTemplate: '<div ng-bind-html="row|permissionFormatter:row.rowIndex" style="line-height: 32px"></div>'
            },
            {
                field:'opttime',
                displayName:'修改时间',
                cellTemplate: '<div ng-bind-html="row|dateFormatter:row.rowIndex" style="line-height: 32px"></div>'
            },
            {
                field:'operation',
                displayName:'操作',
                cellTemplate: '<div ng-bind-html="row|operationFormatter:row.rowIndex" style="line-height: 32px"></div>'// 使用过滤器进行过滤


            }
        ]
    }

   
    
    
    
}]);
// 操作过滤器
mySystem.filter('operationFormatter', function ($sce) {
    return function (data) {
        var str = "";
        var ISLEADER = data.getProperty('ISLEADER');
        if (ISLEADER == 1) {
            str = "<span style=\"color:grey;\">删除</span> | " +
	            "<a href=\"javascript:update('"+data+"')\" " +
	    		"style=\"color:white;text-decoration:none;\">修改</a>";

        } else{
        	// ng-click=\"del('"+code+"')\"
        	var code = data.getProperty("code");
            str = "<a href=\"javascript:del('"+code+"');\" " +
            	"style=\"color:white;text-decoration:none;\">删除</a> | " +
            	"<a href=\"javascript:update('"+data+"')\" " +
            	"style=\"color:white;text-decoration:none;\">修改</a>";
            
        }
        return $sce.trustAsHtml(str);
    }
});
function del(user_id){
	if(confirm("删除后将无法恢复,确认删除吗?")){
		$.ajax({
			url:"/dumai_qt/sysmgr/userDel.do?user_id=" + user_id,
			dataType:"json",
			success:function(result){
				if(result.code==0 && result.body==true){
					alert("删除成功");
					window.location.reload();
				}
				else{
					alert("删除失败");
				}
			},
			error:function(){
				alert("删除失败");
			}
			
		});
	}
}

// 更新用户信息
function update(row){
	console.log(row)
	var user_permission=$(".checkBoxButton");
	var user_permissions = "";
	for(var i=0;i<user_permission.length;i++){
		$(user_permission[i]).hasClass('redColor')
		if($(user_permission[i]).hasClass('checked')){
			user_permissions += "1";
		}else{
			user_permissions += "0";
		}
		if(i<user_permission.length-1){
			user_permissions += ":";
		}
	}
	var sex = document.getElementsByName("sex")[0].value;
	var isLeader = document.getElementsByName("isLeader")[0].value;
	var isvalid = document.getElementsByName("isvalid")[0].value;
	
//	$.ajax({
//		url:"${ctx}/sysmgr/userUpdate.do?code=" + row.code,
//		data:{"user_name":$scope.user_name,
//			"surname":$scope.surname ,
//			"sex":sex ,
//			"isLeader":isLeader ,
//			"user_dept":$scope.user_dept ,
//			"user_role":$scope.user_role ,
//			"user_permission":$scope.user_permissions ,
//			"email":$scope.email ,
//			"office_tel":$scope.office_tel ,
//			"mobile":$scope.mobile ,
//			"isvalid":isvalid},
//		dataType:"json",
//		type:"post",
//		success:function(result){
//			if(result.code==0 && result.body==true){
//				alert("修改成功");
//			}
//			else{
//				alert("修改失败");
//			}
//		},
//		error:function(){
//			alert("修改失败");
//		}
//	});
}




//权限过滤器
mySystem.filter('permissionFormatter', function ($sce) {
    return function (data) {
        var nup = "";
        var ary = data.getProperty('user_permission').split(":");
        for(var i=0;i<3;i++){
    		nup += "<span style='color:";
    		if(ary[i]=="1"){
    			nup += "white'>";
    			if(i==0){
    				nup += "查看</span>|";
    			}else if(i==1){
    				nup += "删除</span>|";
    			}else{
    				nup += "导出</span>";
    			}
    		}else if(ary[i]=="0"){
    			nup += "gray'>";
    			if(i==0){
    				nup += "查看</span>|";
    			}else if(i==1){
    				nup += "删除</span>|";
    			}else{
    				nup += "导出</span>";
    			}
    		}
    	}
        return $sce.trustAsHtml(nup);
    }
});

//日期过滤器
mySystem.filter('dateFormatter', function ($sce) {
    return function (data) {
    	function formatDate(now) { 
    		var year=now.getYear()+1900; 
    		var month=now.getMonth()+1; 
    		var date=now.getDate(); 
    		var hour=now.getHours(); 
    		var minute=now.getMinutes(); 
    		var second=now.getSeconds(); 
    		return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
    		}
        var time = data.getProperty('opttime');
        time=""+formatDate(new Date(time));
        return $sce.trustAsHtml(time);
    }
});
