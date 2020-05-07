<#import "/fw/macro/htmlcomponent.ftl" as html>
<form id="submitForm">
	<div class="table-container-body">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学科</th>
					<th>参与考试人数</th>
					<th>优秀人数</th>
					<th>优秀率</th>
					<th>合格人数</th>
					<th>合格率</th>
					<th>不合格人数</th>
					<th>不合格率</th>
					<th>平均分</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			    <#list dtoList as item>
				<tr>
				    <td>${item_index+1!}</td>
				    <td>${item.subjectName!}</td>
				    <td>${item.countNum!}</td>
				    <td>${item.yxNum!}</td>
				    <td>${item.yxPer!?string("#.##")}%</td>
				    <td>${item.hgNum!}</td>				  
				    <td>${item.hgPer!?string("#.##")}%</td>
				    <td>${item.bhgNum!}</td>				 
				    <td>${item.bhgPer!?string("#.##")}%</td>
				    <td>${item.avgScore!?string("#.##")}</td>
				</tr>
				</#list>
			<#else>
				<tr>
					<td colspan="10" align="center">暂无数据</td>
				</tr>
			</#if>
			</tbody>
		</table>
	</div>
</form>
<script>

</script>