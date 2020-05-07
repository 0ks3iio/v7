/*
2015-12-09
基于jquery的弹出层插件
用法：
1、html：ui-layer  ---->   .ui-layer>(p.tt>a.close+span)+.wrap+(p.dd>a.submit+a.cancel)
2、js根据id或class来控制，所有参数必填
layer('#btnLayer','#uiLayer','.close','标题1');
layer('#btnLayerTable','#uiLayerTable','.close','标题2');
*/
winW=parseInt($(window).width());
winH=parseInt($(window).height());

//点击确认支付操作
function layer(btn,layer,cls,tt,fun,closeFun){
	//btn：打开的按钮，layer：打开的层，cls：关闭按钮；tt：标题
	var layerW=parseInt($(layer).width());
	var layerH=parseInt($(layer).height());
	var layerL=(winW-layerW)/2;
	var layerT=(winH-layerH)/2;
	var reTop=layerT;
	$(layer).find('.title').text(tt);
	$(btn).click(function(e){
		e.preventDefault();
		if ($(this).hasClass('disabled')) {
			return false;
		}
		$(this).addClass('disabled');
		//弹出框前先执行先弹出框 后有问题可以直接关闭
		if(fun && jQuery.isFunction( fun )){
			//验证成功才进行 不用异步调用
			fun();
		}
		if (!$(this).hasClass('disabled')) {
			return false;
		}
		var docH=parseInt($(document).height());
		var winTop=parseInt($(window).scrollTop());
		layerT=reTop+winTop;
		(docH>winH) ? maskH=docH : maskH=winH;
		//先遮罩
		if($(document).find('.ui-layer-mask').length==0){
			$('body').append('<div class="ui-layer-mask" style="height:'+maskH+'px"></div>');
		};
		$(layer).show().css({top:layerT,left:layerL});
		
		$(window).scroll(function(){
			var sTop=parseInt($(window).scrollTop());
			layerT=reTop+sTop;
			$(layer).css({top:layerT});
		});
		
		
	});
	$(layer).find(cls).click(function(e){
		e.preventDefault();
		$(layer).hide();
		$('.ui-layer-mask').remove();
		$(btn).removeClass('disabled');
		if(closeFun && jQuery.isFunction(closeFun)){
			//关闭时候
			closeFun();
		}
	});
};



//自动关闭弹出提示层-公用函数
function autoLayer(btn,obj,mask,cls,tt){
	//btn：打开的按钮，obj：打开的层,mask：0-没有遮罩层，cls：关闭按钮；tt：标题
	var docH=parseInt($(document).height());
	var layerW=$(obj).width();
	var layerH=$(obj).height();
	var layerL=(winW-layerW)/2;
	var layerT=(winH-layerH)/2;
	(docH>winH) ? maskH=docH : maskH=winH;
	$(obj).find('.txt').text(tt);
	$(btn).click(function(e){
		if($(obj).length>0){
			if(mask!=0){
				$('body').append('<div class="ui-layer-mask" style="height:'+maskH+'px"></div>');
			};
			$(obj).css({'top':layerT,'left':layerL}).fadeIn(300).delay(2000).fadeOut(300,function(){
				$('.ui-layer-mask').fadeOut(300).remove();
			});
		}else{
			alert('此对象不存在!');
		};
	});
	$(obj).find(cls).click(function(e){
		e.preventDefault();
		$(obj).hide();
		$('.ui-layer-mask').fadeOut(300).remove();
	});
};
//fun 关闭的时候回调函数
function showSuccessMsg(layer,cls,tt,fun){
	var layerW=parseInt($(layer).width());
	var layerH=parseInt($(layer).height());
	var layerL=(winW-layerW)/2;
	var layerT=(winH-layerH)/2;
	var reTop=layerT;
	$(layer).find('.title').text(tt);
	
	var docH=parseInt($(document).height());
	var winTop=parseInt($(window).scrollTop());
	layerT=reTop+winTop;
	(docH>winH) ? maskH=docH : maskH=winH;
	//先遮罩
	if($(document).find('.ui-layer-mask').length==0){
		$('body').append('<div class="ui-layer-mask" style="height:'+maskH+'px"></div>');
	};
	$(layer).show().css({top:layerT,left:layerL});
	
	$(window).scroll(function(){
		var sTop=parseInt($(window).scrollTop());
		layerT=reTop+sTop;
		$(layer).css({top:layerT});
	});
	
	$(layer).find(cls).click(function(e){
		e.preventDefault();
		$(layer).hide();
		$('.ui-layer-mask').remove();
		if(fun && jQuery.isFunction( fun )){
			fun();
		}
	});
}