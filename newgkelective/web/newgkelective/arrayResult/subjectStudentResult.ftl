<div class="print">
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<p>${dto.className!}</p>
		</div>
	</div>
	<#if arrangeType='01'>
	<div class="filter-item">
		<span class="filter-name">考试类型：</span>
		<div class="filter-content">
			<p>${dto.subjectType!}</p>
		</div>
	</div>
	</#if>
	<div class="filter-item">
		<span class="filter-name">教室：</span>
		<div class="filter-content">
			<p>${dto.placeNames!"未设置"}</p>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">总人数：</span>
		<div class="filter-content">
			<p>${dto.studentNum!}</p>
		</div>
	</div>
</div>

<div class="table-container">
	<div class="table-container-header">共${dto.studentNum!0}份结果</div>
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>姓名</th>
					<th>学号</th>
					<th>性别</th>
					<th>原行政班</th>
					<#if arrangeType='01'>
					<th>新行政班</th>
					</#if>
				</tr>
			</thead>
			<tbody>
				<#if studentList?exists && studentList?size gt 0>
				<#list studentList as item>
				<tr>
					<td>${item_index+1}</td>
					<td>${item.studentName!}</td>
					<td>${item.studentCode!}</td>
					<td><#if item.sex?exists>${sexNameMap[item.sex+""]!}</#if></td>
					<td>${classNameMap[item.classId]!}</td>
					<#if arrangeType='01'>
					<td>${gradeName!}${studentClassMap[item.id]!}</td>
					</#if>
				</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
</div>
