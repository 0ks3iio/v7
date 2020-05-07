<div id="showList">
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		showList();
	});
	function showList(){
		var url =  '${request.contextPath}/scoremanage/plan/head/page';
		$("#showList").load(url);
	}
</script>