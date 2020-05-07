<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="scoreProcessDiv">
</div>
<script type="text/javascript">
	$(function(){
		showScoreProcessIndex();
	});
	function showScoreProcessIndex(){
		var url =  '${request.contextPath}/exammanage/scoreProcessing/head/page';
		$("#scoreProcessDiv").load(url);
	}
</script>