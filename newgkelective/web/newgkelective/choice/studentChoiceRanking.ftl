<#if dtoList?exists && dtoList?size gt 0>
<div class="curr-chart-title">所选科目排名</div>
	<table class="table table-striped table-hover no-margin">
		<thead>
			<tr>
		        <th>所选科目</th>
		        <th>同选学生</th>
		        <th>年级排名</th>
		        <th>同选排名</th>
			</tr>
	    </thead>
	    <tbody>
	    	<#list dtoList as dto>
			<tr>
		        <td>${dto.subjectName!}</td>
		        <td>${dto.selectNum!}</td>
		        <td>${dto.gradeRanking!}</td>
		        <td>${dto.selectRanking!}</td>
			</tr>
			</#list>
	    </tbody>
	</table>
</div>
</#if>