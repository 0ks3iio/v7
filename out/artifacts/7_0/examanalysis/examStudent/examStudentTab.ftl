<div class="box-body clearfix">
	<div class="tab-container">
		<div class="tab-header clearfix">
			<div class="filter filter-f16">
				<div class="filter-item filter-item-right">
					<button class="btn btn-blue" onClick="goBack();">返回</button>
				</div>
			</div>
			<ul class="nav nav-tabs">
				<li class="active">
					<a data-toggle="tab" href="###" aria-expanded="true" onClick="thisExamScore();">当前考试成绩</a>
				</li>
				<li class="">
					<a data-toggle="tab" href="###" aria-expanded="false" onClick="otherExamScore();">本学期历次考试成绩</a>
				</li>
			</ul>
		</div>
		<!-- tab切换开始 -->
		<div class="tab-content" id="tabDiv">
			
		</div><!-- tab切换结束 -->
	</div>
</div>
<script>
	$(function(){
		thisExamScore();
	});

	function goBack() {
		findStudentsScores();
	}
	
	function thisExamScore() {
		var examId = "${examId!}";
		var studentId = "${studentId!}";
		var classId = $("#classId").val();
		var str = "?examId="+examId+"&studentId="+studentId+"&classId="+classId;
		var url = '${request.contextPath}/examanalysis/examStudent/thisExamScore/page'+str;
		$("#tabDiv").load(url);
	}
	
	function otherExamScore() {
		var studentId = "${studentId!}";
		var gradeId = $("#gradeId").val();
		var classId = $("#classId").val();
		var str = "?studentId="+studentId+"&classId="+classId+"&gradeId="+gradeId;
		var url = '${request.contextPath}/examanalysis/examStudent/otherExamScore/page'+str;
		$("#tabDiv").load(url);
	}
</script>