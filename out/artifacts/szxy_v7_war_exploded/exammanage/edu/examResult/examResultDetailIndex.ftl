<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
<a href="" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-body" id="examResultDiv">
	</div>
</div>
<script type="text/javascript">
	$(function(){
		showResultList();
	});
	function showResultList(){
		var url =  '${request.contextPath}/exammanage/edu/examResult/detailList/page';
		$("#examResultDiv").load(url);
	}
</script>
						