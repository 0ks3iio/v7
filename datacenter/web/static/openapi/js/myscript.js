$(function(){
	//框架
	var winH=$(window).height();
	var conH=winH-$('#header').height()-$('#footer').height()-10;
	$('#container').css('min-height',conH);
	
	//表格奇偶行背景色分隔、鼠标滑过变色效果
	function interval(obj){
		$(obj).each(function(){
			var mod=$(this).index()%2;
			if(mod==1){$(this).addClass('odd')};
		});
		$(obj).hover(function(){
			$(this).addClass('hover').siblings('tr').removeClass('hover');
		},function(){
			$(this).removeClass('hover');
		});
	};
	interval('.public-table tr');

	//tab选项卡
	$('.ui-tab-list li').click(function(){
		var $tabWrap=$(this).parents('.ui-tab').children('.ui-tab-wrap');
		$(this).addClass('current').siblings('li').removeClass('current');
		$tabWrap.children('.ui-tab-module:eq('+$(this).index()+')').show().siblings('.ui-tab-module').hide();
	});
	
	//
	$('.app-wrap .dt').click(function(){
		var $wrap=$(this).parent('.app-wrap');
		if(!$wrap.hasClass('app-wrap-open')){
			$wrap.addClass('app-wrap-open');
		}else{
			$wrap.removeClass('app-wrap-open');
		};
	});
	$('.get-code').click(function(e){
		e.preventDefault();
		$(this).parents('.ui-tab-module').find('.get-code-wrap').show();
	});
});