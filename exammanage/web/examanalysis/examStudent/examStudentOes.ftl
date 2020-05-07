<div id="bb" class="tab-pane fade active in">
	<div class="filter filter-f16">
		<div class="filter-item">
			<span class="filter-name">分析类型：</span>
			<div class="filter-content">
				<select name="analysisType" id="analysisType" class="form-control" style="width:120px" onChange="changeType();">
					<option value="all">总分</option>
					<#if courseList?exists && (courseList?size>0)>
						<#list courseList as list>
							<option value="${list.id!}">${list.subjectName!}</option>								
						</#list>
					</#if>
					<option value="classRank">班级名次</option>
					<option value="gradeRank">年级名次</option>
				</select>
			</div>
		</div>
	</div>		
    <div class="filter" id="mychart02Div"> 
    	
    </div>
    <div class="filter filter-f16">
		<div class="filter-item">
			<span class="filter-name">考试名称：</span>
			<div class="filter-content">
				<select name="otherExamId" id="otherExamId" class="form-control" style="width:220px" onChange="changeOtherExam();">
					<#if examInfoList?exists && (examInfoList?size>0)>
						<#list examInfoList as infoList>
							<option value="${infoList.id!}">${infoList.examName!}</option>
						</#list>
					<#else>
						<option value="">---请选择---</option>
					</#if>
				</select>
			</div>
		</div>
	</div>
	<div id="otherExamTableDiv">
		
	</div>
</div>
<script>
	$(function(){
		changeType();
		changeOtherExam();
	});
	
	function changeType() {
		var analysisType = $("#analysisType").val();
		var studentId = "${studentId!}";
		var classId = "${classId!}";
		var gradeId = $("#gradeId").val();
		var str = "?studentId="+studentId+"&classId="+classId+"&analysisType="+analysisType+"&gradeId="+gradeId;
		var url = '${request.contextPath}/examanalysis/examStudent/otherAllExamScoreDiv/page' + str;
		$("#mychart02Div").load(url);
	}
	
	function changeOtherExam() {
		var studentId = "${studentId!}";
		var classId = "${classId!}";
		var examId = $("#otherExamId").val();
		var str = "?studentId="+studentId+"&classId="+classId+"&examId="+examId;
		var url = '${request.contextPath}/examanalysis/examStudent/otherExamScoreDiv/page'+str;
		$("#otherExamTableDiv").load(url);
	}
</script>