<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
		    <li class="active" role="presentation"><a href="javascript:void(0)" onclick="unitList('3');" role="tab" data-toggle="tab">异动记录</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="unitList('1');" role="tab" data-toggle="tab">异动统计（按班）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="unitList('2');" role="tab" data-toggle="tab">异动统计（按年级）</a></li>
			<#if topEdu?default(false)>
			<li role="presentation"><a href="javascript:void(0)" onclick="cityPrimary();" role="tab" data-toggle="tab">异动统计（全市小学）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="cityHign(2);" role="tab" data-toggle="tab">异动统计（全市初中）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="cityHign(3);" role="tab" data-toggle="tab">异动统计（全市高中）</a></li>
			</#if>
		</ul>
		<div class="tab-content" id="itemShowDivId">
			
				
		</div>
	</div>	
</div>

<script>
$(function(){
	unitList('3');
});

function unitList(type){
     var  url =  '${request.contextPath}/newstusys/student/abnormalreport/unitList?type='+type;
     $("#itemShowDivId").load(url);
}

function cityPrimary(){
     var  url =  '${request.contextPath}/newstusys/student/abnormalreport/cityPrimary';
     $("#itemShowDivId").load(url);
}

function cityHign(sec){
     var  url =  '${request.contextPath}/newstusys/student/abnormalreport/cityHign?section='+sec;
     $("#itemShowDivId").load(url);
}
</script>
		

