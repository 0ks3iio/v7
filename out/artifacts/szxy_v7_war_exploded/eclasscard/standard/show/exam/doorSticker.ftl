
<header class="header bgColor-darkgreen height-140">
			<div class="logo test-room"><span>${placeName!}</span></div>
</header>

<div id="exammaincontainer" class="main-container no-padding bgColor-green">
	<div class="test-introduce">
		<div class="grid">
			<div class="grid-cell grid-1of3 text-right">
				考试科目：${subjectName!}
			</div>
			<div class="grid-cell grid-1of3 text-center">
				监考老师：${teacherNames!}
			</div>
			<div class="grid-cell grid-1of3">
				考试时间：${timePeriod!}
			</div>
		</div>
	</div>
	<div class="examinee clearfix">
		<ul class="student-place clearfix">
			<#if studentInfos?exists&&studentInfos?size gt 0>
				<#list studentInfos as item>  
				<#if item_index%4==3>
				<li class="first-row clearfix">
					<ul class="grid clearfix">
				</#if>
						<li class="grid-cell grid-1of4 clearfix">
							<div class="single clearfix">
								<span class="student-number">${item.studentName!}</span>
								<span class="num">${item.studentNo!}</span>
							</div>
						</li>
				<#if item_index%4==3>
					</ul>
				</li>
		    </#if>
			    </#list> 
		    </#if>
		</ul>
	</div>
</div>
<script>
$('#exammaincontainer').height($(window).height()-140);
var lockfulltimestop = null;
$('#fullScreenLayer').off('touchstart').on('touchstart', function(event) {
	if(null != lockfulltimestop){  
       clearInterval(lockfulltimestop);  
   	}
	$('.lock-full').show();
	lockfulltimestop = setTimeout(function(){
		$('.lock-full').hide();
	},6000);
});
</script>