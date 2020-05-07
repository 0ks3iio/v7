<#import "/fw/macro/htmlcomponent.ftl" as html>
<form id="submitForm">
	<div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>教师姓名</th>
					<th>部门</th>
					<th>身份证号</th>
					<th>考试科目</th>
					<th>考试成绩</th>
					<th>考试评价</th>
				</tr>
				</thead>
				<tbody>
					<#assign hasData = false />
					<#if regList?exists && regList?size gt 0>
						<#assign hasData = true />
						<#list regList as item>
						    <tr>
						       <td style="white-space: nowrap">${item.teacherName!}</td>
						       <td style="white-space: nowrap">${item.userName!}</td>
						       <td style="white-space: nowrap">${item.identityCard!}</td>
						       <td><@html.cutOff4List str='${item.subName!}' length=50 /></td>
						       <td>${item.score!}</td>
						       <td>
						                 <#if '${item.gradeCode!}'=='1'>
						                 优秀
						                 <#elseif '${item.gradeCode!}'=='2'>
						                 合格
						                 <#elseif '${item.gradeCode!}'=='3'>
						                 不合格
						                 </#if>
						       </td>
						    </tr>		
						</#list>
					<#else>
						<tr>
							<td colspan="6" align="center">暂无数据</td>
						</tr>
					</#if>
				</tbody>
		</table>
	</div>
</form>
<script>

</script>