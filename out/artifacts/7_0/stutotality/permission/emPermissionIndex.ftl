<div id="showList">
</div>
<script type="text/javascript">
	$(document).ready(function(){
		showList();
	});
	function showList(){
		var url =  '${request.contextPath}/basedata/customrole/customRoleList/page?subsystem=${subsystem!}';
		$("#showList").load(url);
	}
</script>