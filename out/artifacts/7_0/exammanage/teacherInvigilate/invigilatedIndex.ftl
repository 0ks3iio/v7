<div id="invigilateDiv">
</div>
<script type="text/javascript">
	$(function(){
		showArrangeIndex();
	});
	function showArrangeIndex(){
		var url =  '${request.contextPath}/exammanage/teacherInvigilated/head/page';
		$("#invigilateDiv").load(url);
	}
</script>