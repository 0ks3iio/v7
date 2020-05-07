//基于jquery的回到顶部：滚动定位
var $btnTop = $('.feedback .top');
var $btnCode = $('.feedback .code');
var $qrCode = $('.feedback .code-layer');
function GoTop(){
	var scTop = $(document).scrollTop();
	if(scTop >= 500){
		$btnTop.fadeIn(500);
	} else{
		$btnTop.fadeOut(500);
	};
};
GoTop();
$(window).scroll(function(){
	GoTop();
});
//回到顶部
$btnTop.click(function(e){
	e.preventDefault();
	$('html,body').animate({scrollTop:0},500);
});
//二维码
$btnCode.hover(function(){
	$qrCode.show();
}, function(){
	$qrCode.hide();
});