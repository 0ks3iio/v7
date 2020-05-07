/*此文件中展示时间，移到header页面
 * 通知公告滚动，移到通知公告页面*/
$(function(){

	var albumScript = document.getElementById('albumScript').getAttribute('data');

	if (albumScript == null || albumScript == "") {
		albumScript = "5000";
	}

	// 幻灯片
	if($('.slider').length > 0){
		$('.slider').each(function(){
			var that = $(this),
				counter = that.closest('.slider-wrap').find('.slider-counter');
			var sl = that.slick({
				fade: true,
				prevArrow: false,
				nextArrow: false,
				autoplay: true,
				autoplaySpeed: albumScript
			});
			var len = that.find('.slick-slide').length
			if(len > 1){
				counter.text(1 + ' /' + len).show();
			}
			
			$(sl).on('afterChange', function(slick, currentSlide){
				counter.text(currentSlide.currentSlide + 1 + ' /' + currentSlide.slideCount);
			});
		})
	}
		
	// 侧边导航
	$('[data-action=tab]').on('click', function(e){
		e.preventDefault();
		var id = $(this).attr('href').split('#')[1];
		
		$(this).parent().addClass('active').siblings().removeClass('active');
		
		
		$('#'+id).addClass('active').siblings().removeClass('active');
	})

	
});
