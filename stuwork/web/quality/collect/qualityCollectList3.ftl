<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table id="example" class="table table-bordered">
	<thead>
		<tr>
			<th>序号</th>
			<th>姓名</th>
			<th>班级</th>
			<th>体育成绩</th>
			<th>体育成绩折分</th>
		</tr>
	</thead>
	<tbody>
		<#if dtoList?exists&&dtoList?size gt 0>
          	<#list dtoList as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.studentName!}</td>
				<td>${item.className!}</td>
				<td>${item.tycjScore!}</td>
				<td>${item.tycjToScore!}</td>
			</tr>
      	    </#list>
  	    <#else>
			<tr>
				<td  colspan="5" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
	</tbody>
</table>
<#if dtoList?exists&&dtoList?size gt 0>
	<@htmlcom.pageToolBar container="#showListDiv" class="noprint"/>
</#if>