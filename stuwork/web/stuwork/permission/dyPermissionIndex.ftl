<div class="box box-default">
	<div class="box-body">

		<div class="explain">
			<p>说明：班主任默认查自己班的，年级组长默认查自己年级下的班级.</p>
		</div>
		
		<div id="showPermissionList" class="table-container">
		</div>
	</div>
</div>

<script type="text/javascript">

$(function(){
	showPermissionList();
})
function showPermissionList(){
    var   url =  '${request.contextPath}/stuwork/permission/list.action?classType=1&permissionType=1';
    $("#showPermissionList").load(url);
}

</script>