<div class="box box-default clearfix scroll-height no-margin padding-20">
	<ul class="nav nav-tabs" role="tablist">
	<#if eccInfo.type=='10'>
		<li <#if tabType == '3'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('3')" role="tab" data-toggle="tab">公告</a></li>
		<li <#if tabType == '4'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('4')" role="tab" data-toggle="tab">多媒体</a></li>
		<li <#if tabType == '6'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('6')" role="tab" data-toggle="tab">全屏信息发布</a></li>
		<li <#if tabType == '2'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('2')" role="tab" data-toggle="tab">荣誉</a></li>
		<li <#if tabType == '1'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('1')" role="tab" data-toggle="tab">简介</a></li>
	<#else>
		<li <#if tabType == '3'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('3')" role="tab" data-toggle="tab">公告</a></li>
		<li <#if tabType == '4'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('4')" role="tab" data-toggle="tab">多媒体</a></li>
		<li <#if tabType == '6'>class="active"</#if> role="presentation"><a href="javascript:void('0')" onclick="showContent('6')" role="tab" data-toggle="tab">全屏信息发布</a></li>
	</#if>
	</ul>
	<div class="tab-content">
		<div id="tabContent" class="tab-pane active" role="tabpanel">
			
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	showBreadBack(myEclasscardList,true,"我的班牌");
	$('.scroll-height').each(function(){
		$(this).css({
			height: $(window).height() - $(this).offset().top - 60
		})
	});
	showContent('${tabType!}');
	
});

function showContent(type){
	var url = '';
	if(type=='1'){
		url = '${request.contextPath}/eclasscard/standard/description/edit?eccInfoId=${eccInfo.id!}';
	}else if(type=='2'){
		url =  '${request.contextPath}/eclasscard/standard/honor/list?eccInfoId=${eccInfo.id!}&subTabType=${subTabType!}';
	}else if(type=='3'){
		var url =  '${request.contextPath}/eclasscard/standard/bulletin/list?eccInfoId=${eccInfo.id!}'+'&bulletinLevel=2';
	}else if(type=='4'){
		var objectId = '';
		<#if eccInfo.type=='10'>
			objectId="${eccInfo.id!}";
		<#else>
			objectId="${eccInfo.id!}";
		</#if>
		url = '${request.contextPath}/eclasscard/standard/eccfolder/list?objectId='+objectId+'&tabType=4';
	}else if(type=='6'){
		url = '${request.contextPath}/eclasscard/standard/fullscreen/list?eccInfoId=${eccInfo.id!}&tabType=6';
	}
	if(url!='')
	$("#tabContent").load(url);
}
function showFolderDetail(id){
	var url = '${request.contextPath}/eclasscard/standard/photoalbum/list?folderId='+id;
	$("#tabContent").load(url);
}
</script>