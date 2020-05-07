function closeFilter(){
	document.getElementById('mask').remove();
	document.getElementById('filter').style.right='-7.6rem';
};
function closeFilterFull(){
	document.getElementById('mask').remove();
	document.getElementById('filterFull').removeAttribute('style');
};
function closeShowAll(){
	document.getElementById('showAllLayer').style.display='none';
	document.getElementById('______footerMask').remove();
};

//取消链接点击外部虚线框
$('a').attr('hideFocus','true');

$(function(){
	//loading
	function winLoad(){
		var pageH=$(window).height();
		$('#page').height(pageH);
	};
	winLoad();
	$(window).resize(function(){
		winLoad();
	});
	$('#page').css('background','none').children('#pageInner').delay(100).removeClass('hidde');
	
	//按钮效果
	$('a,.unit-list li,.submit-outer').on('touchstart', function(){
		$(this).addClass('active');
	});
	$('a,.unit-list li,.submit-outer').on('touchend', function(){
		$(this).removeClass('active');
	});
	
	//footer
	$('footer a').click(function(e){
		e.preventDefault();
		$(this).addClass('active').siblings('a').removeClass('active');
	});
	
	//tab
	$('.tab li').on('touchstart', function(){
		$(this).addClass('current').siblings('li').removeClass('current');
		$('.tab-grid .tab-item:eq('+$(this).index()+')').show().siblings('.tab-item').hide();
	});
	
	//checkbox
	$('.ui-checkbox').on('touchstart', function(){
		if(!$(this).hasClass('ui-radio')){
			if(!$(this).hasClass('ui-checkbox-current')){
				$(this).addClass('ui-checkbox-current');
			}else{
				$(this).removeClass('ui-checkbox-current');
			};
		}else{
			$(this).parents('form').find('.ui-radio').removeClass('ui-checkbox-current');
			$(this).addClass('ui-checkbox-current');
		};
	});
	
	//header more-sel
	$('.more-sel').click(function(){
		$('.more-layer').toggle();
	});
	$('.more-layer p').click(function(e){
		e.preventDefault();
		$(this).addClass('current').siblings('p').removeClass('current');
		$(this).parent('.more-layer').hide().siblings('.more-sel').children('.txt').text($(this).text());
	});
	$(document).click(function(event){
		var eo=$(event.target);
		if($('.more-layer').is(':visible') && !eo.hasClass('more-sel') && !eo.parent('span').hasClass('more-sel'))
		$('.more-layer').hide();  
	});
	
	//header search
	$('header .abtn-search').on('touchstart', function(){
		$('.list-search-wrap').toggle();
	});
	
	//ui-search
	//$('.ui-search input').on('focus',function(){
		//$(this).parent('.ui-search-inner').siblings('.ui-search-opt').children('.btn').show().siblings('.filter').hide();
	//});
	//$(document).on('click',function(event){
		//var eo=$(event.target);
		//if($('.ui-search .btn').is(':visible') && !eo.parents('div').hasClass('ui-search'))
		//$('.ui-search .btn').hide().siblings('.filter').show();
	//});
	//上弹层-设备名称选择
	$('.device-name').click(function(e){
		e.preventDefault();
		$('body').prepend('<div class="ui-layer-mask ui-layer-mask-close" id="mask" onclick="closeFilterFull()"></div>');
		$('.filter-layer').animate({'bottom':'0'},500);
	});
	//筛选
	$('.ui-search .filter').click(function(e){
		e.preventDefault();
		$('body').prepend('<div class="ui-layer-mask ui-layer-mask-close" id="mask" onclick="closeFilter()"></div>');
		$('.filter-layer').animate({'right':'0'},500);
	});
	$('.filter-layer .opt .submit').click(function(e){
		e.preventDefault();
		$('.ui-layer-mask').remove();
		if($('.filter-layer').hasClass('filter-layer-full')){
			$('.filter-layer').animate({'bottom':'-9.5rem'},500);
			var dev_cate=$('.filter-layer li.current').parent('ul').prev('p.tt').text();
			var dev_name=$('.filter-layer li.current').text();
			$('.device-cate input.txt').val(dev_cate);
			$('.device-name input.txt').val(dev_name);
		}else{
			$('.filter-layer').animate({'right':'-7.6rem'},500);
		};
	});
	$('.filter-layer .opt .reset').click(function(e){
		e.preventDefault();
		$('.filter-layer li').removeClass('current');
	});
//	$('body').on('click','.ui-layer-mask-close',function(e){
//		e.preventDefault();
//		$(this).remove();
//		if($('.filter-layer').hasClass('filter-layer-full')){
//			$('.filter-layer').animate({'bottom':'-9.5rem'},500);
//		}else{
//			$('.filter-layer').animate({'right':'-7.6rem'},500);
//		};
//	});
	$('.filter-layer li').click(function(){
		if(!$('.filter-layer').hasClass('filter-layer-full')){
			$(this).addClass('current').siblings('li').removeClass('current');
		}else{
			$('.filter-layer li').removeClass('current');
			$(this).addClass('current');
		};
	})
	//全部
	$('.show-all').click(function(){
		if(!$('.show-all-layer').is(':visible')){
			//alert(1);
			var allH=$('.scroll-wrap').height();
			var tabH=$('.tab-wrap').height();
			var searchH=$('.ui-search').innerHeight();
			var layerH=allH-tabH-searchH;
			$('.show-all-layer').css('height',layerH).show();
			if($('footer').length){
				if($('#______footerMask').length){
					$('#______footerMask').remove();
				};
				$('body').append('<div id="______footerMask" style="position:fixed;bottom:0;left:0;width:100%;height:1.413333rem;background:rgba(0,0,0,0.7);"></div>');
			};
		}else{
			$('.show-all-layer').hide();
			$('#______footerMask').remove()
		};
	});
	$('.show-all-layer ul li').click(function(){
		$(this).addClass('current').siblings('li').removeClass('current');
		$('.show-all-layer').hide();
		$('.show-all font').text($(this).text());
		//$('#______footerMask').remove();
	});
	
	
	//通讯录
	$('.address-book-sel li').click(function(){
		var this_id=$(this).attr('data-id');
		var this_txt=$(this).children('.name').text();
		if(!$(this).hasClass('current')){
			$(this).addClass('current');
			$('.address-footer .scroll').append('<a href="#" data-id="'+this_id+'">'+this_txt+'</div>');
		}else{
			$(this).removeClass('current');
			$('.address-footer .scroll a[data-id='+this_id+']').remove();
		};
		var this_num=$('.address-footer .scroll a').length;
		$('.address-footer .submit span').text(this_num);
	});
	$('.address-footer .scroll').on('click','a',function(e){
		e.preventDefault();
		var this_id=$(this).attr('data-id');
		var this_txt=$(this).children('.name').text();
		$('.address-book-sel li[data-id='+this_id+']').removeClass('current');
		$(this).remove();
		var this_num=$('.address-footer .scroll a').length;
		$('.address-footer .submit span').text(this_num);
	});
//	var sl_width=$('.address-book-list .slide-wrap ul').width();
//	$('.address-book-list .slide-wrap').width(sl_width*2);
//	$('.address-book-list .slide-wrap ul').width(sl_width);
//	$('.address-book-list .slide-wrap ul.unit li').click(function(){
//		$('.address-book-list .slide-wrap').animate({'left':'-'+sl_width+'px'},500);
//	});
	
	//ui-form
	$('.ui-form li').each(function(){
        var $tt=$(this).children('.tt');
        var $dd=$(this).children('.dd');
		var li_width=$(this).width();
		var icon_width=$(this).children('.icon-img').width();
		var tt_width=$tt.width();
		var dd_padding=parseInt($dd.css('padding-right'))+parseInt($dd.css('padding-left'));
		var dd_width=li_width-tt_width-icon_width-dd_padding-20;
		$dd.width(dd_width);
    });
	$('.ui-form li').click(function(){
		$(this).find('input,select').focus();
	});
	
	//输入框被输入法覆盖问题
//	$('input:text,textarea').on('focus',function(){
//		var ori_h1=$(window).height();
//		var my_top=$(this).offset().top-10;
//		$(window).resize(function(){
//			var ori_h2=$(window).height();
//			if(ori_h1>ori_h2){
//				$('.scroll-wrap').css('margin-top','-'+my_top+'px');
//			}else{
//				$('.scroll-wrap').css('margin-top',0);
//			};
//		});
//		$('.scroll-wrap').scroll(function(){
//			$(this).css('margin-top',0);
//		});
//	});

	//公文详情
	$('#container').on('click','.note-des .more',function(){
		if(!$('.note-des').hasClass('note-des-open')){
			$('.note-des').addClass('note-des-open');
		}else{
			$('.note-des').removeClass('note-des-open');
		};
	});

});