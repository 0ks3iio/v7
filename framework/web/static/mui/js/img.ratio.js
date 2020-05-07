//图片问题：等比例缩放，定位，判断几张图片
$('.mui-media-list').each(function(){
	var $ul=$(this).children('ul')
	var $li=$ul.children('li');
	var len=$li.length;
	var ul_w=$ul.width();
	var li_w;
	//alert(len);
	if (len == 1) {
		li_w = parseInt(ul_w*0.6);
	} else{
		li_w = parseInt(ul_w*0.3);
	};
	//外框大小
	$li.width(li_w).height(li_w);
	$li.children('img').height(li_w);
	//图片定位
	$li.each(function(){
		var $img=$(this).children('img')
		var img_w=$img.width();
		var img_h=$img.height();
		if (img_w > img_h) {
			var img_left=parseInt((img_w-img_h)/2);
			$img.css('left','-'+img_left+'px');
		} else {
			var img_top=(img_h-img_w);
			$img.css({'width':li_w,'height':'auto','top':'-'+img_top+'px'});
		};
	});
});



//按比例，不用判断张数
function imgRatio2(obj,ratio){
	$(obj).children('li').each(function(){
		var $li = $(this);
		var $a = $li.children('a');
		var $img = $a.children('img');
		var img_w = $img.width();
		var img_h = $img.height();
		var a_w = $a.outerWidth();
		var zoom_w = a_w - 2;
		if (ratio == 1) {
			zoom_h = zoom_w;
		} else{
			a_w = parseInt(a_w * ratio);
			zoom_h = parseInt(zoom_w * ratio);
		};
		$a.height(a_w);
		if (img_w > img_h) {
			$img.css({'height':zoom_h,'width':'auto','left':'-'+img_left+'px'});
			var rear_w = $img.width();
			if (rear_w < zoom_w) {
				$img.css({'width':zoom_w,'height':'auto'});
				var rear_h = $img.height();
				var img_top = parseInt((rear_h - zoom_h)/2);
				$img.css({'top':'-'+img_top+'px'});
			} else{
				var img_left = parseInt((rear_w - zoom_w)/2);
				$img.css({'left':'-'+img_left+'px'});
			};
		} else {
			$img.css({'width':zoom_w,'height':'auto'});
			var rear_h = $img.height();
			var img_top = parseInt((rear_h - zoom_h)/2);
			$img.css({'top':'-'+img_top+'px'});
		};
	});
};