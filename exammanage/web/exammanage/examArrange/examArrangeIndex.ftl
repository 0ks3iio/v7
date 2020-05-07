<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="examArrangeDiv">
</div>
<script type="text/javascript">
	$(function(){
		showArrangeIndex();
	});
	function showArrangeIndex(){
		var url =  '${request.contextPath}/exammanage/examArrange/head/page';
		$("#examArrangeDiv").load(url);
	}
</script>