<div id="bodyTestSummary" >
</div>
<div id="showTutorIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	showIndex();
});
function showIndex(){
	var url =  '${request.contextPath}/tutor/teacher/setUp/index';
	$("#bodyTestSummary").load(url);
}
</script>