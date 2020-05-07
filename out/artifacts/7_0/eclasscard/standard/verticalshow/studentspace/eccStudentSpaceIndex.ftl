<div class="box">
	<div class="space">
		<div class="space-header" >
			<div class="role" id="student-img-src">
				<span class="role-img"><img src="${request.contextPath}${showPicUrl!}" alt=""></span>
				<h4 class="role-name">${studentName!}</h4>
			</div>
			<div class="btn-group btn-group-lg">
				<a class="btn <#if tabType==1>active</#if>" href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(1,this)">我的课表</a>
		<#--    <a class="btn " href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(2,this)">我的小纸条</a> -->
				<a class="btn" href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(3,this)">我的请假</a>
				<#if healthData>
				<a class="btn" href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(4,this)">健康数据</a>
				</#if>
				<a class="btn" href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(5,this)">我的荣誉</a>
				<a class="btn <#if tabType==6>active</#if>"  href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(6,this)">我的留言</a>
			</div>
		</div>
				
		<div class="space-content">
			<div class="tab-content" id="studentSpaceDiv">
						
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function(){
		studentSpaceTab("${tabType!}");
	});
	
	function studentSpaceTab(type,objthis) {
		$(objthis).addClass('active').siblings().removeClass('active');
		if (type == 1) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/stuSchedule/page?userId=${userId!}"+"&view="+_view;
		} else if (type == 2) {
		} else if (type == 3) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/stuleave?userId=${userId!}"+"&view="+_view;
		} else if (type == 4) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/stuHealthIndex/page?userId=${userId!}"+"&view="+_view;
		} else if (type == 5) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/stuhonor?userId=${userId!}"+"&view="+_view;
		} else {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/leavae/word/index?receiverId=${userId!}"+"&view="+_view;
		}
		$("#studentSpaceDiv").load(studentSpaceUrl);
	}
</script>
