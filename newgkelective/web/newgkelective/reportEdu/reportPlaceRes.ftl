<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学校名称</th>
					<th>所属教育局</th>
					<th>单位负责人</th>
					<th>联系电话</th>
					<th>行政班数</th>
					<th>可用场地数</th>
				</tr>
			</thead>
			<tbody>
			<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as dto>
				<tr>
					<td>${dto_index+1}</td>
					<td>${dto.unitName!}</td>
					<td>${dto.parentName!}</td>
					<td>${dto.unitHeader!}</td>
					<td>${dto.tel!}</td>
					<td>${dto.xzbNum???then(dto.xzbNum,'--')}</td>
					<td>${dto.placeNum!}</td>
				</tr>
			</#list>
			</#if>
			</tbody>
		</table>
	</div>
	<#if pageInfo.itemsNum gt 0>
	<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="queryRes" allNum=pageInfo.itemsNum/>
	</#if>	
</div>
<script>
function queryRes(pageIndex,pageSize){
	var gradeCode = $("#gradeCode").val();
	var url = "${request.contextPath}/newgkelective/edu/baseItem/placeRes?unitId=${unitId!}&gradeYear="+gradeCode;
	
	if(pageIndex){
		url=url+'&pageIndex='+pageIndex;
	}
	if(pageSize && pageSize!=""){
		url=url+'&pageSize='+pageSize;
	}
	$("#aa").load(url);
}
</script>