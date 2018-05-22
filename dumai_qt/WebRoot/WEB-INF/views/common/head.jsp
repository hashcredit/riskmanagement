<%@ page contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
    <div class="header">
        <ul>
            <li><i class="icon_system"></i>系统设置</li>
            <li><i class="icon_data"></i>数据报表</li>
            <li><i class="icon_traffic"></i>数据流量</li>
            <h5><a href="#login">退出</a><span></span></h5>
        </ul>

        <div class="info_header">
            <label class="info_header_date">
                <div class="markLine"></div>
                <span class="dark"></span>
                <p>时间: <input type="text" id="datepicker" ng-model="date"><i ng-click="dispear()" ></i></p>
            </label>
            <ul>
                <li class="current">贷前审核</li>
                <li>贷中跟踪</li>
                <li>逾期催款</li>
            </ul>
            <label class="selectNameBox"><i class="search"></i><input type="text" placeholder="请输入借款人身份证或是姓名" class="selectName"  ng-model="value" ng-blur="tipErr()"></label>
        </div>
    </div>


<script>
    var myApp = angular.module('myApp', []);
    myApp.controller("myCtrl",["$scope",function ($scope) {
        $( "#datepicker" ).datepicker({
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
    }])

</script>
