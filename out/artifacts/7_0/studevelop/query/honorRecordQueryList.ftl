<title>荣誉评选List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>班级</th>
					<th>荣誉类型</th>
					<th>荣誉名称</th>
					<th>获得日期</th>
					<th style="width:449px">备注</th>
				</tr>
		</thead>
		<tbody>
			<#if honorRecordsList?exists && (honorRecordsList?size > 0)>
				<#list honorRecordsList as list>
					<tr>
						<td>${list.studentName!}</td>
						<td>${list.className!}</td>
						<td>${list.honorTypeStr!}</td>
						<#if list.honorType == "1">
							<td>${mcodeSetting.getMcode('DM-XJRW',(list.honorLevel!))}</td>
						<#else>
							<td>${mcodeSetting.getMcode('DM-QCYGK',(list.honorLevel!))}</td>
						</#if>
						<td>${(list.giveDate?string('yyyy-MM-dd'))?if_exists}</td>
						<td title="${list.remark!}"><p style="margin:0 10px;width:400px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${list.remark!}</p></td>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>		
</div>