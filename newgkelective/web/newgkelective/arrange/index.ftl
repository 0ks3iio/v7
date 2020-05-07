<div id="showList">
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		hidenBreadBack();
		showList();
	});
	function showList(){
		var url =  '${request.contextPath}/newgkelective/index/list/page';
		$("#showList").load(url);
	}
</script>

