<div id="indexShowDiv" class="box box-default">
</div>
<div id="detailShowDiv" class="box box-default" style="display:none">
</div>
<script type="text/javascript">
$(function(){
	teacherAttSumTab();
});
function teacherAttSumTab(){
	var url =  '${request.contextPath}/eclasscard/teacher/sum/tabPage';
	$("#indexShowDiv").load(url);
}
function showDetail(id){
	var beginDate = $("#begindate").val();
	var endDate = $("#enddate").val();
	var url =  '${request.contextPath}/eclasscard/teacher/teaAttance/detail?teacherId='+id+'&beginDate='+beginDate+'&endDate='+endDate;
	$("#detailShowDiv").load(url);
	$("#detailShowDiv").show();
	$("#indexShowDiv").hide();
}
function backIndex(type){
	$("#detailShowDiv").hide();
	$("#indexShowDiv").show();
}
</script>