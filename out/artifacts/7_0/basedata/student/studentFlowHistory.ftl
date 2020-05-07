<#import "/fw/macro/webmacro.ftl" as w>
<title>操作记录 List</title>
<table id="simple-table" class="history-list table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>学生姓名</th>
			<th>身份证件号</th>
			<#if studentFlowType == "0">
				<th>原学校</th>
				<th>原班级</th>
				<th>转出时间</th>
				<th>转出原因</th>
			<#elseif studentFlowType == "1">
				<th>现学校</th>
				<th>现班级</th>
				<th>转入时间</th>
				<th>转入原因</th>
			<#else>
				<th>现学校</th>
				<th>现班级</th>
				<th>转入时间</th>
				<th>转入原因</th>
			</#if>
			<th>验证码</th>
			<th>操作人</th>
		</tr>
	</thead>
	<tbody>
	<#if studentDtos?exists && studentDtos?size gt 0>
		<#list studentDtos as student>
		<tr>
			<td>${student.student.studentName!}</td>
			<td>${student.student.identityCard!}</td>
			<td>${student.studentFlow.schoolName!}</td>
			<td>${student.studentFlow.className!}</td>
			<td>${student.studentFlow.creationTime?string("yyyy-MM-dd")!}</td>
			<td>${student.studentFlow.reason!}</td>
			<td>${student.studentFlow.pin!}</td>
			<td>${student.studentFlow.handleUserName!}</td>
		</tr>
		</#list>
		<#else>
		<tr><td colspan="8">无内容</td></tr>
		</#if>
	</tbody>
</table>
<@w.pagination  container="#b .searchListResult" pagination=pagination page_index=1/>
<#if studentDtos?exists && studentDtos?size gt 0>
</#if>
