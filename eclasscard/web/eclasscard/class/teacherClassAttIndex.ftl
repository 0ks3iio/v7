<div id="indexShowDiv" class="box box-default">
</div>
<div id="detailShowDiv" class="box box-default" style="display:none">
</div>
<script type="text/javascript">
$(function(){
	teacherSigninTab();
});
function teacherSigninTab(){
	var url =  '${request.contextPath}/eclasscard/teacher/signin/tabPage';
	$("#indexShowDiv").load(url);
}
function showDetail(sectionNumber){
	var date = $("#selectDate").val();
	var url =  '${request.contextPath}/eclasscard/teacher/signin/detail?sectionNumber='+sectionNumber+'&date='+date;
	$("#detailShowDiv").load(url);
	$("#detailShowDiv").show();
	$("#indexShowDiv").hide();
}
function backIndex(){
	changeSelect();
	$("#detailShowDiv").hide();
	$("#indexShowDiv").show();
}
</script>