<div id="aa" class="tab-pane fade active in">
    <h3>当前考试成绩概况</h3>
    <div class="table-container">
         <div class="table-container-body">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">科目</th>
						<th class="text-center">年级平均分</th>
						<th class="text-center">班级平均分</th>
						<#if titleList?exists && titleList?size gt 0>
						<#list titleList as title>
						<th class="text-center">本班${title!}平均分</th>
						</#list>
						</#if>
						<th class="text-center">最高分</th>
						<th class="text-center">最低分</th>
						<th class="text-center">统计人数</th>
						<th class="text-center">平均分名次</th>
					</tr>
				</thead>
				<tbody>
				<#if subjectStatDto?exists && subjectStatDto?size gt 0>
				<#list subjectStatDto as dto>
					<tr>
						<td class="text-center">${dto.subjectName!}</td>
						<td class="text-center">${dto.gradeAvg?string('#.##')}</td>
						<td class="text-center">${dto.classAvg?string('#.##')}</td>
					    <#assign spaceMap=dto.spaceMap>
					    <#if spaceMap?exists && spaceMap?size gt 0>
							<#if titleList?exists && titleList?size gt 0>
							<#list titleList as title>
							<td class="text-center">${spaceMap[title]?string('#.##')}</td>
							</#list>
							</#if>
						<#else>
							<#if titleList?exists && titleList?size gt 0>
							<#list titleList as title>
							<th class="text-center">0</th>
							</#list>
							</#if>
						</#if>
						<td class="text-center">${dto.classMax?string('#.##')}</td>
						<td class="text-center">${dto.classMin?string('#.##')}</td>
						<td class="text-center">${dto.statStuNum}</td>
						<td class="text-center">${dto.rank}</td>
					</tr>
				</#list>
				</#if>
				</tbody>
			</table>
	     </div>
	</div>
	<h3>当前学期历次考试概况</h3>
	<div class="filter filter-f16">
           <div class="filter-item">
				<span class="filter-name">科目：</span>
				<div class="filter-content">
					<select name="subjectId" id="subjectId" class="form-control"  onChange="showChart()">
					
					<#if courseList?exists && courseList?size gt 0>
					<#list courseList as dto>
					     <option value="${dto.id!}">${dto.subjectName!}</option>
					</#list>
					<#else>
						<option value="">--请选择--</option>
					</#if>
					</select>
					
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">分析类型：</span>
				<div class="filter-content">
					<select name="analysisType" id="analysisType" class="form-control" onChange="showChart()">
					     <option value="0_allAvg">总体平均分</option>
					     <option value="0_max">最高分</option>
					     <option value="0_min">最低分</option>
					     <#if titleList?exists && titleList?size gt 0>
							<#list titleList as title>
							<option value="1_${title!}">本班${title!}平均分</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
    </div>
	<div class="filter"> 
        <div id="classchart01" style="width:1100px;">
        
        </div> 
    </div>
</div>
<script>
	$(function(){
		showChart();
	});
	function showChart(){
		var examId=$("#examId").val();
		var subjectId=$("#subjectId").val();
		var classId=$("#classId").val();
		var analysisType=$("#analysisType").val();
		if(classId=='' || examId=='' || subjectId=='' || analysisType==''){
			$("#classchart01").text('');
		}else{
			var url =  '${request.contextPath}/examanalysis/examClass/generalChart/page?classId='+classId+'&examId='+examId+'&subjectId='+subjectId+'&analysisType='+analysisType;
			url=encodeURI(url);
			$("#classchart01").load(url);
		}
	}
</script>