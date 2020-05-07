<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>考号</th>
					<th>学号</th>
					<th>班级</th>
					<#if (courseList?exists && courseList?size>0)>
						<#list courseList as couItem>
							<th>${couItem.shortName!}</th>
						</#list>
					</#if>
				</tr>
		</thead>
		<tbody>
			<#if dtoList?exists && (dtoList?size > 0)>
				<#list dtoList as dto>
					<tr>
						<td>${dto.stuName!}</td>
						<td>${dto.stuExamNum!}</td>
						<td>${dto.stuCode!}</td>
						<td>${dto.className!}</td>
						<#if (courseList?exists && courseList?size>0)>
							<#list courseList as couItem>
							<td>
								<#assign myscoreMap=dto.scoreInfoMap>
								<#if (myscoreMap?exists&& (myscoreMap?size>0))  && myscoreMap[couItem.id]?exists>
									${myscoreMap[couItem.id].score!}
									<#if !(myscoreMap[couItem.id].scoreStatus?default('0')=='0')>
										(${mcodeSetting.getMcode("DM-CJLX", '${myscoreMap[couItem.id].scoreStatus!}')})
									</#if>
								</#if>
							</td>
							</#list>
						</#if>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>		
</div>
<script>

</script>