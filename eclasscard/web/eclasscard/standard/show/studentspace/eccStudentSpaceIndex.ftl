<div class="grid">
	<div class="grid-cell space-side">
		<div class="box">
			<div class="box-body scroll-container">
				<div class="role">
					<span class="role-img" id="student-img-src"><img src="${request.contextPath}${showPicUrl!}" alt=""></span>
					<h4 class="role-name">${studentName!}</h4>
				</div>
				
				<ul class="side-nav">
					<li <#if tabType==1> class="active"</#if>><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(1,this)">我的课表</a></li>
		<#--		<li><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(2)">我的小纸条</a></li>  -->
					<li><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(3,this)">我的请假</a></li>
					<#if showZHPJ?default(false)>
						<li <#if tabType==7>class="active"</#if>><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(7,this)">综合评价</a></li>
					</#if>
					<#if healthData>
					<li><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(4,this)">健康数据</a></li>
					</#if>
					<li><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(5,this)">我的荣誉</a></li>
					<li <#if tabType==6>class="active"</#if>><a href="javascript:void(0);" data-action="tab" onclick="studentSpaceTab(6,this)">我的留言</a></li>
					
				</ul>
			</div>
		</div>
	</div>
	<div class="grid-cell space-main">
		<div class="box" id="studentSpaceDiv">
			
		</div>
	</div>
</div>
<script>
	$(document).ready(function(){
		studentSpaceTab("${tabType!}");
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
	});
	
	function studentSpaceTab(type,objthis) {
		if (type == 1) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/stuSchedule/page?userId=${userId!}"+"&view="+_view;
		} else if (type == 2) {
			
		} else if (type == 3) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/stuleave?userId=${userId!}"+"&view="+_view;
		} else if (type == 4) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/stuHealthIndex/page?userId=${userId!}"+"&view="+_view;
		} else if (type == 5) {
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/studentSpace/stuhonor?userId=${userId!}"+"&view="+_view;
		} else if(type == 6){
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/leavae/word/index?receiverId=${userId!}"+"&view="+_view;
		}else if(type == 7){
			var studentSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/stuQuality/page?userId=${userId!}"+"&view="+_view;
		}
		$(objthis).parent().addClass('active').siblings().removeClass('active');
		$("#studentSpaceDiv").load(studentSpaceUrl);
	}
</script>
