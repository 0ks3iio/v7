<table class="table table-bordered table-striped table-hover" id="mytable">
	<thead>
		<tr>
			<th class="text-center" >班级名次</th>
			<th class="text-center" >年级名次</th>
			<th class="text-center" >姓名</th>
			<th class="text-center" >学号</th>
			<th class="text-center">总分</th>
			<#if subjectInfoList?exists && (subjectInfoList?size > 0)>
				<#list subjectInfoList as list>
				<th class="text-center">${list.courseName!}</th>
				</#list>
			</#if>
			<th class="text-center" >详情</th>
		</tr>
	</thead>
	<tbody>
		<#if studentStatDtoList?exists && (studentStatDtoList?size > 0)>
			<#list studentStatDtoList as dto>
		<tr>
			<td class="text-center">${dto.classRank!}</td>
			<td class="text-center">${dto.gradeRank!}</td>
			<td class="text-center">${dto.studentName!}</td>
			<td class="text-center">${dto.studentCode!}</td>
			<#if (dto.scoresList)?exists && ((dto.scoresList)?size > 0)>
				<#list (dto.scoresList) as dtoList>
				<td class="text-center">${dtoList!}</td>
				</#list>
			</#if>
			<td class="text-center"><a class="table-btn" href="javascript:findStudentScores('${dto.studentId!}');">查看</a></td>
		</tr>
			</#list>
		</#if>
	</tbody>
</table>
<script>
	$(function(){
		<#if subjectInfoList?exists && (subjectInfoList?size > 0)>
			var size=${subjectInfoList?size};
			if(size>8){
				width=((size-8)*80+1100)+"px";
				$("#mytable").css({width: width});
			}
		</#if>
	})
	
	function findStudentScores(studentId) {
		var examId = $("#examId").val();
		var str = "?examId="+examId+"&studentId="+studentId;
		var url='${request.contextPath}/examanalysis/examStudent/examScores/page'+str;
		$("#examStudentList").load(url);
	}
</script>