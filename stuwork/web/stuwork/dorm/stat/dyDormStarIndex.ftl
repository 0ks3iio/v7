<div id="itemShowDivId">
	
</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
		var url="${request.contextPath}/stuwork/dorm/stat/starList/page";
		$("#itemShowDivId").load(url);
	}
</script>
