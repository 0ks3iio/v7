<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyear" id="acadyear" class="form-control" onchange="searchGradeReport()">
				<#list acadyearList as ac>
					<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
				</#list>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select name="semester" id="semester" class="form-control" onchange="searchGradeReport()">
				<option value="1" <#if semester == '1'>selected</#if>>第一学期</option>
				<option value="2" <#if semester == '2'>selected</#if>>第二学期</option>
			</select>
		</div>
	</div>
	<#if deploy?default('')!='hangwai'>
	<div class="filter-item filter-item-right">
		<button type="button" class="btn btn-blue " onclick="doGradeExport()">导出</button>
	</div>
	</#if>
</div>
<div class="table-container">
	<div style="width: 100%;display: inline-block;">
	<div class="table-container-body" id="table-head" style="width: 100%;overflow-x: hidden;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr>
				    <th width="360px;" colspan="2" class="text-center">学校名称</th>
				    <th width="90px;" rowspan="2" class="text-center">年级</th>
				    <#if deploy?default('')=='hangwai'>
				    <th width="300px;" colspan="3" class="text-center">学籍增加</th>
				    <th width="500px;" colspan="5" class="text-center">学籍减少</th>
				    <#else>
				    <th width="800px;" colspan="8" class="text-center">转入学生</th>
				    <th width="700px;" colspan="7" class="text-center">转出学生</th>
				    </#if>
				    <th width="17px" rowspan="2" class="text-center"></th>
				</tr>
				<tr>
				    <th width="180px;" class="text-center">所在镇乡（街道）</th>
				    <th width="180px;" class="text-center">校点名称</th>
				    <#if deploy?default('')=='hangwai'>
				    <th width="100px;" class="text-center">复 学</th>
				    <th width="100px;" class="text-center">转入</th>
				    <th width="100px;" class="text-center">其他(增加)</th>
				    <th width="100px;" class="text-center">休 学</th>
				    <th width="100px;" class="text-center">出 境</th>
				    <th width="100px;" class="text-center">转 出</th>
				    <th width="100px;" class="text-center">死 亡</th>
				    <th width="100px;" class="text-center">其他(减少)</th>
				    <#else>
				    <th width="100px;" class="text-center">复 学</th>
				    <th width="100px;" class="text-center">入 境</th>
				    <th width="100px;" class="text-center">县区内<br>转入</th>
				    <th width="100px;" class="text-center">省内转入</th>
				    <th width="100px;" class="text-center">省外转入</th>				    
				    <th width="100px;" class="text-center">户口新报</th>
				    <th width="100px;" class="text-center">一年级<br>补报</th>
				    <th width="100px;" class="text-center">其他增加</th>
				    <th width="100px;" class="text-center">休 学</th>
				    <th width="100px;" class="text-center">出 境</th>
				    <th width="100px;" class="text-center">转往<br>县区内</th>
				    <th width="100px;" class="text-center">转往省内</th>
				    <th width="100px;" class="text-center">转往省外</th>
				    <th width="100px;" class="text-center">死 亡</th>
				    <th width="100px;" class="text-center">其他减少</th>
				    </#if>
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
        <#if deploy?default('')=='hangwai'>
        <td width="100px;">${dto.fx!}</td>
        <td width="100px;">${dto.xqnzr!}</td>
        <td width="100px;">${dto.qtzj!}</td>
        <td width="100px;">${dto.xx!}</td>
        <td width="100px;">${dto.cj!}</td>
        <td width="100px;">${dto.zwxqn!}</td>
        <td width="100px;">${dto.sw!}</td>
        <td width="100px;">${dto.qtjs!}</td>
        <#else>
        <td width="100px;">${dto.fx!}</td>
        <td width="100px;">${dto.rj!}</td>
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
        <td width="100px;">${dto.qtjs!}</td>
        </#if>
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="18" align="center">
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

function doGradeExport(){
    var unitId = $('#unitId').val();
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    document.location.href="${request.contextPath}/newstusys/student/abnormalreport/gradeCountExport?unitId="+unitId+"&acadyear="+acadyear+"&semester="+semester;
}

function searchGradeReport(){
   var acadyear = $('#acadyear').val();
   var semester = $('#semester').val();
   var url =  '${request.contextPath}/newstusys/student/abnormalreport/gradeCount?unitId=${unitId!}&acadyear='+acadyear+'&semester='+semester;
   $("#rosterDiv").load(url); 
}
</script>