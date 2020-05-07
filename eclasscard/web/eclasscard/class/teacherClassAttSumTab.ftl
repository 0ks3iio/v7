<#import "/fw/macro/treemacro.ftl" as treemacro>
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="box box-default">
	<div class="box-body">
		<div class="filter-container">
			<div class="filter">
				<div class="filter-item">
					<span class="filter-name">时段：</span>
					<div class="filter-content">
						<div class="input-group" style="width:260px;">
							<input type="text" id="begindate" class="form-control datepicker"   onchange="changeSelect()" readonly="readonly" value="${beginDate?string('yyyy-MM-dd')}">
							<span class="input-group-addon">
								<i class="fa fa-minus"></i>
							</span>
							<input type="text" id="enddate" class="form-control datepicker" onchange="changeSelect()" readonly="readonly" value="${(nowDate?default(.now))?string('yyyy-MM-dd')}">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="deptId">
<div class="row">
	<div class="col-sm-3">
		<div class="box box-default" id="id1">
			<div class="box-header">
				<h3 class="box-title">部门菜单</h3>
			</div>
			<@treemacro.deptForUnitInsetTree height="550" click="onTreeClick"/>
		</div>
	</div>
	<div class="col-sm-9" id="showList">
		
	</div>
</div>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script type="text/javascript">
$(function(){
	var maxDate = "${(nowDate?default(.now))?string('yyyy-MM-dd')}";
	$('.datepicker').datepicker({
		endDate:maxDate,
		language: 'zh-CN',
    	format: 'yyyy-mm-dd',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
});
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "dept"){
		var id = treeNode.id;
		$("#deptId").val(id);
		doSearch(id);
	}
}
function doSearch(deptId){
	if(deptId==undefined ||deptId==""){
		deptId=$("#deptId").val();
		if(deptId==undefined ||deptId==""){
			return;
		}
	}
	var beginDate = $("#begindate").val();
	if(beginDate==undefined ||beginDate==""){
		layerTipMsg(false,"","请选择开始时间");
		return;
	}
	var endDate = $("#enddate").val();
	if(beginDate>endDate){
		layerTipMsg(false,"","开始时间大于结束时间");
		return;
	}
    var   url =  '${request.contextPath}/eclasscard/teacher/teaAttance/list?deptId='+deptId+'&beginDate='+beginDate+'&endDate='+endDate;
    $("#showList").load(url);
}

function changeSelect(){
	var deptId=$("#deptId").val();
	doSearch(deptId);
}
</script>