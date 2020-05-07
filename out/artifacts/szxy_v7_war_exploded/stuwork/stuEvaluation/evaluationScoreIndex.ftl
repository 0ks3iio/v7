<div id="itemShowDivId">
	
</div>
<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
		var url="${request.contextPath}/stuwork/evaluation/score/list/page";
		$("#itemShowDivId").load(url);
	}
</script>
