<script language="javascript" type="text/javascript">
$(document).ready(function(){
	$("#${right!'0'}_sort").tablesorter({
		headers:{
			0:{sorter:false},
			1:{sorter:false},
			2:{sorter:false},
			3:{sorter:false}
		},
		sortInitialOrder: 'desc',
		widgets: ['fixFirstNumber']  
	});
});
</script>
<#if right?exists>

		<ul id="rangeTab" class="nav nav-tabs nav-tabs-1" role="tablist" style="border: 1px solid #ddd;width:100%;">
			<#list ['A','B','C','D'] as r>
				<li <#if range==r>class="active"</#if> role="presentation" data-range="${r}">
					<a href="javascript:void(0)" onclick="changeRangeTab('${r}')" role="tab" data-toggle="tab">
						${r}层 <span class="badge badge-yellow">${rangeMap[r]!0}</span>
					</a>
				</li>
			</#list>
		</ul>

</div>  
</#if>
<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass">
<div class="table-switch-data default labelInf">
	<span>总数：<em>${dtoList?size!}</em></span>
	<span>男：<em>${manCount!}</em></span>
	<span>女：<em>${woManCount!}</em></span>
	<span>${courseName!}：<em>${courseAvg?string("#.##")!'0'}</em></span>
	<span>语数英：<em>${ysyAvg?string("#.##")!'0'}</em></span>
	<span>总成绩：<em>${totalAvg?string("#.##")!'0'}</em></span>
</div>
<table class="table table-bordered table-striped table-hover no-margin mainTable tablesorter" style="font-size:1em;" id="${right!'0'}_sort">
	<thead>
		<tr>
			<th>
				<label class="pos-rel">
					<input onclick="selAll(event)" name="course-checkbox" type="checkbox" class="wp">
					<span class="lbl"></span>
				</label>
			</th>
			<th>序号</th>
			<th>姓名</th>
			<th>性别</th>
			<th>${courseName!}</th>
			<th>语数英</th>
			<th>总成绩</th>
		</tr>
	</thead>
	<tbody>
		<#if dtoList?exists && (dtoList?size>0)>
			<#list dtoList as dto>
				<tr>
					<td>
						<label class="pos-rel">
							<input name="course-checkbox" type="checkbox" class="wp" value="${dto.studentId!}">
							<span class="lbl"></span>
						</label>
					</td>
					<td>${dto_index +1}</td>
					<td>${dto.studentName!}</td>
					<td>${dto.sex!}</td>
					<td>${dto.subjectScore[subjectId]!}</td>
					<td>${dto.subjectScore["YSY"]!}</td>
					<td>${dto.subjectScore["TOTAL"]!}</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
</div>
