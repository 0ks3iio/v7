	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0)" onclick="showList('1')" role="tab" data-toggle="tab">必修课</a></li>
			<li role="presentation"><a href="javascript:void(0)" onclick="showList('2')" role="tab" data-toggle="tab">选修课</a></li>
		</ul>
		
		<div class="tab-content" id="showDiv">
		</div>
	</div>



<script>
$(function(){
	showList('1');
});

function showList(subjectType) {
	var url = "${request.contextPath}/basedata/courseopen/class/add/page?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}&classId=${classId!}&subjectType="+subjectType ;
	$("#showDiv").load(url);
}

</script>