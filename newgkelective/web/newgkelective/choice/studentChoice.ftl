<div id="showList">
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		hidenBreadBack();
		showList();
	});
	function showList(){
		var url =  '${request.contextPath}/newgkelective/stuChooseSubject/head/page';
		$("#showList").load(url);
	}
</script>

