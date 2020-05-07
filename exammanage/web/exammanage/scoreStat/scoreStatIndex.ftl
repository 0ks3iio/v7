<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="scoreStatDiv">
</div>
<script type="text/javascript">
	$(function(){
		showScoreStatIndex();
	});
	function showScoreStatIndex(){
		var url =  '${request.contextPath}/exammanage/scoreStat/head/page';
		$("#scoreStatDiv").load(url);
	}
</script>