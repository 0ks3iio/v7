<div class="box-header">
	<h3 class="box-title">${className!}</h3>
</div>
<div class="box-body">
	<div class="stat">
		<div class="stat-item">
			<div class="stat-item-content">
				<strong>${scoreNum!}</strong>
				<p>上课日志总得分</p>
			</div>
		</div>
		<div class="stat-item">
			<div class="stat-item-content">
				<strong>${nightScoreNum!}</strong>
				<p>晚自习日志得分</p>
			</div>
		</div>
	</div>

	<table class="table table-bordered">
		<thead>
			<tr>
				<th class="text-center">日志</th>
				<th>节次</th>
				<th>最终得分</th>
				<th>违纪人员</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<#if courseScheduleListResult?exists && courseScheduleListResult?size gt 0>
		   <#list courseScheduleListResult as item>
		      <tr>
		        <#if '${item_index!}' == '0'>
		           <td class="text-center" rowspan="${courseScheduleListResult?size}">上课日志</td>
		        </#if>
				<td>第${item.period!}节</td>
				<td>${item.score!}</td>
				<td>${item.punishStudentName!}</td>
				<td><a class="js-openDetail" href="javascript:doQuery('1','${item.period!}','${item.score!}','${item.punishStudentName!}');">查看明细</a></td>
			 </tr>
		   </#list>
		</#if>
			<tr>
				<td class="text-center">晚自习日志</td>
				<td>/</td>
				<td>${nightScore!}</td>
				<td>${nightPunishStuName!}</td>
				<td><#--a class="js-openDetail" href="javascript:doQuery('2','0','${nightScore!}','${nightPunishStuName!}');">查看明细</a-->/</td>
			</tr>
		</tbody>
	</table>
</div>
<script>
function doQuery(type,period,score,punishStudentName){
    var url=encodeURI('${request.contextPath}/stuwork/courserecord/recordCollectDetail?acadyear=${acadyear!}&semester=${semester!}&week=${week!}&day=${day!}&classId=${classId!}&type='+type+'&period='+period+'&score='+score+'&punishStudentName='+punishStudentName);
	indexDiv = layerDivUrl(url,{title: "考核明细",width:415,height:400});
}
</script>