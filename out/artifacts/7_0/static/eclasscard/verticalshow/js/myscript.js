$(function(){

	// 日期
	function getDate(){
		var date = new Date();
		var week = {
			0 : '日',
			1 : '一',
			2 : '二',
			3 : '三',
			4 : '四',
			5 : '五',
			6 : '六'
		};
		var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var cur_week = '星期' + week[date.getDay()];

		$('.date .time').text(hours + ":" + minutes);
		$('.date .day').text(month + '月' + day +'日');
		$('.date .week').text(cur_week);
	}
	getDate();
	setInterval(getDate, 1000);

	// 幻灯片
	if($('.slider').length > 0){
		$('.slider').each(function(){
			var that = $(this),
				counter = that.closest('.slider-wrap').find('.slider-counter');
			var sl = that.slick({
				fade: true,
				prevArrow: that.parent().find('.slider-prev'),
				nextArrow: that.parent().find('.slider-next'),
				autoplay:true
			});
			counter.text(1 + ' /' + that.find('.slick-slide').length);
			$(sl).on('afterChange', function(slick, currentSlide){
				counter.text(currentSlide.currentSlide + 1 + ' /' + currentSlide.slideCount);
			})
		})
	}
		
	// 侧边导航
	$('[data-action=tab]').on('click', function(e){
		e.preventDefault();
		var id = $(this).attr('href').split('#')[1];
		
		$(this).addClass('active').siblings().removeClass('active');
		
		
		$('#'+id).addClass('active').siblings().removeClass('active');
	})

	
});

// 通知滚动
(function($){
	var $scrollBoxs = $('[data-action=scroll]');

	if($scrollBoxs.length > 0){
		$scrollBoxs.each(function(){
			var _this = $(this);
			if(_this.outerHeight() > _this.parent().outerHeight()){
				var timer = setInterval(function(){
					if(_this.hasClass('post-list-1of2')){
						_this.children().eq(0).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
						_this.children().eq(1).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
					}else{
						_this.children().eq(0).slideUp(300, function(){
							$(this).appendTo(_this).slideDown(300);
						});
					}
				},5000)
			}
		})
	}
})(jQuery)