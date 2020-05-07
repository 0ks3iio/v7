<form  class="rankList">
<div class="table-container-body">
	<table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
				<th class="text-center">周次</th>
				<th>班级</th>
				<th>纪律总分</th>
				<th>排名</th>
				<th>奖励</th>
				<th>卫生总分</th>
				<th>排名</th>
				<th>奖励</th>
			</tr>
		</thead>
		<tbody>
		   <#if weekMap?exists && weekMap?size gt 0>
             <#list weekMap?keys as key>
                <#if weekMap[key]?exists && weekMap?size gt 0>
	                 <#list weekMap[key] as tr>
	                    <tr >
	                       <#if tr_index == 0>
								<td class="text-center" rowspan="${weekMap[key]?size!}">第${key}周</td>
						   </#if>
							<td>${classNameMap[tr.classId]!}</td>
							<td><#if tr.disciplineScore?exists>
							    ${tr.disciplineScore?string("0.##")!}
							    </#if>
							</td>
							<td>${tr.disciplineRank!}</td>
							<td><#if tr.isDisciplineExcellen?default(false)>2</#if></td>
							<td>${tr.healthScore!}</td>
							<td>${tr.healthRank!}</td>
							<td><#if tr.isHealthExcellen?default(false)>2</#if></td>
						</tr>
	                 </#list>
                 </#if>
            </#list>
          <#else>
			<tr>
				<#if tManage?default(false)>
				<td  colspan="5" align="center">
				<#else>
				<td  colspan="4" align="center">
				</#if>
				 ${messageEmpty!"暂无排名"}
				</td>
			<tr>
		  </#if>
		</tbody>
	</table>
</div>
</form>
<script>
$(function(){
       $('#rankExport').show();
       isShow = true;
});



</script>

