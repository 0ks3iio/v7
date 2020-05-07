<div id="showDate">
</div>
<script>
	$(function(){
		$("#showDate").load("${request.contextPath}/newgkelective/edu/itemList/page?type=${type!}");
	})
</script>