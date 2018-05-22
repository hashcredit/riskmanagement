/**
 * Created by Administrator on 2017/4/17 0017.
 */
var  myOverCui= angular.module('myOverCui', ["ngGrid"]);
myOverCui.controller('gridCtrl_cui',['$scope','$http',function ($scope,$http) {
    $http.get("data/overdue.json").success(function (data) {
        $scope.customs=angular.fromJson(data);
    });
    $scope.gridOptions={
        data:'customs',
        enableRowSelection:false,
        columnDefs:[
            {
                field:'id',
                displayName:'序号',
                cellClass:'noCenter'
            },
            {
                field:'overDate',//每一列的属性名
                displayName:'催收日期',//每一列表头展示的名字
                cellClass:'noCenter'
            },
            {
                field:'nextDate',
                displayName:'下次跟进时间',
                cellClass:'noCenter'
            },
            {
                field:'customName',
                displayName:'客户标签',
                cellClass:'noCenter'
            },
            {
                field:'chatContent',
                displayName:'沟通内容',
                cellClass:'noCenter'
            },
            {
                field:'applyReason',
                displayName:'申请理由',
                cellClass:'noCenter'
            },

            {
                field:'applyType',
                displayName:'申请处理类型',
                cellClass:'noCenter'
            },

            {
                field:'status',
                displayName:'审核状态',
                cellClass:'noCenter'
            },
            {
                field:'review_bz',
                displayName:'审核备注',
                cellClass:'noCenter'
            },
            {
                field:'applicant',
                displayName:'申请人',
                cellClass:'noCenter'
            }
        ]
    }

}]);