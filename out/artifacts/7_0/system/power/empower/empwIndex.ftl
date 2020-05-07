<#import "/fw/macro/treemacro.ftl" as treemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<input type="hidden" id="targetId" value="">
<input type="hidden" id="pid" value="">
<div class="clearfix">
	<div class="tree-wrap">
	<#if type?default('') == '2'>
		<h4>角色选择</h4>
			<@treemacro.typeServerRoleForUserInsetTree height="550" click="onTreeClick"/>
		</div>
	<#else>
		<h4>用户选择</h4>
			<@treemacro.ownerTypeUserForUnitInsetTree height="550" click="onTreeClick"/>
		</div>
	</#if>
	<div id="showList" class="table-container">
		
	</div>
</div>
<script>
$(function(){
	showList('','','');
});
function onTreeClick(event, treeId, treeNode, clickFlag){
   var paramType;
   var pid;
   <#if type?default('') == '2'>
      paramType = "role";
      pid = treeNode.pId;
	  $("#pid").val(pid);
   <#else>
      paramType = "user";
      pid = "";
   </#if>
	if(treeNode.type == paramType){
		var id = treeNode.id;
		$("#targetId").val(id);
		showList('2',id,pid);   <#--   现在先不考虑类型之间的区别 -->
	}
}
function showList(type,id,pid){
	var url =  '${request.contextPath}/system/user/power/findPowerList/page?targetId='+id+'&type='+${type!}+'&pid='+pid;
	$("#showList").load(url);
}

function goflushList(){
    id = $("#roleId").val(id);
    pid = $("#pid").val(id);
    showRoleList('2',id,pid);
}

</script>