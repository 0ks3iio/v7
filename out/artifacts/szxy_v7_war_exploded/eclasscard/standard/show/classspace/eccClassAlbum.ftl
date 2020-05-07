<div class="box-header inner-tabs">
	<ul class="tabs js-start">
		<li class="active"><a href="#" data-action="tab" onClick="changeMTab('1',this)">相册</a></li>
		<li><a href="#" data-action="tab" onClick="changeMTab('2',this)">视频</a></li>
		<li class="js-ppt"><a href="#" data-action="tab" onClick="changeMTab('3',this)">PPT</a></li>
	</ul>
</div>
<div id="multimedia" class="tab-content">
				
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
		
		changeMTab('1','photo');
	});
		
	function changeMTab(type,objthis) {
		$(objthis).parent().addClass('active').siblings().removeClass('active');
		var	url = "";
		if (type == "1") {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/photoalbumlist?cardId="+_cardId+"&view="+_view;
		} else if (type == "2") {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/videoalbumlist?cardId="+_cardId+"&view="+_view;
		} else {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/pptalbumlist?cardId="+_cardId+"&view="+_view;
		}
		$("#multimedia").load(url);
	}
</script>