/**
 * Created by Administrator on 2017/4/17 0017.
 */
var  myOverAcc= angular.module('myOverAcc', ["ngGrid"]);
myOverAcc.controller('gridCtrl_account',['$scope','$http',function ($scope,$http) {
    $http.get("data/overdue.json").success(function (data) {
        $scope.customs=angular.fromJson(data);
    });
    $scope.gridOptions={
        data:'customs',
        enableRowSelection:false,
        columnDefs:[
            {
                field:'id',
                displayName:'还款序号',
                cellClass:'noCenter'
            },
            {
                field:'overDate',//每一列的属性名
                displayName:'最后还款日',//每一列表头展示的名字
                cellClass:'noCenter'
            },
            {
                field:'nextDate',
                displayName:'本期应还金额',
                cellClass:'noCenter'
            },
            {
                field:'customName',
                displayName:'本期应还本金',
                cellClass:'noCenter'
            },
            {
                field:'chatContent',
                displayName:'本期应还逾期利息',
                cellClass:'noCenter'
            },
            {
                field:'applyReason',
                displayName:'每日滞纳金',
                cellClass:'noCenter'
            },

            {
                field:'applyType',
                displayName:'逾期天数',
                cellClass:'noCenter'
            },

            {
                field:'status',
                displayName:'还款时间',
                cellClass:'noCenter'
            },
            {
                field:'review_bz',
                displayName:'审核备注',
                cellClass:'noCenter'
            }
        ]
    }

}]);