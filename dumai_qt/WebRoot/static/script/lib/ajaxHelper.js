/**
 * @name:common data channel
 * @description: designed by tianxinyang 
 * @author:tianxinyang
 * @time:
 * @params:ALL function is available
 */


/**
 * contextPath
 */
var IDUMAI_PATH = {
	contextPath: function(){return $("script[name='contextPath']").attr("data-value");}	
		
};

/**
 * params
 */
var IDUMAI_PARAMS = {
		PARAMS : {
				con : {
						fun : "" ,
						des : "" ,
				},
				export  : [],
				execute : {},
				error   : new Array(),
		},
		INIT	:  function(data){
			this.PARAMS.con.fun = data.fun;
			this.PARAMS.con.des = data.des;
	    	this.PARAMS.export = data.export;
	    	this.PARAMS.execute = data.execute;
	    	return this.PARAMS;
	    }
};


/**
 * constant
 */
var IDUMAI_CONSTANT={
	URL: {
		commonControllerChannel		: "commonControllerChannel.do",
		commonControllerChannel2 	: IDUMAI_PATH.contextPath()+"/commonControllerChannel/",
		contextPath					: IDUMAI_PATH.contextPath()+"/"
	},
	METHOD: {
		POST: "POST",
		GET: "GET",
		PUT: "PUT",
		DELETE: "DELETE"
	}
};

/**
 * prepareData
 */
var IDUMAI_PREPAREDATA = {
		prepareSaveData: function(oElement) {
			var saveJOSN = {};
			$.each($(oElement).find('input,select,textarea'), function(index, row) {
				if ($(row).attr("data-save-code")&&$(row).val()!=""&&$(row).val() != null ) {
					saveJOSN[$(row).attr("data-save-code").toString().toUpperCase()] = $(row).val() === null ? "" : $(row).val().toString().toUpperCase();
				}
			});
			return saveJOSN;
		}
};


/**
 * check
 */
var IDUMAI_VALIDATION = {
		isMobile: function(str) {
			var reg = /^((\+?86)|(\(\+86\)))?1\d{10}$/;
	        return reg.test(str);
		},
		isChChar:function(str){
			 var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
			 return reg.test(str);
		},
		isMath:function(str){
			 var reg = /^[0-9]*$/;
			 return reg.test(str);
		},
		isEng_Math:function(str){
			 var reg = /^\w+$/;
			 return reg.test(str);
		}
};

var IDUMAI_MSG = function(msg){
	var str ="";
	$.each(msg,function(index,row){
		if(typeof(row)=='object'){
			if(row.MESSAGE){
				str += index+1+":"+row.MESSAGE+"\n";
			}
		}else{
			str += index+1+":"+row+"\n";
		}
	});
	alert(str);
}

var IDUMAI_ERROR = function(oData){
	if (oData.error.length==0) {
		if(oData.export.PT_ERROR&&oData.export.PT_ERROR.length!=0){
			IDUMAI.M(oData.export.PT_ERROR);
			return false;
		}else{
			return true;
		}
	} else {
		IDUMAI.M(oData.error);
		return false;
	}
}

/**
 * ajax function success or failure
 */
var IDUMAI_FUNCTION = {
		success:function(data){alert("�����ɹ���");console.info(data);},
		failure:function(data){alert("����ʧ�ܣ�");console.info(data);}
};

/**
 * tool contains ajax
 */

eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('6 4={C:z,V:J,P:K,M:R,E:13,F:v,D:{},l:2(e){6 f=e.m||j;3(t(f)==="j"){S.T("W Z 12 \'m")}6 g=e.16||"",9=e.9||j,1={w:f,x:e.y||"p",1a:e.A||"B/p; G=H-8",q:e.q||"I",i:e.i===5?5:n||n,L:N,O:5,o:e.o,Q:2(a,b,c){g?a.k.U===0?g(a):X(a.k[0]):4.D=a},k:2(a,b,c,d){9?9(a):4.F.Y(a)}};6 h=e.10;3(h){3(t(h)=="11"){1["r"]=h}s{1["r"]=14.15(h)}}3(g){7 $.u(1)}s{1.i=5;$.u(1);7 18.D}}};(2($){3(!$){7}$.19({l:2(a){7 4.l(a)},})})(17);',62,73,'|params|function|if|IDUMAI|false|var|return||funFail|||||||||async|undefined|error|AJAX|path|true|beforeSend|json|type|data|else|typeof|ajax|IDUMAI_FUNCTION|url|dataType|dType|IDUMAI_CONSTANT|cType|application|||||charset|utf|POST|IDUMAI_VALIDATION|IDUMAI_PARAMS|context||null|cache||success|IDUMAI_MSG|console|log|length||Missing|alert|failure|the|content|string|parameter|IDUMAI_ERROR|JSON|stringify|funSuccess|jQuery|this|extend|contentType'.split('|'),0,{}))
