<div id="showList">
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		showList();
	});
	function showList(){
		var url =  '${request.contextPath}/basedata/courseopen/real/index/page';
		$("#showList").load(url);
	}
</script>
