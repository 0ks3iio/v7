<#import "/fw/macro/treemacro.ftl" as treemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="box box-default">
	<div class="row">
		<div class="col-sm-3">
			<div class="box box-default">
				<div class="box-header">
					<h3 class="box-title">班级菜单</h3>
				</div>
				<#if unitClass?default(-1) != 2>
					<div class="box-body">
					   <ul id="schoolTree" class="ztree"></ul>
					</div>
				<#else>
					<@treemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
				</#if>
			</div>
		</div>
		<div class="col-sm-9" id="showList">
		</div>											
	</div>								
</div>
<script>
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "class"){
		var id = treeNode.id;
		$('#classId').val(id);
		searchList(id);
	}
}

function searchList(classId){
    var url = "${request.contextPath}/stuwork/militaryTraining/pageList?classId="+classId;
    $("#showList").load(url);
}
</script>