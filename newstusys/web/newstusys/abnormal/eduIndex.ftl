<div class="page-content">
	<div class="page-sidebar">
		<div class="page-sidebar-header ml10 mr10">
			<div class="input-group mt20">
				<input type="text" class="form-control" id="unitName" placeholder="请输入单位名称" value="${searchUnitName!}">
				<a href="javascript:;" class="input-group-addon btn-search">
					<i class="fa fa-search"></i>
				</a>
			</div>
		</div>
		<div class="page-sidebar-body">
			<div class="page-sidebar-body-title">组织架构</div>
			<#if units?exists && units?size gt 0>
			<ul class="chosen-tree chosen-tree-tier1">
				<li class="sub-tree">
					<div class="chosen-tree-item" data-index="1">
						<span class="arrow"></span>
						<span class="name">${loginInfo.unitName!}</span>
					</div>
					<ul class="chosen-tree chosen-tree-tier2" style="width: 383px;">
						<#list units as unit>
						<li class="sub-tree open unit-tree-item" li-id="${unit.id!}">
							<div class="chosen-tree-item" data-index="1${unit_index}" data-id="${unit.id!}">
								<span class="name">${unit.unitName!}</span>
							</div>
						</li>
						</#list>
					</ul>
				</li>
			<ul>		
			<#else>
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
					</span>
					<div class="no-data-body">
						<p class="no-data-txt">没有相关数据</p>
					</div>
				</div>
			</div>
			</#if>
		</div>
	</div>
	
	<div class="page-content-inner">
		<div id="stuDataDiv">
	    	<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
					</span>
					<div class="no-data-body">
						<h3>请选择要操作的学校</h3>
					</div>
				</div>
			</div>
	    </div>
	</div>
</div>
<script>
hidenBreadBack();

//收缩树
$('.page-sidebar').on('click', '.chosen-tree-item .arrow', function(e){
	e.stopPropagation();
	var $scroll = $('.page-sidebar-body'),
		$tree = $scroll.children('.chosen-tree'),
		$li = $(this).parent('.chosen-tree-item').parent('li');
	if ($li.hasClass('sub-tree')) {
		$li.toggleClass('open');
	}
	var sLeft = $scroll.scrollLeft(),
		sWidth = $scroll.width(),
		tWidth = sLeft + sWidth;
	$tree.width(tWidth);
});

$('.unit-tree-item').on('click',function(){
	var uid = $(this).attr('li-id');
	$(this).addClass('active').siblings('li').removeClass('active');
	toSch(uid);
});

$('.btn-search').on('click',function(){
	searchUnit();
});

$('#unitName').keydown(function(event){
    if(event.keyCode==13){
    	searchUnit();
    }
});

function searchUnit(){
	var un = $.trim($('#unitName').val());
	if(un == '${searchUnitName!}'){
		return;
	}
	if(un.indexOf('\'')>-1||un.indexOf('%')>-1){
        layer.tips('机构名称不能包含单引号、百分号等特殊符号！',$('#unitName'), {
					tipsMore: true,
					tips: 3
				});
        return ;
    }
    var url='${request.contextPath}/newstusys/abnormal/edu/index/page?searchUnitName='+encodeURIComponent(un);
    $('.model-div').load(url);
}

function toSch(uid){
	var url = '${request.contextPath}/newstusys/abnormal/sch/index/page?schoolId='+uid;
	$('#stuDataDiv').load(url);
}
</script>