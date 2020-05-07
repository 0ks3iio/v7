/*
 * 操作系统+浏览器版本监测，升级提醒功能
 * IE9以下浏览器提示升级
 * 如当前使用的是“搜狗、QQ”兼容模式，可升级的浏览器列表隐藏当前浏览器；360UA限制无法判断，特增加下面一条提示
 * 如您当前正在使用的是"360、QQ、搜狗、2345"等……双核浏览器，出现此提示请切换浏览器内核到极速模式
 * IE6不可关闭
 * winxp：IE9以下提示升级，chrome、firefox、360安全、QQ桌面、搜狗高速
 * win7：IE9以下提示升级，chrome、firefox、ie11、360安全、QQ桌面
*/
var uaClose = getCookie('updateClose');

if (uaClose == null) {
	var x = navigator;
	var ua = x.userAgent;
	
	//判断浏览器
	var isOpera = ua.indexOf('Opera') > -1;
	var isIE = ua.indexOf('compatible') > -1 && ua.indexOf('MSIE') > -1 && !isOpera;
	var isSG = ua.indexOf('MetaSr') > -1;	    //搜狗
	var isQQ = ua.indexOf('QQBrowser') > -1;	//QQ
	
	//判断windows版本
	var isWin = ua.indexOf('Windows NT') > -1;
	if (isWin) {
		var winStart = ua.indexOf('Windows NT');
		var winStr = ua.substring(winStart);
		var winEnd = winStr.indexOf(';');
		winStr = winStr.substring(0,winEnd);
		winVer = parseFloat(winStr.replace('Windows NT',''));
	};
	
	if(isIE) {
		var reIE = new RegExp('MSIE (\\d+\\.\\d+);');
		reIE.test(ua);
		var fIEVersion = parseFloat(RegExp['$1']);
		
		if(fIEVersion < 9) {
			var _kehou = 0;
			browserInit('hi，您当前使用的浏览器版本过低，可能存在安全风险<br />建议您升级浏览器，获得更佳、更安全的网站体验','如您当前正在使用的是"360、QQ、搜狗、2345"等……双核浏览器，<br />出现此提示请<a href="https://jingyan.baidu.com/article/22a299b539f4b19e18376a5b.html" target="_blank">切换浏览器内核</a>到极速模式','关闭');
			
			//课后网定制
			//var _kehou = 1;
			//browserInit('您当前使用的浏览器版本过低<br />建议您升级或更换浏览器，获得更好的使用体验。','如果您当前使用的是"360、QQ、搜狗、2345"等浏览器，<br />请切换到极速模式。<a href="https://jingyan.baidu.com/article/22a299b539f4b19e18376a5b.html" target="_blank">如何切换极速模式？</a><br />推荐以下浏览器：','以后再说');
			
			if(fIEVersion == 6) {
				$browserClose.hide();
			};
			if (_kehou == 1) {
				$browserIe.hide();
				$browserQq.hide();
				$browserSougou.hide();
				$browserS360.hide();
			} else{
				if (winVer > 6) {
					$browserSougou.hide();
				} else{
					$browserIe.hide();
				};
				if (isQQ) {
					$browserQq.hide();
				};
				if (isSG) {
					$browserSougou.hide();
				};
				$browserC360.hide();
			}
		};
	};
};

//初始化
function browserInit(_title,_dicaryon,_close){
	var Css = '<style type="text/css">'+
		'html.browser-hide,body.browser-hide{overflow:hidden;font:14px/1.5 "Microsoft YaHei","SimSun",Tahoma,Verdana,Arial;outline:none;}'+
		'.browser-update-mask{position: absolute;top: 0;right: 0;bottom: 0;left: 0;z-index:9996;background:#000;filter:Alpha(opacity=20);}'+
		'.browser-update-layer{position:absolute;z-index:9997;width: 620px;height:420px;border-radius: 5px;}'+
		'.browser-update-modal,.browser-update-prop{position:absolute;top:0;right:0;width:560px;height:360px;border-radius: 5px;padding: 30px;overflow:hidden;text-align:center;box-sizing:content-box;}'+
		'.browser-update-modal{z-index:9998;background:#fff;filter:Alpha(opacity=80);}'+
		'.browser-update-prop{z-index:9999;}'+
		'.browser-update-prop a{text-decoration: none;color: #fd5471;}'+
		'.browser-update-prop a:hover{text-decoration:underline;}'+
		'.browser-update-header{line-height: 1.8;font-size: 18px;}'+
		'.browser-upadte-dicaryon{margin-top:20px;}'+
		'.browser-update-list{padding: 30px 0 50px;white-space:nowrap;}'+
		'.browser-update-item{display: inline-block;width: 100px;}'+
		'.browser-update-item span{display: block;text-align: center;}'+
		'.browser-update-item .icon{width: 64px;height: 64px;background: url(https://kehouweb.b0.upaiyun.com//dev/images/browser.png) no-repeat;margin: 0 auto 5px;}'+
		'.browser-update-item.chrome .icon{background-position: 0 0;}'+
		'.browser-update-item.firefox .icon{background-position: 0 -64px;}'+
		'.browser-update-item.ie .icon{background-position: 0 -128px;}'+
		'.browser-update-item.s360 .icon{background-position: 0 -192px;}'+
		'.browser-update-item.qq .icon{background-position: 0 -256px;}'+
		'.browser-update-item.sougou .icon{background-position: 0 -320px;}'+
		'.browser-update-item.c360 .icon{background-position: 0 -384px;}'+
		'.browser-update-item a{display: inline-block;background-color: #fd5471;border-radius: 3px;padding: 5px 10px;margin-top: 10px;color: #fff;}'+
		'.browser-update-item a:hover{background-color: #ff8b9f;text-decoration: none;}'+
		'</style>';
	$('head').append(Css);
	
	var Html = '<div class="browser-update-mask"></div>'+
		'<div class="browser-update-layer">'+
		'<div class="browser-update-modal"></div>'+
		'<div class="browser-update-prop">'+
		'<div class="browser-update-header">'+ _title +'</div>'+
		'<div class="browser-upadte-dicaryon">'+ _dicaryon +'</div>'+
		'<div class="browser-update-list">'+
		'<span class="browser-update-item chrome">'+
		'<span class="icon"></span>'+
		'<span class="name">Chrome</span>'+
		'<a href="http://rj.baidu.com/soft/detail/14744.html" target="_blank">官方下载</a>'+
		'</span>'+
		'<span class="browser-update-item firefox">'+
		'<span class="icon"></span>'+
		'<span class="name">Firefox</span>'+
		'<a href="http://www.firefox.com.cn/" target="_blank">官方下载</a>'+
		'</span>'+
		'<span class="browser-update-item ie">'+
		'<span class="icon"></span>'+
		'<span class="name">IE</span>'+
		'<a href="https://www.microsoft.com/zh-cn/download/internet-explorer.aspx" target="_blank">官方下载</a>'+
		'</span>'+
		'<span class="browser-update-item s360">'+
		'<span class="icon"></span>'+
		'<span class="name">360安全</span>'+
		'<a href="http://se.360.cn/" target="_blank">官方下载</a>'+
		'</span>'+
		'<span class="browser-update-item c360">'+
		'<span class="icon"></span>'+
		'<span class="name">360极速</span>'+
		'<a href="http://chrome.360.cn/" target="_blank">官方下载</a>'+
		'</span>'+
		'<span class="browser-update-item qq">'+
		'<span class="icon"></span>'+
		'<span class="name">QQ桌面</span>'+
		'<a href="http://browser.qq.com/" target="_blank">官方下载</a>'+
		'</span>'+
		'<span class="browser-update-item sougou">'+
		'<span class="icon"></span>'+
		'<span class="name">搜狗高速</span>'+
		'<a href="http://ie.sogou.com/" target="_blank">官方下载</a>'+
		'</span>'+
		'</div>'+
		'<div class="browser-update-footer">'+
		'<a href="#" class="browser-update-close">'+ _close +'</a>'+
		'</div>'+
		'</div>'+
		'</div>';
	$('body').prepend(Html);
	
	$('html,body').addClass('browser-hide');
	$browserMask = $('.browser-update-mask');
	$browserLayer = $('.browser-update-layer');
	$browserClose = $('.browser-update-close');
	$browserIe = $('.browser-update-item.ie');
	$browserSougou = $('.browser-update-item.sougou');
	$browserQq = $('.browser-update-item.qq');
	$browserS360 = $('.browser-update-item.s360');
	$browserC360 = $('.browser-update-item.c360');
	$browserLayer.css({
		top: ($(window).height() - $browserLayer.outerHeight()) / 2,
		left: ($(window).width() - $browserLayer.outerWidth()) / 2
	})
	$browserClose.click(function(e){
		e.preventDefault();
		$browserMask.remove();
		$browserLayer.remove();
		$('html,body').removeClass('browser-hide');
		//设置cookie关闭
		setCookie('updateClose','yes');
	});
};

//cookie相关
///设置cookie   
function setCookie(NameOfCookie, value, expiredays) {
	var ExpireDate = new Date();
	ExpireDate.setTime(ExpireDate.getTime() + (expiredays * 24 * 3600 * 1000));
	document.cookie = NameOfCookie + "=" + escape(value) +
		((expiredays == null) ? "" : "; expires=" + ExpireDate.toGMTString());
};
///获取cookie值   
function getCookie(NameOfCookie) {
	if(document.cookie.length > 0) {
		begin = document.cookie.indexOf(NameOfCookie + "=");
		if(begin != -1) {
			begin += NameOfCookie.length + 1; //cookie值的初始位置   
			end = document.cookie.indexOf(";", begin); //结束位置   
			if(end == -1) end = document.cookie.length; //没有;则end为字符串结束位置   
			return unescape(document.cookie.substring(begin, end));
		};
	};
	return null;
};