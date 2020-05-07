<div id="aa" class="tab-pane fade active in">
    <h3>当前考试情况</h3>
    <div class="table-container">
         <div class="table-container-body">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">科目</th>
						<#if avgTitle?exists && avgTitle?size gt 0>
						<#list avgTitle as title>
						<th class="text-center">年级${title!}平均分</th>
						</#list>
						</#if>
						<th class="text-center">总体平均分</th>
						<th class="text-center">最高分</th>
						<th class="text-center">最低分</th>
						<th class="text-center">统计人数</th>
					</tr>
				</thead>
				<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
				<#list dtoList as dto>
					<tr>
						<td class="text-center">${dto.subjectName!}</td>
					    <#assign avgmap=dto.avgMap>
					    <#if avgmap?exists && avgmap?size gt 0>
							<#if avgTitle?exists && avgTitle?size gt 0>
							<#list avgTitle as title>
							<td class="text-center">${avgmap[title]?default(0)?string('#.##')}</td>
							</#list>
							</#if>
						<#else>
							<#if avgTitle?exists && avgTitle?size gt 0>
							<#list avgTitle as title>
							<th class="text-center">0</th>
							</#list>
							</#if>
						</#if>
						<td class="text-center">${dto.averageScore?string('#.##')}</td>
						<td class="text-center">${dto.maxScore?string('#.##')}</td>
						<td class="text-center">${dto.minScore?string('#.##')}</td>
						<td class="text-center">${dto.statStuNum}</td>
					</tr>
				</#list>
				</#if>
				</tbody>
			</table>
	     </div>
	</div>
	<h3>当前学期历次考试对比</h3>
	<div class="filter filter-f16">
           <div class="filter-item">
				<span class="filter-name">科目：</span>
				<div class="filter-content">
					<select name="subjectId" id="subjectId" class="form-control"  onChange="showChart()">
					
					<#if dtoList?exists && dtoList?size gt 0>
					<#list dtoList as dto>
					     <option value="${dto.subjectId!}">${dto.subjectName!}</option>
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
					     <#if avgTitle?exists && avgTitle?size gt 0>
							<#list avgTitle as title>
							<option value="1_${title!}">年级${title!}平均分</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
    </div>
	<div class="filter"> 
        <div id="mychart01" style="width:1100px;">
        
        </div> 
    </div>
</div>
<script>
	$(function(){
		showChart();
	});
	function showChart(){
		var gradeId=$("#gradeId").val();
		var examId=$("#examId").val();
		var subjectId=$("#subjectId").val();
		var analysisType=$("#analysisType").val();
		if(gradeId=='' || examId=='' || subjectId=='' || analysisType==''){
			$("#mychart01").text('');
		}else{
			var url =  '${request.contextPath}/examanalysis/examGrade/generalChart/page?gradeId='+gradeId+'&examId='+examId+'&subjectId='+subjectId+'&analysisType='+analysisType;
			url=encodeURI(url);
			$("#mychart01").load(url);
		}
	}
</script>