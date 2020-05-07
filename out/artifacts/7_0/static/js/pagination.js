$(function(){
	var $item=$('.page-item a'),
		$prev=$('.page-prev a'),
		$next=$('.page-next a');
	
	function isHas(element, c){
		return $(element).parent().hasClass(c);
	}

	// 判断是否到头
	function isEnd(){
		var index=getCurrentPage();

		if(index===0){
			$prev.parent().addClass('disabled');
		}else{
			$prev.parent().removeClass('disabled');
		}

		if(index===$item.length-1){
			$next.parent().addClass('disabled');
		}else{
			$next.parent().removeClass('disabled');
		}
	}

	// 获取当前页码
	function getCurrentPage(){
		for(var i=0;i<$item.length;i++){
			if(isHas($item[i],'active')){
				return i;
			}
		}
	}

	// 跳页
	$item.on('click',function(e){
		
		if( isHas( $(this), 'active') ) return;

		$(this).parent().addClass('active').siblings().removeClass('active');

		isEnd();
	})

	// 下一页
	$next.on('click',function(e){
		var index=getCurrentPage();
		
		if(index===$item.length-1) return;
		
		$item.eq(index).parent().removeClass('active');
		$item.eq(index+1).parent().addClass('active');

		isEnd();
	})

	// 上一页
	$prev.on('click',function(e){
		var index=getCurrentPage();

		if(index===0) return;
		
		$item.eq(index).parent().removeClass('active');
		$item.eq(index-1).parent().addClass('active');

		isEnd();
	})
})