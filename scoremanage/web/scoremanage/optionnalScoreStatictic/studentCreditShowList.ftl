<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mannReForm">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>

					<th>班级</th>
					<th>姓名</th>
                    <th>学号</th>
					<#if courseTypeList?exists && courseTypeList?size gt 0>
					    <#list courseTypeList as courseType >
                            <th>${courseType.name!}</th>
						</#list>
					</#if>
				</tr>
		</thead>
		<tbody id="list">
			<#if statisticDtoList?exists && (statisticDtoList?size > 0)>
				<#list statisticDtoList as dto>
					<tr>

						<td>${dto.className!}</td>
						<td>${dto.studentName!}</td>
                        <td>${dto.studentCode!}</td>
						<#if dto.scoreList?exists && dto.scoreList?size gt 0>
						    <#list dto.scoreList as score>
								<td> ${score!}</td>
							</#list>
						<#else>
							<#if courseTypeList?exists && courseTypeList?size gt 0>
								<#list courseTypeList as courseType >
                                    <td></td>
								</#list>
							</#if>
						</#if>
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	    <#if studentList?exists && (studentList?size > 0)>
           <@htmlcom.pageToolBar container="#showList"/>
        </#if>

</form>		
<script>
	



</script>