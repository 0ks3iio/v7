<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="examReportDiv">
</div>
<script type="text/javascript">
	$(function(){
		showReportIndex();
	});
	function showReportIndex(){
		var url =  '${request.contextPath}/exammanage/examReport/head/page';
		$("#examReportDiv").load(url);
	}
</script>