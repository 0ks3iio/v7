		<div id="contentId">
		</div>
<script>
$(function(){
	changeFirstTab('','');
})
function changeFirstTab(index,courseId){
	var url = "${request.contextPath}/newgkelective/${divideId!}/";
	url += "teacherClass/choosedXzb?gradeId=${gradeId!}&arrayId=${arrayId!}&arrayItemId=${arrayItemId!}&type=1&courseId=";
	$("#contentId").load(url);
}
</script>