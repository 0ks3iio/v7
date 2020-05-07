<div id="cc" class="tab-pane scroll-container active">
	<ul class="tabs leave-paper-type clearfix">
		<li class="active"><a href="javascript:void(0);" style="color:#fff" data-action="tab" onclick="studentLeaveTab(1,'',this)">请假记录</a></li>
		<li><a href="javascript:void(0);" style="color:#808080" data-action="tab" onclick="studentLeaveTab(2,'',this)">我的请假</a></li>
	</ul>
	
	<div class="tab-content" id="studentLeaveDiv">
		
	</div>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - 480 - 160
			});
		});
	
		studentLeaveTab(1,'',this);
	})
	
	function studentLeaveTab(type,leaveType,objthis) {
		$(objthis).parent().addClass('active').siblings().removeClass('active');
		$(objthis).css('color','#fff').parent().siblings().find('a').css('color','#808080')
		if (type == 1) {
			var studentLeaveUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/showleave?userId=${userId!}"+"&view="+_view+"&leaveType="+leaveType;
		} else {
			var studentLeaveUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/leaveapply?userId=${userId!}"+"&view="+_view;
		}
		$("#studentLeaveDiv").load(studentLeaveUrl)
	}
</script>