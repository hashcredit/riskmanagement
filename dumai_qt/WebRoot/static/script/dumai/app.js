/**
 * Created by Administrator on 2016/12/7 0007.
 */
    define(['jquery',"angular","angular-ui-router","event","ng-grid","angular-animate","controller"],function($, angular){
            var app = angular.module("webApp", ['ui.router','ruleCtrlMod']);
            app.run(function ($rootScope,$state,$stateParams) {
                $rootScope.$state=$state;
                $rootScope.$stateParams=$stateParams;
            });
            app.config(function ($stateProvider,$urlRouterProvider) {
            	 $urlRouterProvider.otherwise('/login');
                $stateProvider.state('first',{
                    url:'/first',
                    templateUrl:'static/templates/first.html'
                }).state('two',{
                    url:'/two/{ruleGroup:[0-9]{1,2}}',
                    views:{
                        '':{
                            templateUrl:'static/templates/user.html'
                        },
                        'two_1@two':{
                            templateUrl:'static/templates/user_1.html'
                        },
                        'two_2@two':{
                            templateUrl:'static/templates/user_2.html'
                        }
                    }
                }).state('third',{
                    url:'/third',
                    templateUrl:'static/templates/dataManage.html'
                }).state('four',{
                    url:'/four',
                    templateUrl:'static/templates/four.html'
                }).state('goldModel',{
                    url:'/goldModel',
                    templateUrl:'static/templates/goldModel.html'
                }).state('login',{
                    url:'/login',
                    templateUrl:'static/templates/login.html'
                });
            });
            return app;
        });

