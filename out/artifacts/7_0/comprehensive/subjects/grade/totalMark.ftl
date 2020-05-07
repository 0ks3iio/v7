<div id="showIndex">
	
</div>
<script>
	$(function(){
		showIndex();
	});
	
	function showIndex() {
		var url =  '${request.contextPath}/comprehensive/subjects/grade/gradeIndex/page';
		$("#showIndex").load(url);
	}
</script>