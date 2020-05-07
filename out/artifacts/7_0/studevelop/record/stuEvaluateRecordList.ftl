<title>期末评价List</title>
<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th style="width:10%">学生姓名</th>
					<th style="width:10%">评语等级</th>
					<th style="width:50%">老师寄语</th>
					<th style="width:30%" >个性特点</th>
				</tr>
		</thead>
		<tbody>
			<#if evaluateRecordList?exists && (evaluateRecordList?size > 0)>
				<#list evaluateRecordList as list>
					<tr>
						<td style="width:10%">${list.studentName!}</td>
						<td style="width:10%">
							<#if (list.evaluateLevel!) == "A">
							优秀
							<#elseif (list.evaluateLevel!) == "B">
							良好
							<#elseif (list.evaluateLevel!) == "C">
							中等
							<#elseif (list.evaluateLevel!) == "D">
							差
							</#if>
						</td>
						<td style="width:50%;word-break:break-all;">${list.teacherEvalContent!}</td>
						<td style="width:30%;word-break:break-all;">${list.strong!}</td>
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>		
</div>