var ctx = "/dumai_qt";
//
//$.ajax({
//	headers:{
//		action:"getctx",
//	},
//	async:false,
//	url:window.location.href,
//	success:function(d){
//		console.log(d);
//	}
//});


(function($){
	
	if(!$) return;
	//让$.getScript支持缓存
    $.getScript = function(url, callback, cache,holdReady) {
    	var holdReadyCtrl = typeof(holdReady)=="boolean" && holdReady;
    	if(holdReadyCtrl){
    		$.holdReady(true);
    	}
		$.ajax({type: 'GET', url: url,
			success:function(){ 
				if(holdReadyCtrl){
		    		$.holdReady(false);
		    	}
				if(typeof(callback)=="function") callback.apply(this,arguments);
			}, 
			dataType: 'script', ifModified: true, cache: cache||false
		});
	};
	
	
	/**
	 * 添加getCSS方法，动态加载CSS文件
	 * @param url CSS文件路径
	 * @param callback 回调函数
	 * @param holdReady是否在请求之前调用$.holdReady(true),成功之后调用$.holdReady(false)
	 */
    $.getCSS = function(url, callback,holdReady){
    	
    	var holdReadyCtrl = typeof(holdReady)=="boolean" && holdReady;
    	if(holdReadyCtrl){
    		$.holdReady(true);
    	}
		var done = false;
		var link = document.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.media = 'screen';
		link.href = url;
		link.onload = link.onreadystatechange = function(){
			if (!done && (!link.readyState || link.readyState == 'loaded' || link.readyState == 'complete')){
				done = true;
				link.onload = link.onreadystatechange = null;
				if(holdReadyCtrl){
		    		$.holdReady(false);
		    	}
				if (callback){
					callback.call(link);
				}
			}
		};
		document.getElementsByTagName("head")[0].appendChild(link);
	};
	
})(jQuery);
/**
 * 
 */

(function($){
	
	if(!$) return;
	
	//加载语言包
	var language =  navigator.language || navigator.userLanguage || "zh_CN";
	$.getScript(ctx+"/js/lib/jquery-easyui-1.5/locale/easyui-lang-"+language.replace("-","_")+".js",function(){
		
		if($.fn.datagrid){//设置默认值简化代码
			$.extend($.fn.datagrid.defaults, {
				loadMsg:'数据加载中...',
				method:"post",
				fit:true,
				nowrap: false,
				striped: true,			
				fitColumns: true,
				pagination:true,
				singleSelect:true,
				rownumbers:true,
				remoteSort: false,
				pageList:[10,15,20,50],
				pageSize:15,
				pagination:true,//分页属性
			});
		}
	},true);
	
	//加载主题
	$.getCSS(ctx+'/js/lib/jquery-easyui-1.5/themes/dumai/easyui.css',null,true);
	
	/**
	 * 扩展两个文本框内容一致校验
	 */
	if($.fn.validatebox){
		$.extend($.fn.validatebox.defaults.rules, {
	        /*必须和某个字段相等*/
	        equalTo: { validator: function (value, param) { return $(param[0]).val() == value; }, message: '{1}' },
	        number: {
	        	validator: function (value, param) { 
	        		return /^\d+(\.\d+)?$/.test(value); 
	        	},
	        	message: '{0}' 
	        },
	        numberLength:{
	        	validator: function (value, param) { 
	        		if(value.length!=param[0]){
	        			return false;
	        		}
	        		return /^\d+$/.test(value); 
	        	},
	        	message: '{1}' 
	        },
	        zh_CN:{//中文
	        	validator: function (value, param) { 
	        		return /^[\u4e00-\u9fa5]*$/.test(value); 
	        	},
	        	message: '{1}' 
	        }
		});
	}
	/**
	 * EasyUI菜单项右键禁止弹出浏览器菜单，并触发点击事件
	 */
	$(document).on('contextmenu',".easyui-menu",function(e){
		e.preventDefault();
		(e.srcElement || e.target).click();
	});
	
	
	//覆盖text方法，添加默认值
	var text = $.fn.text;
	$.fn.text=function(){
		
		if(arguments.length>=2){
			var arg0 = arguments[0];
			
			if(arg0==null || arg0==undefined){
				arguments[0] = arguments[1];
			}
		}
		//切记一定要return
		return text.apply(this,arguments);
	};

})(jQuery);


(function($){
	if(!$) return;
    $.extend({
        /**
         * 将日期格式化成指定格式的字符串
         * @param date 要格式化的日期，不传时默认当前时间，也可以是一个时间戳
         * @param fmt 目标字符串格式，支持的字符有：y,M,d,q,w,H,h,m,S，默认：yyyy-MM-dd HH:mm:ss
         * @returns 返回格式化后的日期字符串
         */
        formatDate: function(date, fmt){
            date = date == undefined ? new Date() : date;
            date = typeof date == 'number' ? new Date(date) : date;
            fmt = fmt || 'yyyy-MM-dd HH:mm:ss';
            var obj =
            {
                'y': date.getFullYear(), // 年份，注意必须用getFullYear
                'M': date.getMonth() + 1, // 月份，注意是从0-11
                'd': date.getDate(), // 日期
                'q': Math.floor((date.getMonth() + 3) / 3), // 季度
                'w': date.getDay(), // 星期，注意是0-6
                'H': date.getHours(), // 24小时制
                'h': date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, // 12小时制
                'm': date.getMinutes(), // 分钟
                's': date.getSeconds(), // 秒
                'S': date.getMilliseconds() // 毫秒
            };
            var week = ['天', '一', '二', '三', '四', '五', '六'];
            for(var i in obj)
            {
                fmt = fmt.replace(new RegExp(i+'+', 'g'), function(m)
                {
                    var val = obj[i] + '';
                    if(i == 'w') return (m.length > 2 ? '星期' : '周') + week[val];
                    for(var j = 0, len = val.length; j < m.length - len; j++) val = '0' + val;
                    return m.length == 1 ? val : val.substring(val.length - m.length);
                });
            }
            return fmt;
        },
        /**
         * 将字符串解析成日期
         * @param str 输入的日期字符串，如'2014-09-13'
         * @param fmt 字符串格式，默认'yyyy-MM-dd'，支持如下：y、M、d、H、m、s、S，不支持w和q
         * @returns 解析后的Date类型日期
         */
        parseDate: function(str, fmt)
        {
            fmt = fmt || 'yyyy-MM-dd';
            var obj = {y: 0, M: 1, d: 0, H: 0, h: 0, m: 0, s: 0, S: 0};
            fmt.replace(/([^yMdHmsS]*?)(([yMdHmsS])\3*)([^yMdHmsS]*?)/g, function(m, $1, $2, $3, $4, idx, old)
            {
                str = str.replace(new RegExp($1+'(\\d{'+$2.length+'})'+$4), function(_m, _$1)
                {
                    obj[$3] = parseInt(_$1);
                    return '';
                });
                return '';
            });
            obj.M--; // 月份是从0开始的，所以要减去1
            var date = new Date(obj.y, obj.M, obj.d, obj.H, obj.m, obj.s);
            if(obj.S !== 0) date.setMilliseconds(obj.S); // 如果设置了毫秒
            return date;
        },
        /**
         * 将一个日期格式化成友好格式，比如，1分钟以内的返回“刚刚”，
         * 当天的返回时分，当年的返回月日，否则，返回年月日
         * @param {Object} date
         */
        formatDateToFriendly: function(date)
        {
            date = date || new Date();
            date = typeof date === 'number' ? new Date(date) : date;
            var now = new Date();
            if((now.getTime() - date.getTime()) < 60*1000) return '刚刚'; // 1分钟以内视作“刚刚”
            var temp = this.formatDate(date, 'yyyy年M月d');
            if(temp == this.formatDate(now, 'yyyy年M月d')) return this.formatDate(date, 'HH:mm');
            if(date.getFullYear() == now.getFullYear()) return this.formatDate(date, 'M月d日');
            return temp;
        },
        /**
         * 将一段时长转换成友好格式，如：
         * 147->“2分27秒”
         * 1581->“26分21秒”
         * 15818->“4小时24分”
         * @param {Object} second
         */
        formatDurationToFriendly: function(second)
        {
            if(second < 60) return second + '秒';
            else if(second < 60*60) return (second-second%60)/60+'分'+second%60+'秒';
            else if(second < 60*60*24) return (second-second%3600)/60/60+'小时'+Math.round(second%3600/60)+'分';
            return (second/60/60/24).toFixed(1)+'天';
        },
        /** 
         * 将时间转换成MM:SS形式 
         */
        formatTimeToFriendly: function(second)
        {
            var m = Math.floor(second / 60);
            m = m < 10 ? ( '0' + m ) : m;
            var s = second % 60;
            s = s < 10 ? ( '0' + s ) : s;
            return m + ':' + s;
        },
        /**
         * 判断某一年是否是闰年
         * @param year 可以是一个date类型，也可以是一个int类型的年份，不传默认当前时间
         */
        isLeapYear: function(year)
        {
            if(year === undefined) year = new Date();
            if(year instanceof Date) year = year.getFullYear();
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        },
        /**
         * 获取某一年某一月的总天数，没有任何参数时获取当前月份的
         * 方式一：$.getMonthDays();
         * 方式二：$.getMonthDays(new Date());
         * 方式三：$.getMonthDays(2013, 12);
         */
        getMonthDays: function(date, month)
        {
            var y, m;
            if(date == undefined) date = new Date();
            if(date instanceof Date)
            {
                y = date.getFullYear();
                m = date.getMonth();
            }
            else if(typeof date == 'number')
            {
                y = date;
                m = month-1;
            }
            var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]; // 非闰年的一年中每个月份的天数
            //如果是闰年并且是2月
            if(m == 1 && this.isLeapYear(y)) return days[m]+1;
            return days[m];
        },
        /**
         * 计算2日期之间的天数，用的是比较毫秒数的方法
         * 传进来的日期要么是Date类型，要么是yyyy-MM-dd格式的字符串日期
         * @param date1 日期一
         * @param date2 日期二
         */
        countDays: function(date1, date2)
        {
            var fmt = 'yyyy-MM-dd';
            // 将日期转换成字符串，转换的目的是去除“时、分、秒”
            if(date1 instanceof Date && date2 instanceof Date)
            {
                date1 = this.format(fmt, date1);
                date2 = this.format(fmt, date2);
            }
            if(typeof date1 === 'string' && typeof date2 === 'string')
            {
                date1 = this.parse(date1, fmt);
                date2 = this.parse(date2, fmt);
                return (date1.getTime() - date2.getTime()) / (1000*60*60*24);
            }
            else
            {
                console.error('参数格式无效！');
                return 0;
            }
        }
    });
    
    $.extend({
    	getFormatter:function(format){
    		return function(v,r,i){
    			return v?$.formatDate(new Date(v),format):"";
    		};
    	},
    	deserialize:function(qs){
    		var kvs = qs.split("&");
    		
    		var obj = {};
    		
    		for(var i in kvs){
    			var tmp =  kvs[i];
    			var kv = tmp.split("=");
    			
    			var k = decodeURIComponent(kv[0]);
    			
    			if(kv.length>1){
    				v = decodeURIComponent(tmp.substring(tmp.indexOf("=")+1));
    			}
    			else {
    				v="";
    			}
    			
    			if(obj[k]!=undefined){
    				obj[k] = [obj[k]];
    				obj[k].push(v);
    			}
    			else obj[k]=v;
    		}
    		return obj;
    	},
    	emptyIf:function(text){
    		if(text===undefined || text===null){
    			return "";
    		}
    		var args = Array.prototype.slice.call(arguments, 1);
    		for(var x in args){
    			if(args[x]===text){
    				return "";
    			}
    		}
    		return text;
    	}
    });
})(jQuery);