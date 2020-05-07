<div class="banner-ppt">
	<ul class="img-ppt clearfix">
		<#if attachments?exists&&attachments?size gt 0>
		<#list attachments as ppt>
			<li><a href="#"><img src="${request.contextPath}${ppt.showPicUrl!}"></a></li>
		</#list>
		</#if>
	</ul>
	<div class="btn-ppt hide">
		<a class="arrow arrow-left prev" href="#">&lt;</a>
		<a class="arrow arrow-right next" href="#">&gt;</a>
	</div>
</div>
<script>
$(document).ready(function(){
	clearInterval(classsapcetimer);
	
	var i=0;
	$('.banner-ppt').height($('.js-height').height()-100);
	$('.banner-ppt .img-ppt li').width($('.js-height').width()-30);
	var firstimg=$('.img-ppt li').first().clone(); 
	$('.img-ppt').append(firstimg).width($('.img-ppt li').length*($('.img-ppt li').width())); 
	
	// 下一个按钮
	$('.btn-ppt .next').click(function(){
		i++;
		if (i==$('.img-ppt li').length) {
			i=1; //这里不是i=0
			$('.img-ppt').css({left:0}); //保证无缝轮播，设置left值
		};
						   
		$('.img-ppt').stop().animate({left:-i*$('.img-ppt li').width()},300);
	})
	// 上一个按钮
	$('.btn-ppt .prev').click(function(){
		i--;
		if (i==-1) {
			i=$('.img-ppt li').length-2;
			$('.img-ppt').css({left:-($('.img-ppt li').length-1)*$('.img-ppt li').width()});
		}
		$('.img-ppt').stop().animate({left:-i*$('.img-ppt li').width()},300);
	})
	//设置按钮的显示和隐藏
	$('.banner-ppt').hover(function(){
		$('.btn-ppt').show();
	},function(){
		$('.btn-ppt').hide();
	})
					
	//定时器自动播放
	classsapcetimer=setInterval(function(){
		i++;
		if (i==$('.img-ppt li').length) {
			i=1;
			$('.img-ppt').css({left:0});
		};
		$('.img-ppt').stop().animate({left:-i*$('.img-ppt li').width()},300);
	},3000)
				  
	//鼠标移入，暂停自动播放，移出，开始自动播放
	$('.banner-ppt').hover(function(){ 
		clearInterval(classsapcetimer);
		},function(){
			classsapcetimer=setInterval(function(){
				i++;
				if (i==$('.img-ppt li').length) {
					i=1;
					$('.img-ppt').css({left:0});
				};
				$('.img-ppt').stop().animate({left:-i*$('.img-ppt li').width()},300);
		},3000)
	})
});
</script>