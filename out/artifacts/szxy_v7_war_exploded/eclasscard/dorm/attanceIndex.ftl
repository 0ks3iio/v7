<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
<div id="itemShowDiv">
	
</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
		var url="${request.contextPath}/eclasscard/dorm/attance/list/page";
		$("#itemShowDiv").load(url);
	}
</script>
