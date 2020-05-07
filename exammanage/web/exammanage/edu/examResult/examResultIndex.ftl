<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="examResultDiv">
</div>
<script type="text/javascript">
	$(function(){
		showResultIndex();
	});
	function showResultIndex(){
		var url =  '${request.contextPath}/exammanage/edu/examResult/head/page';
		$("#examResultDiv").load(url);
	}
</script>