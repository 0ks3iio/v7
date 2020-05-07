// JavaScript Document
$(function(){

	//多级菜单
	$('.js-nav li').hover(function(){
		$(this).find('.subNav').stop().slideDown();
	},function(){
		$(this).find('.subNav').stop().slideUp();
	});
	//----------

	//显示时间
	setInterval("$('.time').html(curTime);",1000);

});
/*-------------自定义函数-------------------------*/
function a_s_r(o,c){
	o.addClass(c).siblings().removeClass(c);
}
//显示当前年月日 时分秒
function curTime(){
    var d = new Date(),
		year =d.getFullYear(),
		month = d.getMonth()+1,
	    date=d.getDate(),
    	h = d.getHours(),
		m = d.getMinutes(),
		s = d.getSeconds(),
		ww= "星期" + "日一二三四五六".charAt(d.getDay());
	return year + "年" + o(month) + "月" + o(date) +'日&nbsp;'+ww;
}

//小于10补0
function o(x){
	return x<10?'0'+x:x;
}
