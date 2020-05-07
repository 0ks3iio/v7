	<p class="tree-name no-margin">
		<b>展示内容</b>
		
		<#if eccFullObjAll.type != '01'>
		<#if eccFullObjAll.status != 1><button class="btn btn-blue btn-30" onclick="fullObjAllEdit('${eccFullObjAll.id!}')">编辑</button>
		<#else>
		<span class="lock-switch clearfix">
			锁定:
			<label>
				<input id="lockScreenOne" <#if eccFullObjAll.lockScreen>checked</#if> class="wp wp-switch js-openNoticeClass" type="checkbox">
				<span class="lbl"></span>
			</label>
		</span>
		</#if>
		<button class="btn btn-default btn-30" onclick="fullObjAllDelete('${eccFullObjAll.id!}')">删除</button>
		</#if>
	</p>
	
	<div class="group-content">
		<p class="group-title no-margin">
			<span>标题：${objectName!}</span>
			<span>来源：<#if eccFullObjAll.type=='01'>全屏公告（编辑、锁定、删除等操作请前往"公告"功能页）<#else>校级多媒体</#if></span>
		</p>
		<div class="show-media lucency">
			<div class="carousel-wrap">
			    <div class="carousel clearfix">
			      	
			    </div>
			    <a class="arrow arrow-left" href="#">&lt;</a>
			    <a class="arrow arrow-right" href="#">&gt;</a>
			    <div class="close-slide">
			      	<i class="fa fa-times" aria-hidden="true"></i>
			    </div>
			</div>
			<p class="no-margin text-center padding-10 show-num"><b class="count-now">1</b> / <span class="sum"></span></p>
		</div>
		<div class="card-list card-list-large no-margin scroll-height2 clearfix js-show">													
			<!--图片浏览-->
			<#if eccFullObjAll.type=='03'&&object?exists&&object.attachments?size gt 0>
				<div class="media-part block">
			        <#list object.attachments as item>
					<div class="card-item">
						<div class="card-content">
							<a href="javascript:;">
								<div class="card-img">
									<img src="${request.contextPath}${item.showPicUrl!}" alt="">
								</div>
								<h4 class="card-name card-name-edit">${item.filename!}</h4>
							</a>
						</div>
					</div>
					</#list>
				</div>
			</#if>
			<!--视频浏览-->
			<#if eccFullObjAll.type=='04'&&object?exists&&object.attachments?size gt 0>
				<div class="media-part block">
			        <#list object.attachments as item>
					<div class="card-item">
						<div class="card-content">
							<a href="javascript:;">
								<div class="card-img ratio-16of9">
									<video src="${item.filePath!}" controls="controls" width="100%">
										your browser does not support the video tag
									</video>
								</div>
								<h4 class="card-name card-name-edit">${item.filename!}</h4>
							</a>
						</div>
					</div>
					</#list>
				</div>
			</#if>
			<!--PPT浏览-->
			<#if eccFullObjAll.type=='05'&&object?exists&&object.attachments?size gt 0>
				<div class="media-part ppt-sign block">
					<div class="banner-ppt">
						<ul class="img-ppt clearfix">
							<#list object.attachments as item>
							<li><a href="javascript:;"><img src="${request.contextPath}${item.showPicUrl!}" alt=""></a></li>
							</#list>
						</ul>
						<div class="btn-ppt">
							<a class="arrow arrow-left prev" href="#">&lt;</a>
				    		<a class="arrow arrow-right next" href="#">&gt;</a>
					 	</div>
					</div>
				</div>
			</#if>
			<!--全屏公告-->
			<#if eccFullObjAll.type=='01'&&object?exists>
				<div class="media-part block">
				<#if object.templetType == 9>
					<div class="post-fullscreen" style="height:100%; width:100%;">
					<div  style=" background-color:${object.grounding!}">${object.content!}</div>
					</div>
				<#else>
			       <div class="post-fullscreen <#if object.templetType == 3>post-fullscreen-welcome<#elseif object.templetType == 2>post-fullscreen-happy<#else>post-fullscreen-default</#if>">
						<div class="post-fullscreen-content <#if object.templetType == 3>post-fullscreen-content-welcome<#elseif object.templetType == 2>post-fullscreen-content-happy<#else>post-fullscreen-content-default</#if>">
							<#if object.templetType == 1><h2>${object.title!}</h2>
							<p>${object.content!}</p>
							<#else>
							<span>${object.content!}</span>
							</#if>
						</div>
					</div>
				</div>
				</#if>
			</#if>
			<#if eccFullObjAll.type!='01'&&(!object?exists || object.attachments?size == 0)>
			<div class="no-data-container" style="padding-top:120px">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>暂无内容</h3>
					</div>
				</div>
			</div>
			</#if>
		</div>
	</div>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript">
$(function(){
	clearInterval(interval_timer);//写在tool.js中的全局变量
	$('.scroll-height2').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 81,
			overflowY: 'auto'
		})
	});
	var $h=$('.js-show').height();
	<#if eccFullObjAll.type=='01'&&object?exists>
		$('.js-show').find('.post-fullscreen').height($h);
	<#elseif eccFullObjAll.type=='05'>	
		$('.js-show').find('.banner-ppt').height($h);
		$('.js-show').find('.banner-ppt .img-ppt li').width($('.group-content').width());
		$('.img-ppt').stop().animate({left:0},300);
		$('.btn-ppt').hide();
		//PPT轮播
		var i=0;
		var timer=null;
		var firstimg=$('.img-ppt li').first().clone(); 
		$('.img-ppt').append(firstimg).width($('.img-ppt li').length*($('.img-ppt img').width())); 
		
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
	<#else>	
		$('.js-show').css('overflow','auto')
	</#if>
	
	//浏览图片	
	var num,count;
	$('.show-media a.arrow-left').click(function (e) {
		var $width=$('.show-media .carousel-wrap').width();
      	count--;
      	count>0?count:count=1;
      	$('.count-now').text(count);
      	$('.show-media .carousel').css('margin-left',-$width*(count-1))
    })
    $('.show-media a.arrow-right').click(function(e){
    	var $width=$('.show-media .carousel-wrap').width();
      	count++;
      	if(count<=num){
      		$('.count-now').text(count);
      		$('.show-media .carousel').css('margin-left',-$width*(count-1))
      	}else{
      		count=num;
      	}
    });
    //点击添加图片
	$('.js-show .card-img img').on('click',function(){
		$('.js-show').css('overflow','hidden');
		$('.show-media').removeClass('lucency');
		num = $('.js-show .media-part.block img').length;
		$('.sum').text(num);
	    var $width=$('.show-media .carousel-wrap').width();
	    var $height=$('.show-media .carousel-wrap').height();
	    $('.js-show .card-item img').each(function(index,item){
	    	var str=$(item).clone();
	    	$('.show-media .carousel').append(str);
	    });
	    count=$(this).parents('.card-item').index();
        $('.count-now').text(count+1);
 		$('.show-media .carousel').find('img').width($width).height($height);
        $('.show-media .carousel').css({
            width: $width*num,
			'margin-left': -$width*count
		});
        count = count + 1;
	});
    //删除浏览
    $('.show-media').on('click','.close-slide',function(){
    	count=1;
    	$('.js-show').css('overflow-y','auto');
    	$('.show-media .carousel').css('margin-left',0);
    	$('.show-media').addClass('lucency');
    	$('.show-media').find('img').remove()
    })
    $('#lockScreenOne').click(function(){
    	var lock = '0';
		if($(this).is(':checked')){
			lock = '1';
		}
		fullObjAllLock(lock);
	});
});

function fullObjAllLock(lock){
	$.ajax({
		url:'${request.contextPath}/eclasscard/standard/fullscreensch/lockobj',
		data: {'id':"${eccFullObjAll.id!}",'lock':lock},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			layer.msg(jsonO.msg);
 			if(jsonO.success){
 				if(lock == '1'){
	                $('.brand-message li.bg-choose').find('span:first-child').append('<i class="fa fa-lock" aria-hidden="true"></i>')
 				}else{
                	$('.brand-message li.bg-choose').find('span:first-child').find('i').remove()
 				}
 			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
</script>