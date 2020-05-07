<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th class="t-center">序号</th>
					<th class="t-center">学生姓名</th>
					<#if code?default("")=="2">
						<#if detailList?exists && detailList?size gt 0>
							<#list detailList as item>
								<th class="t-center">${item.mcodeContent!}</th>
							</#list>
						</#if>
					<#else>
						<#if templateItemList?exists && templateItemList?size gt 0>
							<#list templateItemList as item>
								<th class="t-center">${item.itemName!}</th>
							</#list>
						</#if>
					</#if>
				</tr>
		</thead>
		<tbody>
			<#if stuList?exists && (stuList?size > 0)>
				<#if code?default("")=="2">
					<#list stuList as student>
					   <tr>
					   	  <td class="t-center"  width="5%">${student_index+1}</td>
					   	  <td class="t-center"  width="20%">${student.studentName!}</td>
					   	  <#if detailList?exists && detailList?size gt 0>
							  <#list detailList as item>
						   	  <#if completeMap[student.id+'_'+code+item.thisId]?exists && completeMap[student.id+'_'+code+item.thisId]>
								<td class="t-center"><span style="color:green">完成</span></td>
							  <#else>
							    <td class="t-center"><span style="color:grey">未完成</span></td>
							  </#if>	
							 </#list>
						  </#if>
					   </tr>
	                </#list>
                <#else>
                	<#list stuList as student>
					   <tr>
					   	  <td class="t-center"  width="5%">${student_index+1}</td>
					   	  <td class="t-center"  width="20%">${student.studentName!}</td>
					   	  <#if templateItemList?exists && templateItemList?size gt 0>
							  <#list templateItemList as item>
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
            </#if>
		</tbody>
	</table>
</div>
<script>
$(function(){
});
</script>