<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table id="example" class="table table-bordered">
	<thead>
		<tr>
			<th>序号</th>
			<th>姓名</th>
			<th>班级</th>
			<th>学科成绩总分</th>
			<th>年级排名</th>
			<th>学科成绩折分</th>
			<th>英语笔试成绩</th>
			<th>年级排名</th>
			<th>英语笔试折分</th>
			<th>英语口试</th>
			<th>年级排名</th>
			<th>口试折分</th>
			<th>学科竞赛折分</th>
			<th>学考折分</th>
		</tr>
	</thead>
	<tbody>
		<#if dtoList?exists&&dtoList?size gt 0>
          	<#list dtoList as item>
			<tr>
				<td>${item_index+1}</td>
				<td>${item.studentName!}</td>
				<td>${item.className!}</td>
				<td>${item.xkcjScore!}</td>
				<td>${item.xkcjRanking!}</td>
				<td>${item.xkcjToScore!}</td>
				<td>${item.yybsScore!}</td>
				<td>${item.yybsRanking!}</td>
				<td>${item.yybsToScore!}</td>
				<td>${item.yyksScore!}</td>
				<td>${item.yyksRanking!}</td>
				<td>${item.yyksToScore!}</td>
				<td>${item.xkjsToScore!}</td>
				<td>${item.xkToScore!}</td>
			</tr>
      	    </#list>
  	    <#else>
			<tr>
				<td  colspan="15" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
	</tbody>
</table>
<#if dtoList?exists&&dtoList?size gt 0>
	<@htmlcom.pageToolBar container="#showListDiv" class="noprint"/>
</#if>