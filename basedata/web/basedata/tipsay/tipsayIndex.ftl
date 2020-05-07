<div id="showList">
	
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		
		showTabList();
	});
	function showTabList(){
		var url =  '${request.contextPath}/basedata/tipsay/tab/page?type=${type!}';
		$("#showList").load(url);
	}
</script>