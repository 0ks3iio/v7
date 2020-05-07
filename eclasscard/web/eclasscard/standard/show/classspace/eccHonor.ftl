<div class="box-header inner-tabs">
	<ul class="tabs">
		<li id="clazzHonor" class="active"><a href="javascript:void(0);" data-action="tab" onclick="showHonorList(1)">班级荣誉</a></li>
		<li id="stuHonor"><a href="javascript:void(0);" data-action="tab" onclick="showHonorList(2)">学生荣誉</a></li>
	</ul>
</div>
<div class="tab-content">
	<div id="classOrpersonal" class="tab-pane active">
	</div>
</div>							
<script>
	$(document).ready(function(){
	
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
		
		showHonorList(1);
	});	
	
	function showHonorList(type) {
		var url = "";
		if (type == 1) {
			$("#clazzHonor").addClass('active');
			$("#stuHonor").removeClass('active');
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/classhonor?cardId="+_cardId+"&view="+_view;
			$("#classOrpersonal").load(url);
		} else {
			$("#stuHonor").addClass('active');
			$("#clazzHonor").removeClass('active');
			url = "${request.contextPath}/eccShow/eclasscard/standard/classspace/stuhonor?cardId="+_cardId+"&view="+_view;
			$("#classOrpersonal").load(url);
		}
	}
</script>	