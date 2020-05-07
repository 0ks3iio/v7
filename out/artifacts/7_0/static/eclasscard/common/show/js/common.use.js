var serverCheckTimes = 0;
/* 请求服务，成功说明服务可用，返回true,跳转正常页;否则返回false，跳提示页 */
function getServerUsable(contextPath) {
	var defer = $.Deferred();
	var xhr = $.ajax({
		url:contextPath+'/eccShow/eclasscard/get/server/usable',
		data:{},
		timeout : 5000, //超时时间设置，单位毫秒
		type:'post',
		success:function(data){
			defer.resolve(true);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			defer.resolve(false);
		},
		complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
	　　　　        if(status=='timeout'){
	　　　　        	defer.resolve(false);
	　　　　        	xhr.abort();
			}
　　		}
	});
	return defer.promise();
}
/*检测服务是否可用，可用执行传入函数，不可用跳转提示页*/
function doSkipPage(contextPath,interval_time) {
	 $.when(getServerUsable(contextPath)).done(function(data){
		if(!data){
			if (window.jsInterface && serverCheckTimes>1) {
				if(interval_time)
				clearInterval(interval_time);  
				jsInterface.refresh();
			}
			serverCheckTimes+=1;
			//失败，立马再次检测
			if(serverCheckTimes <= 2)
			doSkipPage(contextPath,interval_time);
		}else{
			serverCheckTimes = 0;
		}
	});
}

/*获取地址栏参数*/
function GetQueryString(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null) return  unescape(r[2]); return null;
}

/*时间格式化*/
Date.prototype.format = function(fmt) {
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
