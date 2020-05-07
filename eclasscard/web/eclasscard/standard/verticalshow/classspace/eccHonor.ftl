<div id="honor" class="tab-pane active">
	<ul class="tabs">
		<li class="active"><a href="javascript:void(0);" data-action="tab" onclick="showHonorList(1,this)">班级荣誉</a></li>
		<li><a href="javascript:void(0);" data-action="tab" onclick="showHonorList(2,this)">学生荣誉</a></li>
	</ul>
	<div class="tab-content scroll-container" id="classOrpersonal">
		
	</div>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflowY: 'auto',
				overflowX: 'hidden',
				height: $(window).height() - 480 - 241
			});
		});
		showHonorList(1,this);
	});	
	
	function showHonorList(type,objthis) {
		$(objthis).parent().addClass('active').siblings().removeClass('active');
		var url = "";
		if (type == 1) {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/classhonor?cardId="+_cardId+"&view="+_view;
		} else {
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/stuhonor?cardId="+_cardId+"&view="+_view;
		}
		$("#classOrpersonal").load(url);
	}
</script>	