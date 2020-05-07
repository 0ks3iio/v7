<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container">
		<form id="subForm">
	<div style="width: 100%;display: inline-block;">
	<div class="table-container-body" id="table-head" style="width: 100%;overflow-x: hidden;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed;">
			<thead>
				<tr>
				    <th width="180px;">所在镇乡（街道）</th>
				    <th width="50px;">学段</th>
				    <th width="180px;">学校名称</th>
				    <th width="90px;">年级</th>
				    <th width="150px;">班级</th>
				    <th width="90px;">班内编号</th>
				    <th width="110px;">学生姓名</th>
				    <th width="50px;">性别</th>
				    <th width="60px;">民族</th>
					<th width="180px;">学籍主号</th>
                    <th width="100px;">户籍省县</th>
                    <th width="100px;">户籍镇街</th>
                    <th width="100px;">户籍社区/村</th>
                    <th width="100px;">父亲姓名</th>
                    <th width="100px;">母亲姓名</th>
                    <th width="110px;">是否为随迁子女<br>或留守儿童</th>
                    <th width="100px;">是否随班就读</th>
                    <th width="100px;">是否为住宿生</th>
                    <th width="90px;">无户口学生</th>
                    <th width="55px;">休学中</th>
					<th width="100px;">备注</th>
					<th width="17px"></th>
				</tr>
			</thead>
		</table>
	</div>
	<div class="table-container-body" id="table-body" style="max-height:500px;width: 100%;overflow-x:scroll;overflow-y:scroll;">
		<table class="table table-bordered no-margin" style="width: 100%;table-layout: fixed">
			<tbody>
				<#if studentList?exists && studentList?size gt 0>
  <#list studentList as student>
  	 <tr>
  	 	<td width="180px;">${student.partyIntroducer!}</td>
        <td width="50px;">${(mcodeSetting.getMcode("DM-RKXD", student.compatriots?string("number")))?if_exists}</td>
        <td width="180px;">${student.schoolName!}</td>
        <td width="90px;">${student.background!}</td>
        <td width="150px;">${student.className!}</td>
        <td width="90px;">${student.classInnerCode!}</td>
        <td width="110px;">${student.studentName!}</td>
        <td width="50px;">${(mcodeSetting.getMcode("DM-XB", student.sex?default(0)?string("number")))?if_exists}</td>
        <td width="60px;">${(mcodeSetting.getMcode("DM-MZ", student.nation))}</td>
        <td width="180px;">${student.unitiveCode!}</td>
        <td width="100px;">${student.registerPlace!}</td>
        <td width="100px;">${student.registerAddress!}</td>
        <td width="100px;"></td>
        <td width="100px;">${student.linkAddress!}</td>
        <td width="100px;">${student.nowaddress!}</td>
        <td width="110px;">
            <#if student.isMigration?exists && student.isMigration=='1'>
                                       随迁子女
            </#if>
            <#if student.stayin?exists && student.stayin==1>
                                       留守儿童
            </#if>
        </td>
        <td width="100px;">
            <#if student.regularClass?exists && student.regularClass!='1'>
                                       随班就读
            </#if>
        </td>
        <td width="100px;">
            <#if student.isBoarding?exists && student.isBoarding=='1'>
                                       住宿生
            </#if>
        </td>
        <td width="90px;">
            <#if student.source?exists && student.source=='4'>
                                       无户口
            </#if>
        </td>
        <td width="55px;"></td>
        <td width="100px;">${student.remark!}</td>
  	 </tr>
  </#list>
  <#else>
  <tr >
  	<td colspan="21" align="center">
  		暂无数据
  	</td>
  <tr>
  </#if>
			
			</tbody>
		</table>
	
		</form>
	</div>
	</div>
	<#if studentList?exists&&studentList?size gt 0>
	         <@htmlcom.pageToolBar container="#rosterDiv" class="noprint"/>
        </#if>
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
    var gradeCode = '${gradeCode!}';
	var searchType = '${searchType!}';
    window.open("${request.contextPath}/newstusys/student/report/studentRosterExport?unitId="+unitId+"&gradeCode="+gradeCode+"&searchType="+searchType);
}

</script>