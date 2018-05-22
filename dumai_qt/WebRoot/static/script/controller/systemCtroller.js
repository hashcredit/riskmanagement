/**
 * Created by Administrator on 2017/4/18 0018.
 */
var mySystem = angular.module('mySystem', ["ngGrid"]);

mySystem.controller('mySystem',['$scope','$http',"$sce",function ($scope,$http,$sce) {
    $scope.selectedIndex="setup";$scope.addFlag=false;$scope.addUserFlag=false;
    $scope.cutoverTab=function (type) {
        $scope.selectedIndex=type;
    };
    $scope.addIP=function(){
        $scope.addFlag=!$scope.addFlag;
    };
    $scope.addGrid=function () {
        $scope.addUserFlag=true;
    };
    $scope.close=function () {
        $scope.addUserFlag=false;
    };
    $('label').click(function(){
        $(this).addClass('checked').siblings().removeClass("checked");
    });
    // 过滤器
    mySystem.filter('typeFormatter', function ($sce) {
        return function (data) {
            var str = "";
            var dh_type = data.getProperty('dh_type');
            var dh_id=data.getProperty('id');
            var dh_content = data.getProperty('dh_content');
            if (dh_type == 0) {
                str = '<input type="text" value="' + dh_content + '" ng-model="name1" aa="'+dh_id+'"/>';

            } else if (dh_type == 1) {
                var radios = dh_content.split('_');
                for (var j = 0; j < radios.length; j++) {
                    str += '<input type="radio" value="'+radios[j]+'" name="content' + data.rowIndex + '" aa="'+dh_id+'"/>' + radios[j]
                }
            } else if (dh_type == 2) {
                var checkboxs = dh_content.split('_');
                for (var i = 0; i < checkboxs.length; i++) {
                    str += '<input type="checkbox" value="'+checkboxs[i]+'" aa="'+dh_id+'"/>' + checkboxs[i]
                }
            }
            return $sce.trustAsHtml(str);
        }
    });
    
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
					}
					else{
						alert(result);
					}
				},
				error:function(){
					alert("不可用");
				}
			});
    	}
    }
    // 添加新用户
    $scope.addUser=function(){
//    	alert($scope.user_permission)
    	var user_permission = document.getElementsByName("user_permission");
    	var user_permissions = "";
    	for(var i=0;i<user_permission.length;i++){
    		if(user_permission[i].checked){
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
		$.ajax({
			url:"/dumai_qt/sysmgr/userAdd.do",
			data:{"user_name":$scope.user_name,
				"surname":$scope.surname ,
				"sex":sex ,
				"isLeader":isLeader ,
				"user_dept":$scope.user_dept ,
				"user_role":$scope.user_role ,
				"user_permission":user_permissions ,
				"email":$scope.email ,
				"office_tel":$scope.office_tel ,
				"mobile":$scope.mobile ,
				"isvalid":isvalid},
			dataType:"json",
			type:"post",
			success:function(result){
				alert(result.code+"||"+result.body)
				if(result.code==0 && result.body==true){
					alert("添加成功");
					window.location.reload();
				}
				else{
					alert("添加失败");
				}
			},
			error:function(){
				alert("添加失败");
			}
		});
    }
    
    // 删除用户
    $scope.del=function(user_id){
		if(confirm("删除后将无法恢复,确认删除吗?")){
			$.ajax({
				url:"$/dumai_qt/sysmgr/userDel.do?user_id=" + user_id,
				dataType:"json",
				success:function(result){
					if(result.code==0 && result.body==true){
						alert("删除成功");
						$('#pageList').datagrid("reload");
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
    $scope.update=function(row){
    	var user_permission = document.getElementsByName("user_permission");
    	var user_permissions = "";
    	for(var i=0;i<user_permission.length;i++){
    		if(user_permission[i].checked){
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
//		var data = $.deserialize($("#form-update").serialize());
//		var user_permissions = $("#form-update [name='user_permission']");
//		
//		var user_permission = [];
//		user_permissions.each(function(){
//			user_permission.push($(this).prop("checked")?1:0);
//		});
//		
//		data.user_permission = user_permission.join(":");
		
		$.ajax({
			url:"${ctx}/sysmgr/userUpdate.do?code=" + row.code,
			data:{"user_name":$scope.user_name,
				"surname":$scope.surname ,
				"sex":sex ,
				"isLeader":isLeader ,
				"user_dept":$scope.user_dept ,
				"user_role":$scope.user_role ,
				"user_permission":user_permissions ,
				"email":$scope.email ,
				"office_tel":$scope.office_tel ,
				"mobile":$scope.mobile ,
				"isvalid":isvalid},
			dataType:"json",
			type:"post",
			success:function(result){
				if(result.code==0 && result.body==true){
					alert("修改成功");
				}
				else{
					alert("修改失败");
				}
			},
			error:function(){
				alert("修改失败");
			}
		});
	}
    
    // 获取审计方式的业务类型
    $http.get("/dumai_qt/sysmgr/funsettings/type.do").success(function(data){
    	console.log(data)
		 if(data){
			 $scope.typeLists=angular.fromJson(data);
		 }
	 }).error(function(){
		 $scope.typeLists=[{"name":"无数据"}];
	 })
	 
    // 获取审计方式的业务类型变动
	 $scope.bValue="21ff186a-77c6-45ec-a78c-956b2a31e092";
	 $scope.changeData=function(){
    	console.log($scope.bValue)
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

	 
}]);


mySystem.controller('systemGridCtrl',['$scope','$http',function ($scope,$http) {
	// 获取白名单
    $http.get("/dumai_qt/sysmgr/funsettings/load.do").success(function (data) {
        $scope.body=angular.fromJson(data);
//        console.log($scope.body)
//        if($scope.white_ips){
//        	console.log(1)
//        }
    });
	
	// 获取数据查询设定业务类型
	
    $http.get("/dumai_qt/sysmgr/funsettings/type.do").success(function (data) {
	    	if(data){
				$scope.typesLists=angular.fromJson(data);
			}
	        $scope.types=angular.fromJson(data);
	        $http.get("/dumai_qt/sysmgr/funsettings/dataViewLoad.do?type_code="+$scope.types[0].code).success(function (data) {
	            $scope.customs=angular.fromJson(data);
//	            for (var i=0;i<$scope.customs.length;i++){
//	            	if($scope.customs[i].checked!="0"){
//	            		$scope.customs[i].checked="checked='true'";
//	            	}else{
//	            		$scope.customs[i].checked="checked='false'";
//	            	}
//	            }
	        });
	    }).error(function(){
			 $scope.typesLists=[{"name":"无数据"}];
	 })
    // 
    $scope.gridOptions={
        data:'customs',
        // enableRowSelection:false,

        // checkboxHeaderTemplate:
        columnDefs:[
            {
                field:'id',
                displayName:'序号',
                cellClass:'noCenter'
            },
            {
                field:'name',
                displayName:'名称',
                cellClass:'noCenter'
            },
            {
                field:'description',
                displayName:'描述',
                cellClass:'noCenter'
            }
        ],
        showSelectionCheckbox:'true'
    }
    
	 // 数据查询设定业务类型变动 
    $scope.aValue="21ff186a-77c6-45ec-a78c-956b2a31e092";
	$scope.changeDatas=function(){
    	if($scope.aValue){
    		$http.get("/dumai_qt/sysmgr/funsettings/dataViewLoad.do?type_code="+$scope.aValue).success(function(data){
    			if(data){
    				$scope.result=angular.fromJson(data);
    			}
    		}).error(function(){
    			alert("无数据")
    		})
    	}else{
    		alert("加载失败")
    	}
    }
   
	
    
}]);
mySystem.controller('userGridCtrl',['$scope','$http',function ($scope,$http) {
    $http.get("/dumai_qt/sysmgr/userList.do").success(function (data) {
        $scope.customs=angular.fromJson(data).body.rows;
//        console.log($scope.customs)
//        angular.forEach($scope.customs,function(item,index){
//        	var ary=item.user_permission.split(":");
//        	var nup = "";
//        	console.log(ary)
//        	for(var i=0;i<3;i++){
//        		nup += "<span style='color:";
//        		if(ary[i]=="1"){
//        			nup += "#000'>";
//        			if(i==0){
//        				nup += "查看</span>|";
//        			}else if(i==1){
//        				nup += "删除</span>|";
//        			}else{
//        				nup += "导出</span>";
//        			}
//        		}else{
//        			nup += "#c0c0c0'>";
//        			if(i==0){
//        				nup += "查看</span>|";
//        			}else if(i==1){
//        				nup += "删除</span>|";
//        			}else{
//        				nup += "导出</span>";
//        			}
//        		}
//        	}
//        	item.user_permission=nup;
//        	angular.forEach(ary,function(item,index){
//        		console.log(index)
//        	})
//        })
   
        
    });
    $scope.gridOptions={
        data:'customs',
        columnDefs:[
            {
                field:'id',
                displayName:'序号'
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
                field:'customName',
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
                cellTemplate:'<div class="quanxian"><span> 查看 </span> | <span> 删除 </span> | <span> 导出 </span></div>', 
            },
            {
                field:'opttime',
                displayName:'修改时间'
            },
            {
                field:'review_bz',
                displayName:'操作'
            }
        ]
    }
    
    

//   
}]);
mySystem.controller('logGridCtrl',['$scope','$http',function ($scope,$http) {
    $http.get("/dumai_qt/sysmgr/logList.do").success(function (data) {
        $scope.customs=angular.fromJson(data).rows;
    });
    $scope.gridOptions={
        data:'customs',
        columnDefs:[
            {
                field:'id',
                displayName:'序号'
            },
            {
                field:'user_name',//每一列的属性名
                displayName:'用户名'
            },
            {
                field:'user_surname',
                displayName:'姓名'
            },
            {
                field:'logtime',
                displayName:'记录时间'
            },
            {
                field:'ipadress',
                displayName:'IP地址'
            },
            {
                field:'content',
                displayName:'操作内容'
            }
        ]
    }
}]);
mySystem.controller("myPassCtrl",["$scope",function ($scope) {
    $scope.classFlag1=false; $scope.classFlag2=false;
    $scope.changeClass1=function() {
        $scope.classFlag1=!$scope.classFlag1;
        if($scope.classFlag1){
            $(".Eye1").prev().attr("type","text")
        }else{
            $(".Eye1").prev().attr("type","password")
        }
    };
    $scope.changeClass2=function() {
        $scope.classFlag2=!$scope.classFlag2;
        if($scope.classFlag2){
            $(".Eye2").prev().attr("type","text")
        }else{
            $(".Eye2").prev().attr("type","password")
        }
    };
    $scope.InputCheckWord=function() {
        if($scope.name || $scope.newPass){
            if($scope.name == $scope.newPass){
                $.ajax({
                	url: "/dumai_qt/sysmgr/upateUserPwd.do",
                	method: "post",
                	traditional: true,
                	data: {
                	    pwd: $scope.newPass                	    
                	},
                	success: function(result) {
                	    if (result.code == '0') {
                	    	alert("修改成功");
                	    	window.location.reload();
                	    } else {
                	        alert("修改失败");
                	    }
                	},
                	error:function() {
                	    alert("修改失败");
                	}
                });
            }else{
                alert("密码输入不一致")
            }
        }
    };
}]);

