<title>活动编辑</title>
<#include "activityConstant.ftl" />
<a href="javascript:;" class="page-back-btn goback-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title"><#if actType==SCHOOLACT>学校活动<#elseif actType==CLASSACT>班级活动<#else>主题活动</#if></h4>
	</div>
	<#if msg?default('') == ''>
	<div class="box-body">
		<div class="activity-info">
			<button class="btn btn-blue pull-right js-editActivity">编辑</button>
			<h3 class="activity-name">${act.actTheme!}</h3>
			<p class="activity-description">${act.actRemark!}</p>
			<p class="activity-birthday">创建时间：${(act.creationTime?string('yyyy-MM-dd'))?if_exists}</p>
		</div>
		<div id="picList"></div>
	</div>
	</#if>
</div>
<script>
$(function(){
	$('.goback-btn').on('click',function(){
		var url = '${request.contextPath}/studevelop/activity/${actType!}/index/page?'+getIndexParam();
		$('.model-div').load(url);
	});
	
<#if msg?default('') == ''>
	$('#picList').load('${request.contextPath}/studevelop/activity/${actType!}/detailList?id=${act.id!}');
	
	// 编辑
	$('.js-editActivity').on('click',function(){
		var url = "${request.contextPath}/studevelop/activity/${actType!}/edit?id=${act.id!}&"+getIndexParam();
		indexDiv = layerDivUrl(url,{title: "修改活动",width:460,height:490});
	});
<#else>
	layerTipMsg(false,"提示","${msg!}");
	$('.goback-btn').click();
</#if>	
});

<#if msg?default('') == ''>
// 编辑保存之后调用的方法来刷新页面
function refreshPage(){
	var url = "${request.contextPath}/studevelop/activity/${actType!}/detail?id=${act.id!}";
	$('.model-div').load(url);
}

function getIndexParam(){
	return "acadyear=${act.acadyear!}"
				+"&semester=${act.semester!}&rangeId=${act.rangeId!}&rangeType=${act.rangeType!}";
}
</#if>
</script>