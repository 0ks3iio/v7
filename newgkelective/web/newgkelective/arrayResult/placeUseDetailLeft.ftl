<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">查看教室课表</h3>
		<div class="filter-item filter-item-right">
			<a href="javascript:" class="btn btn-blue" onclick="toArrayResultIndex()" >返回</a>
			<@htmlcomponent.printToolBar container=".print" printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		</div>
	</div>
	<div class="box-body">
		<div class="tree-wrap">
			<h4>选择教室</h4>
			<div class="input-group">
				<input type="text" class="form-control" id="searchPlaceName" value="${placeName!}" placeholder="请输入教室名称">
				<span class="input-group-addon btn-search">
					<i class="fa fa-search"></i>
				</span>
			</div>
			<div class="tree-list" style="height:450px;margin-top: 0;">
				<#if classroomUseageDtoList?exists && classroomUseageDtoList?size gt 0>
					<#list classroomUseageDtoList as item>
					<a  href="javascript:;" data-place-id="${item.placeId!}" <#if placeId?default('') == item.placeId>class="active"</#if>>${item.placeName!}</a>
					</#list>
				</#if>
			</div>
		</div>
		
		<div id="placeDataDiv"  style="min-height:550px;">
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>请选择要查看的教室</h3>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function toArrayResultIndex(){
	    var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/classroomUseage';
		$("#tableList").load(url);
	}
	<#if placeId?default('') !=''>
	toDetailList2('${placeId!}');
	</#if>
	$('.tree-list a').click(function(){
		$(this).addClass('active').siblings('a').removeClass('active');
		var placeId = $(this).attr('data-place-id');
		toDetailList2(placeId);
	});
	
	function toDetailList2(placeId){
	    var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/classroomUseDetailList?placeId='+placeId;
	    $("#placeDataDiv").load(url);
	}
	
	
	$('#searchPlaceName').keydown(function(event){
	    if(event.keyCode==13){
	    	searchPlace();
	    }
	});
	$('.btn-search').on('click',function(){
		searchPlace();
	});
	
	function searchPlace(){
		var un = $.trim($('#searchPlaceName').val());
		if(un == '${placeName!}'){
			return;
		}
		if(un.indexOf('\'')>-1||un.indexOf('%')>-1){
	        layer.tips('教室名称不能包含单引号、百分号等特殊符号！',$('#searchPlaceName'), {
						tipsMore: true,
						tips: 3
					});
	        return ;
	    }
	    //debugger;
	    toDetailListLeft2(un);
	}
	function toDetailListLeft2(placeName){
		var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/classroomUseDetail?placeName='+placeName;
	    $("#tableList").load(url);
	}
</script>