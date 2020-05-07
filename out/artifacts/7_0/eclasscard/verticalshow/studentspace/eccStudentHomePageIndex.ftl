<div id="aa" class="tab-pane active" role="tabpanel">
	<h2 class="space-stu-title">基本信息</h2>
	<ul class="stu-info">
		<li>姓名：<span>${studentName!} </span></li>
		<li>性别：<span><#if (studentSex!) == 1>男<#else>女</#if></span></li>
		<li>学号：<span>${studentCode!}</span></li>
		<li>班级：<span>${className!}</span></li>
		<li>班主任：<span>${teacherName!}</span></li>
	</ul>
	<div id="homepageTabDiv">
	
	</div>
</div>
<script>
	$(document).ready(function(){
    	<#if healthData>
    		changeTab(1);
		<#else>
    		changeTab(2);
		</#if>
	});
	
	function changeTab(type) {
		var userId = "${userId!}";
		var view = "${view!}";
		if (type == 1) {
			$("#homepageTabDiv").load("${request.contextPath}/eccShow/eclasscard/stuHealthIndex/page?userId="+userId+"&view="+view);
		} else if (type == 2){
			
		}
	}
</script>