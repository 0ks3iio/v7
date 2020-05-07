<div class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">学科名称：</span>
			<div class="filter-content">
				<div class="input-group">
					<input type="text" class="form-control" id="searchName" name="searchName">
					<a href="javascript:;" id="searchSubject" class="input-group-addon"><span><i class="fa fa-search"></i></span></a>
				</div>
			</div>
		</div>
		<div class="filter-item filter-item-right">
		<#if canEdit>
			<a class="btn btn-white" id="importSubjectExcel">导入</a>
			<a class="btn btn-blue js-addSubject">新建学科</a>
			<a class="btn btn-danger">删除</a>
		</#if>
		</div>
	</div>
	<div id="showListDiv"></div>
</div>

<script>
$(function(){
	refreshPage();
	//查询
	$('#searchSubject').on('click',function(){
		refreshPage();
	});
	//导入Excel
	$('#importSubjectExcel').on('click',function(){
		var url = '${request.contextPath}/basedata/import/courseType/index?type=${type!}';
		$('#aa').load(url);
	});
})

function refreshPage(){
	var searchName = $("#searchName").val();
	var url = '${request.contextPath}/basedata/subject/list/page?searchName='+searchName;
	$("#showListDiv").load(url);
}
</script>
