<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>导师姓名</th>
				<th>学生概况</th>
				<th>工作计划</th>
				<th>师生沟通</th>
				<th>家校沟通</th>
				<th>专题活动</th>
				<th>听课学习</th>
				<th>评价学生</th>
				<th>导师学期总结</th>
			</tr>
		</thead>
		<tbody>
		<#if sumRecordDtos?exists&&sumRecordDtos?size gt 0>
          	<#list sumRecordDtos as item>
			<tr>
				<td>${item.teacherName!}</td>
				<td>${item.xsgkNum!}</td>
				<td>${item.gzjhNum!}</td>
				<td>${item.ssgtNum!}</td>
				<td>${item.jxgtNum!}</td>
				<td>${item.zthdNum!}</td>
				<td>${item.tkxxNum!}</td>
				<td>${item.pjxsNum!}</td>
				<td>${item.dszjNum!}</td>
			</tr>
      	    </#list>
  	    <#else>
			<tr>
				<td  colspan="88" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
		</tbody>
	</table>
</div>
