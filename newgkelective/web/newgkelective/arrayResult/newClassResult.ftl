<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="filter">
	<div class="filter-item filter-item-left">
		<@htmlcomponent.printToolBar container=".print"  printDirection='true' printUp=0 printLeft=0 printBottom=0 printRight=0/>
		<a href="javascript:doExportAll();" class="btn btn-white">导出全部课表</a>
	</div>
</div>
<#assign flag=arrangeType?default('01')=='01'>

<div class="table-container print">
	<div class="table-container-header">共${newGkDivideClassList?size!}份结果</div>
		<div class="table-container-body print">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th><#if flag>新</#if>行政班</th>
						<#--<th>班级类型</th>-->
						<#--<th>教室</th>-->
						<#--<th>所属组合</th>-->
						<th>总人数</th>
						<th>男生</th>
						<th>女生</th>
						<th class="noprint">操作</th>
					</tr>
				</thead>
				<tbody>
				<#if newGkDivideClassList?exists && newGkDivideClassList?size gt 0>
				    <#list newGkDivideClassList as item>
					<tr>
						<td>${item_index+1!}</td>
						<td>${item.className!}</td>
						<#--
						<td>
						    <#if '${item.bestType!}' == '1'>
						                        分层教学班
						    <#else>
						                        平行班
						    </#if>
						</td>
						-->
						<#--<td>${item.placeName!}</td>-->
						<#--<td>${item.relateName!}</td>-->
						<td>${item.studentCount!}</td>
						<td>${item.boyCount!}</td>
						<td>${item.girlCount!}</td>
						<td class="noprint">
							<#if flag>
							<a href="javascript:toDetailList('${item.id!}');">查看学生</a> |
							</#if>
							<a href="javascript:toTimetableList('${item.id!}');">查看课表</a>
						</td>
					</tr>	
					</#list>
				</#if>						
				</tbody>
			</table>
		</div>
	</div>
</div>								

<script>
function toDetailList(classId){
    var arrayId = '${arrayId!}';
    var url =  '${request.contextPath}/newgkelective/'+arrayId+'/arrayResult/newClassResultDetailList?classId='+classId;
	$("#tableList").load(url);
}
function toTimetableList(classId){
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/subjectTimetableResult/index/page?classType=1&classId='+classId;
	$("#tableList").load(url);
}
function doExportAll(){
	var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayResult/class/exportTimetableAll?classType=1';
  	document.location.href=url;
}
</script>