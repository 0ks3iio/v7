<div id="bb" class="tab-pane fade active in">
	<h3>各科成绩分布分析</h3>
	<div class="filter filter-f16">
		<div class="filter-item">
			<span class="filter-name">科目：</span>
			<div class="filter-content">
				<select name="subjectId" id="subjectId" class="form-control" style="width:120px" onChange="changeSubject();">
					<#if courseList?exists && (courseList?size>0)>
						<#list courseList as list>
							<option value="${list.id!}">${list.subjectName!}</option>								
						</#list>
					</#if>
				</select>
			</div>
		</div>
	</div>
    <div class="filter" style="height:450px;width:1100px;overflow:auto;" id="mychart02Div"> 
    	
    </div>
    <h3>各科班级前5名分布</h3>
    <table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th class="text-center" rowspan="2">类别</th>
				<th class="text-center" colspan="2">第一名</th>
				<th class="text-center" colspan="2">第二名</th>
				<th class="text-center" colspan="2">第三名</th>
				<th class="text-center" colspan="2">第四名</th>
				<th class="text-center" colspan="2">第五名</th>
			</tr>
			<tr>
				<th class="text-center">姓名</th>
				<th class="text-center">分数</th>
				<th class="text-center">姓名</th>
				<th class="text-center">分数</th>
				<th class="text-center">姓名</th>
				<th class="text-center">分数</th>
				<th class="text-center">姓名</th>
				<th class="text-center">分数</th>
				<th class="text-center">姓名</th>
				<th class="text-center">分数</th>
			</tr>
		</thead>
		<tbody>
		<#if scoresRankMap?exists && (scoresRankMap?size>0)>
			<#list scoresRankMap?keys as key>
				<tr>
					<td class="text-center">${key!}</td>
					<#list scoresRankMap[key] as value>
						<#list value?split("_") as nameAndScores>
							<td class="text-center" title="${nameAndScores!}"><#if (nameAndScores!) != "" && (nameAndScores!)?length gt 5>${nameAndScores?substring(0,5)}...<#else>${nameAndScores!}</#if></td>
						</#list>
					</#list>
				</tr>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	$(function(){
		changeSubject();
	});
	
	function changeSubject() {
		var subjectId = $("#subjectId").val();
		var examId = "${examId!}";
		var gradeId = "${gradeId!}";
		var classId = "${classId!}";
		var str = "?subjectId="+subjectId+"&examId="+examId+"&gradeId="+gradeId+"&classId="+classId;
		var url='${request.contextPath}/examanalysis/examClass/ScoreDistribution/page'+str;
		$("#mychart02Div").load(url); 
	}

</script>