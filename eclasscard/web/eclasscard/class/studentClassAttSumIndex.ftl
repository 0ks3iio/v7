<div id="indexShowDiv" class="box box-default">
</div>
<div id="detailShowDiv" class="box box-default" style="display:none">
</div>
<script type="text/javascript">
$(function(){
	studentAttSumTab();
});
function studentAttSumTab(){
	var url =  '${request.contextPath}/eclasscard/student/sum/tabPage';
	$("#indexShowDiv").load(url);
}
function showDetail(id){
	var beginDate = $("#begindate").val();
	var endDate = $("#enddate").val();
	var url =  '${request.contextPath}/eclasscard/class/stuAttance/detail?studentId='+id+'&beginDate='+beginDate+'&endDate='+endDate;
	$("#detailShowDiv").load(url);
	$("#detailShowDiv").show();
	$("#indexShowDiv").hide();
}
function backIndex(type){
	$("#detailShowDiv").hide();
	$("#indexShowDiv").show();
}
</script>