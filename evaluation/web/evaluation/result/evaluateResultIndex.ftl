<div id="itemShowDivId">
	
</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
		var url="${request.contextPath}/evaluate/stu/list";
		$("#itemShowDivId").load(url);
	}
</script>
