/**
 * Created by Administrator on 2017/4/11 0011.
 */
var myMod = angular.module('myMod', ['ngRoute','GPSMod']);
myMod.run(function ($rootScope) {
    $rootScope.param= location.search;
});
myMod.config(function ($routeProvider) {
    $routeProvider.when('/gpsAlarm',{
        templateUrl:'../static/templates/gps_alarm.html'
    }).when('/gpsInfo',{
        templateUrl:'../static/templates/gps_info.html',
    }).otherwise('/gpsAlarm');
});