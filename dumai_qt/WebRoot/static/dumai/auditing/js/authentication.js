/**
 * 个人身份验证和银行流水查询
 * Created by zgl on 2017/7/3
 */
var managerApp = angular.module('authApp', ['ui.bootstrap']);
managerApp.controller("authCtrl", ["$scope", "$http", function ($scope, $http) {
    $scope.username = sessionStorage.username;

//查询
    $scope.askData = function () {
        $scope.customerName = $("#customerName").val();
        $scope.idcard = $("#idcard").val();
        $scope.bankno = $("#bankno").val();
        authentication($scope.customerName, $scope.idcard, $scope.bankno);
    }

// 查询验证
    function authentication(customerName, idcard, bankno) {
        if (!customerName || !idcard || !bankno) {
            alert("请完善信息");
            return;
        }
        var url = contextPath + "/auth/validate.do";
        $http({
            method: 'POST',
            url: url,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: {
                name: customerName,        //姓名
                idcard: idcard,
                bankcard: bankno,
            },
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            }
        }).success(function (data) {
            console.log(data);
            $scope.bankFlow = data.bankFlow;
            var status = data.status;
            $("#status").html(status);
            if ("一致" != status) {
                return;
            }
            if (!$scope.bankFlow) {
                alert("未查到流水信息");
                return;
            }
            angular.forEach($scope.bankFlow, function (item, index) {
                item.transAmount = parseFloat(item.transAmount / 100).toFixed(2);
            });
        }).error(function () {
            alert("数据请求失败");
        })
    }

}]);
