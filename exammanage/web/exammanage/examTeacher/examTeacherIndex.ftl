<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<div id="examTeacherDiv">
</div>
<script type="text/javascript">
	$(function(){
		showTeacherIndex();
	});
	function showTeacherIndex(){
		var url =  '${request.contextPath}/exammanage/examTeacher/head/page';
		$("#examTeacherDiv").load(url);
	}
</script>