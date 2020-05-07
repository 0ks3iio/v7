<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyear" id="acadyear" class="form-control" onchange="searchClassReport()">
				<#list acadyearList as ac>
					<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
				</#list>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select name="semester" id="semester" class="form-control" onchange="searchClassReport()">
				<option value="1" <#if semester == '1'>selected</#if>>第一学期</option>
				<option value="2" <#if semester == '2'>selected</#if>>第二学期</option>
			</select>
		</div>
	</div>
	<#if deploy?default('')!='hangwai'>
	<div class="filter-item filter-item-right">
		<button type="button" class="btn btn-blue " onclick="doExport()">导出</button>
	</div>
	</#if>
</div>
<div class="table-container">
		<form id="subForm">
		<div style="width: 100%;display: inline-block;">
	<div class="table-container-body" id="table-head" style="width: 100%;overflow-x: hidden;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr>
				    <#assign enSize = 1 />
				    <#if enterFlowTypes?exists && enterFlowTypes?size gt 0>
				    <#assign enSize = enterFlowTypes?size />
				    </#if>
				    <#assign leSize = 1 />
				    <#if leaveFlowTypes?exists && leaveFlowTypes?size gt 0>
				    <#assign leSize = leaveFlowTypes?size />
				    </#if>
				    <th width="360px;" colspan="2" class="text-center">学校名称</th>
				    <th width="90px;" rowspan="2" class="text-center">年级</th>
				    <th width="150px;" rowspan="2" class="text-center">班级</th>
				    <th width="${enSize*100}px;" colspan="${enSize}" class="text-center"><#if deploy?default('')=='hangwai'>学籍增加<#else>转入学生</#if></th>
				    <th width="${leSize*100}px;" colspan="${leSize}" class="text-center"><#if deploy?default('')=='hangwai'>学籍减少<#else>转出学生</#if></th>
				    <th width="17px" rowspan="2" class="text-center"></th>
				</tr>
				<tr>
				    <th width="180px;" class="text-center">所在镇乡（街道）</th>
				    <th width="180px;" class="text-center">校点名称</th>
				    <#if enterFlowTypes?exists && enterFlowTypes?size gt 0>
				    <#list enterFlowTypes as ft>
				    <th width="100px;" class="text-center">${ft.mcodeContent!}</th>
				    </#list>
				    </#if>
				    <#--
				    th width="100px;" class="text-center">入 境</th>
				    <th width="100px;" class="text-center">县区内<br>转入</th>				    
				    <th width="100px;" class="text-center">省内转入</th>
				    <th width="100px;" class="text-center">省外转入</th>
				    <th width="100px;" class="text-center">户口新报</th>
				    <th width="100px;" class="text-center">一年级<br>补报</th>
				    <th width="100px;" class="text-center">其他增加</th-->
				    <#if leaveFlowTypes?exists && leaveFlowTypes?size gt 0>
				    <#list leaveFlowTypes as ft>
				    <th width="100px;" class="text-center">${ft.mcodeContent!}</th>
				    </#list>
				    </#if>
				    <#--
				    th width="100px;" class="text-center">休 学</th>
				    <th width="100px;" class="text-center">出 境</th>
				    <th width="100px;" class="text-center">转往<br>县区内</th>
				    <th width="100px;" class="text-center">转往省内</th>				    
				    <th width="100px;" class="text-center">转往省外</th>
				    <th width="100px;" class="text-center">死 亡</th>
				    <th width="100px;" class="text-center">其他减少</th-->
				</tr>
			</thead>
	</table>	
	</div>
	<div class="table-container-body" id="table-body" style="max-height:500px;width: 100%;overflow-x:scroll;overflow-y:scroll;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed">
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
  <#list dtoList as dto>
  	 <tr>
  	 	<td width="180px;">${dto.address!}</td>
        <td width="180px;">${dto.schoolName!}</td>
        <td width="90px;">${dto.gradeName!}</td>
        <td width="150px;">${dto.className!}</td>
        <#if enterFlowTypes?exists && enterFlowTypes?size gt 0>
        <#list enterFlowTypes as ft>
        <#assign ekey = dto.clsId+ft.thisId />
        <td width="100px;">
        ${(clsFlowCountMap[ekey])?default(0)}</td>
        </#list>
        <#else>
        <td></td>
        </#if>
        <#if leaveFlowTypes?exists && leaveFlowTypes?size gt 0>
        <#list leaveFlowTypes as lft>
        <#assign lkey = dto.clsId+lft.thisId />
        <td width="100px;">
        ${(clsFlowCountMap[lkey])?default(0)}</td>
        </#list>
        <#else>
        <td></td>
        </#if>
        <#--
        td width="100px;">${dto.rj!}</td>
        <td width="100px;">${dto.xqnzr!}</td>
        <td width="100px;">${dto.snzr!}</td>
        <td width="100px;">${dto.swzr!}</td>
        <td width="100px;">${dto.hkxb!}</td>
        <td width="100px;">${dto.ynjbb!}</td>
        <td width="100px;">${dto.qtzj!}</td>
        <td width="100px;">${dto.xx!}</td>
        <td width="100px;">${dto.cj!}</td>
        <td width="100px;">${dto.zwxqn!}</td>
        <td width="100px;">${dto.zwsn!}</td>
        <td width="100px;">${dto.zwsw!}</td>
        <td width="100px;">${dto.sw!}</td>
        <td width="100px;">${dto.qtjs!}</td-->
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="19" align="center">
  		暂无数据
  	</td>
  <tr>
  </#if>
			</tbody>
		</table>
	</div>
	</div>
	</form>
</div>	
<input id="unitId" type="hidden" value="${unitId!}">
<script>
var $tableHead = $('#table-head');
var $tableBody = $('#table-body');
/*设置同步横向滚动*/
$tableBody.scroll(function (ev) {
    $tableHead.scrollLeft($tableBody.scrollLeft()); // 横向滚动条
});

function doExport(){
    var unitId = $('#unitId').val();
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    document.location.href="${request.contextPath}/newstusys/student/abnormalreport/classCountExport?unitId="+unitId+"&acadyear="+acadyear+"&semester="+semester;
}

function searchClassReport(){
   var acadyear = $('#acadyear').val();
   var semester = $('#semester').val();
   var url =  '${request.contextPath}/newstusys/student/abnormalreport/classCount?unitId=${unitId!}&acadyear='+acadyear+'&semester='+semester;
   $("#rosterDiv").load(url); 
}
</script>