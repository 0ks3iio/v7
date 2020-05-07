<div id="carPage">
	
</div>
<script>
	$(function(){
		showStuResult();
	});
	
	function showStuResult() {
		var url = "${request.contextPath}/careerplan/student/resultorlist";
		$("#carPage").load(url);
	}
</script>