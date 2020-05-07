<div class="table-container">
	<div class="table-container-header text-right">
		<button type="button" class="btn btn-blue " onclick="doExport()">导出</button>
	</div>
	<form id="subForm">
	<div style="width: 100%;display: inline-block;">
	
	<div class="table-container-body" id="table-head" style="width: 100%;overflow-x: hidden;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr>
				    <th width="360px;" colspan="2" class="text-center">学校名称</th>
				    <th width="90px;" rowspan="2" class="text-center">年级</th>
				    <th width="150px;" rowspan="2" class="text-center">班级</th>
				    <th width="400px;" colspan="4" class="text-center">实际学生总数</th>
				    <th width="900px;" colspan="9" class="text-center">其他各类学生数</th>
				    <th width="17px" rowspan="2" class="text-center"></th>
				</tr>
				<tr>
				    <th width="180px;" class="text-center">所在镇乡（街道）</th>
				    <th width="180px;" class="text-center">校点名称</th>
				    <th width="100px;" class="text-center">实际班额</th>
				    <th width="100px;" class="text-center">诸暨户籍<br>学生</th>
				    <th width="100px;" class="text-center">市外户籍</th>
				    <th width="100px;" class="text-center">无户口<br>学生</th>				    
				    <th width="100px;" class="text-center">随迁子女<br>（总）</th>
				    <th width="100px;" class="text-center">随迁子女<br>（省外）</th>
				    <th width="100px;" class="text-center">留守儿童</th>
				    <th width="100px;" class="text-center">随班就读</th>
				    <th width="100px;" class="text-center">外籍学生</th>
				    <th width="100px;" class="text-center">港澳台生</th>
				    <th width="100px;" class="text-center">待转入<br>学生</th>
				    <th width="100px;" class="text-center">住宿学生</th>
				    <th width="100px;" class="text-center">备注</th>
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
        <td width="180px;" >${dto.schoolName!}</td>
        <td width="90px;" >${dto.gradeName!}</td>
        <td width="150px;" >${dto.className!}</td>
        <td width="100px;">${dto.clsStudentCount!}</td>
        <td width="100px;">${dto.inCityCount!}</td>
        <td width="100px;">${dto.notCityCount!}</td>
        <td width="100px;">${dto.notHkStuCount!}</td>
        <td width="100px;">${dto.migrationStuCount!}</td>
        <td width="100px;">${dto.syMigrationStuCount!}</td>
        <td width="100px;">${dto.stayinStuCount!}</td>
        <td width="100px;">${dto.regularClassStuCount!}</td>
        <td width="100px;">${dto.normalStuCount!}</td>
        <td width="100px;">${dto.compatriotsCount!}</td>
        <td width="100px;">${dto.nowStateStuCount!}</td>
        <td width="100px;">${dto.boardingStuCount!}</td>
        <td width="100px;">${dto.remark!}</td>
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="17" align="center">
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
    document.location.href="${request.contextPath}/newstusys/student/report/classCountExport?unitId="+unitId;
}
</script>