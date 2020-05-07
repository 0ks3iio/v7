<div id="pageDiv">
</div>
<script>
$(function(){
	var url = '${request.contextPath}/basedata/mastercourseschedule/index/page?type=${type!"1"}';
	$('#pageDiv').load(url);
})
</script>