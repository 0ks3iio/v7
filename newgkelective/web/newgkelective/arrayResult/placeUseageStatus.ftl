<#import "/fw/macro/htmlcomponent.ftl" as htmlcomponent>
<script>
function toPlaceUseDetail(placeId){
	var url = '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/classroomUseDetail?placeId='+placeId;
	$("#tableList").load(url);
}
function doExportAll(){
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/place/exportTimetableAll';
  	document.location.href=url;
}
</script>

<div class="filter">
	<div class="filter-item filter-item-left">
		<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		<a href="javascript:doExportAll();" class="btn btn-white">导出全部课表</a>
	</div>
</div>
<#assign weekDay=5 />
<div class="table-container">
	<div class="table-container-header">共${useageDtos?size}份结果</div>
	<div class="table-container-body print">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>教室</th>
					<#list 0..(weekDays-1) as day>
					<th>${dayOfWeekMap2[day+""]!}</th>
					</#list>
					<th class="noprint">操作</th>
				</tr>
			</thead>
			<tbody>
			<#if useageDtos?exists && useageDtos?size gt 0>
			<#list useageDtos as dto>
				<tr>
					<td>${dto_index+1}</td>
					<td>${dto.placeName}<input type="hidden" name="placeId" value="${dto.placeId}"/></td>
					<#list 0..(weekDays-1) as day>
					<td>${dto.useageMap[day+""]!'0'}</td>
					</#list>
					<td class="noprint"><a href="javascript:" onclick="toPlaceUseDetail('${dto.placeId}')">查看详情</a></td>    
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	</div>
</div>

