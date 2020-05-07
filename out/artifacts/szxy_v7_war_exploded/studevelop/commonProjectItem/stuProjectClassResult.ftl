<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th class="t-center">序号</th>
					<th class="t-center">学生姓名</th>
					<#if subjectList?exists && subjectList?size gt 0>
						<#list subjectList as item>
							<th class="t-center">${item.name!}</th>
						</#list>
					</#if>
				</tr>
		</thead>
		<tbody>
			<#if stuList?exists && (stuList?size > 0)>
				<#list stuList as student>
				   <tr>
				   	  <td class="t-center"  width="5%">${student_index+1}</td>
				   	  <td class="t-center"  width="12%">${student.studentName!}</td>
				   	  <#if subjectList?exists && subjectList?size gt 0>
						  <#list subjectList as item>
					   	  <#if completeMap[student.id+'_'+item.id]?exists && completeMap[student.id+'_'+item.id]>
							<td class="t-center"><span style="color:green">完成</span></td>
						  <#else>
						    <td class="t-center"><span style="color:grey">未完成</span></td>
						  </#if>	
						 </#list>
					  </#if>
				   </tr>
                </#list>
            </#if>
		</tbody>
	</table>
</div>
<script>
$(function(){
});
</script>