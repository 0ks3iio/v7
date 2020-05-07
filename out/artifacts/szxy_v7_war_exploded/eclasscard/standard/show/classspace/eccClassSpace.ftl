<div class="grid">
	<div class="grid-cell space-side">
		<div class="box">
			<div class="box-body scroll-container">
				<div class="role">
					<#if userName?exists>
						<span class="role-img"><img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${userName!}" alt=""></span>
					<#else>
						<#if sex?exists && sex == 1>
						<span class="role-img"><img src="${request.contextPath}/static/eclasscard/standard/show/images/male.png" alt=""></span>
						<#else>
						<span class="role-img"><img src="${request.contextPath}/static/eclasscard/standard/show/images/female.png" alt=""></span>
						</#if>
					</#if>
					<span class="label label-fill label-fill-bluePurple">班主任</span>
					<h4 class="role-name">${teacherName!}</h4>
				</div>

				<ul class="side-nav">
					<li class="active"><a href="javascript:void(0);" data-action="tab" onclick="classSpaceTab('1',this)">班内学生</a></li>
					<li><a href="javascript:void(0);" data-action="tab"  onclick="classSpaceTab('2',this)">班级多媒体</a></li>
					<li><a href="javascript:void(0);" data-action="tab"  onclick="classSpaceTab('3',this)">荣誉</a></li>
					<li><a href="javascript:void(0);" data-action="tab"  onclick="classSpaceTab('4',this)">班级简介</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="grid-cell space-main">
		<div class="box">
			<div class="box-body scroll-container js-height">
				<div class="space-content">
					<div class="tab-content">
						<div id="classSpaceDiv" class="tab-pane active">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="blueimp-gallery" class="blueimp-gallery">
<div class="slides"></div>
<h3 class="title"></h3>
<a class="prev">‹</a>
<a class="next">›</a>
<a class="close">×</a>
<a class="play-pause"></a>
<ol class="indicator"></ol>
</div>
<script>
	$(document).ready(function(){
		classSpaceTab(1);
	})
	
	function classSpaceTab(type,objthis) {
		if (type == 1) {
			var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/students?cardId="+_cardId+"&view="+_view;
		} else if (type == 2) {
			var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/album?view="+_view;
		} else if (type == 3) {
			var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/honortab";
		} else {
			var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/description?cardId="+_cardId+"&view="+_view;
		}
		$(objthis).parent().addClass('active').siblings().removeClass('active');
		$("#classSpaceDiv").load(classSpaceUrl);
	}
</script>
