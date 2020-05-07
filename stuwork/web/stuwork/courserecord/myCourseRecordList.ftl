<div class="table-wrapper">
    <form id="mannReForm">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th class="t-center">节次</th>
					<th class="t-center">班级</th>
					<th class="t-center">课程</th>
					<th class="t-center">考核分</th>
					<th class="t-center">违纪名单</th>
                    <th class="t-center">备注</th>
                    <th class="t-center">操作</th>
				</tr>
		</thead>
		<tbody>
			<#if courseScheduleList?exists && (courseScheduleList?size > 0)>
				<#list courseScheduleList as item>
				   <tr>
				      <td class="t-center" width="6%;">第${item.period!}节</td>
				      <td class="t-center" width="15%;" style="word-break:break-all;">${item.className!}</td>
				      <td class="t-center" width="15%;" style="word-break:break-all;">${item.subjectName!}</td>
				      <td class="t-center" width="5%;">${item.score!}</td>
				      <td class="t-center" width="27%;" style="word-break:break-all;">${item.punishStudentName!}</td>
				      <td class="t-center" width="25%;" style="word-break:break-all;">${item.remark!}</td>
				      <td class="t-center" width="8%;">
				        <#if item.recordId?exists>
				           <a href="javascript:toAdd('${item.classId!}','${item.weekOfWorktime!}','${item.dayOfWeek!}','${item.period!}','${item.subjectId!}','${item.teacherId!}');" class="color-lightblue js-entry">修改</a>
				        <#else>
				           <a href="javascript:toAdd('${item.classId!}','${item.weekOfWorktime!}','${item.dayOfWeek!}','${item.period!}','${item.subjectId!}','${item.teacherId!}');" class="color-lightblue js-entry">录入</a>
				        </#if>
				      </td>
				   </tr>
                </#list>
            </#if>
		</tbody>
	</table>

	</form>	
</div>
<script>
function toAdd(classId,week,day,period,subjectId,tid){
	var url='${request.contextPath}/stuwork/courserecord/myCourseAdd?classId='+classId+'&week='+week+'&day='+day+'&period='+period+'&acadyear=${acadyear!}&semester=${semester!}&queryDate=${queryDate?string('yyyy-MM-dd')!}&subjectId='+subjectId+'&teacherId='+tid;
	indexDiv = layerDivUrl(url,{title: "日志录入",width:500,height:400});
}
</script>