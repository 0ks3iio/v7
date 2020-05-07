<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0)" onclick="roster('1');" role="tab" data-toggle="tab">花名册</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="roster('2');" role="tab" data-toggle="tab">在校生统计表（按班）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="roster('3');" role="tab" data-toggle="tab">在校生统计表（按年级）</a></li>
			<#if topEdu?default(false)>
			<li role="presentation"><a href="javascript:void(0)" onclick="cityPrimary();" role="tab" data-toggle="tab">在校生统计表（<#if deploy?default('')=='zjzj'>全市</#if>小学）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="cityHign(2);" role="tab" data-toggle="tab">在校生统计表（<#if deploy?default('')=='zjzj'>全市</#if>初中）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="cityHign(3);" role="tab" data-toggle="tab">在校生统计表（<#if deploy?default('')=='zjzj'>全市</#if>高中）</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="allCount();" role="tab" data-toggle="tab">汇总统计表</a></li>
			</#if>
		</ul>
		<div class="tab-content" id="itemShowDivId">
			
				
		</div>
	</div>	
</div>

<script>
$(function(){
	roster('1');
});

function roster(type){
     var  url =  '${request.contextPath}/newstusys/student/report/studentRoster?type='+type;
     $("#itemShowDivId").load(url);
}

function cityPrimary(){
     var  url =  '${request.contextPath}/newstusys/student/report/cityPrimary';
     $("#itemShowDivId").load(url);
}

function cityHign(sec){
     var  url =  '${request.contextPath}/newstusys/student/report/cityHign?section='+sec;
     $("#itemShowDivId").load(url);
}

function allCount(){
     var  url =  '${request.contextPath}/newstusys/student/report/allCount';
     $("#itemShowDivId").load(url);
}
</script>
		

