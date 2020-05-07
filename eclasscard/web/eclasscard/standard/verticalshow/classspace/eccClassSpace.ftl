<div class="box box-radius-20">
	<div class="space">
		<div class="space-header">
			<div class="role">
				<#if userName?exists>
					<span class="role-img"><img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${userName!}" alt=""></span>
				<#else>
					<#if sex?exists && sex == 1>
					<span class="role-img"><img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/male.png" alt=""></span>
					<#else>
					<span class="role-img"><img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/female.png" alt=""></span>
					</#if>
				</#if>
				<span class="label label-fill label-fill-bluePurple">班主任</span>
				<h4 class="role-name">${teacherName!}</h4>
			</div>
			<div class="btn-group btn-group-lg">
				<a class="btn active" href="javascript:void(0);" data-action="tab" onclick="classSpaceTab('1',this)">班内学生</a>
				<a class="btn" href="javascript:void(0);" data-action="tab" onclick="classSpaceTab('2',this)">班级多媒体</a>
				<a class="btn" href="javascript:void(0);" data-action="tab" onclick="classSpaceTab('3',this)">　荣誉　</a>
				<a class="btn" href="javascript:void(0);" data-action="tab" onclick="classSpaceTab('4',this)">班级简介</a>
			</div>
		</div>
				
		<div class="space-content relative">
			<div class="tab-content" id='classSpaceVerDiv'>
				
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
var submit = false;
$(document).ready(function(){
	classSpaceTab(1,this);
})
function classSpaceTab(type,objthis) {
	if (submit) {
		return;
	}
	submit = true;
		
	$(objthis).addClass('active').siblings().removeClass('active');
	if (type == 1) {
		var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/students?cardId="+_cardId+"&view="+_view;
	} else if (type == 2) {
		var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/album?cardId="+_cardId+"&view="+_view;
	} else if (type == 3) {
		var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/honortab?view="+_view;
	} else {
		var classSpaceUrl = "${request.contextPath}/eccShow/eclasscard/standard/classspace/description?cardId="+_cardId+"&view="+_view;
	}
	$("#classSpaceVerDiv").load(classSpaceUrl,function(response,status,xhr){
		submit = false;
	});
}
</script>