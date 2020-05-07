<#import "/fw/macro/webmacro.ftl" as w>
<title>操作记录 List</title>
<table id="simple-table" class="history-list table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>学生姓名</th>
			<th>原班级</th>
			<th>转入班级</th>
			<th>转班时间</th>
			<th>操作人</th>
		</tr>
	</thead>
	<tbody>
	<#if datas?exists && datas?size gt 0>
		<#list datas as classFlow>
		<tr>
			<td>${classFlow.studentName!}</td>
			<td>${classFlow.oldClassName!}</td>
			<td>${classFlow.newClassName!}</td>
			<td>${classFlow.creationTime?string("yyyy-MM-dd")!}</td>
			<td>${classFlow.operateUserName!}</td>
		</tr>
		</#list>
		<#else>
		<tr><td colspan="6">无内容</td></tr>
		</#if>
	</tbody>
</table>
<@w.pagination  container="#b .searchListResult" pagination=pagination page_index=2/>
<#if studentDtos?exists && studentDtos?size gt 0>
</#if>