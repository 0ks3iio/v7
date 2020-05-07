<div class="table-container">
	<div class="table-container-header text-right">
		<button type="button" class="btn btn-blue " onclick="doExport()">导出</button>
	</div>
	<div style="width: 100%;display: inline-block;">
	<div class="table-container-body" id="table-head" style="width: 100%;overflow-x:hidden">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr>
				    <th width="160px;" colspan="2" class="text-center">学校名称</th>
				    <th width="50px;" rowspan="2" class="text-center">学段</th>
				    <th width="400px;" colspan="5" class="text-center">实际学生总数</th>
				    <th width="720px;" colspan="9" class="text-center">其他各类学生数</th>
				    <th width="17px" rowspan="2" class="text-center"></th>
				</tr>
				<tr>
				    <th width="80px;" class="text-center">所在镇乡<br>（街道）</th>
				    <th width="80px;" class="text-center">校点名称</th>
				    <th width="80px;" class="text-center">班级总数</th>
				    <th width="80px;" class="text-center">学生总数</th>
				    <th width="80px;" class="text-center">诸暨户籍<br>学生</th>
				    <th width="80px;" class="text-center">市外户籍</th>
				    <th width="80px;" class="text-center">无户口<br>学生</th>				    
				    <th width="80px;" class="text-center">随迁子女<br>（总）</th>
				    <th width="80px;" class="text-center">随迁子女<br>（省外）</th>
				    <th width="80px;" class="text-center">留守儿童</th>
				    <th width="80px;" class="text-center">随班就读</th>
				    <th width="80px;" class="text-center">外籍学生</th>
				    <th width="80px;" class="text-center">港澳台生</th>
				    <th width="80px;" class="text-center">待转入<br>学生</th>
				    <th width="80px;" class="text-center">住宿学生</th>
				    <th width="80px;" class="text-center">备注</th>
				</tr>
			</thead>
	</table>	
	</div>
	<div class="table-container-body" id="table-body" style="max-height:500px;width: 100%;overflow-x:auto;overflow-y:scroll;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed">	
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
  <#list dtoList as dto>
  	 <tr>
  	 	<td width="80px;">${dto.address!}</td>
        <td width="80px;">${dto.schName!}</td>
        <td width="50px;">${dto.sectionName!}</td>
        <td width="80px;">${dto.classCount!}</td>
        <td width="80px;">${dto.clsStudentCount!}</td>
        <td width="80px;">${dto.inCityCount!}</td>
        <td width="80px;">${dto.notCityCount!}</td>
        <td width="80px;">${dto.notHkStuCount!}</td>
        <td width="80px;">${dto.migrationStuCount!}</td>
        <td width="80px;">${dto.syMigrationStuCount!}</td>
        <td width="80px;">${dto.stayinStuCount!}</td>
        <td width="80px;">${dto.regularClassStuCount!}</td>
        <td width="80px;">${dto.normalStuCount!}</td>
        <td width="80px;">${dto.compatriotsCount!}</td>
        <td width="80px;">${dto.nowStateStuCount!}</td>
        <td width="80px;">${dto.boardingStuCount!}</td>
        <td width="80px;">${dto.remark!}</td>
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="16" align="center">
  		暂无数据
  	</td>
  <tr>
  </#if>
			
			</tbody>
		</table>
	</div>
	</div>
</div>	
<script>
var $tableHead = $('#table-head');
var $tableBody = $('#table-body');
/*设置同步横向滚动*/
$tableBody.scroll(function (ev) {
    $tableHead.scrollLeft($tableBody.scrollLeft()); // 横向滚动条
});

function doExport(){
    var unitId = $('#unitId').val();
    document.location.href="${request.contextPath}/newstusys/student/report/cityPrimaryExport";
}
</script>