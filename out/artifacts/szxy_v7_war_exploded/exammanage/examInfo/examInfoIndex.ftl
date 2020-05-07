<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="exammanageDiv">
</div>
<script type="text/javascript">
	$(function(){
		showExamIndex();
	});
	function showExamIndex(){
		var url =  '${request.contextPath}/exammanage/examInfo/head/page';
		$("#exammanageDiv").load(url);
	}
</script>