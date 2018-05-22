var myMarker = angular.module('myMarker', ['chieffancypants.loadingBar']);

myMarker.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.spinnerTemplate = '<span class="loading rhomb"></span>';
}]);

myMarker.controller('markController',['$scope','$http','$filter','cfpLoadingBar','$timeout',function ($scope,$http,$filter,cfpLoadingBar,$timeout) {
	 $scope.start = function() {
	        cfpLoadingBar.start();
	};
	 $scope.complete = function () {
	        cfpLoadingBar.complete();
	};
//loading加载
cfpLoadingBar.start();

/*$scope.orderCode=code;*/
//切割code
var rcode=window.location.search.substr(6)
console.log(rcode)

$http({
    method:'POST',
    url:"/dumai_qt/report/report.do?code="+rcode+"&biz_range=2",
    headers:{'Content-Type': 'application/x-www-form-urlencoded'},
    transformRequest: function(obj) {
        var str = [];
        for(var p in obj){
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
        return str.join("&");
    }
}).success(function (data) {
	console.log(data)
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
        if($scope.loanReportFront.basicinfo){
            $scope.basicInfos=$scope.loanReportFront.basicinfo.detail;
        }
        if($scope.loanReportFront.card_photo){
            $scope.loanReportFront.card_photo="data:image/gif;base64,"+$scope.loanReportFront.card_photo
        }else{
            $scope.loanReportFront.card_photo="/dumai_qt/static/images/userMO.png"
        }
     	$scope.audit_results=$scope.loanReportFront.audit_result;
     	if($scope.audit_results){
           	angular.forEach($scope.audit_results,function(item,index){
           		if(item.result == "false"){
           			item.result="未命中";
           		}else if(item.result == "true"){
           			item.result="命中";
           		}
           	});
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
        //处理base64
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
//处理拖拽窗口
      $scope.detailReport=function(code,name){
    	  var e=e||window.event
    	  $(e.target).parents().next().css("display","block")
    	  console.log($(e.target).next())
                 if(name == "司法涉诉查询"){
                     $(".litigaReport").css("display","block");
                 }else{
                     $(".detailReport").css("display","block");
                 }
            $scope.reportName=name;
            var code=$(this).next().html();          
            detailInfo(code,rcode)
            //详细报告
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
       }

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
//导出图片
$scope.exportImg=function(){
    renderPDF(document.getElementById("baseReport"),"baseReport.pdf","a4");
    
}


/*angular.forEach($(".phoneCheck .aa"),function (item,index){
    $(item).children(".radioButt").children("label:first-child").addClass("checked").attr("checked","true")
    $(item).children(".radioButt").children("label").click(function(){
        var radioId = $(this).attr('name');
        $(this).addClass('checked').siblings().removeClass("checked");
    });
})*/


//伸缩展开 
var flag=true
$scope.closeLiList=function (e) {
	$(".tab").children("tbody").slideUp();
	var e= e||window.event
	
    if(!flag){
    	//小三角
      //  $(iconClassName).addClass("off").removeClass("on");
    	
       // $(this).next().children("tr:first-child").nextAll().css("display","none");
    	$(e.target).parents().parents().next("tbody").slideDown();

    }else{
      //  $(iconClassName).addClass("on").removeClass("off");
    	$(e.target).parents().parents().next("tbody").slideUp();
    }
	flag=!flag;
};






}]);