<div class="box box-default clearfix scroll-height no-margin padding-20">
	<ul class="nav nav-tabs" role="tablist">
		<li <#if tabType == '1'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('1')" role="tab" data-toggle="tab">公告</a></li>
		<li <#if tabType == '3'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('3')"role="tab" data-toggle="tab">多媒体</a></li>
		<li <#if tabType == '4'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('4')"role="tab" data-toggle="tab">全屏信息发布</a></li>
		<li <#if tabType == '2'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('2')"role="tab" data-toggle="tab">荣誉</a></li>
		<#if showSchoolSpace?default("")=="1">
		<li <#if tabType == '5'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('5')"role="tab" data-toggle="tab">校园空间</a></li>
		</#if>
	</ul>
	<div class="tab-content">
		<div id="tabContent" class="tab-pane active" role="tabpanel">
			
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$('.scroll-height').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 60
		})
	});
	var tabType='1';
	<#if tabType == '2'>tabType='2';
	<#elseif tabType == '3'>tabType='3';
	<#elseif tabType == '4'>tabType='4';
	<#elseif tabType == '5'>tabType='5';
	</#if>
	showContent(tabType);
});

function showContent(type){
	var url = '';
	if(type=='1'){
		url =  '${request.contextPath}/eclasscard/standard/bulletin/list'+'?bulletinLevel=1';
	}else if(type=='2'){
		url =  '${request.contextPath}/eclasscard/standard/honor/list';
	}else if(type=='3'){
		url = '${request.contextPath}/eclasscard/standard/eccfolder/listsch?tabType=3';
	}else if(type=='4') {
		url = '${request.contextPath}/eclasscard/standard/fullscreensch/list';
	}else if(type=='5') {
		url = '${request.contextPath}/eclasscard/standard/schoolSpace/page';
	}
	$("#tabContent").load(url);
}
</script>