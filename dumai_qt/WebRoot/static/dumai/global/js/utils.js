/*
*公共方法utils
*
*/
var utils={
    /**
     * 根据身份证号码换算年龄
     */
    getAgeByIdCard:function getAgeByIdCard(idCard){
    var iAge = 0;
    var year = idCard.substring(6, 10);
    iAge = new Date().getFullYear() - parseInt(year);
    return iAge;
},
    /**
     * 根据身份证号码换算性别
     */
    getGenderByIdCard:function getGenderByIdCard(idCard){
    var gender = null;
    var sCardNum = idCard.substring(16, 17);
    if (parseInt(sCardNum) % 2 != 0) {
        gender = "男";
    } else {
        gender = "女";
    }
    return gender;
},
    /*
     * 判断多重对象
     */
    isEqual2:function (x,y) {
       x=JSON.stringify(x);
       y=JSON.stringify(y);
       if(x === y){
           return true;
       }else{
           return false;
       }
    },
    GetQueryString:function GetQueryString(name){
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null){
            return unescape(r[2])
            };
        return null;
        },
   /*
    * 时间转化为时/分
    */
    calTime:function calTime(seconds) {
        	var minutes = parseInt(seconds / 60);
        	var s = seconds % 60;
        	return minutes + ":" + s;
        }
    };
	window.utils=utils;
