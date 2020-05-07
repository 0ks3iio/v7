<#if  roleList?exists &&roleList?size gt 0>
<#list roleList as role>
<div id="${role.id!}" class="count-line">
	<span onclick="loadRoleDetailData('${role.id!}');return false;">${role.name!}<#if role.type! ==0>(系统内置)</#if></span>
	<#if role.type! ==1>
	<div class="pos-right">
		<img src="${request.contextPath}/static/bigdata/images/edits.png" onclick="editRole('${role.id!}')" class="js-edit"/>
		<img src="${request.contextPath}/static/bigdata/images/removes.png" onclick="deleteRole('${role.id!}','${role.name!}')" class="js-remove"/>
	</div>
	</#if>
</div>
</#list>
</#if>
<script type="text/javascript">
	$(document).ready(function(){
	<#if roleId! =="">
		<#if  roleList?exists &&roleList?size gt 0>
			<#list roleList as role>
 					<#if role_index == 0>
							loadRoleDetailData('${role.id!}');
   				 	</#if>
			</#list>
		</#if>
	<#else>
		$("#${roleId!}").addClass('active');
		loadRoleDetailData('${roleId!}');
	</#if>
	});
</script>