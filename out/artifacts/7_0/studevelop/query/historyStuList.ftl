	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>姓名</th>
					<th>性别</th>
					<th>学号</th>
					<th>身份证号</th>
					<th>班级</th>
					<th>学年</th>
					<th>学期</th>
                    <th>操作</th>
				</tr>
		</thead>
		<tbody>
			<#if stuList?exists && (stuList?size > 0)>
				<#list stuList as item>
					<tr>
						<td>${item.studentName!}</td>
                        <td>${mcodeSetting.getMcode("DM-XB",item.sex?default(0)?string)}</td>
                        <td>${item.studentCode!}</td>
						<td>${item.identityCard!}</td>
						<td>${item.className!}</td>
						<td>${item.acadyear!}</td>
						<td><#if item.semester?default(1)==1>第一学期<#else>第二学期</#if></td>
						<td>
				           <a href="javascript:void(0);" onclick="showPdf('${item.studentId!}','${item.acadyear!}','${item.semester!}');" class="table-btn color-red">查看报告单</a>
			            </td>					
					</tr>
				</#list>
			</#if>
		</tbody>
	</table>
<script>

$(function(){
});

function toReport(studentId,acadyear,semester){
   var str = "?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId;
   var url = "${request.contextPath}/studevelop/historyReport/report"+str;
   $("#showList").load(url);
}
</script>