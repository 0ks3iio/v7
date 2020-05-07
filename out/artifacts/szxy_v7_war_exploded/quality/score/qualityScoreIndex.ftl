<div id="indexShowDiv" class="box box-default">
</div>
<div id="detailShowDiv" class="box box-default" style="display:none">
</div>
<script type="text/javascript">
$(function(){
	showStatisticsList();
});
function showStatisticsList(){
	var url =  '${request.contextPath}/quality/sum/tab';
	$("#indexShowDiv").load(url);
}
function showDetail(studentId){
	var url =  '${request.contextPath}/quality/sum/personal?studentId='+studentId;
	$("#detailShowDiv").load(url);
	$("#detailShowDiv").show();
	$("#indexShowDiv").hide();
}
function backIndex(type){
	showStatisticsList;
	$("#detailShowDiv").hide();
	$("#indexShowDiv").show();
}
</script>