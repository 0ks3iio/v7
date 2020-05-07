<#if dtoList?exists && (dtoList?size > 0)>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>课程名</th>
					<th>班级</th>
					<th>录入状态</th>
					<th>考试成绩</th>
					<#if tabIndex?default('1')=='2'>
					<th>是否及格</th>
					<th>学分</th>
					</#if>
					<#if needGeneral?exists && needGeneral>
			        <th>总评成绩</th>
		        	</#if>
		        	<th>状态</th>
				</tr>
		</thead>
		<tbody>
				<#list dtoList as dto>
					<tr>
						<td>${dto.subjectName!}</td>
						<td>${dto.className!}</td>
						<td>${dto.isLock!}</td>
						<#if dto.isLock?default('未录入')=='已录入'>
						<td>${dto.score!}</td>
						<#if tabIndex?default('1')=='2'>
						<td>${dto.isPass!}</td>
						<td>${dto.toScore!}</td>
						</#if>
						<#if needGeneral?exists && needGeneral>
						<td>${dto.toScore!}</td>
						</#if>
						<td>${mcodeSetting.getMcode("DM-CJLX", '${dto.scoreStatus!}')}</td>
						<#else>
						<td>——</td>
						<#if tabIndex?default('1')=='2'>
						<td>——</td>
						<td>——</td>
						</#if>
						<#if needGeneral?exists && needGeneral>
						<td>——</td>
						</#if>
						<td>——</td>
						</#if>
					</tr>
				</#list>
		</tbody>
	</table>
</div>
<#else>
暂无数据
</#if>