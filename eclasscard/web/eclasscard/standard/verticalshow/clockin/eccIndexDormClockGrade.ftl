<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/icon-hint.png"><span id="dormClockGradeSpan"></span>
<script>
	$(document).ready(function(){
		if (changeSpan != null) {
			clearInterval(changeSpan);
		}
		<#if gradeDtos?exists&&gradeDtos?size gt 0>
            $("#dormClockGradeDiv").attr("style","display:block");
            $("#dormClockGradeDiv").addClass("clock-record");
			if (isActivate == "true") {
                $(".brush-face").attr("style","display:block");
            }
            $("#dormClockGradeSpan").html("${gradeDtos[0].attacneTime!} ${gradeDtos[0].gradeName!}考勤");
			<#if gradeDtos?size gt 1>
			var spanList = [
				<#list gradeDtos as item>
				"${item.attacneTime!} ${item.gradeName!}考勤"
				<#if item_index+1 != gradeDtos?size>,</#if>
				</#list>
			];
			var spanIndex = 1;
			var spanLen = spanList.length;
			changeSpan = setInterval(function(){
				$("#dormClockGradeSpan").html(spanList[spanIndex]);
				spanIndex++;
				if (spanIndex >= spanLen) {
					spanIndex = 0;
				}
			}, 1000 * 3)
			</#if>
		<#else>
			$("#dormClockGradeDiv").attr("style","display:none");
			$("#dormClockGradeDiv").removeClass("clock-record");
		</#if>
	})
</script>

