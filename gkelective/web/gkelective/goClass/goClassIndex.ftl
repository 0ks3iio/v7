<script src="${request.contextPath}/gkelective/js/myscriptCommon.js"></script> 
<a href="javascript:" class="page-back-btn gotoIndexClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div id="itemShowDivId">
	
</div>

<script type="text/javascript">
	$(function(){
		itemShowList();
	});
	function itemShowList(){
		var url =  '${request.contextPath}/gkelective/${arrangeId}/goClass/list/page';
		$("#itemShowDivId").load(url);
	}
</script>
