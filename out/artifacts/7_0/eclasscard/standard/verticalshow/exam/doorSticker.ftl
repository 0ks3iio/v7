<div class="main-container">
	<div class="test-introduce">
		<div class="introduce">
			<div class="test-room"><span>${placeName!}</span></div>
			<p class=" ">
				考试科目：${subjectName!}
			</p>
			<p class="">
				监考老师：${teacherNames!}
			</p>
			<p class="">
				考试时间：${timePeriod!}
			</p>
		</div>
	</div>
	<div class="examinee clearfix">
		<ul class="student-place clearfix">
			<#if studentInfos?exists&&studentInfos?size gt 0>
				<#list studentInfos as item>  
				<#if item_index%3==2>
				<li class="first-row clearfix">
					<ul class="grid clearfix">
				</#if>
						<li class="grid-cell grid-1of3 clearfix vetically-center">
							<div class="single clearfix">
								<span class="student-number">${item.studentName!}</span>
								<span class="num">${item.studentNo!}</span>
							</div>
						</li>
				<#if item_index%3==2>
					</ul>
				</li>
		    	</#if>
			    </#list> 
		    </#if>
		</ul>
	</div>
</div>
<script>
$('#fullScreenLayer').off('touchstart').on('touchstart', function(event) {
	$('.lock-full').show();
	setTimeout(function(){
		$('.lock-full').hide();
	},6000);
});
</script>