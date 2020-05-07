<!--PPT浏览-->
<div id="ppts-group" class="tab-pane">
	<div class="card-list card-list-large no-margin clearfix js-show">	
		<div class="media-part ppt-sign block">
			<div class="banner-ppt">
				<ul class="img-ppt clearfix">
					<#if attachments?exists&&attachments?size gt 0>
					<#list attachments as item>
					<li><a href="javascript:;"><img src="${request.contextPath}${item.showPicUrl!}" alt=""></a></li>
					</#list>
					</#if>
				</ul>
				<div class="btn-ppt">
					<a class="arrow arrow-left prev" href="#">&lt;</a>
		    		<a class="arrow arrow-right next" href="#">&gt;</a>
			 	</div>
			</div>
		</div>
	</div>
</div>
<script>
clearInterval(interval_timer);
$('.banner-ppt').height("540px");
$('.banner-ppt .img-ppt li').width("900px");
//PPT轮播
var i=0;
var timer=null;
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
interval_timer=setInterval(function(){
	i++;
	if (i==$('.img-ppt li').length) {
		i=1;
	  	$('.img-ppt').css({left:0});
	};
	$('.img-ppt').stop().animate({left:-i*$('.img-ppt li').width()},300);
	},${speedValue!"5000"})
  
	//鼠标移入，暂停自动播放，移出，开始自动播放
$('.banner-ppt').hover(function(){ 
  		clearInterval(interval_timer);
	},function(){
		interval_timer=setInterval(function(){
	 	i++;
	  	if (i==$('.img-ppt li').length) {
	  		i=1;
	  		$('.img-ppt').css({left:0});
	  	};
	  
	  	$('.img-ppt').stop().animate({left:-i*$('.img-ppt li').width()},300);
	},${speedValue!"5000"})
})
</script>