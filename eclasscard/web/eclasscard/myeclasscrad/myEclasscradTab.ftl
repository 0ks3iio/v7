<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs" role="tablist">
		<#if eccInfo.type=='10'>
			<li class="active" role="presentation"><a href="#aa" onclick="showContent('1')" role="tab" data-toggle="tab">简介</a></li>
		</#if>
			<li role="presentation" <#if !(eccInfo.type=='10')>class="active"</#if>><a href="#bb" onclick="showContent('2')" role="tab" data-toggle="tab">照片</a></li>
			<li role="presentation"><a href="#cc" onclick="showContent('3')" role="tab" data-toggle="tab">通知公告</a></li>
		</ul>
		<div class="tab-content">
			<div id="tabContent" class="tab-pane active" role="tabpanel">
				
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	showBreadBack(myEclasscardList,true,"我的班牌");
	<#if eccInfo.type=='10'>
		showContent('1');
	<#else>
		showContent('2');
	</#if>
});

function showContent(type){
	if(type=='2'){
		var url = '${request.contextPath}/eclasscard/photoAlbum/list?eccInfoId=${eccInfo.id!}';
		$("#tabContent").load(url);
	}else if(type=='3'){
		var url =  '${request.contextPath}/eclasscard/bulletin/list?eccInfoId=${eccInfo.id!}';
		$("#tabContent").load(url);
	}else{
		var url =  '${request.contextPath}/eclasscard/description/edit?eccInfoId=${eccInfo.id!}';
		$("#tabContent").load(url);
	}
}

</script>