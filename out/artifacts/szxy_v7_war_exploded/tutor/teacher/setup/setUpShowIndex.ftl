<div id="setUpIndex" >
</div>
<div id="showTutorIndex" style="display:none">
</div>

<script type="text/javascript">
$(function(){
	setUpIndex();
});
function setUpIndex(){
	var url =  '${request.contextPath}/tutor/teacher/setUp/index';
	$("#setUpIndex").load(url);
	$("#setUpIndex").show();
	$("#showTutorIndex").hide();
}
function showTutorIndex(tutorRoundId){
	var url =  '${request.contextPath}/tutor/teacher/setUp/showTutorIndex?tutorRoundId='+tutorRoundId;
	$("#showTutorIndex").load(url);
	$("#showTutorIndex").show();
	$("#setUpIndex").hide();
}

</script>