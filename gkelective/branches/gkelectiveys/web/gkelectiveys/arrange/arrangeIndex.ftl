<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<!-- 时间 -->
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>

<div id="showList">
	
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(document).ready(function(){
		showList();
	});
	function showList(){
		var url =  '${request.contextPath}/gkelective/arrange/list/page';
		$("#showList").load(url);
	}
</script>

