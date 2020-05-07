<div class="box-header">
	<ul class="tabs">
		<li id="showLeave"><a href="javascript:void(0);" data-action="tab" onclick="studentLeaveTab(1)">请假查询</a></li>
		<li id="LeaveApply"><a href="javascript:void(0);" data-action="tab" onclick="studentLeaveTab(2)">请假申请</a></li>
	</ul>
</div>
<div class="box-body scroll-container">
	<div class="space-content">
		<div class="tab-content">
			<div id="studentLeaveDiv" class="tab-pane active" role="tabpanel">
									
			</div>
		</div>
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
		
		studentLeaveTab(1);
	})
	
	function studentLeaveTab(type,leaveType) {
		if (type == 1) {
			$("#showLeave").addClass('active');
			$("#LeaveApply").removeClass('active');
			var studentLeaveUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/showleave?userId=${userId!}"+"&view="+_view+"&leaveType="+leaveType;
		} else {
			$("#showLeave").removeClass('active');
			$("#LeaveApply").addClass('active');
			var studentLeaveUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/leaveapply?userId=${userId!}"+"&view="+_view;
		}
		$("#studentLeaveDiv").load(studentLeaveUrl)
	}
</script>