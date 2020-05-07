<div class="table-container">
	<div class="table-container-header text-right">
		<button type="button" class="btn btn-blue " onclick="doExport()">导出</button>
	</div>
	<div style="width: 100%;display: inline-block;">
	<div class="table-container-body" id="table-head" style="width: 100%;overflow-x:hidden">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr>
				    <th rowspan="2" class="text-center">学校名称</th>
				    <th width="90px;" rowspan="2" class="text-center">年级</th>
				    <th width="150px;" rowspan="2" class="text-center">班级</th>
				    <th width="80px;" rowspan="2" class="text-center">学生数</th>
				    <th width="560px;" colspan="7" class="text-center">其中</th>
				    <th width="17px" rowspan="2" class="text-center"></th>
				</tr>
				<tr>
				    <th width="80px;" class="text-center">市外生数</th>
				    <th width="80px;" class="text-center">随迁<br>子女数</th>
				    <th width="80px;" class="text-center">留守<br>儿童数</th>
				    <th width="80px;" class="text-center">随班<br>就读数</th>
				    <th width="80px;" class="text-center">住宿生数</th>
				    <th width="80px;" class="text-center">未报户口<br>学生</th>				    
				    <th width="80px;" class="text-center">外籍学生</th>
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
        <td>${dto.schoolName!}</td>
        <td width="90px;">${dto.gradeName!}</td>
        <td width="150px;">${dto.className!}</td>
        <td width="80px;">${dto.clsStudentCount!}</td>
        <td width="80px;">${dto.notCityCount!}</td>
        <td width="80px;">${dto.migrationStuCount!}</td>
        <td width="80px;">${dto.stayinStuCount!}</td>
        <td width="80px;">${dto.regularClassStuCount!}</td>
        <td width="80px;">${dto.boardingStuCount!}</td>
        <td width="80px;">${dto.notHkStuCount!}</td>
        <td width="80px;">${dto.normalStuCount!}</td>
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="11" align="center">
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
    document.location.href="${request.contextPath}/newstusys/student/report/allClazzCountDtoExport";
}
</script>