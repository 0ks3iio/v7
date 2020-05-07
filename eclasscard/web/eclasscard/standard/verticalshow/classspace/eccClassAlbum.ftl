<div id="multimedia" class="tab-pane height-wrap active">
	<ul class="tabs no-margin-left js-height js-start">
		<li class="active"><a href="javascript:void(0);" data-action="tab" onclick="showMultimedia(1,this)">相册</a></li>
		<li><a href="javascript:void(0);" data-action="tab" onclick="showMultimedia(2,this)">视频</a></li>
		<li><a href="javascript:void(0);" data-action="tab" onclick="showMultimedia(3,this)">PPT</a></li>
	</ul>
	<div id="multimediaDiv" class="tab-content height-wrap">
		
	</div>
</div>
<script>
	$(document).ready(function(){
		$('.height-wrap').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
		
		showMultimedia(1,this);
	});	
	
	function showMultimedia(type,objthis) {
		$(objthis).parent().addClass('active').siblings().removeClass('active');
		var	url = "";
		if (type == "1") {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/photoalbumlist?cardId="+_cardId+"&view="+_view;
		} else if (type == "2") {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/videoalbumlist?cardId="+_cardId+"&view="+_view;
		} else {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/pptalbumlist?cardId="+_cardId+"&view="+_view;
		}
		$("#multimediaDiv").load(url);
	}
</script>	