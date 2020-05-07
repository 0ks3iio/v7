<div id="roleIndex" >
</div>
<div id="roleUserIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	roleIndex();
});
function roleIndex(){
	var url =  '${request.contextPath}/system/ap/role/showRoleIndex/page';
	$("#roleIndex").load(url);
	$("#roleIndex").show();
	$("#roleUserIndex").hide();
}
function showRoleUserIndex(roleId){
	var url =  '${request.contextPath}/system/ap/role/roleUserIndex/page?roleId='+roleId;
	$("#roleUserIndex").load(url);
	$("#roleUserIndex").show();
	$("#roleIndex").hide();
}

</script>