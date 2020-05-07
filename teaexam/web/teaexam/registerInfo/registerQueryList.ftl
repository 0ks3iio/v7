<#import "/fw/macro/htmlcomponent.ftl" as html>
<form id="submitForm">
	<div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>教师姓名</th>
					<th>部门</th>
					<th>身份证号</th>
					<th>考试科目/培训项目</th>
					<th>报名状态</th>
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
						       <td style="white-space: nowrap"><#if item.status == 1>审核中<#elseif item.status==2>报名成功<#else>审核未通过(${item.remark!})</#if></td>
						    </tr>		
						</#list>
					<#else>
						<tr>
							<td colspan="5" align="center">暂无数据</td>
						</tr>
					</#if>
				</tbody>
		</table>
	</div>
</form>
<script>
function exportList(){
	<#if !hasData>
		layerTipMsgWarn("提示","没有数据，无法导出!");
	<#else>
		var url = '${request.contextPath}/teaexam/query/registerInfo/export?examId=${examId!}';
		window.open(url);	
	</#if>
}

</script>