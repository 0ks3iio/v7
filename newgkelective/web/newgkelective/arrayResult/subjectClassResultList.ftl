<div class="table-container-header">共<#if dtoList?exists>${dtoList?size!}<#else>0</#if>份结果</div>
<div class="table-container-body">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>班级名称</th>
				<th>科目</th>
				<#if !(isXzbArray!)>
				<th>考试类型</th>
				</#if>
				<#--<th>班级类型</th>-->
				<th>场地</th>
				<th>任课老师</th>
				<th>总人数</th>
				<th>男</th>
				<th>女</th>
				<th class="noprint">操作</th>
			</tr>
		</thead>
		<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.className!}</td>
				<td>${item.subjectName!}</td>
				<#if !(isXzbArray!)>
				<td>${item.subjectType!}</td>
				</#if>
				<#--<td>${item.bestType!"平行班"}</td>-->
				<td><#if item.placeNames! == "">/<#else>${item.placeNames!""}</#if></td>
				<td>${item.teacherNames!}</td>
				<td>${item.studentNum!}</td>
				<td>${item.boyNum!}</td>
				<td>${item.girlNum!}</td>
				<td class="noprint">
					<a href="javascript:void(0);" onClick="searchListLeft('${item.classId!}','1');">查看学生</a> |
					<a href="javascript:void(0);" onClick="searchListLeft('${item.classId!}','0');">查看课表</a>
				</td>
			</tr>
			</#list>
			</#if>
		</tbody>
	</table>
</div>

<script>
	function searchListLeft(classId,openType){
		var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectStudentResultLeft?classId='+classId+'&openType='+openType;
		$("#tableList").load(url);
	}
	<#--
		function searchStudentList(classId){
			var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectStudentResult?classId='+classId;
			$("#tableList").load(url);	
		}
		
		function searchTimetableList(classId){
			var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectTimetableResult/index/page?classType=2&classId='+classId;
			$("#tableList").load(url);
		}
	-->
	
</script>